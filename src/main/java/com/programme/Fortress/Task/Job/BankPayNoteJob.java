package com.programme.Fortress.Task.Job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.programme.Fortress.Function.BankTrade.entity.BankPayment;
import com.programme.Fortress.Function.Business.dao.PayNoteMapper;
import com.programme.Fortress.Function.Business.entity.PayNote;
import com.programme.Fortress.Function.Business.service.PayNoteService;
import com.programme.Fortress.Function.ToolCase.dao.SysMessageMapper;
import com.programme.Fortress.Function.ToolCase.entity.SysMessage;
import com.programme.Fortress.Other.Feign.ToATOMOSB;
import com.programme.Fortress.Util.MyDateUtil;
import com.programme.Fortress.Util.Redis.RedisUtil;
import com.programme.Fortress.Util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Clob;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("BankPayNoteJob")
public class BankPayNoteJob {

    @Autowired
    private PayNoteService payNoteService;
    @Autowired
    private PayNoteMapper payNoteMapper;
    @Autowired
    private SysMessageMapper sysMessageMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ToATOMOSB atomosb;

    public void execute(){
        //统计条数模板
        SysMessage messageCount = sysMessageMapper.selectOne(new QueryWrapper<SysMessage>()
                .eq("type", "NUM")
                .eq("message_type", "SQL"));
        //查询明细模板
        SysMessage sysMessage = sysMessageMapper.selectOne(new QueryWrapper<SysMessage>()
                .eq("type", "BankPaymentXml")
                .eq("message_type", "SQL"));
        //分页查询模板
        SysMessage sysMessageRow = sysMessageMapper.selectOne(new QueryWrapper<SysMessage>()
                .eq("type", "ROWNUM")
                .eq("message_type", "SQL"));
        //拼装查询时间
        String selectSql = sysMessage.getMessage()
                .replaceAll("#DATAFROM#", LocalDateTime.now().plus(-15, ChronoUnit.MINUTES).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .replaceAll("#DATATO#", MyDateUtil.getMaxDate("yyyy-MM-dd HH:mm:ss"));
        log.info("[{}]拼装银行账户付款报文查询SQL[{}]","开始",selectSql);
        String selectNumSql = messageCount.getMessage()
                .replaceAll("#ID#", "*")
                .replaceAll("#SQL#", selectSql);
        log.info("[{}]拼装银行账户付款报文统计SQL[{}]","完成",selectNumSql);
        try {
            String retrunCount = atomosb.getData(selectNumSql);
            log.info("执行银行账户付款报文统计返回[{}]",retrunCount);
            List<Map<String,Object>> paymentCountList = (List<Map<String,Object>>) JSONArray.parse(retrunCount);
            String totalCount = paymentCountList.get(0).get("NUM").toString();//总条数
            if("0".equals(totalCount)){
                log.info("当前时间段未查询到交易！");
            }else {
                int forCount=Integer.parseInt(totalCount) % 10;//10笔1页
                for (int i = 0; i < forCount; i++) {
                    try {
                        String selectRowSql=sysMessageRow.getMessage().replaceAll("#SQL#", selectSql)
                                .replaceAll("#ROW#", Integer.toString(i * 10 + 1))
                                .replaceAll("#ROWNEXT#",Integer.toString(i * 10 +10));
                        log.info("拼装完成银行账户付款报文明细SQL[{}]",selectRowSql);
                        String retrunMsg = atomosb.getData(selectRowSql);
                        log.info("执行完成银行账户付款报文明细返回[{}]",retrunMsg);
                        List<Map<String,Object>> acctBalanceList = (List<Map<String,Object>>) JSONArray.parse(retrunMsg);
                        log.info("开始执行银行账户付款报文入库");
                        for (int y = 0; y < acctBalanceList.size(); y++) {
                            Integer count = payNoteMapper.selectCount(new QueryWrapper<PayNote>().eq("ordernum", acctBalanceList.get(0).get("ordernum").toString()));
                            if (count>0) continue;
                            payNoteService.insertPayNote(payNoteService.getOrdernumPayNote(acctBalanceList.get(0).get("tranxml").toString(),
                                    acctBalanceList.get(0).get("ordernum").toString()));
                        }
                        log.info("执行银行账户付款报文入库完成");
                    }catch (Exception e){
                        log.error("银行账户付款报文入库异常",e);
                    }
                }
            }
        }catch (Exception e){
            log.error("向核心OSB查询银行账户付款报文异常",e);
            return;
        }
    }
}
