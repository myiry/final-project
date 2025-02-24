package com.example.entity.vo;

import lombok.Data;

@Data
public class ForgetPasswordVo {
    private String username;
    private String password;
    private String confirmPassword;
}
