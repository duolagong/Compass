package com.programme.Fortress.Function.BankTrade.action;

import com.alibaba.fastjson.JSON;
import com.programme.Fortress.Function.BankTrade.service.SysCurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SysCurrencyAction {

    @Autowired
    private SysCurrencyService sysCurrencyService;

    @GetMapping(value = "/SysCurrency")
    public String SysCurrency(Model mode){
        mode.addAttribute("msg","");
        return "menu/SysCurrency";
    }

    @GetMapping(value = "/sysCurrency")
    @ResponseBody
    public String getSysCurrency(){
        return JSON.toJSONString(sysCurrencyService.getSysCurrency());
    }
}
