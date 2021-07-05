package com.atguigu.eduservice.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestEasyExcel {
    public static void main(String[] args) {
        //实现excel写的操作
        //1、设置写如文件夹的地址和excel的名称
        String filename = "E:\\write.xlsx";

        //2、调用easyExcel里面的方法实现写操作(文件路径名称, )
        EasyExcel.write(filename, DemoData.class).sheet("学生列表").doWrite(getData());
    }

    //创建List集合
    private static List<DemoData> getData(){
        List<DemoData> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setSno(i);
            data.setSname("zxx"+ i);
            list.add(data);
        }
        return list;
    }
}
