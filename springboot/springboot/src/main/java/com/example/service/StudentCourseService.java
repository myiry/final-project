package com.example.service;

import com.example.entity.Student;
import com.example.entity.StudentCourse;
import com.example.exception.CustomException;
import com.example.mapper.StudentCourseMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 */

/**
 * 学生选课的逻辑
 */
@Service
public class StudentCourseService {

    @Resource
    private StudentCourseMapper studentCourseMapper;

    public void add(StudentCourse studentCourse) {
        // 我们需要做一个判断看一下是否已经选了 如果已经选择了就抛出一个异常
        // 先查询
        StudentCourse selectByCourseById = studentCourseMapper.selectByCondition(studentCourse);
        // 通过学生ID和课程ID做一次查询的筛选，看看这个学生之前有没有选过课程
        if (selectByCourseById !=null) {  // 说明已经选择选过了
            throw new CustomException("您已选过这个课程");
        }
        studentCourseMapper.insert(studentCourse);
    }


    /**
     * 选课页面展示
     * @param pageNum
     * @param pageSize
     * @param studentCourse
     * @return
     */
    public PageInfo<StudentCourse> selectPage(Integer pageNum, Integer pageSize, StudentCourse studentCourse) {
        // 使用分页插件
        PageHelper.startPage(pageNum,pageSize);

        List<StudentCourse> studentCourseList = studentCourseMapper.selectAll(studentCourse);

        return PageInfo.of(studentCourseList); // 进行分页
    }


    /**
     * 删除选课记录
     * @param id
     */
    public void deleteById(Integer id) {
        studentCourseMapper.deleteById(id);
    }
}
