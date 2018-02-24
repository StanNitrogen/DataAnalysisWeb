package com.songtech.ypoi.test;

import com.songtech.ypoi.externalUtil.ExternalUtil;
import com.songtech.ypoi.params.ExcelParams;
import com.songtech.ypoi.test.testModel.TestModel;
import com.songtech.ypoi.test.testModel.TestModel2;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Create By YINN on 2018/1/24 16:03
 * Description :
 */
public class ExUtilTest {

    private static String xml_url = "C:\\Users\\33976\\IdeaProjects\\src\\sys_core\\src\\main\\java\\com\\songtech\\core\\ypoi\\xml\\ex-util-test.xml";

    public static void main(String[] args) {

        List<TestModel> datalist = new ArrayList<>();
        List<TestModel2> datalist2 =  new ArrayList<>();
        for (int j=0;j<9999;j++){
            TestModel tm = new TestModel();
            tm.setGogogo("goString"+j);
            tm.setTest1("string"+j);
            tm.setTest2(j);
            tm.setTest3(new Date());
            datalist.add(tm);

            TestModel2 tm2 = new TestModel2();
            tm2.setaDouble(new Double(j+1.101));
            tm2.setCeshi("测试:"+j);
            tm2.setBigDecimal(new BigDecimal(222.222222D+j));
            tm2.setCcc(Integer.toString(j+1000));
            tm2.setDateqqq(new Date());

            datalist2.add(tm2);
        }

        try {
            long startTime = System.currentTimeMillis();

            Workbook workbook = ExternalUtil.exportExcel(xml_url,new ExcelParams(), datalist, datalist2);

            long endTime = System.currentTimeMillis();
            float seconds = (endTime - startTime) / 1000F;
            System.out.println("执行时间:共執行"+seconds/60+"min。  共:"+ seconds + "秒");

            OutputStream os = new FileOutputStream("E:\\exUtile-test.xls");
            workbook.write(os);

//            List<TestModel2> rList = iExcelImport.importExcel(workbook.getSheetAt(1),map2,TestModel2.class,new ExcelParams());
            os.flush();
            os.close();


        } catch (Exception e) {

            System.out.println("错误---------------------------->");
            e.printStackTrace();
        }

    }
}
