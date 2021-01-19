package com.programme.Fortress.Task.quartz;

import com.programme.Fortress.Util.SpringContextUtil;
import com.programme.Fortress.Function.ToolCase.entity.PlanJob;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.lang.reflect.Method;

public class QuartzFactory implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //获取调度数据
        PlanJob planJob = (PlanJob) jobExecutionContext.getMergedJobDataMap().get("planJob");
        //获取对应的Bean
        Object object = SpringContextUtil.getBean(planJob.getBeanName());
        try {
            //利用反射执行对应方法
            Method method = object.getClass().getMethod(planJob.getMethodName());
            method.invoke(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
