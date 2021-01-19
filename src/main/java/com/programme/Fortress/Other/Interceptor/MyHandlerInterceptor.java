package com.programme.Fortress.Other.Interceptor;

import com.programme.Fortress.Util.Redis.RedisUtil;
import com.programme.Fortress.Util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class MyHandlerInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 目标方法执行前校验
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest servletRequest, HttpServletResponse servletResponse, Object handler) throws Exception {
        /*Object loginUser =request.getSession().getAttribute("loginSign");
        if(loginUser == null){
            request.setAttribute("msg","无登录权限，请先登录！");
            response.sendRedirect("/");
            *//*request.getRequestDispatcher("/index.html").forward(request,response);*//*
            return false;
        }else {
            return true;
        }*/
        Cookie[] cookies = servletRequest.getCookies();
        if (cookies == null) {
            servletResponse.sendRedirect("/");
            return false;
        }
        String token = "";
        for (Cookie cookie : cookies) {
            if ("COMPASS_TOKEN".equals(cookie.getName())) {
                token = cookie.getValue();
                break;
            }
        }
        if (StringUtil.checkEmpty(token)){
            servletResponse.sendRedirect("/");
            return false;
        }
        //判断一下当前的tooken是否存在或有没有过期
        if (null == redisUtil.get("COM:USER:" + token)) {
            servletRequest.setAttribute("indexRemind","您的用户在另一个地方登录了,请妥善管理！");
            servletResponse.sendRedirect("/");
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
