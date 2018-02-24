package com.songtech.ypoi.externalInterface;

import com.songtech.ypoi.exception.YpoiBaseException;
import com.songtech.ypoi.params.ExcelParams;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

/**
 * Create By YINN on 2018/1/22 10:31
 * Description :
 */
public interface IExcelExportByTemp {
    public Workbook exportByTemp(InputStream is, ExcelParams ep, Collection<?>... datas) throws YpoiBaseException, IOException, InvalidFormatException, NoSuchFieldException, IllegalAccessException;
}
