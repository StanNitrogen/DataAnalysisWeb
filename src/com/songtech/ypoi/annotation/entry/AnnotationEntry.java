package com.songtech.ypoi.annotation.entry;


import com.songtech.ypoi.exception.YpoiBaseException;

import java.lang.annotation.Annotation;

/**
 * Create By 33976 on 2018/1/3
 */
public class AnnotationEntry {

//    /**
//     * 废弃
//     * */
//    public void getAnnotation(Class<?> pojo, Class<ExcelTarget> e) throws YpoiBaseException {
//        ExcelTarget et = this.getAnnotationClassObject(pojo,e);
//        //判断是二维表 或者 散表excel
//        if (et.value() == "row" || et.value().equals("row")){
//            Field[] fields = pojo.getDeclaredFields();
//            for (Field f : fields){
//                if (f.isAnnotationPresent(Excel.class)){
//                    for (Annotation an : f.getDeclaredAnnotations()){
//
//                    }
//                }
//            }
//        }
//    }


    /**
     * @param pojo 被取参数的实体类。。。
     * @param clazz 自己定义的注解接口
     */
    public static  <T extends Annotation> T getAnnotationClassObject(Class<?> pojo, Class<T> clazz) throws YpoiBaseException {
        if(pojo.isAnnotationPresent(clazz)){
            T t = pojo.getAnnotation(clazz);
            return t;
        }else {
            throw new YpoiBaseException("未定义注解!");
        }
    }


//    private
}
