package com.programme.Fortress.Function.ToolCase.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.programme.Fortress.Function.ToolCase.entity.SysUser;

import java.beans.beancontext.BeanContext;

public interface SysUserMapper extends BaseMapper<SysUser> {
    Long getDual();
}
