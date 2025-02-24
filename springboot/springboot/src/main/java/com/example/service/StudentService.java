package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Student;
import com.example.exception.CustomException;
import com.example.mapper.StudentMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 */

/**
 * 学生登录
 */
@Service
public class StudentService {
    @Resource
    private StudentMapper studentMapper;

    /**
     * 登录逻辑
     */

    public Account login(Account account) { // Account 只能拿到三个数据
        Account dbStudent = studentMapper.selectUsername(account.getUsername());// 根据用户名去查 根据账号查到用户信息
        if (dbStudent == null) {
            // 没有用户
            throw new CustomException("账号或密码错误");
        }
        // 比较密码 （表单密码和数据库中的密码进行比较 admin 是表单数据 dbAdmin是数据库数据）
        if (!account.getPassword().equals(dbStudent.getPassword())) { // 如果密码不相等
            throw new CustomException("账号或密码错误");
        }
        // 登录成功
        return dbStudent;

    }

    /**
     * 学生注册
     * @param account
     */
    public void register(Account account) {
        // 封装一个学生实体
        Student student = new Student();
        student.setUsername(account.getUsername()); // 账号 前端数据传过来的
        student.setPassword(account.getPassword()); // 密码 前端数据传过来的
        // 把它加到数据中
        this.add(student);

    }

    /**
     *  新增
     * @param student
     */
    public void add(Student student) {
        // 先去数据库中查询一波看有没有
        //  查询一下
        Student dbstudent = studentMapper.selectUsername(student.getUsername());
        // 去判断一下
        if (dbstudent != null) { // 已有同名信息， 不允许插入
            throw new CustomException("账号已存在");

        }
        if (ObjectUtil.isEmpty(student.getName())) { // 如果昵称为空
            student.setName(student.getUsername()); // 如果昵称为空 则把账号当做昵称
        }
        // 插入数据库
        studentMapper.insert(student);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteById(Integer id) {
        studentMapper.deleteById(id);
    }

    /**
     * 修改
     * @param student
     */
    public void updateById(Student student) {
        studentMapper.updateById(student);
    }

    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @param student
     * @return
     */
    public PageInfo<Student> selectPage(Integer pageNum, Integer pageSize, Student student) {
        // 分页
        PageHelper.startPage(pageNum,pageSize);
        // 添加条件
        List<Student> studentList = studentMapper.selectAll(student);
        return PageInfo.of(studentList);
    }
}
