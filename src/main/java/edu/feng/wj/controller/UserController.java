package edu.feng.wj.controller;

import edu.feng.wj.pojo.AdminRole;
import edu.feng.wj.pojo.User;
import edu.feng.wj.service.AdminPermissionService;
import edu.feng.wj.service.AdminRoleService;
import edu.feng.wj.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("api/admin/role")
    public List<AdminRole> listRoles() throws Exception {
        System.out.println("被执行");
        return adminRoleService.list();
    }
}