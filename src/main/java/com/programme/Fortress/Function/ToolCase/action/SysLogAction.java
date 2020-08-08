package com.programme.Fortress.Function.ToolCase.action;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class SysLogAction {

    @GetMapping(value = "/sysLog")
    @ResponseBody
    public String getSysLog(){
        String success = "<div class=\"media\"><div class=\"media-img-wrap\"><div class=\"avatar avatar-xs\">" +
                "<span class=\"avatar-text avatar-text-success rounded-circle\"><span class=\"initial-wrap\"><span>#errCode#</span></span>" +
                "</span></div></div><div class=\"media-body\"><div><span class=\"d-block mb-5\">" +
                "<span class=\"font-weight-500 text-dark text-capitalize\">#errCode#</span><span class=\"pl-5\">#errMsg#</span>" +
                "</span><span class=\"d-block font-13\">#Date#</span></div></div></div>";
        return success;
    }
}
