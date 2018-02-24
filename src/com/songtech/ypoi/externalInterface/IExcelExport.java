package com.songtech.ypoi.externalInterface;

import com.songtech.ypoi.config.Config;
import com.songtech.ypoi.params.ExcelParams;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Create By YINN on 2018/1/10 14:39
 * Description :
 */
public interface IExcelExport {
    /**
     * @param excelParams excel导出参数
     * @param configObject 配置对象若为Map则 key:"对应数据列参数名，即要导出的实体类中的字段名"（String），
     *                     value：Config类 装在当前字段的配置信息，（Config类型）
     * @param wb 要导出的workbook对象
     * @param datas 要导出的数据
     */
    public Workbook export(Workbook wb, List<Map<String, Config>> configObject, ExcelParams excelParams, Collection<?>... datas) throws Exception;
}
