package com.example.userlogin.repository;

import com.example.userlogin.entity.UserDO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserDO, Long> {

    /**
     * 根据用户名查找用户
     */
    Optional<UserDO> findByUsername(String username);

    /**
     * 根据邮箱查找用户
     */
    Optional<UserDO> findByEmail(String email);

    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否存在
     */
    boolean existsByEmail(String email);

    /**
     * 根据用户名模糊搜索（分页）
     */
    Page<UserDO> findByUsernameContainingIgnoreCase(String username, Pageable pageable);

    /**
     * 根据邮箱模糊搜索（分页）
     */
    Page<UserDO> findByEmailContainingIgnoreCase(String email, Pageable pageable);
}
