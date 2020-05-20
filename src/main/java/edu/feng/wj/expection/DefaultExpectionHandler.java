package edu.feng.wj.expection;

import edu.feng.wj.result.Result;
import edu.feng.wj.result.ResultFactory;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: wj
 * @description: TODO
 * @author: feng
 * @create: 2020-05-19 17:10
 * @version: 1.0
 */

@ControllerAdvice
public class DefaultExpectionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handleAuthorizationExpection(UnauthorizedException e) {
        String message = "认证授权失败";
        return ResultFactory.buildFailResult(message);
    }
}