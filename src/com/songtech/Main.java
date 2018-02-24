package com.songtech;

import com.songtech.jdbc.JDBCObject;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        try {
            Long st = System.currentTimeMillis();
//            Workbook workbook = new Anaylysis().resolve("E:\\辽宁在途订单数据v1.0 20180106.xlsx");
            Long end = System.currentTimeMillis();
            System.out.println((end - st) / 1000f + "秒");

            OutputStream os = new FileOutputStream("E:\\比对结果.xls");
//            workbook.write(os);
//
//            os.flush();
//            os.close();


        } catch (Exception e) {

            System.out.println("错误---------------------------->");
            e.printStackTrace();
        }
    }
}
