package com.example.entity;

import lombok.Data;

/**
 * @date 2024/1/23 11:23
 */
@Data
/*student和admin公共的父类*/
public class Account {
    /*账号*/
    private String username;
    /*密码*/
    private String password;
    /*呢称*/
    private String name;
    /*角色*/  // 用于区分角色是Student 还是 admin
    private String email;

    private Integer userId;

    private String sex;

    private String birth;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
