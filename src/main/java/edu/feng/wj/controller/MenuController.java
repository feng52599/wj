package edu.feng.wj.controller;

import edu.feng.wj.pojo.AdminMenu;
import edu.feng.wj.service.AdminMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.List;

/**
 * @program: wj
 * @description: TODO
 * @author: feng
 * @create: 2020-05-18 13:31
 * @version: 1.0
 */
@RestController
public class MenuController {
    @Autowired
    AdminMenuService adminMenuService;

    @GetMapping("/api/menu")
    public List<AdminMenu> menu(){
        List <AdminMenu> menus =  adminMenuService.getMenusByCurrentUser();
        for (AdminMenu menu : menus) {
            menu.setChildren(adminMenuService.getAllByParentId(menu.getId()));
        }

        // 为什么删除子项时用 iterator.remove() 而不用 List 的 remove 方法呢？是因为使用 List 遍历时，如果删除了某一个元素，后面的元素会补上来，也就是说后面元素的索引和列表长度都会发生改变。而循环仍然继续，
        // 循环的次数仍是最初的列表长度，这样既会漏掉一些元素，又会出现下标溢出，运行时表现就是会报 ConcurrentModificationException。
        // 而 iterator.remove() 进行了一些封装，会把当前索引和循环次数减 1，从而避免了这个问题。
        Iterator<AdminMenu> iterator = menus.iterator();
        while (iterator.hasNext()) {
            AdminMenu menu = iterator.next();
            if (menu.getParentId() != 0) {
                iterator.remove();
            }
        }
        return menus;
    }
}