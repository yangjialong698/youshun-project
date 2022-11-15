package com.ennova.pubinfocommon.utils;

import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

public class ExcelWriteUtil {


    public static void createHeader(HttpServletResponse response, String tableName, List<String> titles, List<List<String>> list) {
        SXSSFWorkbook wb = writeExcel(tableName, titles, list);
        OutputStream outputStream = null;
        try {
            tableName = URLEncoder.encode(tableName, "UTF-8");
            outputStream = response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition", "attachment;filename="+tableName+".xlsx");
            response.setContentType("application/msexcel");
            wb.write(outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SXSSFWorkbook writeExcel(String tableName, List<String> titles, List<List<String>> list) {
        //创建工作簿
        SXSSFWorkbook xssfWorkbook = null;
        xssfWorkbook = new SXSSFWorkbook(1000);
        int size = list.size();
        if (size==0){
            //创建工作表
            SXSSFSheet sheet = xssfWorkbook.createSheet();
            //创建标题行
            SXSSFRow sxssfTitleRow = sheet.createRow(0);
            //创建列，即单元格Cell
            //XSSFCell xssfCell;
            //把List里面的数据写到excel中
            for (int i=0;i<titles.size();i++) {
                //从第一行开始写入
                //创建每个单元格Cell，即列的数据
                SXSSFCell cell = sxssfTitleRow.createCell(i);
                cell.setCellValue(titles.get(i));
            }
        }else {
            int pageNum;
            if (size%1000000==0){
                pageNum=size/1000000;
            }else {
                pageNum=size/1000000+1;
            }
            for (int v=0;v<pageNum;v++){
                //创建工作表
                SXSSFSheet sheet = xssfWorkbook.createSheet();
                //创建标题行
                SXSSFRow sxssfTitleRow = sheet.createRow(0);
                //创建列，即单元格Cell
                //XSSFCell xssfCell;
                //把List里面的数据写到excel中
                for (int i=0;i<titles.size();i++) {
                    //从第一行开始写入
                    //创建每个单元格Cell，即列的数据
                    SXSSFCell cell = sxssfTitleRow.createCell(i);
                    cell.setCellValue(titles.get(i));
                }
                for (int i=v*1000000;i<(v+1)*1000000 && i<size;i++){
                    List<String> strings = list.get(i);
                    SXSSFRow sxssfRow= sheet.createRow(i-(v*1000000)+1);
                    for(int j=0;j<strings.size();j++){
                        SXSSFCell sxssfCell = sxssfRow.createCell(j);
                        sxssfCell.setCellValue(strings.get(j));
                    }
                }
            }
        }
        titles.clear();
        list.clear();
        return xssfWorkbook;
    }


}
