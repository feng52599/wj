package edu.feng.wj.service;

import edu.feng.wj.dao.JotterArticleDAO;
import edu.feng.wj.pojo.JotterArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @program: wj
 * @description: TODO
 * @author: feng
 * @create: 2020-05-24 16:12
 * @version: 1.0
 */

@Service
public class JotterArticleService {
    @Autowired
    JotterArticleDAO jotterArticleDAO;

    public void addOrUpdate(JotterArticle jotterArticle) {
        jotterArticleDAO.save(jotterArticle);
    }

    // Spring Data 提供了 org.springframework.data.domain.Page 类，该类包含了页码、页面尺寸等信息，
    // 可以很方便地实现分页。我们要做的，就是编写一个传入页码与页面尺寸参数的方法，这个方法可以写在 service 层。
    public Page list(int page, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return jotterArticleDAO.findAll(PageRequest.of(page, size));
    }

    public JotterArticle findById(int id) {
        return jotterArticleDAO.findById(id);
    }

    public void delete(int id) {
        jotterArticleDAO.deleteById(id);
    }
}