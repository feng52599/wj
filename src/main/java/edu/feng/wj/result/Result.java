package edu.feng.wj.result;

import lombok.Data;

/**
 * @program: wj
 * @description:
 * @author: feng
 * @create: 2020-04-20 10:16
 */
@Data
public class Result {
    //响应码  实际上由于响应码是固定的，code 属性应该是一个枚举值，这里作了一些简化。
    private int code;
    private String message;
    private Object result;

    public Result(int code, String message, Object result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

}