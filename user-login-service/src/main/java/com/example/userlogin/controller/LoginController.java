package com.example.userlogin.controller;

import com.example.userlogin.dto.UserLoginDTO;
import com.example.userlogin.service.UserService;
import com.example.userlogin.util.JwtUtil;
import com.example.userlogin.vo.UserVO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${jwt.cookie-name}")
    private String cookieName;

    /**
     * 用户登录
     * POST /api/login
     */
    @PostMapping
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO loginDTO, HttpServletResponse httpResponse) {
        Optional<UserVO> userVO = userService.login(loginDTO);
        if (userVO.isPresent()) {
            UserVO user = userVO.get();
            String token = jwtUtil.generateToken(user.getId(), user.getUsername());

            Cookie cookie = new Cookie(cookieName, token);
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/api");
            cookie.setMaxAge(jwtUtil.getExpirationSeconds());
            cookie.setAttribute("SameSite", "Lax");
            httpResponse.addCookie(cookie);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "登录成功");
            response.put("user", user);
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "用户名或密码错误");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    /**
     * 用户登出 - 清除 cookie
     * POST /api/logout
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse httpResponse) {
        Cookie cookie = new Cookie(cookieName, "");
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/api");
        cookie.setMaxAge(0);
        cookie.setAttribute("SameSite", "Lax");
        httpResponse.addCookie(cookie);

        Map<String, String> response = new HashMap<>();
        response.put("message", "登出成功");
        return ResponseEntity.ok(response);
    }
}
