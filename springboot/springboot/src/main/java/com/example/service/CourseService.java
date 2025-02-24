package com.example.service;

import com.example.entity.Course;
import com.example.mapper.CourseMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 */

/**
 * 这里写业务逻辑
 */
@Service
public class CourseService {

    @Resource
    private CourseMapper courseMapper;


    // total 查询的总数， list是数据列表
    // pageNum 当前页， pageSize是每页的个数
    public PageInfo<Course> selectPage(Integer pageNum, Integer pageSize, Course course) {
        PageHelper.startPage(pageNum, pageSize);
//        List<Course> courseList = courseMapper.selectAll();// 分页
        List<Course> courseList = courseMapper.selectAll(course);// 加入条件进行分页

        return PageInfo.of(courseList); // 进行分页
    }

    /**
     * 新增
     * @param course
     */
    public void add(Course course) {
        courseMapper.insert(course);
    }

    /**
     *  修改
     * @param course
     */
    public void updateById(Course course) {
        courseMapper.updateById(course);

    }


    /**
     * 删除
     * @param id
     */
    public void deleteById(Integer id) {
        courseMapper.deleteById(id);
    }
}
