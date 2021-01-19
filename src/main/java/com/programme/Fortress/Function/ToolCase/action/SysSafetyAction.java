package com.programme.Fortress.Function.ToolCase.action;

import com.programme.Fortress.Function.ToolCase.entity.ResultBean;
import com.programme.Fortress.Function.ToolCase.service.SysSafetyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
public class SysSafetyAction {

    @Autowired
    private SysSafetyService sysSafetyService;

    /**
     * 登录系统
     * @param userid    用户名
     * @param password  密码
     * @param model
     * @param servletResponse
     * @return
     */
    @GetMapping(value = "/sysSafety/login")
    public String loginSystem(@RequestParam("userid") String userid, @RequestParam("password") String password,
                              RedirectAttributesModelMap model, HttpServletResponse servletResponse) {
        ResultBean resultBean = sysSafetyService.loginSystem(userid, password, servletResponse);
        if (resultBean.isStatus()) {
            return "redirect:/Compass";
        } else {
            model.addFlashAttribute("indexRemind", resultBean.getErrMsg());
            return "redirect:/";
        }
    }

    /**
     * 登出系统
     * @param model
     * @param cookie
     * @return
     */
    @GetMapping(value = "/sysSafety/loginout")
    public String loginoutSystem(Model model, @CookieValue(value = "COMPASS_TOKEN", required = true) String cookie) {
        ResultBean resultBean = sysSafetyService.loginoutSystem(cookie);
        model.addAttribute("indexRemind", resultBean.getErrMsg());
        return "redirect:/";
    }


}
