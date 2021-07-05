package com.atguigu.eduservice.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExcelListener extends AnalysisEventListener<DemoData> {

    //创建list集合封装最终的数据
    List<DemoData> list = new ArrayList<DemoData>();

    //一行一行的读取数据
    @Override
    public void invoke(DemoData demoData, AnalysisContext analysisContext) {
        System.out.println("***"+ demoData);
    }

    @Override
    public void invokeHead(Map headMap, AnalysisContext context) {
        System.out.println("表头："+ headMap);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
