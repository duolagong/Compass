package com.programme.Fortress.Function.Business.action;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping(value = "/FlowMap")
public class FlowMapAction {

    @GetMapping
    public String FlowMap(Model model){
        model.addAttribute("msg","");
        return "menu/FlowMap";
    }
}
