package com.programme.Fortress.Function.Business.action;

import com.programme.Fortress.Function.Business.service.EcosphereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EcosphereAction {

    @Autowired
    private EcosphereService ecosphereService;

    /**
     * 生态圈
     * @param model
     * @return
     */
    @GetMapping(value = "/Ecosphere")
    public String Ecosphere(Model model){
        model.addAttribute("msg","");
        return "menu/Ecosphere";
    }

    @GetMapping(value = "/ecosphere")
    @ResponseBody
    public String getEcosphere(){
        String ecosphere = ecosphereService.getEcosphere();
        return ecosphere;
    }
}
