package com.example.mapper;

import com.example.entity.Course;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 */
public interface CourseMapper {

   /* // 根据id进行倒序
    @Select("select * from course order by id desc")
    List<Course> selectAll();*/

    // 根据id进行倒序
    @Select("select * from course where name like " +
            " concat('%',#{name}, '%') and no like concat('%',#{no}, '%') " +
            "and teacher like concat('%',#{teacher}, '%') order by id desc")
    List<Course> selectAll(Course course); // 进行模糊查询

    // 新增
    @Insert("insert into course (name, no, descr, times, teacher) VALUES (#{name}, #{no},#{descr},#{times},#{teacher})")
    void insert(Course course);

    // 修改
    @Update("update course set no = #{no}, name = #{name}, descr = #{descr}, times = #{times}, teacher = #{teacher} where id = #{id}")
    void updateById(Course course);

    // 删除
    @Delete("delete from course where id = #{id}")
    void deleteById(Integer id);
}
