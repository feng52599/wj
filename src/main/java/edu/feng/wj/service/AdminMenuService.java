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
        // userRole 包含多个角色信息，对这些角色信息进行遍历 用这种方式解决了菜单项重复问题，但是在controller层要对父子节点进行重新遍历
        for (AdminUserRole userRole : userRoleList) {
            List<AdminRoleMenu> roleMenuList = adminRoleMenuService.findAllByRid(userRole.getRid());
            for (AdminRoleMenu roleMenu : roleMenuList) {
                // 增加防止多角色状态下菜单重复的逻辑
                AdminMenu menu = adminMenuDAO.findById(roleMenu.getMid());
                boolean isExist = false;
                for (AdminMenu m : menus) {
                    // 如果菜单重复
                    if (m.getId() == menu.getId()) {
                        isExist = true;
                    }
                }
                // 菜单不重复直接添加
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

    // 遗弃
    public void handleMenus(List<AdminMenu> menus) {
        // 把当前menu的id作为父节点，父节点为该id的所有menu，这就是它的children
        for (AdminMenu menu : menus) {
            List<AdminMenu> children = getAllByParentId(menu.getId());
            menu.setChildren(children);
        }

        Iterator<AdminMenu> iterator = menus.iterator();

        // 剔除掉所有子项，只保留第一层的父项。比如 c 是 b 的子项，b 是 a 的子项，我们最后只要保留 a 就行，因为 a 包含了 b 和 c
        // 只剩下a 但是其他的项都被这个项关联了
        // 为什么删除子项时用 iterator.remove() 而不用 List 的 remove 方法呢？
        // 是因为使用 List 遍历时，如果删除了某一个元素，后面的元素会补上来，也就是说后面元素的索引和列表长度都会发生改变。
        // 而循环仍然继续，循环的次数仍是最初的列表长度，这样既会漏掉一些元素，又会出现下标溢出，
        // 运行时表现就是会报 ConcurrentModificationException。而 iterator.remove() 进行了一些封装，会把当前索引和循环次数减 1，从而避免了这个问题。
        while (iterator.hasNext()) {
            AdminMenu menu = iterator.next();
            if (menu.getParentId() != 0)
                iterator.remove();
        }
    }

    // 查询出带parentId带所有menu
    public List<AdminMenu> getAllByParentId(int parentId) {
        return adminMenuDAO.findAllByParentId(parentId);
    }

}