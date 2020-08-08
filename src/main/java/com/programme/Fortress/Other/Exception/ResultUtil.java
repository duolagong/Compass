package com.programme.Fortress.Other.Exception;

import com.alibaba.fastjson.JSONObject;
import com.programme.Fortress.Function.ToolCase.entity.ResultBean;
import com.programme.Fortress.Other.Exception.ExceptionEnum;
import org.springframework.stereotype.Service;

public class ResultUtil{

    public static ResultBean error(int code, String msg) {
        ResultBean result = new ResultBean();
        result.setErrCode(code);
        result.setStatus(false);
        result.setErrMsg(msg);
        return result;
    }

    /**
     * 自定义异常返回
     *
     * @param exceptionEnum
     * @return
     */
    public static ResultBean exception(ExceptionEnum exceptionEnum) {
        ResultBean result = new ResultBean();
        result.setErrCode(exceptionEnum.getCode());
        result.setStatus(false);
        result.setErrMsg(exceptionEnum.getMsg());
        return result;
    }

    /**
     * 业务成功返回
     *
     * @param data
     * @return
     */
    public static ResultBean success(Object data) {
        ResultBean result = success(data, "处理成功");
        result.setErrMsg("ok");
        return result;
    }

    public static ResultBean success(Object data, String msg) {
        ResultBean result = new ResultBean();
        result.setErrCode(1000);
        result.setStatus(true);
        result.setErrMsg(msg);
        result.setData(data);
        return result;
    }

    public static ResultBean fail(String msg) {
        ResultBean result = new ResultBean();
        result.setErrCode(1000);
        result.setStatus(false);
        result.setErrMsg(msg);
        return result;
    }
}
