package com.ennova.pubinfoproduct.utils;

import com.ennova.pubinfocommon.vo.DateFormatUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Date;

public class ExcelReaderUtil {

    private static final String XLS = "xls";
    private static final String XLSX = "xlsx";

    public static Sheet getSheetByExcel(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            String fileName = file.getOriginalFilename();
            //获取文件类型
            if (fileName == null) {
                return null;
            }
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
            Workbook workbook = getWorkbook(inputStream, fileType);
            return workbook.getSheetAt(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Workbook getWorkbook(InputStream inputStream, String fileType) throws IOException {
        Workbook workbook = null;
        if (fileType.equalsIgnoreCase(XLS)) {
            workbook = new HSSFWorkbook(inputStream);
        } else if (fileType.equalsIgnoreCase(XLSX)) {
            workbook = new XSSFWorkbook(inputStream);
        }
        return workbook;
    }

    public static String convertCellValueToString(Cell cell) {
        if(cell==null){
            return null;
        }
        String returnValue = null;
        switch (cell.getCellType()) {
            case NUMERIC:   //数字
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    returnValue = DateFormatUtil.dateToString(date, DateFormatUtil.format_normal);
                } else {
                    double doubleValue = cell.getNumericCellValue();
                    DecimalFormat df = new DecimalFormat("0");
                    returnValue = df.format(doubleValue);
                }
                break;
            case STRING:   //字符串
                returnValue = cell.getStringCellValue();
                break;
            case BOOLEAN:
                boolean booleanValue = cell.getBooleanCellValue();
                returnValue = Boolean.toString(booleanValue);
                break;
            case BLANK:
                break;
            case FORMULA:
                Double numericCellValue = cell.getNumericCellValue();
                returnValue = String.valueOf(numericCellValue.intValue());
                break;
            case ERROR:
                break;
            default:
                break;
        }
        return ReplaceCharUtil.replaceChar(returnValue);
    }
}
