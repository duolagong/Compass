package com.programme.Fortress.Task.service;

import com.programme.Fortress.Function.ToolCase.entity.PlanJob;
import com.programme.Fortress.Task.JobOperateEnum;
import org.quartz.SchedulerException;

public interface QuartzService {

    /**
     * 服务器启动执行定时任务
     */
    void timingTask();

    /**
     * 新增定时任务
     */
    void addJob(PlanJob job);

    /**
     * 操作定时任务
     */
    void operateJob(JobOperateEnum jobOperateEnum, PlanJob job) throws SchedulerException;

    /**
     * 启动所有任务
     */
    void startAllJob() throws SchedulerException;

    /**
     * 暂停所有任务
     */
    void pauseAllJob() throws SchedulerException;
}
