package com.programme.Fortress.Function.ToolCase.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.programme.Fortress.Function.ToolCase.entity.ResultBean;
import com.programme.Fortress.Function.ToolCase.entity.SysMessage;

import java.util.List;
import java.util.Map;

public interface SysMessageService extends IService<SysMessage> {

    List<SysMessage> getSysMessage();

    ResultBean addSysMessage(SysMessage sysMessage);

    ResultBean upDateSysMessage(SysMessage sysMessage);

    ResultBean deleteSysMessage(long id);
}
