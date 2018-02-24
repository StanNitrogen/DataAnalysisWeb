package com.songtech.ypoi.style;

import com.songtech.ypoi.config.Config;
import com.songtech.ypoi.params.ExcelParams;
import com.songtech.ypoi.style.border.ExcelBaseBorder;
import com.songtech.ypoi.style.border.IExcelBaseBorder;
import com.songtech.ypoi.style.color.ExcelBaseColor;
import com.songtech.ypoi.style.color.IExcelBaseColor;
import com.songtech.ypoi.style.font.ExcelBaseFont;
import com.songtech.ypoi.style.font.IExcelBaseFont;
import com.songtech.ypoi.style.position.ExcelBaseAlignment;
import com.songtech.ypoi.style.position.IExcelBaseAlignment;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * Create By 33976 on 2018/1/4
 */
public abstract class AbstractBaseStyle implements IExcelStyle{

    //默认行高 default title's height
    short TITLE_HEIGHT = 5;

    //默认表头行高
    short TABLE_HEADER_HEIGHT = 7;

    //默认单元格行高
    short DATA_CELLS_HEIGHT = 9;

    private IExcelBaseColor iExcelBaseColor = new ExcelBaseColor();

    private IExcelBaseFont iExcelBaseFont = new ExcelBaseFont();

    private IExcelBaseBorder iExcelBaseBorder = new ExcelBaseBorder();

    private IExcelBaseAlignment iExcelBaseAlignment = new ExcelBaseAlignment();

    /**
     * 标题style  title's style
     * @param sheet 当前sheet对象 Present Sheet Object
     * @param style wb的样式 Workbook's style
     * @param ep excel各项参数 params of the excel
     * @param conf Config配置
     */
    @Override
    public Cell setTitleStyle(Sheet sheet, CellStyle style, ExcelParams ep, Config conf) {
        //设置表头合并 merge
        sheet.addMergedRegion(new CellRangeAddress(conf.getTitleStartRow(),
                conf.getTitleStartRow() + conf.getTitleRowspan(),
                                                conf.getTitleStartColumn(),
                conf.getTitleStartColumn() + conf.getTitleColspan()));

        //创建行 create row
        Row row = sheet.createRow(conf.getTitleStartRow());
        ExcelStyleParamsVO esp = this.getStyleVOBlank(ep);

        // 设置行高 title's row height
        if (esp.getTitle_Height() != null){
            row.setHeight(esp.getTitle_Height());
        }else {
            row.setHeight(TITLE_HEIGHT);
        }

        // 创建单元格 create cells
        Cell cell = row.createCell(conf.getTitleStartColumn(),CellType.STRING);

        // 设置背景色 set background color
        style = iExcelBaseColor.setBackgroundColor(style, esp.getTitle_BackGroundColor());

        // 设置字体样式 set title's font
        Font font = cell.getSheet().getWorkbook().createFont();
        style = iExcelBaseFont.setFont(style, cell,esp.getTitle_Font(),esp.isTitle_IsFontBold(),
                esp.getTitle_FontSize(),esp.getTitle_FontColor(),esp.isTitle_FontWrap(),font);

        // 设置边框 set title's
        style = iExcelBaseBorder.setBorder(style, esp.isTitle_HasTopBorder(),
                esp.isTitle_HasBottomBorder(),esp.isTitle_HasLeftBorder(),esp.isTitle_HasRightBorder());

        // 设置居中属性 set title's position(align)
        style = iExcelBaseAlignment.setAlignment(style, cell, esp.getTitle_Alignment());

        cell.setCellStyle(style);
        return cell;
    }

    /**
     * 表头样式
     * table-header's style
     * */
    @Override
    public CellStyle setHeaderStyle(Cell cell, CellStyle style, ExcelParams ep,Font font) {
        ExcelStyleParamsVO esp = this.getStyleVOBlank(ep);


        // 设置背景色 set background color
        style = iExcelBaseColor.setBackgroundColor(style, esp.getTableHeader_BackGroundColor());

        // 设置字体样式 set title's font
        style = iExcelBaseFont.setFont(style, cell, esp.getTableHeader_Font(),esp.isTableHeader_IsFontBold(),
                esp.getTableHeader_FontSize(),esp.getTableHeader_FontColor(),esp.isTableHeader_FontWrap(),font);

        // 设置边框 set title's
        style = iExcelBaseBorder.setBorder(style, esp.isTableHeader_HasTopBorder(),
                esp.isTableHeader_HasBottomBorder(),esp.isTableHeader_HasLeftBorder(),esp.isTableHeader_HasRightBorder());

        // 设置居中属性 set title's position(align)
        style = iExcelBaseAlignment.setAlignment(style, cell, esp.getTableHeader_Alignment());

        return style;
    }

    /**
     * 数据单元格样式
     * data-cells' style
     * */
    @Override
    public CellStyle setDataCellsStyle(Cell cell, CellStyle style, ExcelParams ep,Font font) {
        ExcelStyleParamsVO esp = this.getStyleVOBlank(ep);


        // 设置背景色 set background color
        style = iExcelBaseColor.setBackgroundColor(style, esp.getDataCell_BackGroundColor());

        // 设置字体样式 set dataCells' font
        style = iExcelBaseFont.setFont(style, cell, esp.getDataCell_Font(),esp.isDataCell_IsFontBold(),
                esp.getDataCell_FontSize(),esp.getDataCell_FontColor(),esp.isDataCell_FontWrap(),font);

        // 设置边框 set dataCells'
        style = iExcelBaseBorder.setBorder(style, esp.isDataCell_HasTopBorder(),
                esp.isDataCell_HasBottomBorder(),esp.isDataCell_HasLeftBorder(),esp.isDataCell_HasRightBorder());

        // 设置居中属性 set dataCells' alignment(align)
        style = iExcelBaseAlignment.setAlignment(style, cell, esp.getDataCell_Alignment());

        return style;
    }

    //判空返回
    protected ExcelStyleParamsVO getStyleVOBlank(ExcelParams ep){
        ExcelStyleParamsVO esp;
        if (ep.getExcelStyleParamsVO() == null){
            esp = new ExcelStyleParamsVO();
        }else {
            esp = ep.getExcelStyleParamsVO();
        }
        return esp;
    }

    public IExcelBaseColor getiExcelBaseColor() {
        return iExcelBaseColor;
    }

    public void setiExcelBaseColor(IExcelBaseColor iExcelBaseColor) {
        this.iExcelBaseColor = iExcelBaseColor;
    }

    public IExcelBaseFont getiExcelBaseFont() {
        return iExcelBaseFont;
    }

    public void setiExcelBaseFont(IExcelBaseFont iExcelBaseFont) {
        this.iExcelBaseFont = iExcelBaseFont;
    }

    public IExcelBaseBorder getiExcelBaseBorder() {
        return iExcelBaseBorder;
    }

    public void setiExcelBaseBorder(IExcelBaseBorder iExcelBaseBorder) {
        this.iExcelBaseBorder = iExcelBaseBorder;
    }

    public IExcelBaseAlignment getiExcelBaseAlignment() {
        return iExcelBaseAlignment;
    }

    public void setiExcelBaseAlignment(IExcelBaseAlignment iExcelBaseAlignment) {
        this.iExcelBaseAlignment = iExcelBaseAlignment;
    }
}
