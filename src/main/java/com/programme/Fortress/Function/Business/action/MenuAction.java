package com.programme.Fortress.Function.Business.action;

import com.programme.Fortress.Function.ToolCase.entity.SysUser;
import com.programme.Fortress.Function.ToolCase.service.SysSafetyService;
import jdk.nashorn.internal.ir.ReturnNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
public class MenuAction {

    @Autowired
    private SysSafetyService sysSafetyService;

    /**
     * 导航栏
     *
     * @param model
     * @return
     */
    @GetMapping(value = "/Head")
    public String HomeHead(Model model) {
        model.addAttribute("userInfo", "success");
        return "menu/Head";
    }

    @GetMapping(value = "/Compass")
    public String PageTab(Model model, @CookieValue(value = "COMPASS_TOKEN", required = true) String cookie) {
        SysUser sysUser = sysSafetyService.getCookieUserInfo(cookie);
        log.info("用户信息[{}]", sysUser);
        model.addAttribute("userInfo", sysUser);
        return "menu/PageTab";
    }

    /**
     * 系统任务
     *
     * @param model
     * @return
     */
    @GetMapping(value = "/PlanRule")
    public String PlanRule(Model model) {
        model.addAttribute("msg", "spring mvc");
        return "menu/PlanRule";
    }

    /**
     * 支付通账户权限
     *
     * @param model
     * @return
     */
    @GetMapping(value = "/AcctPower")
    public String AccPower(Model model) {
        model.addAttribute("msg", "");
        return "menu/AcctPower";
    }

    /**
     * 账户余额
     *
     * @param model
     * @return
     */
    @GetMapping(value = "/Balance")
    public String Balance(Model model) {
        model.addAttribute("msg", "");
        return "menu/Balance";
    }

    @GetMapping(value = "/SysMessage")
    public String SysMessage(Model model) {
        model.addAttribute("msg", "");
        return "menu/SysMessage";
    }

    @GetMapping(value = "/PostMan")
    public String PostMan(Model model) {
        model.addAttribute("msg", "");
        return "menu/PostMan";
    }

    /**
     * 测试
     *
     * @param model
     * @return
     */
    @GetMapping(value = "/test1")
    public String CompanyNum1(Model model) {
        return "menu/test1";
    }

    @GetMapping(value = "/test")
    public String CompanyNum2(Model model) {
        return "PageTab";
    }
}
