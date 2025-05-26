package org.example.constant;

import lombok.Getter;

/**
 * 错误码枚举
 */

@Getter
public enum ErrorCode {
    // 通用错误
    SUCCESS(200, "成功"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    INTERNAL_ERROR(500, "系统内部错误"),

    // 认证相关
    LOGIN_FAILED(1001, "用户名或密码错误"),
    ACCOUNT_DISABLED(1002, "账号已被禁用"),
    TOKEN_EXPIRED(1003, "Token已过期"),
    TOKEN_INVALID(1004, "Token无效"),

    // 用户相关
    USER_EXISTS(2001, "用户已存在"),
    USER_NOT_FOUND(2002, "用户不存在"),

    // 秒杀相关
    SECKILL_ENDED(3001, "秒杀已结束"),
    SECKILL_REPEAT(3002, "不能重复秒杀"),
    STOCK_NOT_ENOUGH(3003, "库存不足"),
    RATE_LIMIT(3004, "操作过于频繁，请稍后再试");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}