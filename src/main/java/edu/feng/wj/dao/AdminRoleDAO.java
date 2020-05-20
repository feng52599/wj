package edu.feng.wj.dao;

import edu.feng.wj.pojo.AdminRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRoleDAO extends JpaRepository<AdminRole,Integer> {
    public AdminRole findById(int id);
}
