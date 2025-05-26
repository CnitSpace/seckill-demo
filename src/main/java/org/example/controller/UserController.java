package org.example.controller;

import org.example.dto.UserDTO;
import org.example.entity.User;
import org.example.service.UserService;
import org.example.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册接口
     */
    @PostMapping("/register")
    public ResultVO<User> register(@RequestBody @Validated UserDTO userDTO) {
        User user = userService.register(userDTO);
        return ResultVO.success(user);
    }

    /**
     * 根据用户名查询用户
     */
    @GetMapping("/{username}")
    public ResultVO<User> getUserByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username);
        if (user == null) {
            return ResultVO.fail("用户不存在");
        }
        return ResultVO.success(user);
    }
}