package com.programme.Fortress.Function.BankTrade.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.programme.Fortress.Function.BankTrade.entity.BankAcctId;
import com.programme.Fortress.Function.BankTrade.service.BankAcctIdServer;
import com.programme.Fortress.Function.Business.dao.BankIdMapper;
import com.programme.Fortress.Function.Business.entity.BankId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
public class BankAcctIdAcction {

    @Autowired
    private BankAcctIdServer bankAcctIdServer;
    @Autowired
    private BankIdMapper bankIdMapper;

    @GetMapping(value = "/BankAcctId")
    public String BankAcctId(Model model){
        List<BankId> bankIds = bankIdMapper.selectList(new QueryWrapper<BankId>().ne("id","Z01"));
        model.addAttribute("agentids",bankIds);
        return  "menu/BankAcctId";
    }

    @GetMapping(value = "/bankAcctid/{agentid}",produces ={"application/json"})
    @ResponseBody
    public Object getBankAcctId(@PathVariable("agentid") String agentid){
        return bankAcctIdServer.getBankAcctId(agentid);
    }

    @PostMapping(value = "/bankAcctId", produces ={"application/json"})
    @ResponseBody
    public Object getBankAcctId(@RequestBody BankAcctId bankAcctId){
        return bankAcctIdServer.getBankAcctId(bankAcctId);
    }

}
