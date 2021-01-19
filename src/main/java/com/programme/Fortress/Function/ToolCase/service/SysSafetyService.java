package com.programme.Fortress.Function.ToolCase.service;

import com.programme.Fortress.Function.ToolCase.entity.ResultBean;
import com.programme.Fortress.Function.ToolCase.entity.SysUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SysSafetyService {

    public ResultBean loginSystem(String userid, String password, HttpServletResponse servletResponse);

    public SysUser getCookieUserInfo(String cookie);

    public ResultBean loginoutSystem(String cookie);
}
