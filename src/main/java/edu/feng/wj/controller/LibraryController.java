package edu.feng.wj.controller;

import edu.feng.wj.pojo.Book;
import edu.feng.wj.service.BookService;
import edu.feng.wj.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    // 在MyWebConfigurer.java设置允许所有请求跨域，便可以不用@CrossOrigin
    @GetMapping("/api/books")
//    @CrossOrigin
    public List<Book> list() throws Exception {
        return bookService.list();
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
    public List<Book> listByCategory(@PathVariable("cid") int cid) throws Exception {
        if (0 != cid) {
            return bookService.listByCategory(cid);
        } else {
            return list();
        }
    }

    @CrossOrigin
    @GetMapping("/api/search")
    public List<Book> searchResult(@RequestParam("keywords") String keywords) {
        // 关键词为空时查询出所有书籍
        if ("".equals(keywords)) {
            return bookService.list();
        } else {
            return bookService.Search(keywords);
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