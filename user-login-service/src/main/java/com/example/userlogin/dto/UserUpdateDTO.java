package com.example.userlogin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * User Update DTO - 用户更新数据传输对象
 */
public class UserUpdateDTO {

    @Size(min = 3, max = 50, message = "用户名长度必须在3-50之间")
    private String username;

    @Size(min = 6, message = "密码长度至少为6位")
    private String password;

    @Email(message = "邮箱格式不正确")
    private String email;

    // 无参构造函数
    public UserUpdateDTO() {
    }

    // 全参构造函数
    public UserUpdateDTO(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
