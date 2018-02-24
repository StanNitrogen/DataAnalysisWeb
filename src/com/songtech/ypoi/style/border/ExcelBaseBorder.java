package com.songtech.ypoi.style.border;

import com.songtech.ypoi.style.ExcelStyleParamsVO;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

/**
 * Create By YINN on 2018/1/5 14:57
 * Description :
 */
public class ExcelBaseBorder implements IExcelBaseBorder{

//    private CellStyle setBorder(CellStyle style, ExcelStyleParamsVO esp){
//        return style;
//    }

    @Override
    public CellStyle setBorder(CellStyle style, boolean top, boolean bottom, boolean left, boolean right) {

        if (top) style.setBorderTop(BorderStyle.THIN);//上边框

        if (bottom) style.setBorderBottom(BorderStyle.THIN); //下边框

        if (left) style.setBorderLeft(BorderStyle.THIN);//左边框

        if (right) style.setBorderRight(BorderStyle.THIN);//右边框

        return style;
    }

    @Override
    public boolean setBorderByDiy(CellStyle style, Cell cell, ExcelStyleParamsVO esp) {
        return false;
    }
}
