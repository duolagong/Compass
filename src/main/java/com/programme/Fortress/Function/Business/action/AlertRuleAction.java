package com.programme.Fortress.Function.Business.action;

import com.programme.Fortress.Function.Business.entity.AlertRule;
import com.programme.Fortress.Function.Business.service.AlertRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AlertRuleAction {

    @Autowired
    private AlertRuleService alertRuleService;

    @GetMapping(value = "/AlertRule")
    public String AlertRule(Model mode){
        mode.addAttribute("msg","");
        return "menu/AlertRule";
    }

    @GetMapping(value = "/alertRule")
    @ResponseBody
    public String getAlertConfig(){
        return alertRuleService.getAlertRule();
    }

    @PostMapping(value = "/alertRule")
    @ResponseBody
    public Object addAlertRule(@RequestBody AlertRule alertRule){
        return alertRuleService.addAlertRule(alertRule);
    }

    @PutMapping(value = "/alertRule")
    @ResponseBody
    public Object upDateAlertRule(@RequestBody AlertRule alertRule){
        return alertRuleService.upDateAlertRule(alertRule);
    }

    @DeleteMapping(value = "/alertRule/{id}",produces ={"application/json"} )
    @ResponseBody
    public Object deleteAlertRule(@PathVariable("id")String id){
        return alertRuleService.deleteAlertRule(id);
    }

    @PutMapping(value = "/alertRule/state/{id}",produces = {"application/json"})
    @ResponseBody
    public Object startAlertRule(@PathVariable("id")String id){
        return alertRuleService.startAlertRule(id);
    }

    @PutMapping(value = "/alertRule/pause/{id}",produces = {"application/json"})
    @ResponseBody
    public Object pauseAlertRule(@PathVariable("id")String id){
        return alertRuleService.pauseAlertRule(id);
    }
}
