package com.songtech.ypoi.style.font;

import com.songtech.ypoi.style.ExcelStyleParamsVO;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

/**
 * Create By YINN on 2018/1/5 14:57
 * Description :
 */
public class ExcelBaseFont implements IExcelBaseFont {

    private final String DEFAULT_FONT = "黑体";

    private final short DEFAULT_FONTSIZE = 15;

    private final short DEFAULT_FONTCOLOR = HSSFColor.BLACK.index;

    public CellStyle setFont(CellStyle style, Cell cell, String fontname, boolean bold, Short fontSize, Short fontColor, boolean isWrap, Font font) {
//        Font font = cell.getSheet().getWorkbook().createFont();

        //字体
        if (fontname == null) {
            fontname = DEFAULT_FONT;
        }
        font.setFontName(fontname);

        //是否加粗
        font.setBold(bold);//粗体显示

        //字体大小
        if (fontSize == null) {
            fontSize = DEFAULT_FONTSIZE;
        }
        font.setFontHeightInPoints(fontSize);

        //字体颜色
        if (fontColor == null) {
            fontColor = DEFAULT_FONTCOLOR;
        }
        font.setColor(fontColor);

        style.setFont(font);

        style.setWrapText(isWrap);

        return style;
    }

    @Override
    public CellStyle setTitleFont(CellStyle style, Cell cell, ExcelStyleParamsVO esp, Font font) {
        //标题字体
        String titleFontName = DEFAULT_FONT;
        if (esp.getTitle_Font() != null) {
            titleFontName = esp.getTitle_Font();
        }

        //标题字体大小
        short titleFontSize = DEFAULT_FONTSIZE;
        if (esp.getTitle_FontSize() == null) {
            titleFontSize = esp.getTitle_FontSize();
        }

        //标题字体颜色
        short titleFontColor = DEFAULT_FONTCOLOR;
        if (esp.getTitle_FontColor() == null) {
            titleFontColor = esp.getTitle_FontColor();
        }

        this.setFont(style, cell, DEFAULT_FONT, esp.isTitle_IsFontBold(), titleFontSize, titleFontColor, esp.isTitle_FontWrap(), font);

        return style;
    }

    /**
     * 暂时不建议使用 会导致多个样式覆盖
     */
    @Override
    public boolean setFontByDiy(CellStyle style, Cell cell, ExcelStyleParamsVO esp) {
        boolean flag = false;
//        try {
//            cell.setCellStyle(this.setFont(style, cell, esp));
//            flag = true;
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        return flag;
    }
}
