package org.example.vo;

import org.example.entity.User;
import lombok.Data;

@Data
public class LoginResultVO {
    private String token;
    private String tokenType = "Bearer";
    private User userInfo;

    public LoginResultVO(String token, User user) {
        this.token = token;
        this.userInfo = user;
        // 敏感信息不返回
        this.userInfo.setPassword(null);
        this.userInfo.setSalt(null);
    }
}