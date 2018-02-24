package com.songtech.ypoi.style.font;

import com.songtech.ypoi.style.ExcelStyleParamsVO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Create By YINN on 2018/1/5 14:55
 * Description :
 */
public interface IExcelBaseFont {

//    /**
//     * 背景色
//     */
//    public CellStyle setFontColor(CellStyle style, Cell cell);
//
//    /**
//     * 背景色
//     */
//    public CellStyle setFontColor(CellStyle style, Cell cell, Short color);
//
//    /**
//     * 设置字体
//     */
//    public CellStyle setFont(CellStyle style, ExcelStyleParamsVO esp, Workbook wb);

    /**
     * 设置字体
     */
    public boolean setFontByDiy(CellStyle style, Cell cell, ExcelStyleParamsVO esp);

    /**
     * 设置字体
     */
    public CellStyle setTitleFont(CellStyle style, Cell cell, ExcelStyleParamsVO esp, Font font);

    /**
     *
     * */
    public CellStyle setFont(CellStyle style, Cell cell, String fontname, boolean bold, Short fontSize, Short fontColor, boolean isWrap, Font font);
}
