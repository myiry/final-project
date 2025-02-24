package com.example.controller;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Result;
import com.example.common.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Student;
import com.example.entity.Users;
import com.example.entity.vo.ForgetPasswordVo;
import com.example.service.AdminService;
import com.example.service.IUsersService;
import com.example.service.StudentService;
import org.apache.catalina.User;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 登录权限者 admin和Student
 */
@RestController
public class WebController {

    @Resource
    private AdminService adminService;

    @Resource
    private StudentService studentService;

    @Resource
    private IUsersService usersService;
    /**
     * 默认请求接口
     */
    @GetMapping("/")
    public Result hello() {
        return Result.success();
    }


    /**
     * 登录
     * @return
     * @RequestBody 接收表单传来的数据
     * 通过Account 去接受表单数据
     */
    @PostMapping("/login")
    public Result login(@RequestBody Account account) { // 四个数据
        return Result.success(usersService.login(account));
    }


    /**
     * 注册接口
     * @param account
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody Account account) { // 四个数据
        if (ObjectUtil.isEmpty(account.getUsername()) || ObjectUtil.isEmpty(account.getPassword())) { // 账号或者密码为空
            return Result.error("账号密码必填"); // 这里做一个校验
        }
        usersService.register(account);
        return Result.success();
    }

    @PostMapping("/sendReset")
    public Result sendResetLink(@RequestBody ForgetPasswordVo request) {
        // 调用服务层处理重置密码逻辑
        usersService.sendReset(request);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Account user) {
        Users users = new Users();
        users.setPassword(user.getPassword());
        users.setEmail(user.getEmail());
        users.setUsername(user.getUsername());
        users.setSex(user.getSex());
        users.setBirth(user.getBirth());
        users.setId(user.getUserId());
        usersService.updateById(users);
        return Result.success();
    }

}
