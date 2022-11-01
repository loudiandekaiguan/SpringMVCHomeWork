package com.lyf.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import com.lyf.domain.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ResourcesInterceptor extends HandlerInterceptorAdapter {
    private List<String> ignoreUrl;
    public ResourcesInterceptor(List<String> ignoreUrl){
        this.ignoreUrl = ignoreUrl;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user_session = (User) request.getSession().getAttribute("USER_SESSION");
        String uri = request.getRequestURI();

        if(uri.contains("login")){
            return true;
        }

        if(user_session != null){
            if("ADMIN".equals(user_session.getRole())){
                return true;
            }else {
                for(String url : ignoreUrl){
                    if(uri.contains(url)){
                        return true;
                    }
                }
            }
        }
        request.setAttribute("msg", "您还没有登录，请先登录！！！");
        request.getRequestDispatcher("/admin/login.jsp").forward(request, response);
//        System.out.println("拦截了一个页面——————————————————————");
        return false;
    }
}
