package com.songtech.ypoi.test;

import com.songtech.ypoi.xml.ResolveXML;
import org.dom4j.DocumentException;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;

/**
 * Create By YINN on 2018/1/23 9:24
 * Description :
 */
public class ReadXML {

    private static String xml_url = "C:\\Users\\33976\\IdeaProjects\\src\\sys_core\\src\\main\\java\\com\\songtech\\core\\ypoi\\xml\\excel-test.xml";

    public static void main(String[] args) throws FileNotFoundException, IllegalAccessException, DocumentException, NoSuchFieldException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        long startTime = System.currentTimeMillis();
        ResolveXML.resolve(xml_url);
        long endTime = System.currentTimeMillis();
        float seconds = (endTime - startTime) / 1000F;
        System.out.println("执行时间:共"+seconds/60+"min");
    }
}
