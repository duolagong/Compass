package com.programme.Fortress.Other.Exception;

public enum ExceptionEnum {
    SIGN_ERROR(1001, "签名异常"),
    PARAM_ERROR(1002, "参数异常"),
    DATA_NOT_FUND(1003, "数据不存在"),
    LOGIN_ERROR(1004, "登录异常"),
    PERMISSION_ERROR(1005, "权限异常"),
    SERVICE_ERROR(1006, "服务异常"),
    OPERATE_ERROR(1007, "异常操作"),
    REQUEST_ERROR(1008, "异常请求"),
    UNKNOW_ERROR(1009, "未知错误"),
    MAKE_ERROR(1010, "组装报文异常"),;

    private Integer code;

    private String msg;

    ExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
