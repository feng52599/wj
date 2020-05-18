package edu.feng.wj.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;


/**
 * id	唯一标识
 * path	与 Vue 路由中的 path 对应，即地址路径
 * name	与 Vue 路由中的 name 属性对应
 * name_zh	中文名称，用于渲染导航栏（菜单）界面
 * icon_cls	element 图标类名，用于渲染菜单名称前的小图标
 * component	组件名，用于解析路由对应的组件
 * parent_id	父节点 id，用于存储导航栏层级关系
 * 数据库中不存在对应字段的属性，需要用 @Transient 注记标注出来 与数据库中不同的是这个 Children 属性，用于存储子节点。
 */
@Entity
@Table(name = "admin_menu")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class AdminMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;
    String path;
    String name;
    String nameZh;
    String iconCls;
    String component;
    int parentId;
    @Transient
    List<AdminMenu> children;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameZh() {
        return nameZh;
    }

    public void setNameZh(String nameZh) {
        this.nameZh = nameZh;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public List<AdminMenu> getChildren() {
        return children;
    }

    public void setChildren(List<AdminMenu> children) {
        this.children = children;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
}
