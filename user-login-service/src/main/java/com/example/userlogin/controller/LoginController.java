package com.example.userlogin.controller;

import com.example.userlogin.dto.UserLoginDTO;
import com.example.userlogin.service.UserService;
import com.example.userlogin.vo.UserVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/login")
@CrossOrigin(origins = "*")
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * POST /api/login
     */
    @PostMapping
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        Optional<UserVO> userVO = userService.login(loginDTO);
        if (userVO.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "登录成功");
            response.put("user", userVO.get());
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "用户名或密码错误");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }
}
