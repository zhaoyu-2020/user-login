package com.example.userlogin.service;

import com.example.userlogin.dto.UserLoginDTO;
import com.example.userlogin.dto.UserRegisterDTO;
import com.example.userlogin.dto.UserUpdateDTO;
import com.example.userlogin.entity.UserDO;
import com.example.userlogin.repository.UserRepository;
import com.example.userlogin.util.UserConverter;
import com.example.userlogin.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 创建新用户（注册）
     */
    @SuppressWarnings("null")
    public UserVO createUser(UserRegisterDTO registerDTO) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            throw new RuntimeException("用户名已存在: " + registerDTO.getUsername());
        }

        // 检查邮箱是否已存在
        if (registerDTO.getEmail() != null && userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new RuntimeException("邮箱已存在: " + registerDTO.getEmail());
        }

        // DTO转DO
        UserDO userDO = UserConverter.toUserDO(registerDTO);
        UserDO savedUser = userRepository.save(userDO);

        // DO转VO
        return UserConverter.toVO(savedUser);
    }

    /**
     * 根据ID获取用户
     */
    public Optional<UserVO> getUserById(long id) {
        return userRepository.findById(id)
                .map(UserConverter::toVO);
    }

    /**
     * 根据用户名获取用户
     */
    public Optional<UserVO> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(UserConverter::toVO);
    }

    /**
     * 分页获取所有用户
     */
    public Page<UserVO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(UserConverter::toVO);
    }

    /**
     * 根据用户名搜索用户（分页）
     * 支持模糊查询（忽略大小写）
     */
    public Page<UserVO> searchUsersByUsername(String username, Pageable pageable) {
        return userRepository.findByUsernameContainingIgnoreCase(username, pageable)
                .map(UserConverter::toVO);
    }

    /**
     * 根据邮箱搜索用户（分页）
     * 支持模糊查询（忽略大小写）
     */
    public Page<UserVO> searchUsersByEmail(String email, Pageable pageable) {
        return userRepository.findByEmailContainingIgnoreCase(email, pageable)
                .map(UserConverter::toVO);
    }

    /**
     * 更新用户信息
     */
    @SuppressWarnings("null")
    public UserVO updateUser(long id, UserUpdateDTO updateDTO) {
        UserDO userDO = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在，ID: " + id));

        // 使用Converter更新DO
        UserConverter.updateUserDO(userDO, updateDTO);
        UserDO updatedUser = userRepository.save(userDO);

        // DO转VO
        return UserConverter.toVO(updatedUser);
    }

    /**
     * 删除用户
     */
    public void deleteUser(long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("用户不存在，ID: " + id);
        }
        userRepository.deleteById(id);
    }

    /**
     * 用户登录验证
     */
    public Optional<UserVO> login(UserLoginDTO loginDTO) {
        Optional<UserDO> userDO = userRepository.findByUsername(loginDTO.getUsername());
        if (userDO.isPresent() && userDO.get().getPassword().equals(loginDTO.getPassword())) {
            return Optional.of(UserConverter.toVO(userDO.get()));
        }
        return Optional.empty();
    }
}
