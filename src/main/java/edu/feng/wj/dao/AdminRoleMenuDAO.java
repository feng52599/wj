package edu.feng.wj.dao;

import edu.feng.wj.pojo.AdminRoleMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @program: wj
 * @description: TODO
 * @author: feng
 * @create: 2020-05-18 11:44
 * @version: 1.0
 */
public interface AdminRoleMenuDAO extends JpaRepository<AdminRoleMenu,Integer> {
    List<AdminRoleMenu> findAllByRid(List<Integer> rid);
}