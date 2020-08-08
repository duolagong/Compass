package com.programme.Fortress.Other.Aspect;

import com.programme.Fortress.Other.Exception.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Action切面
 */
@Slf4j
@Component
@Aspect
public class FunctionAspect {

    @Autowired
    private ExceptionHandler exceptionHandler;

    private final String pointcut="execution(public * com.programme.Fortress.Function.*.action..*(..))";

    @Pointcut(pointcut)
    public void executeService(){};

    @Before("executeService()")
    public void doBeforeAdvice(JoinPoint joinPoint){
        log.info("执行[{}]类的[{}]方法，请求参数[{}]", joinPoint.getTarget().getClass().getSimpleName(),joinPoint.getSignature().getName(),joinPoint.getArgs());
    }

    @AfterReturning(value = "executeService()", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint,Object result){
        log.info("执行[{}]类的[{}]方法，请求返回[{}]",joinPoint.getTarget().getClass().getSimpleName(),joinPoint.getSignature().getName(),result);
    }

    /*@Around("executeService()")*/
    public Object doAroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable{
        //返回的结果
        Object result = null;
        Object[] args = joinPoint.getArgs();
        log.info("方法请求的参数[{}]",args);
        try {
            joinPoint.proceed();
            result = joinPoint.proceed();
            log.info("方法返回的参数[{}]",result);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            result = exceptionHandler.exceptionGet(e);
        }finally {
            //记录日志？？？？
        }
        return result;
    }
}
