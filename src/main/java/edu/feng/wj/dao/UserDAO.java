package edu.feng.wj.dao;

import edu.feng.wj.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserDAO extends JpaRepository<User,Integer> {
    User findByUsername(String username);

    User getByUsernameAndPassword(String username,String password);

    // 这是 JPQL 的写法 查询出指定的数据 这里没有查出密码
    // 使用原生的 SQL 语句，指定 nativeQuery = true 即可。
    @Query(value = "select new User(u.id,u.username,u.name,u.phone,u.email,u.enabled) from User u")
    List<User> list();
}
