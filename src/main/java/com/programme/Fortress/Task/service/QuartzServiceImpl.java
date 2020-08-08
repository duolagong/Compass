package com.programme.Fortress.Task.service;

import com.programme.Fortress.Function.ToolCase.entity.PlanJob;
import com.programme.Fortress.Function.ToolCase.service.PlanJobService;
import com.programme.Fortress.Task.JobOperateEnum;
import com.programme.Fortress.Task.quartz.QuartzFactory;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class QuartzServiceImpl implements QuartzService {

    /**
     * 调度器
     */
    @Autowired
    private Scheduler scheduler;

    @Autowired
    private PlanJobService jobService;

    /**
     * 服务器启动执行定时任务
     */
    @Override
    public void timingTask() {
        //查询数据库是否存在需要定时的任务
        List<PlanJob> scheduleJobs = jobService.list(null);
        for (PlanJob planJob : scheduleJobs) {
            try {
                this.addJob(planJob);
                if (planJob.getStatus()==0) {
                    this.operateJob(JobOperateEnum.PAUSE, planJob);
                }
            } catch (Exception e) {
                log.error("初始化加载-定时任务失败", e);
            }

        }
    }

    /**
     * 新增定时任务
     *
     * @param job
     */
    @Override
    public void addJob(PlanJob job) {
        try {
            //创建触发器
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName())
                    .withSchedule(CronScheduleBuilder.cronSchedule(job.getCronExpression()))
                    .startNow()
                    .build();

            //创建任务
            JobDetail jobDetail = JobBuilder.newJob(QuartzFactory.class)
                    .withIdentity(job.getJobName())
                    .build();

            //传入调度的数据，在QuartzFactory中需要使用
            jobDetail.getJobDataMap().put("planJob", job);
            //调度作业
            scheduler.scheduleJob(jobDetail, trigger);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 操作定时任务
     *
     * @param jobOperateEnum
     * @param job
     */
    @Override
    public void operateJob(JobOperateEnum jobOperateEnum, PlanJob job) throws SchedulerException {
        JobKey jobKey = new JobKey(job.getJobName());
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        switch (jobOperateEnum) {
            case START:
                if (jobDetail != null) {
                    scheduler.resumeJob(jobKey);
                } else {
                    this.addJob(job);
                }
                break;
            case PAUSE:
                if (jobDetail != null) scheduler.pauseJob(jobKey);
                break;
            case DELETE:
                if (jobDetail != null) scheduler.deleteJob(jobKey);
                break;
            case UPDATE:
                scheduler.deleteJob(jobKey);
                if (job.getStatus()==1) this.addJob(job);
                break;
        }
    }

    /**
     * 启动所有任务
     */
    @Override
    public void startAllJob() throws SchedulerException {
        scheduler.start();
    }

    /**
     * 暂停所有任务
     */
    @Override
    public void pauseAllJob() throws SchedulerException {
        scheduler.standby();
    }
}
