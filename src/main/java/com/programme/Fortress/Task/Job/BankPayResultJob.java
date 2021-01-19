package com.programme.Fortress.Task.Job;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.programme.Fortress.Function.BankTrade.service.BankPaymentService;
import com.programme.Fortress.Function.Business.dao.PayNoteMapper;
import com.programme.Fortress.Function.Business.entity.PayNote;
import com.programme.Fortress.Function.Business.service.PayNoteService;
import com.programme.Fortress.Function.ToolCase.dao.SysMessageMapper;
import com.programme.Fortress.Function.ToolCase.entity.SysMessage;
import com.programme.Fortress.Other.Feign.ToATOMOSB;
import com.programme.Fortress.Util.MyDateUtil;
import com.programme.Fortress.Util.Redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("BankPayResultJob")
public class BankPayResultJob {

    @Autowired
    private BankPaymentService bankPaymentService;
    @Autowired
    private SysMessageMapper sysMessageMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ToATOMOSB atomosb;

    public void execute(){
        List<Map<String,Object>> acctBalanceList=null;
        try {
            //查询明细模板
            SysMessage sysMessage = sysMessageMapper.selectOne(new QueryWrapper<SysMessage>()
                    .eq("type", "BankResult")
                    .eq("message_type", "SQL"));
            String retrunMsg = atomosb.getData(sysMessage.getMessage());
            acctBalanceList = (List<Map<String,Object>>) JSONArray.parse(retrunMsg);
        }catch (Exception e){
            log.error("向核心OSB查询银行结果通知异常",e);
            return;
        }
        log.info("开始执行银行结果通知查询操作");
        if (acctBalanceList.size() == 0) {
            log.info("未查询到银行结果通知资源！");
            return;
        } else {
            for (Map map : acctBalanceList) {
                try {
                    String queryXml = bankPaymentService.getResultQueryXml(map.get("version").toString(),"1031_000", map.get("agentId").toString(), map.get("ordernum").toString(), map.get("subbranchid").toString());
                    log.info("准备发送1031结果通知查询[{}]",queryXml);
                    bankPaymentService.sendOSBNotice(queryXml);
                } catch (Exception e) {
                    log.error("向核心OSB发送银行结果通知查询异常",e);
                }
            }
        }
    }
}
