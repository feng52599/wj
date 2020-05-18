package edu.feng.wj.interceptor;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor  implements HandlerInterceptor{

   @Override
   public boolean preHandle (HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
       // 放行 options 请求，否则无法让前端带上自定义的 header 信息，导致 sessionID 改变，shiro 验证失败
       if (HttpMethod.OPTIONS.toString().equals(httpServletRequest.getMethod())) {
           httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
           System.out.println("第一次访问：" + true);
           return true;
       }

       Subject subject = SecurityUtils.getSubject();
       // 只有当验证失败且记住五失效时但会false
       if (!subject.isAuthenticated() && !subject.isRemembered()) {
           return false;
       }
       System.out.println("是否记忆："+subject.isRemembered());
       System.out.println("是否验证："+subject.isAuthenticated());
       System.out.println("最终返回" + true);
       return true;
   }

   private boolean begingWith(String page, String[] requiredAuthPages) {
       boolean result = false;
       for (String requiredAuthPage : requiredAuthPages) {
           if(StringUtils.startsWith(page, requiredAuthPage)) {
               result = true;
               break;
           }
       }
       return result;
   }
}