package edu.feng.wj.dao;

import edu.feng.wj.pojo.JotterArticle;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @program: wj
 * @description: TODO
 * @author: feng
 * @create: 2020-05-24 16:06
 * @version: 1.0
 */
public interface JotterArticleDAO extends JpaRepository<JotterArticle, Integer> {
    JotterArticle findById(int id);
}