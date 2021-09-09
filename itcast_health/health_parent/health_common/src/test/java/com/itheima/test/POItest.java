package com.itheima.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.*;

/**
 * @program: itcast_health
 * @description
 * @author: ShenXinran
 * @create: 2021-08-13 15:46
 **/
public class POItest {

    @Test
    public void test1() throws IOException {
        XSSFWorkbook excl1 = new XSSFWorkbook(new FileInputStream(new File("f:\\poi.xlsx")));
        XSSFSheet sheet = excl1.getSheetAt(0);
        for (Row row : sheet) {
            for (Cell cell : row) {
                System.out.println(cell.getStringCellValue());
            }
        }
        excl1.close();
    }

    @Test
    public void test2() throws IOException {
        XSSFWorkbook excel = new XSSFWorkbook();
        XSSFSheet sheet = excel.createSheet("新工作表");
        XSSFRow title = sheet.createRow(0);
        title.createCell(0).setCellValue("姓名");
        title.createCell(1).setCellValue("地址");
        title.createCell(2).setCellValue("年龄");

        XSSFRow dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue("小明");
        dataRow.createCell(1).setCellValue("南京");
        dataRow.createCell(2).setCellValue("30");

        FileOutputStream out = new FileOutputStream(new File("f:\\hello.xlsx"));
        excel.write(out);
        out.flush();
        excel.close();
    }
}
