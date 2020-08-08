package com.programme.Fortress.Other.ThreadPool;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Var;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Controller
@EnableAsync
public class MyThreadPool {

    @Bean
    public Executor ServiceExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);    //核心线程数
        executor.setMaxPoolSize(30);    //最大线程数
        executor.setQueueCapacity(999); //队列大小
        executor.setThreadNamePrefix("compass-"); //线程前缀
        //达到最大线程数的后续处理
        //不在新线程中执行任务，使用原本调用者的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //初始化
        executor.initialize();
        log.info("MyThreadPool 线程池初始化完成！");
        return executor;
    }
}
