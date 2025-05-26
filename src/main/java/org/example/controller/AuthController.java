package org.example.controller;

import org.example.dto.LoginDTO;
import org.example.service.AuthService;
import org.example.vo.LoginResultVO;
import org.example.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResultVO<LoginResultVO> login(@RequestBody @Validated LoginDTO loginDTO) {
        LoginResultVO result = authService.login(loginDTO);
        return ResultVO.success(result);
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public ResultVO<String> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token.replace("Bearer ", ""));
        return ResultVO.success("登出成功");
    }

    @PostMapping("/verify")
    public ResultVO<String> verifyToken(@RequestHeader("Authorization") String token) {
        return ResultVO.success("token status:" + authService.verifyToken(token));
    }
}