package com.itheima.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 入门案例
 */
public class POITest {

    //方式一：
    //使用POI可以从一个已经存在的Excel文件中读取数据
    //@Test
    public void readExcel() throws IOException {
        //获取Excel对象
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook("C:\\working\\read.xlsx");
        //获取工作簿
        //xssfWorkbook.getSheet("Sheet1")
        XSSFSheet sheetAt = xssfWorkbook.getSheetAt(0);//index:下标从0开始
        //遍历sheetAt 得到每一行对象
        for (Row row : sheetAt) {
            //row:每一行
            //循环遍历每一列
            for (Cell cell : row) {
                //cell:单元格
                System.out.println(cell.getStringCellValue());
            }
            System.out.println("*******************************************");
        }
        //释放资源
        xssfWorkbook.close();
    }

    //方式二：getLastRowNum：获取最后一行行号 getLastCellNum：获取最后一列的列号
    //@Test
    public void readExcel2() throws IOException {
        //获取Excel对象
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook("C:\\working\\read.xlsx");
        //获取工作簿
        //xssfWorkbook.getSheet("Sheet1")
        XSSFSheet sheetAt = xssfWorkbook.getSheetAt(0);//index:下标从0开始
        int lastRowNum = sheetAt.getLastRowNum();
        System.out.println("lastRowNum******************"+lastRowNum);
        //遍历lastRowNum次数+1
        for(int i = 0;i<=lastRowNum;i++){
            //获取每一行对象
            XSSFRow row = sheetAt.getRow(i);
            System.out.println("getLastCellNum*****************"+row.getLastCellNum());
            //获取每一列 getLastCellNum
            for(int j = 0;j<row.getLastCellNum();j++){
                System.out.println(row.getCell(j));
            }
            System.out.println("**************************************************************");
        }
        xssfWorkbook.close();
    }
    //使用POI可以在内存中创建一个Excel文件并将数据写入到这个文件，最后通过输出流将内存中的Excel文件下载到磁盘
    //
    //@Test
    public void createExcel() throws IOException {
        //创建一个空的Excel对象
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        //创建sheet对象
        XSSFSheet sheet = xssfWorkbook.createSheet("info");
        //创建标题行
        XSSFRow titleRow = sheet.createRow(0);
        //第一行 第一列
        titleRow.createCell(0).setCellValue("编号");
        titleRow.createCell(1).setCellValue("姓名");
        titleRow.createCell(2).setCellValue("年龄");
        //List<T>  list.size()
        //创建数据行
        XSSFRow dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue("001");
        dataRow.createCell(1).setCellValue("老李");
        dataRow.createCell(2).setCellValue("16");

        OutputStream outputStream = new FileOutputStream("C:\\working\\create.xlsx");
        //输出流将Excel对象从内存中写入磁盘
        xssfWorkbook.write(outputStream);
        //释放资源
        outputStream.flush();
        outputStream.close();
        //关闭xssfWorkbook
        xssfWorkbook.close();



    }


}
