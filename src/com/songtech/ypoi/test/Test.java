package com.songtech.ypoi.test;

import com.songtech.ypoi.config.Config;
import com.songtech.ypoi.excel.base.ExcelExport;
import com.songtech.ypoi.excel.base.ExcelImport;
import com.songtech.ypoi.externalInterface.IExcelExport;
import com.songtech.ypoi.externalInterface.IExcelImport;
import com.songtech.ypoi.params.ExcelParams;
import com.songtech.ypoi.test.testModel.TestModel;
import com.songtech.ypoi.test.testModel.TestModel2;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * Create By YINN on 2018/1/10 14:53
 * Description :
 */
public class Test {
    private static IExcelExport iExcelExport = new ExcelExport();

    public IExcelExport getiExcelExport() {
        return iExcelExport;
    }

    public void setiExcelExport(IExcelExport iExcelExport) {
        this.iExcelExport = iExcelExport;
    }


    private static IExcelImport iExcelImport = new ExcelImport();

    public IExcelImport getiExcelImport() {
        return iExcelImport;
    }

    public void setiExcelImport(IExcelImport iExcelImport) {
        this.iExcelImport = iExcelImport;
    }


    public void delFile(){
        File file=new File("E:\\test.xls");
        if(file.exists() && file.isFile())
            file.delete();
    }

    public static void main(String[] args) {
        HSSFWorkbook wb = new HSSFWorkbook();

        Config test1 = new Config("sheet1",0,"test1",0);
        Config test2 = new Config("sheet1",0,"test2",1);
        Config test3 = new Config("sheet1",0,"test3",2);
        Config gogogo = new Config("sheet1",0,"gogogo",3);

        //////////////////////-----------------------------------------------------------
        Config aDouble = new Config("sheet2",1,"双精度",0);
        aDouble.setImport(true);

        Config bigDecimal = new Config("sheet2",1,"高精度",2);
        bigDecimal.setWidth(8);
        bigDecimal.setImport(true);

        Config ceshi = new Config("sheet2",1,"测试测试ccc",4);
        ceshi.setImport(true);

        Config dateqqq = new Config("sheet2",1,"时间",1);
        dateqqq.setImport(true);

        Config ccc = new Config("sheet2",1,"随意字符串",3);

        Map<String,Config> map = new HashMap<>();
        map.put("test1",test1);
        map.put("test2",test2);
        map.put("test3",test3);
        map.put("gogogo",gogogo);

        Map<String,Config> map2 = new HashMap<>();
        map2.put("aDouble",aDouble);
        map2.put("ceshi",ceshi);
        map2.put("bigDecimal",bigDecimal);
        map2.put("dateqqq",dateqqq);
        map2.put("ccc",ccc);


        List<TestModel> datalist = new ArrayList<>();
        List<TestModel2> datalist2 =  new ArrayList<>();
            for (int j=0;j<500;j++){
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
                List<Map<String,Config>> confs = new ArrayList<>();
                confs.add(map);
                confs.add(map2);
            Workbook workbook = iExcelExport.export(wb,confs,new ExcelParams(),datalist,datalist2);
            OutputStream os = new FileOutputStream("E:\\test.xls");
            workbook.write(os);

            List<TestModel2> rList = iExcelImport.importExcel(workbook.getSheetAt(1),map2,TestModel2.class,new ExcelParams());
            os.flush();
            os.close();


        } catch (Exception e) {

            System.out.println("错误---------------------------->");
            e.printStackTrace();
        }
    }
}
