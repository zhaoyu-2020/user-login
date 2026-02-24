package com.example.userlogin.util;

import com.example.userlogin.dto.UserRegisterDTO;
import com.example.userlogin.dto.UserUpdateDTO;
import com.example.userlogin.entity.UserDO;
import com.example.userlogin.vo.UserVO;

/**
 * User对象转换工具类
 * 负责DO、VO、DTO之间的转换
 */
public class UserConverter {

    /**
     * UserDO转UserVO
     * 将数据对象转换为视图对象，不包含敏感信息
     */
    public static UserVO toVO(UserDO userDO) {
        if (userDO == null) {
            return null;
        }

        UserVO vo = new UserVO();
        vo.setId(userDO.getId());
        vo.setUsername(userDO.getUsername());
        vo.setEmail(userDO.getEmail());
        vo.setCreatedAt(userDO.getCreatedAt());
        vo.setUpdatedAt(userDO.getUpdatedAt());
        return vo;
    }

    /**
     * UserRegisterDTO转UserDO
     * 将注册请求数据转换为数据对象
     */
    public static UserDO toUserDO(UserRegisterDTO registerDTO) {
        if (registerDTO == null) {
            return null;
        }

        UserDO userDO = new UserDO();
        userDO.setUsername(registerDTO.getUsername());
        userDO.setPassword(registerDTO.getPassword());
        userDO.setEmail(registerDTO.getEmail());
        return userDO;
    }

    /**
     * UserUpdateDTO更新UserDO
     * 将更新请求数据应用到数据对象
     */
    public static void updateUserDO(UserDO userDO, UserUpdateDTO updateDTO) {
        if (userDO == null || updateDTO == null) {
            return;
        }

        if (updateDTO.getUsername() != null && !updateDTO.getUsername().isEmpty()) {
            userDO.setUsername(updateDTO.getUsername());
        }
        if (updateDTO.getPassword() != null && !updateDTO.getPassword().isEmpty()) {
            userDO.setPassword(updateDTO.getPassword());
        }
        if (updateDTO.getEmail() != null && !updateDTO.getEmail().isEmpty()) {
            userDO.setEmail(updateDTO.getEmail());
        }
    }
}
