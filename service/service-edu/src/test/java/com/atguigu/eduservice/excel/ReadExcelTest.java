package com.atguigu.eduservice.excel;

import com.alibaba.excel.EasyExcel;

public class ReadExcelTest {
    public static void main(String[] args) {
        //1、设置写如文件夹的地址和excel的名称
        String filename = "E:\\write.xlsx";

        EasyExcel.read(filename,DemoData.class, new ExcelListener()).sheet().doRead();
    }
}
