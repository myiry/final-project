package com.example.service;

import com.example.entity.Account;
import com.example.exception.CustomException;
import com.example.mapper.AdminMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 */
@Service
public class AdminService {
    @Resource
    private AdminMapper adminMapper;
    /**
     * 登录逻辑
     */

    public Account login(Account account) { // Account 只能拿到三个数据
        Account dbAdmin = adminMapper.selectUsername(account.getUsername());// 根据用户名去查 根据账号查到用户信息
        if (dbAdmin == null) {
            // 没有用户
            throw new CustomException("账号或密码错误");
        }
        // 比较密码 （表单密码和数据库中的密码进行比较 admin 是表单数据 dbAdmin是数据库数据）
        if (!account.getPassword().equals(dbAdmin.getPassword())) {
            throw new CustomException("账号或密码错误");
        }
       // 登录成功
        return dbAdmin;
    }
}
