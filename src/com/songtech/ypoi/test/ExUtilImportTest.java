package com.songtech.ypoi.test;

import com.songtech.ypoi.config.Config;
import com.songtech.ypoi.externalUtil.ExternalUtil;
import com.songtech.ypoi.params.ExcelParams;
import com.songtech.ypoi.test.testModel.TestModel;
import com.songtech.ypoi.test.testModel.TestModel2;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

/**
 * Create By YINN on 2018/1/25 14:36
 * Description :
 */
public class ExUtilImportTest {

    private final static String XML_URL = "C:\\Users\\33976\\IdeaProjects\\src\\sys_core\\src\\main\\java\\com\\songtech\\core\\ypoi\\xml\\ex-util-test.xml";

    public static void main(String[] args) throws Exception {
        Workbook wb = ExternalUtil.getWorkBookBySteam(new FileInputStream(new File("E:\\exUtileByTemp-test.xls")));
        List<Map<String,Config>> configs = ExternalUtil.getConfigs(XML_URL);
        Class<?>[] classes = {TestModel.class,TestModel2.class};
        for (int i=0; i<wb.getNumberOfSheets();i++){
            long st = System.currentTimeMillis();
            List list = ExternalUtil.importExcel(wb.getSheetAt(i),configs.get(i),classes[i],new ExcelParams());
            long end = System.currentTimeMillis();
            System.out.println("共："+(end - st)/1000F +"秒");
        }
    }
}
