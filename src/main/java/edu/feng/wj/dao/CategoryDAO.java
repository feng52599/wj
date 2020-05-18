package edu.feng.wj.dao;

import edu.feng.wj.pojo.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @program: wj
 * @description:
 * @author: feng
 * @create: 2020-04-24 10:57
 */
public interface CategoryDAO extends JpaRepository<Category, Integer> {
}