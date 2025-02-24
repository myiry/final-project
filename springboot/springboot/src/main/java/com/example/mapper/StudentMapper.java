package com.example.mapper;

import com.example.entity.Student;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 */
public interface StudentMapper {

    // 查询
    @Select("select * from student where username = #{username}")
    Student selectUsername(String username);

    // 新增
    @Insert("insert into student (username, password, name, phone, email, sex, birth, avatar, role) " +
            "values (#{username},#{password},#{name},#{phone},#{email},#{sex},#{birth},#{avatar}, #{role})")
    void insert(Student student);

    // 删除
    @Delete("delete from student where id = #{id}")
    void deleteById(Integer id);

    // 修改
    @Update("update student set username = #{username},password = #{password},name = #{name},phone = #{phone},email = #{email},sex = #{sex},birth = #{birth},avatar = #{avatar} where id = #{id} ")
    void updateById(Student student);

  /*  // 查询
    @Select("select *from student where name like concat('%',#{name},'%')" +
            "and sex like concat('%',#{sex},'%')" +
            "and username like concat('%',#{username},'%')")
    List<Student> selectAll(Student student);*/
  // 查询
//  @Select("select *from student where name like concat('%',#{name},'%')" +
//          "and username like concat('%',#{username},'%')")
  List<Student> selectAll(Student student);
}
