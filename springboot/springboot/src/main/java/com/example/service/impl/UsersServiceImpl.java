package com.example.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Student;
import com.example.entity.Users;
import com.example.entity.vo.ForgetPasswordVo;
import com.example.exception.CustomException;
import com.example.mapper.UsersMapper;
import com.example.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2025-02-13
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {


    @Override
    public Account login(Account account) {

        Users users = baseMapper.selectOne(new LambdaQueryWrapper<Users>().eq(Users::getUsername, account.getUsername()));
        if (users == null) {
            // 没有用户
            throw new CustomException("账号或密码错误");
        }
        Account dbStudent = new Account();// 根据用户名去查 根据账号查到用户信息
        // 比较密码 （表单密码和数据库中的密码进行比较 admin 是表单数据 dbAdmin是数据库数据）
        if (!account.getPassword().equals(users.getPassword())) { // 如果密码不相等
            throw new CustomException("账号或密码错误");
        }
        dbStudent.setName(users.getUsername());
        dbStudent.setUserId(users.getId());
        dbStudent.setUsername(users.getUsername());
        dbStudent.setPassword(users.getPassword());
        dbStudent.setEmail(users.getEmail());
        dbStudent.setSex(users.getSex());
        dbStudent.setBirth(users.getBirth());

        // 登录成功
        return dbStudent;
    }

    @Override
    public void register(Account account) {
        Users users = new Users();
        users.setUsername(account.getUsername());
        users.setPassword(account.getPassword());
        users.setEmail(account.getEmail());
        // 先去数据库中查询一波看有没有
        //  查询一下
        Users dbusers = baseMapper.selectOne(new LambdaQueryWrapper<Users>().eq(Users::getUsername, account.getUsername()));
        // 去判断一下
        if (dbusers != null) { // 已有同名信息， 不允许插入
            throw new CustomException("账号已存在");

        }
        baseMapper.insert(users);
    }

    @Override
    public void sendReset(ForgetPasswordVo request) {
        Users dbusers = baseMapper.selectOne(new LambdaQueryWrapper<Users>().eq(Users::getUsername, request.getUsername()));
        // 去判断一下
        if (dbusers == null) { // 已有同名信息， 不允许插入
            throw new CustomException("账号不存在");
        }
        dbusers.setPassword(request.getPassword());
        baseMapper.updateById(dbusers);
    }


}
