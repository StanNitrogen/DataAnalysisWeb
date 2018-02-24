package com.songtech.ypoi.excel.base;

import com.google.common.primitives.Primitives;
import com.songtech.ypoi.config.Config;
import com.songtech.ypoi.excel.CommonUtil;
import com.songtech.ypoi.exception.YpoiBaseException;
import com.songtech.ypoi.externalInterface.IExcelHandler;
import com.songtech.ypoi.externalInterface.IExcelImport;
import com.songtech.ypoi.params.ExcelParams;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Create By YINN on 2018/1/12 15:19
 * Description : common class using for import excel
 */
public class ExcelImport implements IExcelImport {

    /**
     * Import common method :
     *
     * @param clazz     return-type's class
     * @param sheet     present sheet
     * @param configMap config
     * @param ep        excel params
     */

    @Override
    public <T> List<T> importExcel(Sheet sheet, Map<String, Config> configMap, Class<T> clazz, ExcelParams ep)
            throws YpoiBaseException, IllegalAccessException, InstantiationException,
            NoSuchFieldException, NoSuchMethodException, InvocationTargetException {

        //验证 valid
        if (sheet == null) {
            return null;
        }

        //find'ut table-header and configs' relations
        Map<String, Integer> relationMap = CommonUtil.getTableHeaderMap(sheet, configMap);
        if (relationMap == null) {
            throw new YpoiBaseException("未从excel中寻找到与配置相同的属性");
        }

        List<T> resultList = new ArrayList<T>();

        //获取初始行，并移除元素 get start-row ,and then remove this element from relationMap.
        Integer startRow = relationMap.get("rowIndex") + 1;
        relationMap.remove("rowIndex");

        if (clazz.newInstance() instanceof Map<?, ?>) {

            //循环行 loop for rows
            for (int i = startRow; i < sheet.getLastRowNum() + 1; i++) {
                if (sheet.getRow(i).getPhysicalNumberOfCells() != 0) {
                    Row row = sheet.getRow(i);

                    Map<String, Object> valueMap = new HashMap<>();
                    //循环列名 loop for columns
                    for (Map.Entry<String, Integer> entry : relationMap.entrySet()) {
                        if (entry != null) {
                            valueMap.put(entry.getKey(), CommonUtil.getCellValue(row.getCell(entry.getValue())));
                        }
                    }
                    resultList.add((T) valueMap);
                }
            }

            return resultList;
        } else {

            //循环行 loop for row
            for (int i = startRow; i < sheet.getLastRowNum() + 1; i++) {
                if (sheet.getRow(i).getPhysicalNumberOfCells() != 0) {
                    Row row = sheet.getRow(i);

                    //传入泛型实例化 instance generic
                    T newT = clazz.newInstance();

                    //循环列名 loop for column
                    for (Map.Entry<String, Integer> entry : relationMap.entrySet()) {

                        //判断是否需要导入 Need't import
                        if (entry != null && configMap.get(entry.getKey()).isImport()) {
                            Field field = clazz.getDeclaredField(entry.getKey());
                            field.setAccessible(true);

                            //单元格值 cell-value
                            Object value = CommonUtil.getCellValue(row.getCell(entry.getValue()));

                            //自定义处理器处理数据 handle datas with excel-handler class
                            IExcelHandler handler = ep.getiExcelHandler();
                            if (handler != null && handler.getNeedHandlerFields() != null && handler.getNeedHandlerFields().length > 0) {
                                handler.importHandler(newT, configMap.get(entry.getKey()).getColName(), value);
                            }

                            //处理日期格式
                            if (configMap.get(entry.getKey()).getImportFormatter() != null &&
                                    configMap.get(entry.getKey()).getImportFormatter() != "") {
                                value = CommonUtil.trans(value, configMap.get(entry.getKey()).getImportFormatter());
                            }

//                            String methodName = "set" + field.getName().toUpperCase().substring(0,1) + field.getName().substring(1);
//                            Method m = clazz.getMethod(methodName,field.getType());
//                            m.setAccessible(true);
//                            m.invoke(newT,value);

                            //set value

                            //赋值
                            CommonUtil.setByReflection(field, value, newT);
//                            if (field.getType().isPrimitive()) {
//                                field.set(newT, Primitives.wrap(field.getType()).getConstructor(String.class).newInstance((String) value));
//                            } else {
//                                if (field.getType().getSimpleName() != "Date" && !"Date".equals(field.getType().getSimpleName())) {
//                                    Constructor<?> c = field.getType().getConstructor(String.class);
//                                    if (c == null) {
//                                        field.set(newT, field.getType().cast(value));
//                                    } else {
//                                        field.set(newT, c.newInstance(value.toString()));
//                                    }
//                                } else {
//                                    field.set(newT, CommonUtil.dateParse(value));
//                                }
//                            }
//                            //将单元格值赋给实体类
//                            field.set(newT, field.getType().cast(value));
                        }
                    }
                    resultList.add(newT);
                }
            }
            return resultList;
        }
    }

    /**
     * 获取sheetList get sheetList
     */
    @Override
    public List<Sheet> getSheetList(Workbook wb) {
        if (wb == null || wb.getNumberOfSheets() == 0) {
            return null;
        }

        List<Sheet> sheetList = new ArrayList<>();
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            sheetList.add(wb.getSheetAt(i));
        }
        return sheetList;
    }

}
