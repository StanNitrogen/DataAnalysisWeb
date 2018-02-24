package com.songtech.ypoi.excel.handler;

import com.songtech.ypoi.externalInterface.IExcelHandler;

/**
 * Create By YINN on 2018/1/10 9:40
 * Description :
 */
public class IExcelHandlerImpl<T> implements IExcelHandler<T> {
    /**
     * 需要处理的字段
     */
    private String[] needHandlerFields;

    @Override
    public Object exportHandler(T obj, String name, Object value) {
        return value;
    }

    @Override
    public Object importHandler(T obj, String name, Object value) {
        return value;
    }


    @Override
    public String[] getNeedHandlerFields() {
        return needHandlerFields;
    }

    @Override
    public void setNeedHandlerFields(String[] needHandlerFields) {
        this.needHandlerFields = needHandlerFields;
    }
}
