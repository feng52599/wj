package edu.feng.wj.controller;

import edu.feng.wj.pojo.AdminPermission;
import edu.feng.wj.pojo.AdminRole;
import edu.feng.wj.pojo.User;
import edu.feng.wj.result.Result;
import edu.feng.wj.result.ResultFactory;
import edu.feng.wj.service.AdminPermissionService;
import edu.feng.wj.service.AdminRoleService;
import edu.feng.wj.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: wj
 * @description: TODO
 * @author: feng
 * @create: 2020-05-19 16:04
 * @version: 1.0
 */
@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    AdminPermissionService adminPermissionService;
    @Autowired
    AdminRoleService adminRoleService;

    @RequiresPermissions("/api/admin/user")
    @GetMapping("/api/admin/user")
    public List<User> listUsers() throws Exception {
        return userService.list();
    }


    // 修改
    @PutMapping("/api/admin/user")
    public Result editUser(@RequestBody User requestUser) {
        userService.editUser(requestUser);
        String message = "修改用户信息成功";
        return ResultFactory.buildSuccessResult(message);
    }

    @PutMapping("/api/admin/user/status")
    public Result updateUserStatus(@RequestBody User requestUser) {
        User user = userService.getByUsername(requestUser.getUsername());
        user.setEnabled(requestUser.isEnabled());
        userService.addOrUpdate(user);
        String message = "用户" + requestUser.getUsername() + "状态更新成功";
        return ResultFactory.buildSuccessResult(message);
    }

    /**
     * 这是注册时的设置密码
     *
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
     */
    // 重置密码
    @PutMapping("/api/admin/user/password")
    public Result resetPassword(@RequestBody User requestUser) {
        User user = userService.getByUsername(requestUser.getUsername());
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        String encodedPassword = new SimpleHash("md5", "123", salt, times).toString();
        user.setSalt(salt);
        user.setPassword(encodedPassword);
        userService.addOrUpdate(user);
        String message = "重置密码成功";
        return ResultFactory.buildSuccessResult(message);
    }

    @GetMapping("/api/admin/perm")
    public List<AdminPermission> listPerms() {
        return adminPermissionService.list();
    }

    @GetMapping("api/admin/role")
    public List<AdminRole> listRoles() throws Exception {
        System.out.println("被执行");
        return adminRoleService.list();
    }

    @PutMapping("api/admin/role/status")
    public Result updateRoleStatus(@RequestBody AdminRole requestRole) {
        AdminRole adminRole = adminRoleService.findById(requestRole.getId());
        adminRole.setEnabled(requestRole.isEnabled());
        adminRoleService.addOrUpdate(adminRole);
        String message = "角色" + requestRole.getName() + "状态更新成功";
        return ResultFactory.buildSuccessResult(message);
    }

}