package edu.feng.wj.service;

import edu.feng.wj.dao.UserDAO;
import edu.feng.wj.pojo.AdminRole;
import edu.feng.wj.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: wj
 * @description:
 * @author: feng
 * @create: 2020-04-20 10:40
 */

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;
    @Autowired
    AdminRoleService adminRoleService;
    @Autowired
    AdminUserRoleService adminUserRoleService;

    public boolean isExist(String username) {
        User user = getByUsername(username);
        return null != user;
    }

    public User getByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    public User get(String username, String password) {
        return userDAO.getByUsernameAndPassword(username, password);
    }

    public void add(User user) {
        userDAO.save(user);
    }

    // 直接在查询用户信息时就把角色信息查询出来
    // 使用这种方法需要在 User 实体类中添加属性来存放角色信息，但是由于数据库中并没有相应定义，所以我们要加上 @Transient 注释。
    public List<User> list() {
        List<User> users = userDAO.list();
        List<AdminRole> roles;
        for (User user : users) {
            roles = adminRoleService.listRolesByUser(user.getUsername());
            user.setRoles(roles);
        }
        return users;
    }

    public void addOrUpdate(User user) {
        userDAO.save(user);
    }

    public void editUser(User requestUser) {
        User userInDB = userDAO.findByUsername(requestUser.getUsername());
        userInDB.setName(requestUser.getName());
        userInDB.setPhone(requestUser.getPhone());
        userInDB.setEmail(requestUser.getEmail());
        adminUserRoleService.saveRoleChanges(userInDB.getId(), requestUser.getRoles());
    }
}

