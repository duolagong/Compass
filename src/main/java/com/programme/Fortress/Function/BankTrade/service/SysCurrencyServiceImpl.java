package com.programme.Fortress.Function.BankTrade.service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.programme.Fortress.Function.BankTrade.entity.BankAcctId;
import com.programme.Fortress.Function.BankTrade.entity.SysCurrency;
import com.programme.Fortress.Function.Business.entity.ReturnData;
import com.programme.Fortress.Function.ToolCase.dao.SysMessageMapper;
import com.programme.Fortress.Function.ToolCase.entity.SysMessage;
import com.programme.Fortress.Other.Feign.ToATOMOSB;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SysCurrencyServiceImpl implements SysCurrencyService {

    @Autowired
    private SysMessageMapper sysMessageMapper;
    @Autowired
    private ToATOMOSB atomosb;

    @Override
    public Object getSysCurrency() {
        try {
            //明细查询模板
            SysMessage sysMessage = sysMessageMapper.selectOne(new QueryWrapper<SysMessage>()
                    .eq("type", "Currency")
                    .eq("message_type", "SQL"));
            String messageSql = sysMessage.getMessage();
            log.info("拼装成的系统币种查询SQL[{}]", messageSql);
            String retrunMsg = atomosb.getData(messageSql);
            log.info("执行系统币种查询返回[{}]" + retrunMsg);
            List<SysCurrency> currencyList = (List<SysCurrency>) JSONArray.parse(retrunMsg);
            return currencyList;
        } catch (Exception e) {
            log.error("获取系统币种失败",e);
            return null;
        }
    }
}
