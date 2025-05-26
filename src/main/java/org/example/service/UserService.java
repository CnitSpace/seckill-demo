package org.example.service;

import org.example.dto.UserDTO;
import org.example.entity.User;

public interface UserService {
    /**
     * 注册新用户
     */
    User register(UserDTO userDTO);

    /**
     * 根据用户名查询用户
     */
    User findByUsername(String username);
}