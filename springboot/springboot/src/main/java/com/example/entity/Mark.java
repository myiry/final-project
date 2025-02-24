package com.example.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("mark")
@ColumnWidth(50)
public class Mark {
    @TableId(type = IdType.AUTO)
    @ExcelIgnore
    private Long markId;

    @ExcelProperty("学科名称")
    private String module;

    @ExcelProperty("分数")
    private Integer score;
    @ExcelIgnore
    private Long userId;

    @ExcelProperty("年")
    private Integer year;

    @ExcelProperty("学期")
    private Integer semester;
    @ExcelIgnore
    private Date createTime;

}