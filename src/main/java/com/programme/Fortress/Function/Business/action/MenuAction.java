package com.programme.Fortress.Function.Business.action;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuAction {

    /**
     * 导航栏
     * @param model
     * @return
     */
    @GetMapping(value = "/Head")
    public String HomeHead(Model model){
        model.addAttribute("msg","spring mvc");
        return "menu/Head";
    }

    @GetMapping(value = "/Compass")
    public String PageTab(Model model){
        model.addAttribute("msg","spring mvc");
        return "menu/PageTab";
    }

    /**
     * 系统任务
     * @param model
     * @return
     */
    @GetMapping(value = "/PlanRule")
    public String PlanRule(Model model){
        model.addAttribute("msg","spring mvc");
        return "menu/PlanRule";
    }

    /**
     * 支付通账户权限
     * @param model
     * @return
     */
    @GetMapping(value = "/AcctPower")
    public String AccPower(Model model){
        model.addAttribute("msg","");
        return "menu/AcctPower";
    }

    /**
     * 账户余额
     * @param model
     * @return
     */
    @GetMapping(value = "/Balance")
    public String Balance(Model model){
        model.addAttribute("msg","");
        return "menu/Balance";
    }

    @GetMapping(value = "/SysMessage")
    public String SysMessage(Model model){
        model.addAttribute("msg","");
        return "menu/SysMessage";
    }

    @GetMapping(value = "/FormatCon")
    public String FormatCon(Model model){
        model.addAttribute("msg","");
        return "menu/FormatCon";
    }

    @GetMapping(value = "/PostMan")
    public String PostMan(Model model){
        model.addAttribute("msg","");
        return "menu/PostMan";
    }

    /**
     * 测试
     * @param model
     * @return
     */
    @GetMapping(value = "/test1")
    public String CompanyNum1(Model model){
        return "menu/test1";
    }

    @GetMapping(value = "/test")
    public String CompanyNum2(Model model){
        return "PageTab";
    }
}
