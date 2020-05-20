package edu.feng.wj.realm;

import edu.feng.wj.pojo.User;
import edu.feng.wj.service.AdminPermissionService;
import edu.feng.wj.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 * @program: wj
 * @description: shiro Realm类 这是使用shiro的第一步，创建 Realm类 并重写获取认证与授权信息的方法
 * @author: feng
 * @create: 2020-04-25 09:52
 */
public class WJRealm extends AuthorizingRealm {
    @Autowired
    UserService userService;

    @Autowired
    AdminPermissionService adminPermissionService;
    // 重写获取授权信息方法
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 获取当前用户的所有权限
        String username = principalCollection.getPrimaryPrincipal().toString();
        Set<String> permissions = adminPermissionService.listPermissionURLsByUser(username);

        // 将权限放入授权信息中
        SimpleAuthorizationInfo s = new SimpleAuthorizationInfo();
        s.setStringPermissions(permissions);
        return s;
    }
    // 获取认证信息，即根据token中的用户名从数据库中获取盐、密码等并返回。
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = token.getPrincipal().toString();
        // 从数据库根据用户名取出用户
        User user = userService.getByUsername(username);
        String passwordInDB = user.getPassword();
        String salt = user.getSalt();
        // 根据用户名、盐、密码、和getname（）返回简单授权方法  salt 需要是byte[]
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(username, passwordInDB, ByteSource.Util.bytes(salt), getName());
        return authenticationInfo;
    }
}