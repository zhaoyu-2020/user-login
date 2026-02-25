package com.example.userlogin.controller;

import com.example.userlogin.dto.UserLoginDTO;
import com.example.userlogin.dto.UserUpdateDTO;
import com.example.userlogin.service.UserService;
import com.example.userlogin.vo.UserVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
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

    /**
     * 分页获取所有用户
     * @param page 页码（从0开始，默认0）
     * @param size 每页大小（默认10）
     * @param sortBy 排序字段（默认id，支持: id, username, email, createdAt, updatedAt）
     * @param sortOrder 排序顺序（ASC或DESC，默认ASC）
     */
    @GetMapping
    public ResponseEntity<Page<UserVO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortOrder) {
        
        @SuppressWarnings("null")
        Sort.Direction direction = Sort.Direction.fromString(sortOrder.toUpperCase());
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<UserVO> userPage = userService.getAllUsers(pageRequest);
        return ResponseEntity.ok(userPage);
    }

    /**
     * 根据用户名搜索用户（分页）
     * @param username 用户名（支持模糊查询）
     * @param page 页码（从0开始，默认0）
     * @param size 每页大小（默认10）
     * @param sortBy 排序字段（默认id）
     * @param sortOrder 排序顺序（ASC或DESC，默认ASC）
     */
    @GetMapping("/search/username")
    public ResponseEntity<Page<UserVO>> searchByUsername(
            @RequestParam String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortOrder) {
        
        @SuppressWarnings("null")
        Sort.Direction direction = Sort.Direction.fromString(sortOrder.toUpperCase());
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<UserVO> userPage = userService.searchUsersByUsername(username, pageRequest);
        return ResponseEntity.ok(userPage);
    }

    /**
     * 根据邮箱搜索用户（分页）
     * @param email 邮箱（支持模糊查询）
     * @param page 页码（从0开始，默认0）
     * @param size 每页大小（默认10）
     * @param sortBy 排序字段（默认id）
     * @param sortOrder 排序顺序（ASC或DESC，默认ASC）
     */
    @GetMapping("/search/email")
    public ResponseEntity<Page<UserVO>> searchByEmail(
            @RequestParam String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortOrder) {
        
        @SuppressWarnings("null")
        Sort.Direction direction = Sort.Direction.fromString(sortOrder.toUpperCase());
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<UserVO> userPage = userService.searchUsersByEmail(email, pageRequest);
        return ResponseEntity.ok(userPage);
    }

    /**
     * 根据ID获取用户
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<UserVO> userVO = userService.getUserById(id);
        if (userVO.isPresent()) {
            return ResponseEntity.ok(userVO.get());
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "用户不存在");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO updateDTO) {
        try {
            UserVO updatedUser = userService.updateUser(id, updateDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "用户删除成功");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}
