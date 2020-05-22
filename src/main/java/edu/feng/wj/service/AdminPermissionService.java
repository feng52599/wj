package edu.feng.wj.service;

import edu.feng.wj.dao.AdminPermissionDAO;
import edu.feng.wj.dao.AdminRolePermissionDAO;
import edu.feng.wj.pojo.AdminPermission;
import edu.feng.wj.pojo.AdminRole;
import edu.feng.wj.pojo.AdminRolePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @program: wj
 * @description: TODO
 * @author: feng
 * @create: 2020-05-19 10:50
 * @version: 1.0
 */

@Service
@Transactional
public class AdminPermissionService {
    @Autowired
    AdminPermissionDAO adminPermissionDAO;

    @Autowired
    AdminRoleService adminRoleService;

    @Autowired
    AdminRolePermissionDAO adminRolePermissionDAO;

    /**
     * <=> List<AdminRole> Roles =  listRolesByUser(username));
     * List<Integer> rids = new ArrayList<>();
     * for (AdminRole Role: Roles){
     * rids.add(Role.getid());
     * }
     * 相当于把获取的集合 的类进行getid
     */
    public Set<String> listPermissionURLsByUser(String username) {
        // 获取用户对应所有的角色id
        List<Integer> rids = adminRoleService.listRolesByUser(username).stream().map(AdminRole::getId).collect(Collectors.toList());

        // 通过中间表 以rid 获取所有pid
        List<Integer> pids = adminRolePermissionDAO.findAllByRid(rids).stream().map(AdminRolePermission::getPid).collect(Collectors.toList());

        List<AdminPermission> perms = adminPermissionDAO.findAllById(pids);

        Set<String> URLs = perms.stream().map(AdminPermission::getUrl).collect(Collectors.toSet());

        return URLs;
    }


    /**
     * 判断用户请求接口是否在权限列表中
     */

    public boolean needFilter(String requestAPI) {
        List<AdminPermission> ps = adminPermissionDAO.findAll();
        for (AdminPermission p : ps) {
            if (p.getUrl().equals(requestAPI)) {
                return true;
            }
        }
        return false;
    }

    public List<AdminPermission> list() {return adminPermissionDAO.findAll();}
}