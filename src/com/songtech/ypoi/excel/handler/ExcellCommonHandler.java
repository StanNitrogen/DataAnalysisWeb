package com.songtech.ypoi.excel.handler;

import com.songtech.ypoi.excel.handler.handlerModel.ExportObject;

import java.util.List;
import java.util.Map;

/**
 * Create By YINN on 2018/1/10 14:26
 * Description :
 */
public class ExcellCommonHandler<T> extends IExcelHandlerImpl<T>{

    /**
     * 需要处理的list集合
     * 其中Map中 KEY为 param数组中的值, VALUE 为转换List<ExportObject>
     */
    private Map<String,List<ExportObject>> handlerMap;

    /**
     * 需要处理的参数名
     * 数组中的值为Excel中的表头, 也为@Excel(name = 'xxx')中的name
     */
    private String[] paramArr;

    /**
     * @Constructor
     * */
    public ExcellCommonHandler(Map<String,List<ExportObject>> handlerMap, String[] paramArr){
        this.handlerMap = handlerMap;
        this.paramArr = paramArr;
    }

    /**
     * 导出自定义数据处理对象
     * @param obj <T>处理泛型
     * @param name 当前字段名称
     * @param value 当前字段值
     * @return
     */
    @Override
    public Object exportHandler(T obj, String name, Object value) {
        for (int i=0 ; i < paramArr.length; i++){
            if ((paramArr[i]).equals(name)) {
                //通用处理器
                for (ExportObject eo : (List<ExportObject>)handlerMap.get(paramArr[i])) {
                    if (value != null){
                        if (eo.getId() != null && eo.getId().trim().equals(value.toString().trim())){
                            return eo.getName();
                        }
                    }else {
                        return "-";
                    }
                }
            }
        }
        return super.exportHandler(obj, name, value);
    }

    /**
     * 导入自定义数据处理对象
     * @param obj <T>处理泛型
     * @param name 当前字段名称
     * @param value 当前字段值
     * @return
     */
    @Override
    public Object importHandler(T obj, String name, Object value) {
        for (int i=0 ; i < paramArr.length; i++){
            if ((paramArr[i]).equals(name)) {
                for (ExportObject eo : (List<ExportObject>)handlerMap.get(paramArr[i])) {
                    if (value != null){
                        if (eo.getName() != null && eo.getName().trim().equals(value.toString().trim())){
                            return eo.getId();
                        }
                    }else {
                        return "";
                    }
                }
            }
        }
        return super.importHandler(obj, name, value);
    }
}
