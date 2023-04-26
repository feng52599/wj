package edu.feng.wj.controller;

import edu.feng.wj.pojo.Book;
import edu.feng.wj.resp.BookResp;
import edu.feng.wj.result.Result;
import edu.feng.wj.result.ResultFactory;
import edu.feng.wj.service.BookService;
import edu.feng.wj.service.RedisService;
import edu.feng.wj.utils.CastUtils;
import edu.feng.wj.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
    public List<BookResp> list() {
        List<Book> books;
        // List <Book> returnList = new ArrayList<>();
        String key = "booklist";
        // Object bookCache = null ;
        Object bookCache = redisService.get(key);

        if (bookCache == null) {
            // Sort sort = new Sort(Sort.Direction.DESC, "id");
            books = bookService.list();
            // 这边的操作不对
            redisService.set(key, books);
        } else {
            books = CastUtils.objectConvertToList(bookCache, Book.class);
        }

        String s = "";
        // String a = null;

        // 取出这个books之后要做一些处理要
        // 1.过滤
        // List<Book> returnList = books.stream().filter(v -> v.getAuthor().equals("余秀华")).collect(Collectors.toList());
        // 2.自然排序
        // List<Book> returnList = books.stream().sorted().collect(Collectors.toList());

        // 3.非自然排序
        // List<Book> returnList = books.stream().sorted(Comparator.comparing(Book::getAuthor)).collect(Collectors.toList());
        // List<Book> returnList = books.stream().sorted((o1, o2) -> {
        //     if (o1.getId() == o2.getId()) {
        //         return 0;
        //     } else {
        //         return o1.getId() > o2.getId() ? -1 : 1;
        //     }
        // }).collect(Collectors.toList());

        // 4.map
        // List<String> collect = books.stream().map(Book::getAuthor).collect(Collectors.toList());
        // collect.forEach(System.out::println);

        // limit
        // List<Book> returnList = books.stream().filter(v -> "余秀华".equals(v.getAuthor())).limit(1).collect(Collectors.toList());

        // distinct 输出所有作者名字
        List<String> Authors = books.stream().map(Book::getAuthor).distinct().collect(Collectors.toList());
        Authors.forEach(System.out::println);

        // count
        long count = books.stream().map(Book::getAuthor).distinct().count();
        System.out.println("Author Num : " + count);
        // group BY
        Map<String, List<Book>> map = books.stream().collect(Collectors.groupingBy(Book::getAuthor));

        // 有键值冲突，选前者，参考hashMap 此处JDK8 有bug
        Map<String, String> authorToTitle = books.stream().filter(v -> null != v).collect(Collectors.toMap(Book::getAuthor, Book::getTitle, (a, b) -> a));
        books.stream().map(Book::getAuthor).collect(Collectors.toList());
        // map 函数式编程
        List<BookResp> returnList = books.stream().map(book -> {
            BookResp bookResp = new BookResp();
            BeanUtils.copyProperties(book, bookResp);
            return bookResp;
        }).collect(Collectors.toList());

        return returnList;
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