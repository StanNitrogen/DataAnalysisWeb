package com.songtech.ypoi.style;

import com.songtech.ypoi.config.Config;
import com.songtech.ypoi.params.ExcelParams;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * Create By 33976 on 2018/1/3
 */
public class DefaultExcelStyle extends AbstractBaseStyle implements IExcelStyle {

    /**
     * 默认标题样式
     */
    @Override
    public Cell setDefaultTitleStyle(Sheet sheet, CellStyle style, ExcelParams ep, Config conf) {

        ExcelStyleParamsVO esp = super.getStyleVOBlank(ep);
        esp.setTitle_BackGroundColor(null);
        esp.setTitle_Height(super.TITLE_HEIGHT);
        esp.setTitle_Font("黑体");
        esp.setTitle_FontSize((short) 20);
        esp.setTitle_Alignment("center");

        ep.setExcelStyleParamsVO(esp);
        return super.setTitleStyle(sheet, style, ep, conf);
    }

    /**
     * 默认表头样式
     */
    @Override
    public CellStyle setDefaultHeaderStyle(Cell cell, CellStyle style, ExcelParams ep, Font font) {
        ExcelStyleParamsVO esp = super.getStyleVOBlank(ep);

        esp.setTableHeader_BackGroundColor(null);
        esp.setTableHeader_Height(super.TABLE_HEADER_HEIGHT);
        esp.setTableHeader_Font("黑体");
        esp.setTableHeader_FontSize((short) 15);
        esp.setTableHeader_Alignment("center");
        ep.setExcelStyleParamsVO(esp);

        return super.setHeaderStyle(cell, style, ep, font);
    }

    /**
     * 默认单元格样式
     */
    @Override
    public CellStyle setDefaultDataCellsStyle(Cell cell, CellStyle style, ExcelParams ep, Font font) {
        ExcelStyleParamsVO esp = super.getStyleVOBlank(ep);

        esp.setTableHeader_BackGroundColor(null);
        esp.setTableHeader_Height(super.DATA_CELLS_HEIGHT);
        esp.setTableHeader_Font("黑体");
        esp.setTableHeader_FontSize((short) 10);
        esp.setTableHeader_Alignment("center");
        ep.setExcelStyleParamsVO(esp);

        return super.setHeaderStyle(cell, style, ep, font);
    }
}
