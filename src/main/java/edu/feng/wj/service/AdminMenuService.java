package edu.feng.wj.service;

import edu.feng.wj.dao.AdminMenuDAO;
import edu.feng.wj.pojo.AdminMenu;
import edu.feng.wj.pojo.AdminRoleMenu;
import edu.feng.wj.pojo.AdminUserRole;
import edu.feng.wj.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: wj
 * @description: TODO
 * @author: feng
 * @create: 2020-05-18 11:49
 * @version: 1.0
 */
@Service
public class AdminMenuService {
    @Autowired
    AdminMenuDAO adminMenuDAO;
    @Autowired
    UserService userService;
    @Autowired
    AdminUserRoleService adminUserRoleService;
    @Autowired
    AdminRoleMenuService adminRoleMenuService;


    public List<AdminMenu> getMenusByCurrentUser() {
        // 先从session中获取用户名，再从数据库中获取当前用户
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        User user = userService.getByUsername(username);

        System.out.println(username);
        System.out.println("user: " + user.getUsername());
        // 获得当前用户对应所有角色的id列表
        /**
         *  <=> List<AdminUserRole> urRole =  adminUserRoleService.listAllByUid(user.getId());
         *         List<Integer> rids = new ArrayList<>();
         *         for (AdminUserRole adminUserRole: urRole){
         *             rids.add(adminUserRole.getRid());
         *         }
         */
        List<AdminUserRole> userRoleList = adminUserRoleService.listAllByUid(user.getId());
        List<AdminMenu> menus = new ArrayList<>();
        for (AdminUserRole userRole : userRoleList) {
            List<AdminRoleMenu> roleMenuList = adminRoleMenuService.findAllByRid(userRole.getRid());
            for (AdminRoleMenu roleMenu : roleMenuList) {
                // 增加防止多角色状态下菜单重复的逻辑
                AdminMenu menu = adminMenuDAO.findById(roleMenu.getMid());
                boolean isExist = false;
                for (AdminMenu m : menus) {
                    if (m.getId() == menu.getId()) {
                        isExist = true;
                    }
                }
                if (!isExist) {
                    menus.add(menu);
                }
            }
        }
        return menus;
    }

    /**
     * 以下是原始版本，可以看出调用逻辑
     * @param menus
     */
    // public List<AdminMenu> getMenusByCurrentUser() {
    //     // 先从session中获取用户名，再从数据库中获取当前用户
    //     String username = SecurityUtils.getSubject().getPrincipal().toString();
    //     User user = userService.getByUsername(username);
    //
    //     System.out.println(username);
    //     System.out.println("user: " + user.getUsername());
    //     // 获得当前用户对应所有角色的id列表
    //     /**
    //      *  <=> List<AdminUserRole> urRole =  adminUserRoleService.listAllByUid(user.getId());
    //      *         List<Integer> rids = new ArrayList<>();
    //      *         for (AdminUserRole adminUserRole: urRole){
    //      *             rids.add(adminUserRole.getRid());
    //      *         }
    //      */
    //     List<Integer> rids = adminUserRoleService.listAllByUid(user.getId()).stream().map(AdminUserRole::getRid).collect(Collectors.toList());
    //
    //     System.out.println("rids" + rids);
    //     // 根据角色查询出对应的菜单项
    //     List<Integer> menuIds = adminRoleMenuService.listAllByRid(rids).stream().map(AdminRoleMenu::getMid).collect(Collectors.toList());
    //
    //
    //
    //
    //     System.out.println(menuIds);
    //
    //     List<AdminMenu> menus = adminMenuDAO.findAllById(menuIds).stream().distinct().collect(Collectors.toList());
    //     System.out.println("menus" + menus);
    //
    //     handleMenus(menus);
    //     return menus;
    // }

    public void handleMenus(List<AdminMenu> menus) {
        for (AdminMenu menu : menus) {
            List<AdminMenu> children = getAllByParentId(menu.getId());
            menu.setChildren(children);
        }

        Iterator<AdminMenu> iterator = menus.iterator();

        while (iterator.hasNext()) {
            AdminMenu menu = iterator.next();
            if (menu.getParentId() != 0)
                iterator.remove();
        }
    }

    public List<AdminMenu> getAllByParentId(int parentId) {
        return adminMenuDAO.findAllByParentId(parentId);
    }

}