package edu.feng.wj.controller;

import edu.feng.wj.dao.UserDAO;
import edu.feng.wj.result.Result;
import edu.feng.wj.result.ResultFactory;
import edu.feng.wj.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import edu.feng.wj.pojo.User;
import org.springframework.web.util.HtmlUtils;

import java.util.Objects;

/**
 * @program: wj
 * @description: 登录控制类
 * @author: feng
 * @create: 2020-04-20 10:17
 */

@Controller
public class LoginController {
    @Autowired
    UserService userService;

       // @CrossOrigin
    // @ResponseBody的作用是将java对象转为json格式的数据.
    @PostMapping(value = "api/login")
    @ResponseBody
    public Result login(@RequestBody User requestUser) {
        // 对 html 标签进行转义，防止 XSS 攻击
        String username = requestUser.getUsername();
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, requestUser.getPassword());
        usernamePasswordToken.setRememberMe(true);
        try {
            //  subject.login(usernamePasswordToken) 就可以执行验证
            //  该过程会产生 session，并自动把 sessionId 设置到 cookie。
            subject.login(usernamePasswordToken);
            return ResultFactory.buildSuccessResult(username);
        }catch (Exception e){
            String message="账号或密码错误";
            return ResultFactory.buildFailResult(message);
        }
    }

    @PostMapping("api/register")
    @ResponseBody
    public Result register(@RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        username = HtmlUtils.htmlEscape(username);

        // 为什么要去重复设置username
        user.setUsername(username);

        boolean exist = userService.isExist(username);

        if (exist) {
            String message = "该用户名已被使用";
            return ResultFactory.buildFailResult(message);
        }

        //生成盐 默认长度16位
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();

        System.out.println("盐值: " + salt);

        // 设置hash算法迭代次数
        int times = 2;

        // 得到hash后的密码
        String encodedPassword = new SimpleHash("md5", password, salt, times).toString();

        System.out.println("加密后密码" + encodedPassword);

        // 存储用户信息 ,将盐值和被加密后的密码放入数据库
        // 在登录时，先找出该用户的盐值，并对盐值进行hash，然后会密码进行hash，最后将两个进行连接，得到最终密码与数据库密码进行比对
        user.setSalt(salt);
        user.setPassword(encodedPassword);
        userService.add(user);

        return ResultFactory.buildSuccessResult(user);
    }

    @ResponseBody
    @GetMapping("api/logout")
    public Result logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        String message = "成功登出";
        return ResultFactory.buildSuccessResult(message);
    }

    @ResponseBody
    @GetMapping(value = "api/authentication")
    public String authentication(){
        return "身份认证成功";
    }

}
