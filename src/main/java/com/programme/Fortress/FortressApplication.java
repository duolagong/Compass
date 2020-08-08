package com.programme.Fortress;

import com.programme.Fortress.Other.Listener.InitListener;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableEurekaClient
@MapperScan(value="com.programme.Fortress.Function.*.dao")//扫描接口
@SpringBootApplication
public class FortressApplication {

	/*@Autowired
	public InitListener listener;
	*//**
	 * 注册监听器(启动Netty)
	 * @return
	 *//*
	public ServletListenerRegistrationBean servletListenerRegistrationBean() {
		ServletListenerRegistrationBean servletListenerRegistrationBean = new ServletListenerRegistrationBean();
		servletListenerRegistrationBean.setListener(listener);
		return servletListenerRegistrationBean;
	}*/

	public static void main(String[] args) {
		SpringApplication.run(FortressApplication.class, args);
	}
}
