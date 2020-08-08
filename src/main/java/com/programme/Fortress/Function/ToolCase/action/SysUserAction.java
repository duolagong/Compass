package com.programme.Fortress.Function.ToolCase.action;

import com.programme.Fortress.Function.ToolCase.entity.SysUser;
import com.programme.Fortress.Function.ToolCase.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@Controller
public class SysUserAction {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping(value = "/SysUser")
    public String User(Model mode){
        mode.addAttribute("msg","");
        return "menu/SysUser";
    }

    @GetMapping(value = "/SysUser/login")
    public String loginSystem(@RequestParam("userid") String userid,@RequestParam("password") String password, Model mode, HttpSession httpSession){
        if(!StringUtils.isEmpty(userid) && !StringUtils.isEmpty(password)){
            if(sysUserService.loginSystem(userid, password)){
                httpSession.setAttribute("loginSign",userid);
                httpSession.setMaxInactiveInterval(6*60*60);
                //登录成功，防止表单重复提交，重定向
                return "redirect:/Compass";
            }else {
                mode.addAttribute("msg","账户或密码错误！");
                return "redirect:/";
            }
        }else {
            mode.addAttribute("msg","登录失败！");
            return "redirect:/";
        }
    }

    @GetMapping(value = "/sysUser")
    @ResponseBody
    public String getUser(){
        return sysUserService.getUser();
    }

    @PutMapping(value = "/sysUser")
    @ResponseBody
    public Object upDateUser(@RequestBody SysUser sysUser){
        return sysUserService.upDateUser(sysUser);
    }

    @DeleteMapping(value = "/sysUser/{id}",produces = {"application/json"})
    @ResponseBody
    public Object deleteUser(@PathVariable("id")String id){
        return sysUserService.deleteUser(id);
    }

    @PostMapping(value = "/sysUser")
    @ResponseBody
    public Object addUser(@RequestBody SysUser sysUser){
        return sysUserService.addUser(sysUser);
    }
}
