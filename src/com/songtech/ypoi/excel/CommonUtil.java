package com.songtech.ypoi.excel;

import com.google.common.primitives.Primitives;
import com.songtech.ypoi.config.Config;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.commons.lang3.time.DateUtils.parseDate;

/**
 * Create By YINN on 2018/1/4
 */
public class CommonUtil {

    private final static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

//    /**
//     * 获取/创造 sheet，如果原模板有sheet则直接不创建，否则根据excelParams创建参数。
//     * @param excelParams
//     * @param wb
//     * @return List<Sheet> sheetList
//     */
//    public static Sheet makeSheet(Workbook wb, ExcelParams excelParams){
//        List<Sheet> sheetList = new ArrayList<>();
//        if (wb != null && wb.getNumberOfSheets() > 0){
//            for (int i=0;i<wb.getNumberOfSheets();i++){
//                sheetList.add(wb.getSheetAt(i));
//            }
//        }else {
//            if (excelParams.getSheetNum() > 0 ){
//                for (String stName : excelParams.getSheetName()){
//                    sheetList.add(wb.createSheet(stName));
//                }
//            }
//        }
//        return sheetList;
//    }

    /**
     * 获取/创造 sheet，如果原模板有sheet则直接不创建，否则创建。
     * @param wb
     * @param sheetName
     * @param sheetIndex
     * @return Sheet
     */
    public static Sheet makeSheet(Workbook wb, String sheetName, Integer sheetIndex){
        Sheet sheet;
        if (wb == null || sheetIndex > wb.getNumberOfSheets() - 1 || wb.getSheetAt(sheetIndex) == null){
           sheet = wb.createSheet(sheetName);
        }else {
            sheet = wb.getSheetAt(sheetIndex);
        }
        return sheet;
    }

    //行存在则取出 否则创建
    public static Row makeRow(Sheet sheet, Integer rowIndex){
        Row row;
        if (sheet.getRow(rowIndex) == null) {
            row = sheet.createRow(rowIndex);
        } else {
            row = sheet.getRow(rowIndex);
        }
        return row;
    }

    //单元格存在则取出 否则创建
    public static Cell makeCell(Row row, Integer colIndex, Config conf){
        Cell cell;
        if (row.getCell(colIndex) == null) {
            if (conf == null){
                cell = row.createCell(colIndex,getCellType(1));
            }else {
                cell = row.createCell(colIndex,getCellType(conf.getType()));
            }
        } else {
            cell = row.getCell(colIndex);
        }
        return cell;
    }

    /**
     * 获取单元格类型
     * set cell's type
     */
    public static CellType getCellType(Integer type) {
        CellType cellType;
        switch (type){
            case 0 :
                cellType = CellType.NUMERIC;
                break;
            case 1 :
                cellType = CellType.STRING;
                break;
            case 2 :
                cellType = CellType.FORMULA;
                break;
            case 3 :
                cellType = CellType.BLANK;
                break;
            case 4 :
                cellType = CellType.BOOLEAN;
                break;
            case 5 :
                cellType = CellType.ERROR;
                break;
            default :
                cellType = CellType.STRING;
        }
        return cellType;
    }

    /**
     * 格式化字段
     * */
    public static Object trans(Object value,String formatter){
        if (StringUtils.isBlank(formatter)){
            formatter = "YYYY-MM-DD";
        }
        if (value instanceof Date){
            SimpleDateFormat dateFormater = new SimpleDateFormat(formatter);
            value = dateFormater.format((Date)value);
        }
        return value;
    }

    /**
     * 获取单元格的值
     * */
    public static String getKeyValue(Cell cell) {
        Object obj = getCellValue(cell);
        return obj == null ? null : obj.toString().trim();
    }


    /**
     * 获取单元格的值
     * @param cell
     * @return
     */
    public static String getCellValue(Cell cell) {
        if (cell == null)
            return "";

        cell.setCellType(Cell.CELL_TYPE_STRING);
        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {

            return cell.getStringCellValue();

        } else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {

            return String.valueOf(cell.getBooleanCellValue());

        } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {

            return cell.getCellFormula();

        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {

            return String.valueOf(cell.getNumericCellValue());

        } else {
            cell.setCellType(Cell.CELL_TYPE_STRING);
            return cell.getStringCellValue();
        }
    }


    /**
     * 获取实体类中字段名 与 excel表格中列坐标对应
     * 同时包括entry<"rowIndex",rowIndex>
     */
    public static Map<String,Integer> getTableHeaderMap(Sheet sheet, Map<String,Config> configMap){
        Map<String,Integer> tableHeaderMap = new HashMap<>();

        if (configMap == null){
            return null;
        }
        //循环配置map 获取excel表头名 与 实体类字段 与 表头坐标对应关系
        if (tableHeaderMap.size() < configMap.size()){
            for (Map.Entry<String,Config> entry : configMap.entrySet()){
                if (entry.getValue().getColName() != null){
                    //循环行
                    for (int i=0; i < sheet.getLastRowNum()+1; i++){
                        if (sheet.getRow(i).getPhysicalNumberOfCells() != 0){

                            //如果已经取到表头坐标，则终止本层循环提高效率
                            if (tableHeaderMap!=null && tableHeaderMap.get(entry.getKey()) != null){
                                break;
                            }
                            Row row = sheet.getRow(i);
                            //循环列
                            Iterator<Cell> cells = row.cellIterator();
                            while (cells.hasNext()){
                                Cell cell = cells.next();
                                //如果表头与config配置中的名称对应，则向resultMap中赋值
                                if (entry.getValue().getColName() == CommonUtil.getKeyValue(cell)
                                        || entry.getValue().getColName().equals(CommonUtil.getKeyValue(cell))){

                                    //行列坐标
                                    tableHeaderMap.put(entry.getKey(),cell.getColumnIndex());
                                    if (tableHeaderMap.get("rowIndex") == null){
                                        tableHeaderMap.put("rowIndex",i);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return tableHeaderMap;
    }

    /**
     * 日期型字符串转化为日期 格式
     * it parses str to date's instance
     * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
     *   "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
     *   "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
     */
    public static Date dateParse(Object str) {
        if (str == null){
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }
//    public static

    /**
     * 通过反射赋值（同时强制转换）
     * it sets value to a instance by reflection meanwhile casting value
     * */
    public static void setByReflection(Field f, Object value, Object objInstance) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        Constructor<?> c = null;
        f.setAccessible(true);
        if (f.getType().isPrimitive()) {
            f.set(objInstance, Primitives.wrap(f.getType()).getConstructor(String.class).newInstance((String) value));
        } else if (f.getType().getSimpleName() == "Date" || "Date".equals(f.getType().getSimpleName())){
            f.set(objInstance, CommonUtil.dateParse(value));
        }else {
            try {
                c = f.getType().getConstructor(String.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            if (c == null) {
                f.set(objInstance, f.getType().cast(value));
            } else {
                f.set(objInstance, c.newInstance((String)value));
            }
        }
    }
}
