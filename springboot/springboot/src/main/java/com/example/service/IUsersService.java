package com.example.service;

import com.example.entity.Account;
import com.example.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.vo.ForgetPasswordVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2025-02-13
 */
public interface IUsersService extends IService<Users> {

    public Account login(Account account);

    void register(Account account);

    void sendReset(ForgetPasswordVo request);
}
