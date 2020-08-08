package com.programme.Fortress.Function.ToolCase.entity;

import lombok.Data;

@Data
public class ResultBean<T> {

    private int errCode;

    private String errMsg;

    private boolean status;

    private T data;
}
