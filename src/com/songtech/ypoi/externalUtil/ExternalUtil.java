package com.songtech.ypoi.externalUtil;

import com.songtech.ypoi.config.Config;
import com.songtech.ypoi.excel.base.ExcelExport;
import com.songtech.ypoi.excel.base.ExcelExportByTemp;
import com.songtech.ypoi.excel.base.ExcelImport;
import com.songtech.ypoi.externalInterface.IExcelExport;
import com.songtech.ypoi.externalInterface.IExcelExportByTemp;
import com.songtech.ypoi.externalInterface.IExcelImport;
import com.songtech.ypoi.params.ExcelParams;
import com.songtech.ypoi.xml.ResolveXML;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.DocumentException;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Create By YINN on 2018/1/22 11:34
 * Description :
 */
public class ExternalUtil {
    private static IExcelExport iExcelExport = new ExcelExport();
    private static IExcelImport iExcelImport = new ExcelImport();
    private static IExcelExportByTemp iExcelExportByTemp = new ExcelExportByTemp();

    /**
     * 导出(无模板)
     */
    public static Workbook exportExcel(String xmlUrl, ExcelParams ep, Collection<?>... datas) throws Exception {

        //读取配置
        List<Map<String, Config>> configs = getConfigs(xmlUrl);

        //判断excel版本
        if (ep == null) {
            ep = new ExcelParams();
        }
        String excelVersion = ep.getExcelVersion();

        Workbook wb = null;
        if (excelVersion != null) {
            if (excelVersion == "xls" || "xls".equals(excelVersion)) {
                wb = new HSSFWorkbook();
            } else if (excelVersion == "xlsx" || "xlsx".equals(excelVersion)) {
                wb = new XSSFWorkbook();
            } else {
                wb = new SXSSFWorkbook();
            }
        }else {
            wb = new HSSFWorkbook();
        }

        if (configs.size() != datas.length) {
            throw new Exception("配置信息与给出数据长度不一致");
        }

        return iExcelExport.export(wb, configs, ep, datas);
    }

    /**
     * 导入
     */
    public static <T> List<T> importExcel(Sheet sheet, Map<String, Config> configMap, Class<T> clazz,
                                          ExcelParams ep) throws Exception {
        return iExcelImport.importExcel(sheet, configMap, clazz, ep);
    }

    /**
     * 导入
     */
    public static <T> List<T> importExcel(String xmlUrl, Workbook wb, int sheetNum, Class<T> clazz,ExcelParams ep) throws Exception {
        //读取配置
        List<Map<String, Config>> configs = getConfigs(xmlUrl);
        return iExcelImport.importExcel(wb.getSheetAt(sheetNum), configs.get(sheetNum), clazz, ep);
    }

    /**
     * 读取全部excel
     */
    public static <T> List<List<T>> importExcelForAll(String xmlUrl, InputStream is, Class<T> clazz, ExcelParams ep) throws Exception {

        Workbook wb = WorkbookFactory.create(is);
        //读取配置
        List<Map<String, Config>> configs = getConfigs(xmlUrl);
        List<List<T>> result = new ArrayList<>();
        for (int i = 0; i<configs.size(); i++){
            result.add(iExcelImport.importExcel(wb.getSheetAt(i), configs.get(i), clazz, ep));
        }
        return result;
    }

    /**
     * 读取全部excel
     */
    public static <T> List<List<T>> importExcelForAll(String xmlUrl, File file, Class<T> clazz, ExcelParams ep) throws Exception {
        return importExcelForAll(xmlUrl, new FileInputStream(file), clazz,ep);
    }

    /**
     * 通过模板导出
     */
    public static Workbook exportExcelByTemp(String tempUrl,ExcelParams ep, Collection<?>... datas) throws Exception {
        return iExcelExportByTemp.exportByTemp(new FileInputStream(tempUrl), ep, datas);
    }

    /**
     * 读取配置 获取配置文件list信息
     */
    public static List<Map<String,Config>> getConfigs(String xmlUrl) throws IllegalAccessException, InstantiationException, FileNotFoundException, NoSuchMethodException, DocumentException, InvocationTargetException, NoSuchFieldException {
        return ResolveXML.resolve(xmlUrl);
    }

    /**
     * 获取list<sheet>
     */
    public static List<Sheet> getSheetList(Workbook wb){
        return iExcelImport.getSheetList(wb);
    }

    /**
     * 通过流获取workbook
     */
    public static Workbook getWorkBookBySteam(InputStream is) throws IOException, InvalidFormatException {
        return WorkbookFactory.create(is);
    }

    /***************************************** GET - SET *******************************************/
    public static IExcelExport getiExcelExport() {
        return iExcelExport;
    }

    public static void setiExcelExport(IExcelExport iExcelExport) {
        ExternalUtil.iExcelExport = iExcelExport;
    }

    public static IExcelImport getiExcelImport() {
        return iExcelImport;
    }

    public static void setiExcelImport(IExcelImport iExcelImport) {
        ExternalUtil.iExcelImport = iExcelImport;
    }

    public static IExcelExportByTemp getiExcelExportByTemp() {
        return iExcelExportByTemp;
    }

    public static void setiExcelExportByTemp(IExcelExportByTemp iExcelExportByTemp) {
        ExternalUtil.iExcelExportByTemp = iExcelExportByTemp;
    }
}
