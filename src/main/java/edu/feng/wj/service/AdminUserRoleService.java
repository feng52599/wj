package edu.feng.wj.service;

import edu.feng.wj.dao.AdminUserRoleDAO;
import edu.feng.wj.pojo.AdminUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.Role;
import java.util.List;

/**
 * @program: wj
 * @description: TODO
 * @author: feng
 * @create: 2020-05-18 12:48
 * @version: 1.0
 */
@Service
@Transactional
public class AdminUserRoleService {
    @Autowired
    AdminUserRoleDAO adminUserRoleDAO;

    public List<AdminUserRole> listAllByUid(int uid){
        return adminUserRoleDAO.findAllByUid(uid);
    }
}