package com.programme.Fortress.Function.ToolCase.action;

import com.programme.Fortress.Function.ToolCase.service.TrafficMonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class TrafficMonitorAction {

    @Autowired
    private TrafficMonitorService monitorService;

    @GetMapping(value = "/TrafficMonitor")
    public String TrafficMonitor() {
        return "menu/TrafficMonitor";
    }

    @GetMapping(value = "/trafficMonitor/init")
    @ResponseBody
    public Object getInitTrafficMonitor(){
        return monitorService.initTrafficMonitor();
    }
}
