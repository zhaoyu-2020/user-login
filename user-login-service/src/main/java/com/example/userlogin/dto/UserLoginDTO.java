package com.example.userlogin.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * User Login DTO - 用户登录数据传输对象
 */
public class UserLoginDTO {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    // 无参构造函数
    public UserLoginDTO() {
    }

    // 全参构造函数
    public UserLoginDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getter and Setter methods
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
}
