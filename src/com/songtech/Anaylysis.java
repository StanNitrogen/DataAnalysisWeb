package com.songtech;

import com.songtech.cache.AnalysisCache;
import com.songtech.cache.CacheManager;
import com.songtech.jdbc.JDBCObject;
import com.songtech.ypoi.excel.CommonUtil;
import com.songtech.ypoi.style.border.ExcelBaseBorder;
import com.songtech.ypoi.style.border.IExcelBaseBorder;
import com.songtech.ypoi.style.color.ExcelBaseColor;
import com.songtech.ypoi.style.color.IExcelBaseColor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Create By YINN on 2018/1/26 14:25
 * Description : data-grafted's tool
 */
public class Anaylysis {

    private List<List<String>> cacheList = new ArrayList<>();
    private IExcelBaseColor iExcelBaseColor = new ExcelBaseColor();
    private IExcelBaseBorder iExcelBaseBorder = new ExcelBaseBorder();
    private String cacheMsg = "";
    final static String LINE_BREAK = "\n";

    //错误计算map
    private Map<Integer, Map<String, Integer>> errMap = new HashMap<>();

    //总数
    private int innTotal = 0;
    //总数
    private int sqlTotal = 0;

    //统计
    private Map<Integer, Map<String, Integer>> statisMap = new HashMap();

    //excel 数据字典模板
    private String mPath = System.getProperty("user.dir") + "\\resource\\数据字典模板.xlsx";

    public Anaylysis() {
    }

    public Anaylysis(String mPath) {
        this.mPath = mPath;
    }

    public Workbook resolve(InputStream is) throws Exception {
        Workbook wb = null;
        try {
            wb = WorkbookFactory.create(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //读取配置文件
//        Map<Integer, Map<String, Map<String, Object>>> configsMap = AnaUtil.resolveConfig(this.mPath);

        Map<Integer, Map<String, Map<String, Object>>> configsMap =
                (Map<Integer, Map<String, Map<String, Object>>>)CacheManager.getValue("configs");
        //生成背景色
        CellStyle blackBgcolor = wb.createCellStyle();
        blackBgcolor = iExcelBaseColor.setBackgroundColor(blackBgcolor, IndexedColors.YELLOW.getIndex());
        CellStyle border = wb.createCellStyle();
        border = iExcelBaseBorder.setBorder(border, true, true, true, true);

//        JDBCObject jdbcObject = new JDBCObject();
        //遍历sheet
        Map<String, Integer> sumMap = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            Map<String, Map<String, Object>> configs = configsMap.get(i);

            //清空本类缓存
            errMap.clear();
            innTotal = 0;
            sqlTotal = 0;

            Sheet sheet = wb.getSheetAt(i);
            List<Map<String, Object>> keys = AnaUtil.getKeys(sheet);
            List<String> caches = new ArrayList<>();

            int sum = 0;

            for (int r = 1; r < sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row.getPhysicalNumberOfCells() < 1) {
                    continue;
                }

                sum++;
//                String lastErrorMsg = "";
                StringBuffer sb = new StringBuffer("");
                Iterator<Cell> its = row.cellIterator();
                while (its.hasNext()) {
                    Cell cell = its.next();
                    int colIndex = cell.getColumnIndex();

                    if (colIndex > keys.size() - 1) {
                        break;
                    }


                    String fieldName = (String) keys.get(colIndex).get("name");
                    String value = CommonUtil.getKeyValue(cell);


                    if (colIndex == 0) {
                        caches.add(value);
                    }

                    if (configs.get(fieldName) == null) {
                        continue;
                    }

                    //生成错误信息
                    StringBuffer msg = new StringBuffer("");
                    msg.append(compare(configs.get(fieldName), value, cell, fieldName, errMap.get(colIndex)));
//                    msg.append(compare(configs.get(fieldName), value, cell, jdbcObject, fieldName, errMap.get(colIndex)));
                    if (StringUtils.isNotBlank(msg.toString())) {
                        sb.append(msg);
                        cell.setCellStyle(blackBgcolor);
                    }

//                    its.remove();
                }

                //生成错误报告
                Cell msgCell = CommonUtil.makeCell(row, Integer.valueOf(row.getLastCellNum()), null);
                msgCell.setCellStyle(border);

                if (StringUtils.isNotBlank(sb.toString())) {
                    msgCell.setCellValue(sb.toString());
                }
            }
            //设置错误列宽
            sheet.setColumnWidth(sheet.getRow(0).getLastCellNum() + 1, 30);

            //错误率行
            int newRowNum = sheet.getLastRowNum();
            int isNotNullSum = 0;
            int sqlErrorSum = 0;
            Row errowRow = CommonUtil.makeRow(sheet, newRowNum);
            for (Map.Entry<Integer, Map<String, Integer>> entry : errMap.entrySet()) {

                Map<String, Integer> errEntry = entry.getValue();
                if (errEntry == null) {
                    continue;
                }

                Cell c = CommonUtil.makeCell(errowRow, entry.getKey(), null);
                c.setCellStyle(border);

                StringBuffer sb = new StringBuffer();

                for (Map.Entry<String, Integer> e : errEntry.entrySet()) {
                    int value = e.getValue() == null ? 0 : e.getValue();
                    String key = e.getKey();

                    int errorTotal = 0;
                    if (key.equals("非空错误")) {
                        isNotNullSum += value;
                        errorTotal = innTotal;
                    } else if (key.equals("字典验证错误")) {
                        sqlErrorSum += value;
                        errorTotal = sqlTotal;
                    }
                    sb.append(key);
                    sb.append("总数:");
                    sb.append(value);
                    sb.append(",");
                    sb.append(key);
                    sb.append("错误率:");
                    if (sum != 0) {
                        sb.append(value * 100 / errorTotal);
                        sb.append("%");
                    } else {
                        sb.append("0%");
                    }
                    sb.append("\n");
                }

//                String msg = "错误总数:" + entry.getValue() + LINE_BREAK + "错误率:" + (entry.getValue() / sum) * 100 + "%";
                c.setCellValue(sb.toString());
            }

            Map<String, Integer> map = null;
            if (statisMap == null || statisMap.get(i) == null) {
                map = new HashMap<>();
            } else {
                map = statisMap.get(i);
            }

            map.put("isNotNullErrorNum", isNotNullSum);
            map.put("sqlErrorSum", sqlErrorSum);
            map.put("innTotal",innTotal);
            map.put("sqlTotal",sqlTotal);
            if (map == null || map.get("sum") == null) {
                map.put("total", sum);
            }

            statisMap.put(i, map);

            sumMap.put(sheet.getSheetName(), sum);
            cacheList.add(caches);
        }

//        jdbcObject.destroy();

        String errorReport = validConsistency(cacheList, cacheList.size() - 1);

        //一致性检测报告
        createReport(wb, errorReport, sumMap);

        return wb;
    }

    private void createReport(Workbook wb, String errorReport, Map<String, Integer> sumMap) {
        //一致性校验
        Sheet errorSheet = CommonUtil.makeSheet(wb, "错误报告", wb.getNumberOfSheets());
        Row erow = CommonUtil.makeRow(errorSheet, 0);
        erow.setHeight((short) 1000);
        Cell hearder_ecell0 = CommonUtil.makeCell(erow, 0, null);
        hearder_ecell0.setCellValue("一致性校验：");

        String report[] = errorReport.split("\n");
        for (int i = 0; i < report.length; i++) {
            Cell ecell0 = CommonUtil.makeCell(erow, i + 1, null);
            ecell0.setCellValue(report[i]);
            errorSheet.setColumnWidth(i + 1, 4000);
        }


        //数据总量统计
        Row row = CommonUtil.makeRow(errorSheet, 1);
        int index = 0;
        for (Map.Entry<String, Integer> entry : sumMap.entrySet()) {
            Cell cell0 = CommonUtil.makeCell(row, 2 * index, null);
            cell0.setCellValue(entry.getKey() + ",数据总数:");
            Cell cell1 = CommonUtil.makeCell(row, 2 * index + 1, null);
            cell1.setCellValue(entry.getValue());
            index++;
        }

    }

    //map 计算
    private Map<String, Integer> calculation(Map<String, Integer> calMap, String errorType) {
        //每列错误总数
        if (calMap != null) {
            Integer errorCount = calMap.get(errorType);
            if (errorCount != null) {
                errorCount++;
                calMap.put(errorType, errorCount);
            } else {
                calMap.put(errorType, 1);
            }
        } else {
            calMap = new HashMap<>();
            calMap.put(errorType, 1);
        }
        return calMap;
    }

    //验证
    private String compare(Map<String, Object> conf, Object value, Cell cell, String colName, Map<String, Integer> calMap) throws Exception {
        StringBuffer error = new StringBuffer("");

        String isNotNull = (String) conf.get("isNotNull");
        if (StringUtils.isNotBlank(isNotNull)) {
            String rs = isNotNull(isNotNull, value);
            if (StringUtils.isNotBlank(rs)) {
                error.append(rs);
                error.append(LINE_BREAK);
                calMap = calculation(calMap, "非空错误");
            }
        }

        String sql = (String) conf.get("sql");
        if (StringUtils.isNotBlank((String) value)) {
            if (StringUtils.isNotBlank(sql)) {
                String rs = sqlValid(sql, value);
                if (StringUtils.isNotBlank(rs)) {
                    error.append(rs);
                    error.append(LINE_BREAK);
                    calMap = calculation(calMap, "字典验证错误");
                }
            }
        }
        if (StringUtils.isNotBlank(error)) {
            error.insert(0, "当前行" + colName + "列：");
            error.append(LINE_BREAK);
            errMap.put(cell.getColumnIndex(), calMap);
        }


        return error.toString();
    }

    //类型验证
    private String validType(String type, Object value, Cell cell, CellStyle style) {
        String msg = "";
        if (type == null || value == null) {
            return msg;
        } else {
            type = type.toLowerCase();
        }

        switch (type) {
            case "int":
                if (!Pattern.compile("^[-\\+]?[\\d]*$").matcher(value.toString()).matches()) {
                    cell.setCellStyle(style);
                    msg = "单元格值类型不匹配，应为int";
                }
                break;
            //不严谨判断
            case "float":
                if (!Pattern.compile("^[-\\+]?[.\\d]*$").matcher(value.toString()).matches()) {
                    cell.setCellStyle(style);
                    msg = "单元格值类型不匹配,应为float";
                }
                break;
            case "datetime":
                String rexp = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
                if (!Pattern.compile(rexp).matcher(value.toString()).matches()) {

                }
                break;
        }

        return msg;
    }

    //判空
    private String isNotNull(String inn, Object value) {
        String msg = "";
        innTotal++;
        if (inn != null && "Y".equals(inn)) {
            if (value == null || "".equals(value.toString())) {
//                cell.setCellStyle(style);
                msg = "单元格值有空值或空串，但配置中此字段非空";
            }
        }
        return msg;
    }

    //对比数据库
    private String sqlValid(String sql, Object value) throws Exception {
        String msg = "";
        if (StringUtils.isBlank(sql)) {
            return msg;
        }

        sqlTotal++;
//        List results = jdbcObject.query(sql);
        List<String> results = (List<String>) CacheManager.getValue(sql);
        if (results == null || results.size() < 1) {
            throw new Exception(sql + "未在数据库中查询出任何值");
        }

        boolean flag = false;
        for (String result : results) {
            if (value.toString().equals(result)) {
                flag = true;
                break;
            }
        }

        if (!flag) {
            msg = "当前字段未从字典中查出对应值";
//            cell.setCellStyle(style);
        }
        return msg;
    }

    //数据一致性 递归检验
    private String validConsistency(List<List<String>> caches, int i) {
        StringBuffer sb = new StringBuffer(cacheMsg);

        if (i > 0) {
            Map<String, Integer> map = null;
            if (statisMap == null || statisMap.get(i) == null) {
                map = new HashMap<>();
            } else {
                map = statisMap.get(i);
            }

            //循环检测
            List<String> present = caches.get(i);
            int errorSum = 0;
            for (int j = 0; j < i; j++) {
                for (String value : present) {
                    if (!contains(caches.get(j), value)) {
                        sb.append("第");
                        sb.append(i + 1);
                        sb.append("sheet页,");
                        sb.append(value);
                        sb.append("此条数据不存在于第");
                        sb.append(j + 1);
                        sb.append("sheet页中\n");
                        errorSum++;
                    }
                }
            }

            map.put("consistence", errorSum);
            statisMap.put(i, map);
            i--;
            cacheMsg = sb.toString();
            return validConsistency(caches, i);
        } else {
            cacheMsg = sb.toString();
            return cacheMsg;
        }
    }

    //包含方法
    private boolean contains(List<String> cpList, String value) {
        boolean flag = false;
        for (String comp : cpList) {
            if (value.equals(comp)) {
                flag = true;
            }
        }
        return flag;
    }

    public Map<Integer, Map<String, Integer>> getStatisMap() {
        return statisMap;
    }
}
