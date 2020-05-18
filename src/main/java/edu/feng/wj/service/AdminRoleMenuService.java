package edu.feng.wj.service;

import edu.feng.wj.dao.AdminRoleMenuDAO;
import edu.feng.wj.pojo.AdminRoleMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @program: wj
 * @description: TODO
 * @author: feng
 * @create: 2020-05-18 13:11
 * @version: 1.0
 */
@Service
@Transactional
public class AdminRoleMenuService {
    @Autowired
    AdminRoleMenuDAO adminRoleMenuDAO;

    public List<AdminRoleMenu> findAllByRid(int rid){
        return adminRoleMenuDAO.findAllByRid(rid);
    }
}