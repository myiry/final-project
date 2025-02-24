package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 */

public class Admin extends Account{

    /**
     * Admin中可以不用谢
     * username
     * password
     * name
     * role
     */
    /*ID*/
    private Integer id;
    /*用户名*/
    private String username;
    /*密码*/
    private String password;

    /*用户呢称*/
    private String name;

    /*角色*/
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }



    public void setName(String name) {
        this.name = name;
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
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
