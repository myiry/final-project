package com.example.mapper;

import com.example.entity.Student;
import com.example.entity.StudentCourse;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 */
public interface StudentCourseMapper {

    @Insert("insert into student_course (name, no, student_id, course_id) VALUES (#{name},#{no},#{studentId},#{courseId})")
    void insert(StudentCourse studentCourse);



    // 判断一下学生的有没有选过课
    @Select("select *from student_course where student_id = #{studentId} and course_id = #{courseId}")
    StudentCourse selectByCondition(StudentCourse studentCourse);

//    @Select("select *from student_course where name like concat('%',#{name}, '%') and no like concat('%',#{no},'%') and student_id=#{studentId}")
    List<StudentCourse> selectAll(StudentCourse studentCourse);


    void deleteById(Integer id);
}
