package edu.feng.wj.service;

import edu.feng.wj.dao.AdminRoleDAO;
import edu.feng.wj.pojo.AdminMenu;
import edu.feng.wj.pojo.AdminPermission;
import edu.feng.wj.pojo.AdminRole;
import edu.feng.wj.pojo.AdminUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: wj
 * @description: TODO
 * @author: feng
 * @create: 2020-05-19 10:52
 * @version: 1.0
 */
@Service
@Transactional
public class AdminRoleService {
    @Autowired
    AdminRoleDAO adminRoleDAO;
    @Autowired
    UserService userService;
    @Autowired
    AdminUserRoleService adminUserRoleService;
    @Autowired
    AdminPermissionService adminPermissionService;
    @Autowired
    AdminMenuService adminMenuService;

    public List<AdminRole> listRolesByUser(String username) {
        // 获取用户id
        int uid = userService.getByUsername(username).getId();
        //
        List<AdminRole> roles = new ArrayList<>();
        // 获取用户对应角色 用户角色对应 urs包含了uid rid=
        List<AdminUserRole> urs = adminUserRoleService.listAllByUid(uid);
        for (AdminUserRole ur : urs) {
            roles.add(adminRoleDAO.findById(ur.getRid()));
        }
        return roles;
    }

    public List<AdminRole> list() {
        List<AdminRole> roles = adminRoleDAO.findAll();
        List<AdminPermission> perms;
        List<AdminMenu> menus;
        for (AdminRole role : roles) {
            perms = adminPermissionService.listPermissionByRoleId(role.getId());
            menus = adminMenuService.listMenusByRoleId(role.getId());
            role.setPerms(perms);
            role.setMenus(menus);
        }

        return roles;
    }

    public AdminRole findById(int id) {
        return adminRoleDAO.findById(id);
    }

    public void addOrUpdate(AdminRole adminRole) {
        adminRoleDAO.save(adminRole);
    }
}