package com.programme.Fortress.Function.ToolCase.action;

import com.alibaba.fastjson.JSONObject;
import com.programme.Fortress.Function.ToolCase.service.FormatConService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/formatCon")
public class FormatConAction {

    @Autowired
    private FormatConService formatConService;

    @PostMapping
    public String formatCon(@RequestBody Map FormatMap){
        String convert = formatConService.formatConvert(FormatMap);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Msg",convert);
        return jsonObject.toJSONString();
    }
}
