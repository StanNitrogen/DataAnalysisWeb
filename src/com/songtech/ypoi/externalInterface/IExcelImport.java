package com.songtech.ypoi.externalInterface;

import com.songtech.ypoi.config.Config;
import com.songtech.ypoi.exception.YpoiBaseException;
import com.songtech.ypoi.params.ExcelParams;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * Create By YINN on 2018/1/10 14:39
 * Description :
 */
public interface IExcelImport {

    public <T> List<T> importExcel(Sheet sheet, Map<String, Config> configMap, Class<T> clazz, ExcelParams ep) throws YpoiBaseException, IllegalAccessException, InstantiationException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException;

    public List<Sheet> getSheetList(Workbook wb);
}
