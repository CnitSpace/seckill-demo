package org.example.service.impl;

import org.example.constant.ErrorCode;
import org.example.dao.UserMapper;
import org.example.dto.LoginDTO;
import org.example.entity.User;
import org.example.exception.BusinessException;
import org.example.service.AuthService;
import org.example.util.JwtTokenUtil;
import org.example.vo.LoginResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public LoginResultVO login(LoginDTO loginDTO) {
        // 1. 验证用户名是否存在
        User user = userMapper.selectByUsername(loginDTO.getUsername());
        if (user == null) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }

        // 2. 验证账号状态
        if (user.getStatus() != 1) {
            throw new BusinessException(ErrorCode.ACCOUNT_DISABLED);
        }

        // 3. 验证密码
        if (!passwordEncoder.matches(loginDTO.getPassword() + user.getSalt(), user.getPassword())) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }

        // 4. 更新登录信息
        userMapper.updateLoginInfo(user.getId());

        // 5. 生成Token
        String token = jwtTokenUtil.generateToken(user);

        // 6. 返回登录结果
        return new LoginResultVO(token, user);
    }

    @Override
    public void logout(String token) {
        // 验证token测试
        Boolean isToken = jwtTokenUtil.validateToken(token);
        System.out.println(isToken);
        // 使Token失效
        jwtTokenUtil.invalidateToken(token);
    }

    @Override
    public boolean verifyToken(String token) {
        // 验证token测试
        return  jwtTokenUtil.validateToken(token);
    }
}