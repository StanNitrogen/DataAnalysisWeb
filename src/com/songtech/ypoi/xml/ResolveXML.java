package com.songtech.ypoi.xml;

import com.songtech.ypoi.config.Config;
import com.songtech.ypoi.excel.CommonUtil;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Create By YINN on 2018/1/22 15:23
 * Description :
 */
public class ResolveXML {

    /**
     * @Method resolve method
     */
    public static List<Map<String, Config>> resolve(String xmlPath) throws FileNotFoundException, DocumentException, NoSuchFieldException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        InputStream is = new FileInputStream(new File(xmlPath));
        if (is == null) {
            return null;
        }

        //创建读取器
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(is);

        //获取根节点
        Element root = document.getRootElement();
        List<Element> sheets = root.element("Sheets").elements("Sheet");

        List<Map<String, Config>> result = new ArrayList<>();
        Integer sheetOrder = 0;
        for (Element sheet : sheets) {

            //获取sheet属性
            List<Attribute> sheetAttrs = sheet.attributes();

            //获取col元素list
            List<Element> cols = sheet.element("Columns").elements("column");

            LinkedHashMap<String, Config> colMap = new LinkedHashMap<>();

            for (Element col : cols) {
                String key = col.element("fieldName").getText();
                Config conf = new Config();

                //获取元素的迭代器 循环遍历
                Iterator<Element> it = col.elementIterator();
                setValByIterator(it, conf);

                //获取标题迭代器 循环遍历赋值
                if (sheet.element("Titles") != null && sheet.element("Titles").element("title") != null){
                    //获取title元素
                    Element title = sheet.element("Titles").element("title");
                    Iterator<Element> titleIt = title.elementIterator();
                    setValByIterator(titleIt, conf);
                }

                for (Attribute attr : sheetAttrs) {
                    Field f = Config.class.getDeclaredField(attr.getName());
                    f.setAccessible(true);
                    CommonUtil.setByReflection(f, attr.getText(), conf);
                }
                colMap.put(key, conf);
            }

            result.add(sort(colMap,new HashMap<String, Config>()));
        }

        return result;
    }


    //迭代赋值
    private static void setValByIterator(Iterator it, Config conf) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        while (it.hasNext()) {
            Element el = (Element) it.next();
            //向conf中存储值
            if (!el.getName().equals("fieldName") && el.getName() != "fieldName") {
                Field f = Config.class.getDeclaredField(el.getName());
                f.setAccessible(true);
                CommonUtil.setByReflection(f, el.getText(), conf);
            }
            it.remove();
        }
    }


    /**
     * @Method recursion-sort
     * @param tarMap Map which it needs to be sorted
     * @param orderMap result
     */
    private static Map<String, Config> sort(LinkedHashMap<String, Config> tarMap, Map<String, Config> orderMap) {
        String key = null;
        Config preConfig = null;
        int value = -1;

        //循环寻找最大的orderNum
        for (Iterator it = tarMap.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, Config> entry = (Map.Entry<String, Config>) it.next();
            Config conf = entry.getValue();
            if (conf != null && conf.getOrderNum() != null && conf.getOrderNum() > value) {
                key = entry.getKey();
                value = conf.getOrderNum();
                preConfig = conf;
            }
        }


        //如果找到最大的排序号
        if (key != null) {
            preConfig.setOrderNum(tarMap.size() - 1);
            orderMap.put(key, preConfig);
            tarMap.remove(key);

            //递归
            if (tarMap.size() > 0) {
                return sort(tarMap, orderMap);
            }else {
                return orderMap;
            }
        } else {

            int order = 0;
            //如果剩余所有conf中排序号都为空，则直接执行排序
            for (Iterator it = tarMap.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Config> entry = (Map.Entry<String, Config>) it.next();
                Config conf = entry.getValue();
                conf.setOrderNum(order);
                orderMap.put(entry.getKey(), conf);
                order++;
            }

            return orderMap;
        }
    }
}
