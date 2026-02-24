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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
     * 获取所有用户
     */
    public List<UserVO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserConverter::toVO)
                .collect(Collectors.toList());
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
