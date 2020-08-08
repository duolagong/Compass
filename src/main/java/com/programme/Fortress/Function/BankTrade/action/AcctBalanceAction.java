package com.programme.Fortress.Function.BankTrade.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.programme.Fortress.Function.BankTrade.entity.AcctBalance;
import com.programme.Fortress.Function.BankTrade.service.AcctBalanceService;
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
public class AcctBalanceAction {

    @Autowired
    private AcctBalanceService acctBalanceService;
    @Autowired
    private BankIdMapper bankIdMapper;

    @GetMapping(value = "/AcctBalance")
    public String AcctBalance(Model model){
        List<BankId> bankIds = bankIdMapper.selectList(new QueryWrapper<BankId>().ne("id","Z01"));
        model.addAttribute("agentids",bankIds);
        return  "menu/AcctBalance";
    }

    @GetMapping(value = "/acctBalance/message/{agentid}/{acctid}/{curcode}/{txdate}")
    @ResponseBody
    public Object getAcctBalanceMessage(@PathVariable("agentid")String agentid,@PathVariable("acctid")String acctid,
            @PathVariable("curcode")String curcode,@PathVariable("txdate")String txdate){
        return acctBalanceService.getAcctBalance(agentid,acctid,curcode,txdate.replaceAll("\\-",""));
    }

    @PostMapping(value = "/acctBalance")
    @ResponseBody
    public Object getAcctBalance(@RequestBody AcctBalance acctBalance){
        return acctBalanceService.getAcctBalance(acctBalance);
    }
}
