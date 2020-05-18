package edu.feng.wj.controller;

import edu.feng.wj.pojo.AdminMenu;
import edu.feng.wj.service.AdminMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return menus;
    }
}