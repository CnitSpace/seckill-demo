package org.example.service.impl;

import org.example.dao.UserMapper;
import org.example.dto.UserDTO;
import org.example.entity.User;
import org.example.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User register(UserDTO userDTO) {
        // 检查用户名是否已存在
        User existingUser = userMapper.selectByUsername(userDTO.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        // DTO转Entity
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);

        // 设置额外属性
        user.setSalt(UUID.randomUUID().toString().replace("-", ""));
        user.setPassword(passwordEncoder.encode(userDTO.getPassword() + user.getSalt()));
        user.setStatus(1); // 1表示正常状态
        user.setLoginCount(0);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());

        // 保存到数据库
        userMapper.insert(user);
        return user;
    }

    @Override
    public User findByUsername(String username) {
        if (!StringUtils.hasText(username)) {
            return null;
        }
        return userMapper.selectByUsername(username);
    }
}