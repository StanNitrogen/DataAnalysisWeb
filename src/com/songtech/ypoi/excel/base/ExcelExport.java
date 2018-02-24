package com.songtech.ypoi.excel.base;

import com.songtech.ypoi.config.Config;
import com.songtech.ypoi.excel.CommonUtil;
import com.songtech.ypoi.externalInterface.IExcelExport;
import com.songtech.ypoi.externalInterface.IExcelHandler;
import com.songtech.ypoi.exception.YpoiBaseException;
import com.songtech.ypoi.params.ExcelParams;
import com.songtech.ypoi.style.DefaultExcelStyle;
import com.songtech.ypoi.style.IExcelStyle;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Create By YINN on 2018/1/3
 * 提供三种模式
 * 1.XML配置
 * 2.数据库读取配置
 * 3.注解配置
 *
 * @version 1.0.0
 */
public class ExcelExport implements IExcelExport {

    final Integer MAX_ROW = 65535;

    private IExcelStyle iExcelStyle = new DefaultExcelStyle();


    /**
     * export method
     *
     * @param wb          模板或空workbook
     * @param excelParams 导出参数
     * @param pojos       (本想写成object参数)配置Map
     * @param datas       需导出数据
     */
    @Override
    public Workbook export(Workbook wb, List<Map<String, Config>> pojos, ExcelParams excelParams, Collection<?>... datas) throws Exception {

        //参数验证 - valid
        if (pojos == null || pojos.size() < 1 || excelParams == null) {
            throw new YpoiBaseException("参数错误!");
        }

        if (pojos.size() != datas.length) {
            throw new YpoiBaseException("配置信息组与给出数据集合长度不一!");
        }

        CellStyle cs = wb.createCellStyle();

        for (Map<String, Config> configMap : pojos) {

            /**
             * 循环一次 取出配置文件(为了取配置文件中的sheetName和sheetOrder),在同一个map下 所有
             * config类中的sheetName与sheetOrder参数一样,所以取map中任意一个值就可以,故只循环一次
             * 设计上应该更改为两个配置类,呈树状结构,SheetConfig 与 参数config
             * sheetconfig中 装有一个sheet中需要导出的所有字段 List<fields>与sheetName与sheetOrder
             */
            Config conf = null;
            for (Map.Entry<String, Config> entry : configMap.entrySet()) {
                if (entry != null && entry.getValue() != null) {
                    conf = entry.getValue();
                    break;
                } else {
                    throw new YpoiBaseException("配置参数错误");
                }
            }

            //创建sheet页
            Sheet sheet = CommonUtil.makeSheet(wb, conf.getSheetName(), conf.getSheetOrder());

            Integer headerStartRow = 0;
            Integer headerStartColumn = 0;


            //创建标题
            if (conf.getTitleName() != null) {
                createTitle(sheet, excelParams, cs, conf);
                headerStartRow = conf.getTitleStartRow() + 1;
                headerStartColumn = conf.getTitleStartColumn();
            }

            //创建表头及数据单元格
            createHeaderAndDataCells(sheet, excelParams, cs, datas[conf.getSheetOrder()], configMap, headerStartColumn, headerStartRow, wb);
        }
        return wb;
    }

    /**
     * 创建表头及标题，并赋予样式
     * Create title and then set style for item.
     *
     * @param
     */
    protected void createTitle(Sheet sheet, ExcelParams excelParams, CellStyle cs, Config conf) {
        if (sheet != null) {
            //标题 title
            if (excelParams != null && conf.getTitleName() != null) {
                Cell cell;
                if (excelParams.getExcelStyleParamsVO() != null && !excelParams.getExcelStyleParamsVO().isTitleNotExist()) {
                    cell = iExcelStyle.setTitleStyle(sheet, cs, excelParams, conf);
                } else {
                    cell = iExcelStyle.setDefaultTitleStyle(sheet, cs, excelParams, conf);
                }
                cell.setCellValue(conf.getTitleName());
            }
        }
    }

    /**
     * @param titleColIndex,titleRowIndex 第一个写入的单元格的起始坐标
     */
    protected void createHeaderAndDataCells(Sheet sheet, ExcelParams ep, CellStyle cs, Collection<?> data, Object config, Integer titleColIndex, Integer titleRowIndex, Workbook wb) throws Exception {
        if (config instanceof Map<?, ?>) {

            /**
             * 迭代Collection
             * 行读取
             * （*如果ConfigMap为外层则为列读取写入，效率高但不可避免传入集合错位，并且得解决迭代器删除问题）
             */
            Iterator<?> its = data.iterator();
            Integer dataCellRowIndex = titleRowIndex + 1;

            //当时为写成通用,全部接收Object参数 进行判断转换,但考虑不够,其实外层传入obj判断就好,
            //所以此段判断冗余
            Map<String, ?> configMap = (Map<String, ?>) config;
            Font font = wb.createFont();
            while (its.hasNext()) {
                if (dataCellRowIndex >= MAX_ROW) {
                    break;
                }

                Object it = its.next();

                for (Map.Entry<String, ?> entry : configMap.entrySet()) {
                    //配置类
                    if (entry.getValue() instanceof Config) {

                        //获取配置参数类
                        Config conf = (Config) entry.getValue();

                        //取得(创建)行对象
                        Row titleRow = CommonUtil.makeRow(sheet, titleRowIndex);
                        //列坐标
                        Integer colIndex = titleColIndex + conf.getOrderNum();

                        Cell titleCell;
                        //创建表头单元格，如果表头不存在 则执行赋予样式，表头写入名称，否则不进行任何操作
                        if (titleRow.getCell(colIndex) == null) {
                            titleCell = titleRow.createCell(colIndex, CommonUtil.getCellType(1));

                            //设置单元格样式
                            if (ep.getExcelStyleParamsVO() != null && !ep.getExcelStyleParamsVO().isTableHeaderNotExist()) {
                                cs = iExcelStyle.setHeaderStyle(titleCell, cs, ep, font);
                            } else {
                                cs = iExcelStyle.setDefaultHeaderStyle(titleCell, cs, ep, font);
                            }

                            //设置样式
                            titleCell.setCellStyle(cs);
                            sheet.setColumnWidth(colIndex, conf.getWidth() * 1000);
                            //赋值
                            titleCell.setCellValue(conf.getColName());
                        }

                        //创建数据
                        if (it instanceof Map<?, ?>) {
                            // 暂不实现List<Map>形式数据
                        } else {
                            createDateCell(sheet, cs, ep, entry, dataCellRowIndex, colIndex, it, font);
                        }
                    }
                }
                //移除迭代器,读取下一行
                its.remove();
                dataCellRowIndex++;
            }
        } else {
        }
    }


    /**
     * @param sheet            当前sheet
     * @param cs               样式
     * @param ep               excel导出参数
     * @param entry            当前字段名与配置对应map实体
     * @param dataCellRowIndex 行号
     * @param colIndex         列号
     * @param it               循环迭代器中的当前一条数据
     */
    private void createDateCell(Sheet sheet, CellStyle cs, ExcelParams ep, Map.Entry<String, ?> entry,
                                Integer dataCellRowIndex, Integer colIndex, Object it, Font font) throws NoSuchFieldException, IllegalAccessException {
        //获取配置参数类
        Config conf = (Config) entry.getValue();

        //取得(创建)行对象
        Row presentDataRow = CommonUtil.makeRow(sheet, dataCellRowIndex);

        Cell cell = CommonUtil.makeCell(presentDataRow, colIndex, conf);

        //设置单元格样式
        if (ep.getExcelStyleParamsVO() != null && !ep.getExcelStyleParamsVO().isTableHeaderNotExist()) {
            cs = iExcelStyle.setDataCellsStyle(cell, cs, ep, font);
        } else {
            cs = iExcelStyle.setDefaultDataCellsStyle(cell, cs, ep, font);
        }

        //设置样式
        cell.setCellStyle(cs);

        //获取VO类
        Field field = it.getClass().getDeclaredField(entry.getKey());
        field.setAccessible(true);

        Object value = field.get(it);

//                            if (conf.getName() == "高精度" || conf.getName().equals("高精度")){
//                                System.out.println(presentDataRow.getRowNum()+","+colIndex+","+entry.getKey()+","+field.get(it));
//                            }
        //自定义处理器处理数据
        IExcelHandler handler = ep.getiExcelHandler();
        if (handler != null && handler.getNeedHandlerFields() != null && handler.getNeedHandlerFields().length > 0) {
            handler.exportHandler(it, conf.getColName(), value);
        }

        //处理日期格式
        if (field.getType().getSimpleName().equals("Date") || field.getType().getSimpleName() == "Date"
                || StringUtils.isNotBlank(conf.getExportFormatter())) {
            value = CommonUtil.trans(value, conf.getExportFormatter());
        }

        value = value == null ? "" : value;

        cell.setCellValue(value.toString());
    }

//    private void setValueForCell(Cell cell, CellStyle style, Object val, Workbook wb, Config conf) {
//
//        try {
//            if (val == null) {
//                cell.setCellValue("");
//            } else if (val instanceof String) {
//                cell.setCellValue((String) val);
//            } else if (val instanceof Integer) {
//                cell.setCellValue((Integer) val);
//            } else if (val instanceof Long) {
//                cell.setCellValue((Long) val);
//            } else if (val instanceof Double) {
//                cell.setCellValue((Double) val);
//            } else if (val instanceof Float) {
//                cell.setCellValue((Float) val);
//            } else if (val instanceof Date) {
//                DataFormat format = wb.createDataFormat();
//                if (conf.getExportFormatter() == null) {
//                    style.setDataFormat(format.getFormat(conf.getExportFormatter()));
//                } else {
//                    style.setDataFormat(format.getFormat("YYYY-MM-DD"));
//                }
//
//                cell.setCellValue((Date) val);
//            } else if (val instanceof BigDecimal) {
//                double doubleVal = ((BigDecimal) val).doubleValue();
//                DataFormat format = wb.createDataFormat();
//                style.setDataFormat(format.getFormat("￥#,##0.00"));
//                cell.setCellValue(doubleVal);
//            } else {
//                cell.setCellValue(val.toString());
////                if (fieldType != Class.class){
////                    cell.setCellValue((String)fieldType.getMethod("setValue", Object.class).invoke(null, val));
////                }else{
////                    cell.setCellValue((String)Class.forName(this.getClass().getName().replaceAll(this.getClass().getSimpleName(),
////                            "fieldtype."+val.getClass().getSimpleName()+"Type")).getMethod("setValue", Object.class).invoke(null, val));
////                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    public IExcelStyle getiExcelStyle() {
        return iExcelStyle;
    }

    public void setiExcelStyle(IExcelStyle iExcelStyle) {
        this.iExcelStyle = iExcelStyle;
    }
}
