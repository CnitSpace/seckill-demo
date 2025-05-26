package org.example.service;

import org.example.dto.LoginDTO;
import org.example.entity.User;
import org.example.vo.LoginResultVO;

public interface AuthService {
    /**
     * 用户登录
     */
    LoginResultVO login(LoginDTO loginDTO);

    /**
     * 登出
     */
    void logout(String token);

    /**
     * 验证token
     */
    boolean verifyToken(String token);
}