package com.songtech;

import com.songtech.ypoi.excel.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.util.*;

/**
 * Create By YINN on 2018/1/26 15:31
 * Description :
 */
public class AnaUtil {

    public static Map<Integer, Map<String, Map<String, Object>>> resolveConfig(String path) throws Exception {
        Workbook wb = WorkbookFactory.create(new File(path));
        Sheet sheet = wb.getSheetAt(0);

        //获取map中 keys
        List<String> keys = new ArrayList<>();
        Row keysRow = CommonUtil.makeRow(sheet, 1);
        Iterator<Cell> it = keysRow.cellIterator();
        while (it.hasNext()) {
            Cell cell = it.next();
            keys.add(getKeyValue(cell));
            it.remove();
        }

        Map<Integer, Map<String, Map<String, Object>>> retMap = new HashMap<>();
        for (int i = 2; i < sheet.getLastRowNum() + 1; i++) {

            Row row = CommonUtil.makeRow(sheet, i);
            Map<String, Object> map = new HashMap<>();
            String fieldName = null;

            int type = 0;
            Iterator<Cell> its = row.cellIterator();
            while (its.hasNext()) {
                Cell cell = its.next();
                int index = cell.getColumnIndex();

                //普通列
                if (index > 1) {
                    map.put(keys.get(index), getKeyValue(cell));

                //字段名称
                } else if (index == 1) {
                    String fvalue = getKeyValue(cell);
                    if (StringUtils.isBlank(fvalue)) {
                        throw new Exception("配置excel中fieldname列有空值");
                    }
                    fieldName = getKeyValue(cell);

                //订单状态
                } else {
                    type = Integer.valueOf(getKeyValue(cell));
                }
                its.remove();
            }
            if (StringUtils.isNotBlank(fieldName)) {
                if (retMap.get(type) == null) {
                    Map<String, Map<String, Object>> m = new HashMap<>();
                    m.put(fieldName, map);
                    retMap.put(type, m);
                } else {
                    Map m = retMap.get(type);
                    m.put(fieldName, map);
                    retMap.put(type, m);
                }

            }
//            retMap.put()
        }

        return retMap;
    }

//    public static List<Map<String,Object>> resolveConfig(String path) throws Exception{
//        Workbook wb = WorkbookFactory.create(new File(path));
//        Sheet sheet = wb.getSheetAt(0);
//
//        //获取map中 keys
//        List<String> keys = new ArrayList<>();
//        Row keysRow = CommonUtil.makeRow(sheet,1);
//        Iterator<Cell> it = keysRow.cellIterator();
//        while (it.hasNext()){
//            Cell cell = it.next();
//            keys.add(getKeyValue(cell));
//            it.remove();
//        }
//
//        List<Map<String,Object>> results = new ArrayList<>();
//        for (int i=2;i<sheet.getLastRowNum()+1;i++){
//            Row row = CommonUtil.makeRow(sheet,i);
//            Map<String,Object> map = new HashMap<>();
//            Iterator<Cell> its = row.cellIterator();
//            while (its.hasNext()){
//                Cell cell = its.next();
//                map.put(keys.get(cell.getColumnIndex()),getKeyValue(cell));
//                its.remove();
//            }
//            results.add(map);
//        }
//
//        return results;
//    }

    public static List<Map<String, Object>> resolve(String path, Sheet sheet) throws Exception {

        //获取map中 keys
        List<String> keys = new ArrayList<>();
        Row keysRow = CommonUtil.makeRow(sheet, 0);
        Iterator<Cell> it = keysRow.cellIterator();
        while (it.hasNext()) {
            Cell cell = it.next();
            String key = CommonUtil.getKeyValue(cell);
            if (StringUtils.isNotBlank(key)) {
                keys.add(CommonUtil.getKeyValue(cell));
            }
            it.remove();
        }

        List<Map<String, Object>> results = new ArrayList<>();
        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
            Row row = CommonUtil.makeRow(sheet, i);
            Map<String, Object> map = new HashMap<>();
            Iterator<Cell> its = row.cellIterator();
            while (its.hasNext()) {
                Cell cell = its.next();
                if (cell.getColumnIndex() > keys.size() - 1) {
                    break;
                }
                map.put(keys.get(cell.getColumnIndex()), CommonUtil.getKeyValue(cell));
                its.remove();
            }
            results.add(map);
        }

        return results;
    }

    public static List<Map<String, Object>> getKeys(Sheet sheet) {
        //获取map中 keys
        List<Map<String, Object>> keys = new ArrayList<>();
        Row keysRow = CommonUtil.makeRow(sheet, 0);
        Iterator<Cell> it = keysRow.cellIterator();

        while (it.hasNext()) {
            Cell cell = it.next();
            String key = CommonUtil.getKeyValue(cell);
            if (StringUtils.isBlank(key)) {
                continue;
            }
            Map<String, Object> keyMap = new HashMap<>();
            keyMap.put("name", key);
            keyMap.put("colIndex", cell.getColumnIndex());
            keyMap.put("rowIndex", cell.getRowIndex());

            keys.add(keyMap);
        }

        return keys;
    }

    /**
     * 获取单元格的值
     */
    private static String getKeyValue(Cell cell) {
        Object obj = getCellValue(cell);
        return obj == null ? null : obj.toString().trim();
    }


    /**
     * 获取单元格的值
     *
     * @param cell
     * @return
     */
    private static String getCellValue(Cell cell) {
        if (cell == null)
            return "";

        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {

            return cell.getStringCellValue();

        } else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {

            return String.valueOf(cell.getBooleanCellValue());

        } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {

            return cell.getCellFormula();

        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {

            return String.valueOf((int) cell.getNumericCellValue());

        } else {
            cell.setCellType(Cell.CELL_TYPE_STRING);
            return cell.getStringCellValue();
        }
    }
}
