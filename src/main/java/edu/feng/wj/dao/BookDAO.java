package edu.feng.wj.dao;

import edu.feng.wj.pojo.Book;
import edu.feng.wj.pojo.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @program: wj
 * @description:
 * @author: feng
 * @create: 2020-04-24 10:52
 */
public interface BookDAO extends JpaRepository<Book, Integer> {
    //    findAllByCategory() 之所以能实现，是因为在 Book 类中有如下注解：
    // 实际上是把 category 对象的 id 属性作为 cid 进行了查询。
    List<Book> findAllByCategory(Category category);

    // 翻译过来就是“根据标题或作者进行模糊查询” 这里要写两个参数，一个代表标题，一个代表作者
    List<Book> findAllByTitleLikeOrAuthorLike(String keyword1, String keyword2);

}