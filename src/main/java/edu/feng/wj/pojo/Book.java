package edu.feng.wj.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

/**
 * @program: wj
 * @description: 书籍类
 * @author: feng
 * @create: 2020-04-24 10:43
 */


@Entity
@Table(name = "book")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    // hibernate语法，设置了多对一 在数据库需要设置将category作为book的外键
    @ManyToOne
    // @JoinColumn与@Column标记一样，是用于注释表中的字段的。它的属性与@Column属性有很多相同之处，例如name属性是用来标识表中所对应的字段的名
    // @JoinColumn与@Column相区别的是：@JoinColumn注释的是保存表与表之间关系的字段，它要标注在实体属性上(相当于外键)。而@Column标注的是表
    // @JoinColumn(name="rid", referencedColumnName="cid")     //name=定义外键在本表的字段名 referencedColumnName =关联外键对象的哪个字段
    @JoinColumn(name = "cid")
    private Category category;

    String cover;
    String title;
    String author;
    String date;
    String press;
    String abs;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public String getAbs() {
        return abs;
    }

    public void setAbs(String abs) {
        this.abs = abs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}



