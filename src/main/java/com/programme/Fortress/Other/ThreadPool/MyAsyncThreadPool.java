package com.programme.Fortress.Other.ThreadPool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Configuration
@EnableAsync
public class MyAsyncThreadPool {
    @Bean(name = "myAsyncPoolTaskExecutor")
    public ThreadPoolTaskExecutor getMyAsyncPoolTaskExecutor(){
        ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();
        poolTaskExecutor.setCorePoolSize(10);//核心线程数
        poolTaskExecutor.setMaxPoolSize(100);//最大线程数
        poolTaskExecutor.setQueueCapacity(200);//换存队列
        poolTaskExecutor.setKeepAliveSeconds(60);//非核心线程空闲时间
        poolTaskExecutor.setThreadNamePrefix("MY-Async-");//池名
        /**
         * 溢出策略
         * ThreadPoolExecutor.AbortPolicy   丢弃任务并抛出RejectedExecutionException异常
         * ThreadPoolExecutor.DiscardOldestPolicy   丢弃任务但不抛出异常
         * ThreadPoolExecutor.DiscardPolicy     丢弃队列最前面的任务，然后重新尝试执行任务（重复操作）
         * ThreadPoolExecutor.CallerRunsPolicy      重试添加当前任务，使用原本调用者的线程来执行
         */
        poolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        poolTaskExecutor.initialize();
        log.info("MyAsyncThreadPool 线程池初始化完成！");
        return poolTaskExecutor;
    }
}
