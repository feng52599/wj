package edu.feng.wj.controller;

import edu.feng.wj.pojo.JotterArticle;
import edu.feng.wj.result.Result;
import edu.feng.wj.result.ResultFactory;
import edu.feng.wj.service.JotterArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

/**
 * @program: wj
 * @description: TODO
 * @author: feng
 * @create: 2020-05-24 16:11
 * @version: 1.0
 */
@RestController
public class JotterController {
    @Autowired
    JotterArticleService jotterArticleService;

    @PostMapping("/api/admin/content/article")
    public Result saveArticle(@RequestBody  @Valid JotterArticle jotterArticle) {
        // jotterArticle.setArticleDate();
        java.sql.Date articleTime = new java.sql.Date(new java.util.Date().getTime());
        jotterArticle.setArticleDate(articleTime);
        jotterArticleService.addOrUpdate(jotterArticle);
        return ResultFactory.buildSuccessResult("保存成功");
    }

    @GetMapping("/api/article/{size}/{page}")
    public Result listArticle(@PathVariable("size") int size, @PathVariable("page") int page) {
        return ResultFactory.buildSuccessResult(jotterArticleService.list(page - 1, size));
    }

    @GetMapping("/api/jotter/article/{id}")
    public Result getArticleDetail(@PathVariable("id") int id) {
        return ResultFactory.buildSuccessResult(jotterArticleService.findById(id));
    }

    @GetMapping("/api/article/{id}")
    public JotterArticle getOneArticle(@PathVariable("id") int id) {
        return jotterArticleService.findById(id);
    }


    @DeleteMapping("/api/admin/content/article/{id}")
    public Result deleteArticle(@PathVariable("id") int id) {
        jotterArticleService.delete(id);
        return ResultFactory.buildSuccessResult("删除成功");
    }

}