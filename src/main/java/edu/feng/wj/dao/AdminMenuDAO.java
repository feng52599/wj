package edu.feng.wj.dao;

import edu.feng.wj.pojo.AdminMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @program: wj
 * @description: TODO
 * @author: feng
 * @create: 2020-05-18 11:45
 * @version: 1.0
 */
public interface AdminMenuDAO extends JpaRepository<AdminMenu,Integer> {
    List<AdminMenu> findAllByParentId(int parentId);
    AdminMenu findById(int id);

}