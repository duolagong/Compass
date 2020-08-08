package com.programme.Fortress.Function.Business.action;

import com.programme.Fortress.Function.Business.service.NoticeSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Controller
public class NoticeSendAction {

    @Autowired
    private NoticeSendService noticeSendService;

    @GetMapping(value = "/NoticeSend")
    public String NoticeSend(Model model){
        model.addAttribute("msg","");
        return "menu/NoticeSend";
    }

    @PostMapping(value = "/noticeSend")
    @ResponseBody
    public Object getNoticeSend(@RequestBody HashMap hashMap){
        return noticeSendService.getNoticeSend(hashMap);
    }

    @PutMapping(value = "/noticeSend/{ordernum}")
    @ResponseBody
    public Object noteSend1031(@PathVariable("ordernum")String ordernum){
        return noticeSendService.noteSend1031(ordernum);
    }
}
