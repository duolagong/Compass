package com.programme.Fortress.Function.ToolCase.action;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.programme.Fortress.Function.Business.dao.BankIdMapper;
import com.programme.Fortress.Function.Business.entity.BankId;
import com.programme.Fortress.Function.ToolCase.dao.SysMessageMapper;
import com.programme.Fortress.Function.ToolCase.entity.SysMessage;
import com.programme.Fortress.Function.ToolCase.service.FormatConService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class FormatConAction {

    @Autowired
    private FormatConService formatConService;
    @Autowired
    private BankIdMapper bankIdMapper;
    @Autowired
    private SysMessageMapper sysMessageMapper;

    @GetMapping(value = "/FormatCon")
    public String FormatCon(Model model) {
        List<BankId> bankIds = bankIdMapper.selectList(new QueryWrapper<BankId>().ne("id","Z01"));
        List<String> typeList = sysMessageMapper.getType("COMPASS", "XML");
        model.addAttribute("agentids",bankIds);
        model.addAttribute("types",typeList);
        return "menu/FormatCon";
    }

    @PostMapping(value = "/formatCon")
    @ResponseBody
    public String formatCon(@RequestBody Map FormatMap){
        String convert = formatConService.formatConvert(FormatMap);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Msg",convert);
        return jsonObject.toJSONString();
    }
}
