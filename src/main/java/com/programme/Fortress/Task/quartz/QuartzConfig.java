package com.programme.Fortress.Task.quartz;

import org.quartz.Scheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzConfig {
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(){
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setOverwriteExistingJobs(true);//覆盖已存在的任务
        schedulerFactoryBean.setStartupDelay(20);//延时90秒启动定时任务，避免系统未完全启动却开始执行定时任务的情况
        return schedulerFactoryBean;
    }
    @Bean(name = "scheduler")
    public Scheduler scheduler() {
        return schedulerFactoryBean().getScheduler();
    }
}
