package com.example.common;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.example.entity.Mark;
import java.util.ArrayList;
import java.util.List;

public class MarkDataListener extends AnalysisEventListener<Mark> {
    private final List<Mark> marks = new ArrayList<>();

    @Override
    public void invoke(Mark mark, AnalysisContext analysisContext) {
        marks.add(mark);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 所有数据解析完成后的操作
    }

    public List<Mark> getList() {
        return marks;
    }
}