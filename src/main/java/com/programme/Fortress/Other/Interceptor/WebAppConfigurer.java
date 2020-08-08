package com.programme.Fortress.Other.Interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Deprecated
public class WebAppConfigurer  extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/main.html").setViewName("sucess");//试图映射
    }

    //所有的 WebMvcConfigurerAdapter
    @Bean//将组件组成到容器中
    public WebMvcConfigurerAdapter webMvcConfigurerAdapter(){
        WebMvcConfigurerAdapter webMvcConfigurerAdapter = new WebMvcConfigurerAdapter(){
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/").setViewName("index");//试图映射
                registry.addViewController("/index.html").setViewName("index");//试图映射
            }

            //注册拦截器
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                //无需担心静态资源，springboot已经做好了映射
                registry.addInterceptor(new MyHandlerInterceptor()).addPathPatterns("/**")
                        .excludePathPatterns("/index.html","/","/user/login");//拦截所有的链接，但不拦截"/index.html"和"/"和"/user/login"
            }
        };
        return webMvcConfigurerAdapter;
    }
}
