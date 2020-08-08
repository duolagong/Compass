package com.programme.Fortress.Function.Business.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.programme.Fortress.Function.Business.dao.AcctPowerMapper;
import com.programme.Fortress.Function.Business.entity.AcctPower;
import com.programme.Fortress.Function.ToolCase.entity.ResultBean;
import com.programme.Fortress.Other.Exception.ResultUtil;
import com.programme.Fortress.Other.Exception.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/acctPower")
public class AcctPowerAction {
    @Autowired
    private AcctPowerMapper acctPowerMapper;

    @GetMapping
    public String getAcctPower() {
        try {
            List<AcctPower> acctPowers = acctPowerMapper.selectList(null);
            return JSON.toJSONString(acctPowers);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @PutMapping
    public ResultBean updateAcctPower(@RequestBody Map map) {
        try {
            AcctPower acctPower = new AcctPower();
            acctPower.setVersion(map.get("version").toString());
            acctPower.setAgentid(map.get("agentid").toString());
            acctPower.setAcctid(map.get("acctid").toString());
            acctPower.setQuerypower(map.get("querypower").toString());
            acctPower.setPaypower(map.get("paypower").toString());
            acctPower.setBillpower(map.get("billpower").toString());
            acctPowerMapper.updateById(acctPower);
            return ResultUtil.success("","修改成功");
        } catch (Exception e) {
            log.error("修改账户权限发生异常",e);
            return ResultUtil.fail(e.getMessage());
        }
    }


}
