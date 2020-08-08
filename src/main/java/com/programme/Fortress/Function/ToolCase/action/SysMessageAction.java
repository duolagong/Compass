package com.programme.Fortress.Function.ToolCase.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.programme.Fortress.Function.ToolCase.entity.SysMessage;
import com.programme.Fortress.Function.ToolCase.service.SysMessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sysMessage")
public class SysMessageAction {

    @Autowired
    private SysMessageServiceImpl sysMessageService;

    @GetMapping
    public String getSysMessage() {
        List<SysMessage> planJobs = sysMessageService.getSysMessage();
        return JSON.toJSONString(planJobs);
    }

    @PostMapping
    public Object addSysMessage(@RequestBody SysMessage sysMessage){
        return sysMessageService.addSysMessage(sysMessage);
    }

    @PutMapping
    public Object updateSysMessage(@RequestBody SysMessage sysMessage){
        return sysMessageService.upDateSysMessage(sysMessage);
    }

    @DeleteMapping(value = "/{id}" ,produces = {"application/json"})
    public Object deleteSysMessage(@PathVariable("id") long id){
        return sysMessageService.deleteSysMessage(id);
    }
}
