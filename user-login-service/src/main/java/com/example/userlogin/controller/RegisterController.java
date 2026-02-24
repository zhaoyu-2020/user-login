package com.example.userlogin.controller;

import com.example.userlogin.dto.UserRegisterDTO;
import com.example.userlogin.service.UserService;
import com.example.userlogin.vo.UserVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/register")
@CrossOrigin(origins = "*")
public class RegisterController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     * POST /api/register
     */
    @PostMapping
    public ResponseEntity<?> register(@Valid @RequestBody UserRegisterDTO registerDTO) {
        try {
            UserVO userVO = userService.createUser(registerDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "注册成功");
            response.put("user", userVO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}
