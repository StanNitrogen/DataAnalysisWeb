package com.songtech.ypoi.style.position;

import com.songtech.ypoi.style.ExcelStyleParamsVO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 * Create By YINN on 2018/1/5 14:55
 * Description :
 */
public interface IExcelBaseAlignment {

    /**
     * 设置标题居中 靠左 靠右
     */
    public CellStyle setTitleAlignment(CellStyle style, Cell cell, ExcelStyleParamsVO esp);

    /**
     * 设置标题居中 靠左 靠右
     */
    public CellStyle setAlignment(CellStyle style, Cell cell, String position);

    /**
     * 设置居中 靠左 靠右
     */
    public boolean setAlignmentByDiy(CellStyle style, Cell cell, ExcelStyleParamsVO esp);
}
