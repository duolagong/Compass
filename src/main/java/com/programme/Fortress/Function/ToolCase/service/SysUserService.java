package com.programme.Fortress.Function.ToolCase.service;

import com.programme.Fortress.Function.ToolCase.entity.ResultBean;
import com.programme.Fortress.Function.ToolCase.entity.SysUser;

import java.util.Map;

public interface SysUserService {
    String getUser(String cookie);

    ResultBean addUser(SysUser sysUser);

    ResultBean deleteUser(String userId);

    ResultBean upDateUser(SysUser sysUser);

    Map alertPhoneInfor(String type);
}
