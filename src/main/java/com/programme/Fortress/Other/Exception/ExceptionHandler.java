package com.programme.Fortress.Other.Exception;

import com.programme.Fortress.Function.ToolCase.entity.ResultBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 异常切面
 */
@Slf4j
@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultBean exceptionGet(Exception e){
        if(e instanceof DescribeException){
            DescribeException exception = (DescribeException) e;
            return ResultUtil.error(exception.getCode(),exception.getMessage());
        }
        /*log.error("【系统异常】{}",e.getMessage());*/
        log.error("【系统异常】{}",e);
        return ResultUtil.exception(ExceptionEnum.SERVICE_ERROR);
    }
}
