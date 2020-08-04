package edu.feng.wj.controller;

import edu.feng.wj.pojo.Book;
import edu.feng.wj.result.Result;
import edu.feng.wj.result.ResultFactory;
import edu.feng.wj.service.BookService;
import edu.feng.wj.service.RedisService;
import edu.feng.wj.utils.CastUtils;
import edu.feng.wj.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @program: wj
 * @description:
 * @author: feng
 * @create: 2020-04-24 11:25
 */

@RestController
public class LibraryController {
    @Autowired
    BookService bookService;
    @Autowired
    RedisService redisService;

    // 在MyWebConfigurer.java设置允许所有请求跨域，便可以不用@CrossOrigin
    @GetMapping("/api/books")
//    @CrossOrigin
    public List<Book> list() {
        List<Book> books;
        String key = "booklist";
        Object bookCache = redisService.get(key);

        if (bookCache == null) {
            // Sort sort = new Sort(Sort.Direction.DESC, "id");
            books = bookService.list();
            redisService.set(key, books);
        } else {
            books = CastUtils.objectConvertToList(bookCache, Book.class);
        }
        return books;
    }


    @PostMapping("/api/admin/content/books")
    public Result addOrUpdateBooks(@RequestBody @Valid Book book) {
        redisService.delete("booklist");
        bookService.addOrUpdate(book);
        return ResultFactory.buildSuccessResult("修改成功");
    }

    @PostMapping("/api/admin/content/books/delete")
    public Result deleteBook(@RequestBody @Valid Book book) {
        redisService.delete("booklist");
        bookService.deleteById(book.getId());
        return ResultFactory.buildSuccessResult("删除成功");
    }

    @PostMapping("/api/books")
//    @CrossOrigin
    public Book addOrUpdate(@RequestBody Book book) throws Exception {
        bookService.addOrUpdate(book);
        return book;
    }

    @PostMapping("/api/delete")
//    @CrossOrigin
    public void delete(@RequestBody Book book) throws Exception {
        bookService.deleteById(book.getId());
    }


    @GetMapping("/api/categories/{cid}/books")
//    @CrossOrigin
    public Result listByCategory(@PathVariable("cid") int cid) throws Exception {
        if (0 != cid) {
            return ResultFactory.buildSuccessResult(bookService.listByCategory(cid));
        } else {
            return ResultFactory.buildSuccessResult(bookService.list());
        }
    }

    @GetMapping("/api/search")
    public Result searchResult(@RequestParam("keywords") String keywords) {
        // 关键词为空返回所有书
        if ("".equals(keywords)) {
            return ResultFactory.buildSuccessResult(bookService.list());
        } else {
            return ResultFactory.buildSuccessResult(bookService.Search(keywords));
        }
    }


    @CrossOrigin
    @PostMapping("api/covers")
    public String coversUpload(MultipartFile file) throws Exception {
        String folder = "/Users/feng/Downloads/bookimage";
        File imageFolder = new File(folder);
        File f = new File(imageFolder, StringUtils.getRandomString(6) + file.getOriginalFilename()
                .substring(file.getOriginalFilename().length() - 4));
        if (!f.getParentFile().exists())
            f.getParentFile().mkdirs();
        try {
            file.transferTo(f);
            String imgURL = "http://localhost:8443/api/file/" + f.getName();
            return imgURL;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }



}