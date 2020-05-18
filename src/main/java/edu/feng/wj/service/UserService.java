package edu.feng.wj.service;

import edu.feng.wj.dao.UserDAO;
import edu.feng.wj.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public boolean isExist(String username) {
        User user = getByUsername(username);
        return null!=user;
    }

    public User getByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    public User get(String username, String password){
        return userDAO.getByUsernameAndPassword(username, password);
    }

    public void add(User user) {
        userDAO.save(user);
    }
}

