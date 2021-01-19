package com.programme.Fortress.Function.ToolCase.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.programme.Fortress.Function.ToolCase.entity.SysMessage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysMessageMapper extends BaseMapper<SysMessage> {

    Long getDual();

    List<String> getType(@Param(value = "typeSource")String typeSource,@Param(value = "messageType")String messageType);
}
