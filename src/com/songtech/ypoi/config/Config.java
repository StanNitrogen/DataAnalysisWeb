package com.songtech.ypoi.config;

/**
 * Create By YINN on 2018/1/9 13:57
 * Description :
 */
public class Config {
    //sheet
    private String sheetName;
    private Integer sheetOrder;
    private String classPath;
    //标题
    private String titleName;
    private Integer titleStartRow;
    private Integer titleRowspan;
    private Integer titleStartColumn;
    private Integer titleColspan;
    //字段
    private String colName;
    private Integer orderNum;
    private Integer width = 4;
    //0数字，1文本，2公式，3空，4布尔
    private Integer type = 1;
    private String importFormatter;
    private String exportFormatter;
    private boolean isImport = false;

    public Config() {
    }

    public Config(String sheetName, Integer sheetOrder, String colName, Integer orderNum) {
        this.sheetName = sheetName;
        this.sheetOrder = sheetOrder;
        this.colName = colName;
        this.orderNum = orderNum;
    }

    public Config(String sheetName, Integer sheetOrder, String titleName, Integer titleStartRow, Integer titleRowspan, Integer titleStartColumn, Integer titleColspan, String colName, Integer orderNum, Integer width, Integer type, String importFormatter, String exportFormatter, boolean isImport) {
        this.sheetName = sheetName;
        this.sheetOrder = sheetOrder;
        this.titleName = titleName;
        this.titleStartRow = titleStartRow;
        this.titleRowspan = titleRowspan;
        this.titleStartColumn = titleStartColumn;
        this.titleColspan = titleColspan;
        this.colName = colName;
        this.orderNum = orderNum;
        this.width = width;
        this.type = type;
        this.importFormatter = importFormatter;
        this.exportFormatter = exportFormatter;
        this.isImport = isImport;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public Integer getSheetOrder() {
        return sheetOrder;
    }

    public void setSheetOrder(Integer sheetOrder) {
        this.sheetOrder = sheetOrder;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public Integer getTitleStartRow() {
        return titleStartRow;
    }

    public void setTitleStartRow(Integer titleStartRow) {
        this.titleStartRow = titleStartRow;
    }

    public Integer getTitleRowspan() {
        return titleRowspan;
    }

    public void setTitleRowspan(Integer titleRowspan) {
        this.titleRowspan = titleRowspan;
    }

    public Integer getTitleStartColumn() {
        return titleStartColumn;
    }

    public void setTitleStartColumn(Integer titleStartColumn) {
        this.titleStartColumn = titleStartColumn;
    }

    public Integer getTitleColspan() {
        return titleColspan;
    }

    public void setTitleColspan(Integer titleColspan) {
        this.titleColspan = titleColspan;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getImportFormatter() {
        return importFormatter;
    }

    public void setImportFormatter(String importFormatter) {
        this.importFormatter = importFormatter;
    }

    public String getExportFormatter() {
        return exportFormatter;
    }

    public void setExportFormatter(String exportFormatter) {
        this.exportFormatter = exportFormatter;
    }

    public boolean isImport() {
        return isImport;
    }

    public void setImport(boolean anImport) {
        isImport = anImport;
    }

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }
}
