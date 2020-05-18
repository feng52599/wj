package edu.feng.wj.service;

import edu.feng.wj.dao.BookDAO;
import edu.feng.wj.dao.CategoryDAO;
import edu.feng.wj.pojo.Book;
import edu.feng.wj.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @program: wj
 * @description:
 * @author: feng
 * @create: 2020-04-24 11:16
 */
@Service
@Transactional
public class BookService {
    @Autowired
    BookDAO bookDAO;
    @Autowired
    CategoryService categoryService;

    public List<Book> list(){
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return bookDAO.findAll(sort);
    }

//这里注意一下 save() 方法的作用是，当主键存在时更新数据，当主键不存在时插入数据。
    public void addOrUpdate(Book book){
        bookDAO.save(book);
    }

    public void deleteById(int id) {
        bookDAO.deleteById(id);
    }

    public List<Book> listByCategory(int cid) {
        Category category = categoryService.get(cid);
        return bookDAO.findAllByCategory(category);
    }


    public List<Book> Search(String keywords) {
        return bookDAO.findAllByTitleLikeOrAuthorLike('%' + keywords + '%', '%' + keywords + '%');
    }
}