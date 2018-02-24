package com.songtech.ypoi.style.border;

import com.songtech.ypoi.style.ExcelStyleParamsVO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 * Create By YINN on 2018/1/5 14:55
 * Description :
 */
public interface IExcelBaseBorder {

    /**
     * 设置字体
     */
    public CellStyle setBorder(CellStyle style, boolean top, boolean bottom, boolean left, boolean right);

    /**
     * 设置字体
     */
    public boolean setBorderByDiy(CellStyle style, Cell cell, ExcelStyleParamsVO esp);
}
