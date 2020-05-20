package edu.feng.wj.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

/**
 * @program: wj
 * @description:
 * @author: feng
 * @create: 2020-04-20 10:08
 */

@Entity
@Table(name = "user")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;
    String username;
    String password;
    String salt;
    String name;
    String phone;
    String email;
    boolean enabled;
    // 使用这种方法需要在 User 实体类中添加属性来存放角色信息，但是由于数据库中并没有相应定义，所以我们要加上 @Transient 注释。
    @Transient
    List<AdminRole> roles;

    // 默认构造函数
    public User() {
    }

    // 用于配合自定义查询的构造函数
    public User(int id, String username, String name, String phone, String email, boolean enabled) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.enabled = enabled;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<AdminRole> getRoles() {
        return roles;
    }

    public void setRoles(List<AdminRole> roles) {
        this.roles = roles;
    }
}