package com.songtech.ypoi.params;


import com.songtech.ypoi.externalInterface.IExcelHandler;
import com.songtech.ypoi.style.ExcelStyleParamsVO;
import org.apache.poi.ss.formula.functions.T;

/**
 * Create By YINN on 2018/1/3
 */
public class ExcelParams {
//    //excel样式接口
//    private IExcelStyle iexcelStyle;

    //excel数据处理器接口
    private IExcelHandler iExcelHandler;

    //样式model
    private ExcelStyleParamsVO excelStyleParamsVO;

    //excel版本
    private String excelVersion;

    public IExcelHandler getiExcelHandler() {
        return iExcelHandler;
    }

    public void setiExcelHandler(IExcelHandler iExcelHandler) {
        this.iExcelHandler = iExcelHandler;
    }

    public ExcelStyleParamsVO getExcelStyleParamsVO() {
        return excelStyleParamsVO;
    }

    public void setExcelStyleParamsVO(ExcelStyleParamsVO excelStyleParamsVO) {
        this.excelStyleParamsVO = excelStyleParamsVO;
    }

    public String getExcelVersion() {
        return excelVersion;
    }

    public void setExcelVersion(String excelVersion) {
        this.excelVersion = excelVersion;
    }
}
