package edu.feng.wj.dao;

import edu.feng.wj.pojo.AdminRolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @program: wj
 * @description: TODO
 * @author: feng
 * @create: 2020-05-19 10:42
 * @version: 1.0
 */
public interface AdminRolePermissionDAO extends JpaRepository<AdminRolePermission, Integer> {
    List<AdminRolePermission> findAllByRid(List<Integer> rids);
}