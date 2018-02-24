package com.songtech.ypoi.style;


import org.apache.commons.lang3.StringUtils;

/**
 * Create By 33976 on 2018/1/3
 * 所有样式参数实体类ValueObject
 */
public class ExcelStyleParamsVO {

    /**
     * 表头
     */
    private Short tableHeader_Height; //行高
    private String tableHeader_Font; //字体
    private Short tableHeader_FontSize; //字体大小
    private Short tableHeader_FontColor; //字体颜色
    private boolean tableHeader_FontWrap = true; //是否换行
    private boolean tableHeader_IsFontBold = true; //字体是否加粗
    private Short tableHeader_BackGroundColor; //背景色
    private boolean tableHeader_HasTopBorder = true; //上边框
    private boolean tableHeader_HasBottomBorder = true; //下边框
    private boolean tableHeader_HasLeftBorder = true; //左边框
    private boolean tableHeader_HasRightBorder = true; //右边框
    private String tableHeader_Alignment; //字体位置（居中center，靠左left，靠右right）

    /**
     * 标题
     */
    private Short title_Height;
    private String title_Font;
    private Short title_FontSize;
    private Short title_FontColor;
    private boolean title_FontWrap = true;
    private boolean title_IsFontBold = true;
    private Short title_BackGroundColor;
    private boolean title_HasTopBorder = true;
    private boolean title_HasBottomBorder = true;
    private boolean title_HasLeftBorder = true;
    private boolean title_HasRightBorder = true;
    private String title_Alignment;

    /**
     * 数据
     */
    private Short dataCell_RowHeight;
    private String dataCell_Font;
    private Short dataCell_FontSize;
    private Short dataCell_FontColor;
    private boolean dataCell_FontWrap = true;
    private boolean dataCell_IsFontBold = false;
    private Short dataCell_BackGroundColor;
    private boolean dataCell_HasTopBorder = true;
    private boolean dataCell_HasBottomBorder = true;
    private boolean dataCell_HasLeftBorder = true;
    private boolean dataCell_HasRightBorder = true;
    private String dataCell_Alignment;

    public boolean isTitleNotExist(){
        boolean flag = false;
        if (this == null){
            flag = true;
        }else {
            if (title_Height == null && StringUtils.isBlank(title_Font) &&
                    title_FontSize == null && title_FontColor == null
            && title_BackGroundColor == null && StringUtils.isBlank(title_Alignment)
            && title_FontWrap && title_IsFontBold && title_HasTopBorder
            && title_HasBottomBorder && title_HasLeftBorder && title_HasRightBorder)
                flag = true;
        }
        return flag;
    }

    public boolean isTableHeaderNotExist(){
        boolean flag = false;
        if (this == null){
            flag = true;
        }else {
            if (tableHeader_Height == null && StringUtils.isBlank(tableHeader_Font) &&
                    tableHeader_FontSize == null && tableHeader_FontColor == null
                    && tableHeader_BackGroundColor == null && StringUtils.isBlank(tableHeader_Alignment)
                    && tableHeader_FontWrap && tableHeader_IsFontBold && tableHeader_HasTopBorder
                    && tableHeader_HasBottomBorder && tableHeader_HasLeftBorder && tableHeader_HasRightBorder)
                flag = true;
        }
        return flag;
    }

    public boolean isDataCellHeaderNotExist(){
        boolean flag = false;
        if (this == null){
            flag = true;
        }else {
            if (dataCell_RowHeight == null && StringUtils.isBlank(dataCell_Font) &&
                    dataCell_FontSize == null && dataCell_FontColor == null
                    && dataCell_BackGroundColor == null && StringUtils.isBlank(dataCell_Alignment)
                    && dataCell_FontWrap && dataCell_IsFontBold && dataCell_HasTopBorder
                    && dataCell_HasBottomBorder && dataCell_HasLeftBorder && dataCell_HasRightBorder)
                flag = true;
        }
        return flag;
    }

    public Short getTableHeader_Height() {
        return tableHeader_Height;
    }

    public void setTableHeader_Height(Short tableHeader_Height) {
        this.tableHeader_Height = tableHeader_Height;
    }

    public String getTableHeader_Font() {
        return tableHeader_Font;
    }

    public void setTableHeader_Font(String tableHeader_Font) {
        this.tableHeader_Font = tableHeader_Font;
    }

    public Short getTableHeader_FontSize() {
        return tableHeader_FontSize;
    }

    public void setTableHeader_FontSize(Short tableHeader_FontSize) {
        this.tableHeader_FontSize = tableHeader_FontSize;
    }

    public Short getTableHeader_FontColor() {
        return tableHeader_FontColor;
    }

    public void setTableHeader_FontColor(Short tableHeader_FontColor) {
        this.tableHeader_FontColor = tableHeader_FontColor;
    }

    public boolean isTableHeader_FontWrap() {
        return tableHeader_FontWrap;
    }

    public void setTableHeader_FontWrap(boolean tableHeader_FontWrap) {
        this.tableHeader_FontWrap = tableHeader_FontWrap;
    }

    public boolean isTableHeader_IsFontBold() {
        return tableHeader_IsFontBold;
    }

    public void setTableHeader_IsFontBold(boolean tableHeader_IsFontBold) {
        this.tableHeader_IsFontBold = tableHeader_IsFontBold;
    }

    public Short getTableHeader_BackGroundColor() {
        return tableHeader_BackGroundColor;
    }

    public void setTableHeader_BackGroundColor(Short tableHeader_BackGroundColor) {
        this.tableHeader_BackGroundColor = tableHeader_BackGroundColor;
    }

    public boolean isTableHeader_HasTopBorder() {
        return tableHeader_HasTopBorder;
    }

    public void setTableHeader_HasTopBorder(boolean tableHeader_HasTopBorder) {
        this.tableHeader_HasTopBorder = tableHeader_HasTopBorder;
    }

    public boolean isTableHeader_HasBottomBorder() {
        return tableHeader_HasBottomBorder;
    }

    public void setTableHeader_HasBottomBorder(boolean tableHeader_HasBottomBorder) {
        this.tableHeader_HasBottomBorder = tableHeader_HasBottomBorder;
    }

    public boolean isTableHeader_HasLeftBorder() {
        return tableHeader_HasLeftBorder;
    }

    public void setTableHeader_HasLeftBorder(boolean tableHeader_HasLeftBorder) {
        this.tableHeader_HasLeftBorder = tableHeader_HasLeftBorder;
    }

    public boolean isTableHeader_HasRightBorder() {
        return tableHeader_HasRightBorder;
    }

    public void setTableHeader_HasRightBorder(boolean tableHeader_HasRightBorder) {
        this.tableHeader_HasRightBorder = tableHeader_HasRightBorder;
    }

    public String getTableHeader_Alignment() {
        return tableHeader_Alignment;
    }

    public void setTableHeader_Alignment(String tableHeader_Alignment) {
        this.tableHeader_Alignment = tableHeader_Alignment;
    }

    public Short getTitle_Height() {
        return title_Height;
    }

    public void setTitle_Height(Short title_Height) {
        this.title_Height = title_Height;
    }

    public String getTitle_Font() {
        return title_Font;
    }

    public void setTitle_Font(String title_Font) {
        this.title_Font = title_Font;
    }

    public Short getTitle_FontSize() {
        return title_FontSize;
    }

    public void setTitle_FontSize(Short title_FontSize) {
        this.title_FontSize = title_FontSize;
    }

    public Short getTitle_FontColor() {
        return title_FontColor;
    }

    public void setTitle_FontColor(Short title_FontColor) {
        this.title_FontColor = title_FontColor;
    }

    public boolean isTitle_FontWrap() {
        return title_FontWrap;
    }

    public void setTitle_FontWrap(boolean title_FontWrap) {
        this.title_FontWrap = title_FontWrap;
    }

    public boolean isTitle_IsFontBold() {
        return title_IsFontBold;
    }

    public void setTitle_IsFontBold(boolean title_IsFontBold) {
        this.title_IsFontBold = title_IsFontBold;
    }

    public Short getTitle_BackGroundColor() {
        return title_BackGroundColor;
    }

    public void setTitle_BackGroundColor(Short title_BackGroundColor) {
        this.title_BackGroundColor = title_BackGroundColor;
    }

    public boolean isTitle_HasTopBorder() {
        return title_HasTopBorder;
    }

    public void setTitle_HasTopBorder(boolean title_HasTopBorder) {
        this.title_HasTopBorder = title_HasTopBorder;
    }

    public boolean isTitle_HasBottomBorder() {
        return title_HasBottomBorder;
    }

    public void setTitle_HasBottomBorder(boolean title_HasBottomBorder) {
        this.title_HasBottomBorder = title_HasBottomBorder;
    }

    public boolean isTitle_HasLeftBorder() {
        return title_HasLeftBorder;
    }

    public void setTitle_HasLeftBorder(boolean title_HasLeftBorder) {
        this.title_HasLeftBorder = title_HasLeftBorder;
    }

    public boolean isTitle_HasRightBorder() {
        return title_HasRightBorder;
    }

    public void setTitle_HasRightBorder(boolean title_HasRightBorder) {
        this.title_HasRightBorder = title_HasRightBorder;
    }

    public String getTitle_Alignment() {
        return title_Alignment;
    }

    public void setTitle_Alignment(String title_Alignment) {
        this.title_Alignment = title_Alignment;
    }

    public Short getDataCell_RowHeight() {
        return dataCell_RowHeight;
    }

    public void setDataCell_RowHeight(Short dataCell_RowHeight) {
        this.dataCell_RowHeight = dataCell_RowHeight;
    }

    public String getDataCell_Font() {
        return dataCell_Font;
    }

    public void setDataCell_Font(String dataCell_Font) {
        this.dataCell_Font = dataCell_Font;
    }

    public Short getDataCell_FontSize() {
        return dataCell_FontSize;
    }

    public void setDataCell_FontSize(Short dataCell_FontSize) {
        this.dataCell_FontSize = dataCell_FontSize;
    }

    public Short getDataCell_FontColor() {
        return dataCell_FontColor;
    }

    public void setDataCell_FontColor(Short dataCell_FontColor) {
        this.dataCell_FontColor = dataCell_FontColor;
    }

    public boolean isDataCell_FontWrap() {
        return dataCell_FontWrap;
    }

    public void setDataCell_FontWrap(boolean dataCell_FontWrap) {
        this.dataCell_FontWrap = dataCell_FontWrap;
    }

    public boolean isDataCell_IsFontBold() {
        return dataCell_IsFontBold;
    }

    public void setDataCell_IsFontBold(boolean dataCell_IsFontBold) {
        this.dataCell_IsFontBold = dataCell_IsFontBold;
    }

    public Short getDataCell_BackGroundColor() {
        return dataCell_BackGroundColor;
    }

    public void setDataCell_BackGroundColor(Short dataCell_BackGroundColor) {
        this.dataCell_BackGroundColor = dataCell_BackGroundColor;
    }

    public boolean isDataCell_HasTopBorder() {
        return dataCell_HasTopBorder;
    }

    public void setDataCell_HasTopBorder(boolean dataCell_HasTopBorder) {
        this.dataCell_HasTopBorder = dataCell_HasTopBorder;
    }

    public boolean isDataCell_HasBottomBorder() {
        return dataCell_HasBottomBorder;
    }

    public void setDataCell_HasBottomBorder(boolean dataCell_HasBottomBorder) {
        this.dataCell_HasBottomBorder = dataCell_HasBottomBorder;
    }

    public boolean isDataCell_HasLeftBorder() {
        return dataCell_HasLeftBorder;
    }

    public void setDataCell_HasLeftBorder(boolean dataCell_HasLeftBorder) {
        this.dataCell_HasLeftBorder = dataCell_HasLeftBorder;
    }

    public boolean isDataCell_HasRightBorder() {
        return dataCell_HasRightBorder;
    }

    public void setDataCell_HasRightBorder(boolean dataCell_HasRightBorder) {
        this.dataCell_HasRightBorder = dataCell_HasRightBorder;
    }

    public String getDataCell_Alignment() {
        return dataCell_Alignment;
    }

    public void setDataCell_Alignment(String dataCell_Alignment) {
        this.dataCell_Alignment = dataCell_Alignment;
    }
}
