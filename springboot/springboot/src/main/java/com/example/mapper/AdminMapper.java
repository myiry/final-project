package com.example.mapper;

import com.example.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 */
@Mapper
public interface AdminMapper {
    @Select("select * from admin where username = #{username}")
    Admin selectUsername(String username);
}
