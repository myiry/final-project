package com.example.service;

import com.example.entity.Grade;
import com.example.exception.CustomException;
import com.example.mapper.GradeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 */

/**
 * 这里面写逻辑
 */
@Service
public class GradeService {

    @Resource
    private GradeMapper gradeMapper;


    public void add(Grade grade) {
        // 做个校验
        // 打分之前先判断一下是否已经打过分了
        Grade dbGrade = gradeMapper.selectByCondition(grade);
        if (dbGrade !=null) { // 打过分了
            throw new CustomException("您已经打过分了");
        }
        gradeMapper.insert(grade);
    }

    /**
     * 分页加条件查询的业务逻辑
     * @param pageNum
     * @param pageSize
     * @param grade
     * @return
     */
    public PageInfo<Grade> selectPage(Integer pageNum, Integer pageSize, Grade grade) {
        // 使用分页插件
        PageHelper.startPage(pageNum,pageSize);
        List<Grade> gradeList = gradeMapper.selectAll(grade);
        return PageInfo.of(gradeList);
    }

    public void updateById(Grade grade) {
        gradeMapper.update(grade);
    }

    public void deleteById(Integer id) {
        gradeMapper.delete(id);
    }
}
