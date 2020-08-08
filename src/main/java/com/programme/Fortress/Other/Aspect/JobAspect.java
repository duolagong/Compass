package com.programme.Fortress.Other.Aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Component
@Aspect
public class JobAspect {

    private final String pointcut = "execution(public * com.programme.Fortress.Task.Job..*.*(..))";

    @Pointcut(pointcut)
    public void executeService(){}

    //@Before("executeService()")
    public void doBeforeAdvice(JoinPoint joinPoint){
        String taskName = getTaskName(joinPoint);
        log.info("-------------"+taskName+"开始执行--------------");
    }

    @Around("executeService()")
    public Object doAroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable{
        Object result = null;
        doBeforeAdvice(joinPoint);
        try {
            joinPoint.proceed();
            doAfter(joinPoint);
        }catch (Throwable e){
            log.error("系统自动任务发生错误",e);
        }finally {
            //记录日志？？？？
        }
        return result;
    }

    //@After("executeService()")
    public void doAfter(JoinPoint joinPoint){
        String taskName = getTaskName(joinPoint);
        //这里加存入系统日志库的日志
        log.info("-------------"+taskName+"执行完成--------------");
    }

    /**
     * 获取定时任务类加名字
     * @param joinPoint
     * @return
     */
    private String getTaskName(JoinPoint joinPoint){
        return joinPoint.getTarget().getClass().getSimpleName()+"."+joinPoint.getSignature().getName();
    }

}
