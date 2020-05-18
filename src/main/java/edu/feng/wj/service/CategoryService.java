package edu.feng.wj.service;

import org.springframework.data.domain.Sort;
import edu.feng.wj.dao.CategoryDAO;
import edu.feng.wj.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @program: wj
 * @description:
 * @author: feng
 * @create: 2020-04-24 10:58
 */

@Service
@Transactional
public class CategoryService {
    @Autowired
    CategoryDAO categoryDAO;

    // 查询所有jpa
    public List<Category> list() {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return categoryDAO.findAll(sort);
    }

    public Category get(int id) {
        Category category = categoryDAO.findById(id).orElse(null);
        return category;
    }
}