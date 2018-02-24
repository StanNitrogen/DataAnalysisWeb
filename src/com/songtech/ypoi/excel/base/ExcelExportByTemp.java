package com.songtech.ypoi.excel.base;

import com.songtech.ypoi.excel.CommonUtil;
import com.songtech.ypoi.exception.YpoiBaseException;
import com.songtech.ypoi.externalInterface.IExcelExportByTemp;
import com.songtech.ypoi.externalInterface.IExcelHandler;
import com.songtech.ypoi.params.ExcelParams;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Create By YINN on 2018/1/18 11:01
 * Description :
 */
public class ExcelExportByTemp implements IExcelExportByTemp {
    final static String FLAG= "$";
    final static String DOUBLEFLAG= "$$";
    final static String SPILT = "::";
    private CellStyle cloneStyle;
//    final static String DOLLAR_FUNCTION = "${}";

    /**
     * @Method 模板导出
     * @param ep
     * @param datas
     * @param is
     *
     * excel中格式 $$为纵向写入 $为单元格写入
     * $$field:YYYY-MM-DD 或 $field:YYYY-MM-DD
     */

    public Workbook exportByTemp(InputStream is, ExcelParams ep, Collection<?>... datas) throws YpoiBaseException, IOException, InvalidFormatException, NoSuchFieldException, IllegalAccessException {
        if (is == null || datas == null || datas.length == 0){
            throw new YpoiBaseException("excel模板导出，参数错误！");
        }

        Workbook wb = WorkbookFactory.create(is);

        //遍历sheet
        for (int i=0; i<wb.getNumberOfSheets();i++){
            Sheet sheet = wb.getSheetAt(i);
            Collection<?> data = datas[i];

            //获取关系list
            List<Map<String,Object>> relations = this.getRelations(sheet);

            //目前只实现List<?>数据
            if (!(data instanceof List)){
                continue;
            }

            //计数器
            Integer count = 0;
            Iterator<?> its = data.iterator();
            while (its.hasNext()){

                //迭代器当前元素
                Object it = its.next();

                if (relations == null || relations.size() < 1){
                    continue;
                }

                if (it instanceof Map){
                    /**
                     * 暂不实现
                     * */
                }else {
                    //循环关系表
                    for (Map<String,Object> relation : relations){
                        if (relation == null){
                            continue;
                        }else {

                            Field field = it.getClass().getDeclaredField((String) relation.get("name"));
                            field.setAccessible(true);
                            Object value = field.get(it);

                            Row row;
                            //判断是否是二维表 不是 则执行一次跳出循环 否则继续执行
                            if (relation.get("isList") != null && !(boolean) relation.get("isList")){
                                row = CommonUtil.makeRow(sheet,(Integer) relation.get("rowIndex"));
                            }else {
                                row = CommonUtil.makeRow(sheet,(Integer) relation.get("rowIndex") + count);
                            }

//                            row.setRowStyle(cloneStyle);
                            Cell cell = CommonUtil.makeCell(row,(Integer) relation.get("colIndex"),null);

                            //自定义处理器处理数据
                            if (ep != null){
                                IExcelHandler handler = ep.getiExcelHandler();
                                if (handler != null && handler.getNeedHandlerFields() != null && handler.getNeedHandlerFields().length > 0) {
                                    handler.exportHandler(it, (String) relation.get("name"), value);
                                }
                            }
                            //处理日期
                            if (StringUtils.isNotBlank((String)relation.get("format"))){
                                value = CommonUtil.trans(value,(String)relation.get("format"));
                            }

                            value = value == null ? "" : value;

                            cell.setCellValue(value.toString());
                            cell.setCellStyle((CellStyle)relation.get("style"));

                        }
                    }
                }

                count++;
                its.remove();
            }
        }
        return wb;
    }

    /**
     * 获取关系map
     * get relation Map
     * */
    private List<Map<String,Object>> getRelations(Sheet sheet){
        List<Map<String,Object>> results = new ArrayList<>();
        for (int i=sheet.getFirstRowNum(); i <= sheet.getLastRowNum();i++){

            Row row = sheet.getRow(i);
            if (row.getPhysicalNumberOfCells() > 0){
                Iterator<Cell> cells = row.cellIterator();
                while (cells.hasNext()){

                    Cell cell = cells.next();
                    String value = CommonUtil.getCellValue(cell);
                    Map<String,Object> relations = new HashMap<>();

                    //判断是单元格写 或 纵向写
                    if (value != null && value.contains(DOUBLEFLAG)){

                        if (value.trim().split(SPILT).length > 1){
                            relations.put("name",value.trim().split(SPILT)[0].replace(DOUBLEFLAG,""));
                            relations.put("format",value.trim().split(SPILT)[1].replace(DOUBLEFLAG,""));
                        }else {
                            relations.put("name",value.trim().split(SPILT)[0].replace(DOUBLEFLAG,""));
                        }

                        relations.put("rowIndex",row.getRowNum());
                        relations.put("colIndex",cell.getColumnIndex());
                        relations.put("isList",true);
                        relations.put("style",cell.getCellStyle());
                        results.add(relations);
//                        if (this.getCloneStyle() == null){
//                            this.setCloneStyle(row.getRowStyle());
//                        }
//                        cells.remove();
                    }else if (value != null && value.contains(FLAG)){

                        if (value.trim().split(SPILT).length > 1){
                            relations.put("name",value.trim().split(SPILT)[0].replace(FLAG,""));
                            relations.put("format",value.trim().split(SPILT)[1].replace(FLAG,""));
                        }else {
                            relations.put("name",value.trim().split(SPILT)[0].replace(FLAG,""));
                        }

                        relations.put("rowIndex",row.getRowNum());
                        relations.put("colIndex",cell.getColumnIndex());
                        relations.put("isList",false);
                        relations.put("style",cell.getCellStyle());
                        results.add(relations);
//                        cells.remove();
//                        if (this.getCloneStyle() == null){
//                            this.setCloneStyle(row.getRowStyle());
//                        }
                    }
                }
            }
        }

        return results;
    }

    public CellStyle getCloneStyle() {
        return cloneStyle;
    }

    public void setCloneStyle(CellStyle cloneStyle) {
        this.cloneStyle = cloneStyle;
    }
}
