package com.programme.Fortress.Function.ToolCase.action;

import com.programme.Fortress.Function.ToolCase.entity.ResultBean;
import com.programme.Fortress.Function.ToolCase.service.SysMemoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class SysMemoryAction {

    @Autowired
    private SysMemoryService sysMemoryService;

    @GetMapping(value = "/SysMemory")
    public String SysMemory(){
        return "menu/SysMemory";
    }

    @GetMapping(value = "/sysMemory/init")
    @ResponseBody
    public Object getInitTotalMemory(){
        return sysMemoryService.getSysMemoryInit();
    }

    @GetMapping(value = "/sysMemory/freeMemory/{timestamp}")
    @ResponseBody
    public Object getFreelMemory(@PathVariable("timestamp")Long timestamp){
        return sysMemoryService.getFreeMemory(timestamp);
    }

    @GetMapping(value = "/sysMemory/useMemory/{timestamp}")
    @ResponseBody
    public Object getUseMemory(@PathVariable("timestamp")Long timestamp){
        return sysMemoryService.getUseMemory(timestamp);
    }

    @GetMapping(value = "/sysMemory/utilization/{timestamp}")
    @ResponseBody
    public Object getMemoryUtilization(@PathVariable(value = "timestamp")Long timestamp){
        return sysMemoryService.getMemoryUtilization(timestamp);
    }

}
