package com.songtech.ypoi.style;


import com.songtech.ypoi.config.Config;
import com.songtech.ypoi.params.ExcelParams;
import org.apache.poi.ss.usermodel.*;

/**
 * Create By 33976 on 2018/1/4
 */
public interface IExcelStyle {

    public Cell setTitleStyle(Sheet sheet, CellStyle style, ExcelParams ep, Config config);

    public Cell setDefaultTitleStyle(Sheet sheet, CellStyle style, ExcelParams ep, Config config);

    public CellStyle setHeaderStyle(Cell cell, CellStyle style, ExcelParams ep, Font font);

    public CellStyle setDefaultHeaderStyle(Cell cell, CellStyle style, ExcelParams ep, Font font);

    public CellStyle setDataCellsStyle(Cell cell, CellStyle style, ExcelParams ep, Font font);

    public CellStyle setDefaultDataCellsStyle(Cell cell, CellStyle style, ExcelParams ep, Font font);

//    public void setStyle(Workbook wb , ExcelParams ep);
//
//    public void setStyle(Sheet st , ExcelParams ep);
//
//    public void setStyle(Row row , ExcelParams ep);
//
//    public void setStyle(Cell cell , ExcelParams ep);
}
