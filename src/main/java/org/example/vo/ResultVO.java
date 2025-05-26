package org.example.vo;

import lombok.Data;

@Data
public class ResultVO<T> {
    private int code;
    private String message;
    private T data;

    public static <T> ResultVO<T> success(T data) {
        ResultVO<T> result = new ResultVO<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    public static <T> ResultVO<T> fail(String message) {
        ResultVO<T> result = new ResultVO<>();
        result.setCode(400);
        result.setMessage(message);
        return result;
    }
}