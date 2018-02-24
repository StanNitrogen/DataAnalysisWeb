package com.songtech.ypoi.style.color;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 * Create By YINN on 2018/1/5 14:55
 * Description :
 */
public interface IExcelBaseColor {

    /**
     * 默认背景色
     */
    public CellStyle setBackgroundColor(CellStyle style);

    /**
     * 设置背景色
     */
    public CellStyle setBackgroundColor(CellStyle style, Short color);
}
