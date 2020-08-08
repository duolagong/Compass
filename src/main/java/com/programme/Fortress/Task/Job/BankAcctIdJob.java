package com.programme.Fortress.Task.Job;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.programme.Fortress.Function.BankTrade.entity.BankAcctId;
import com.programme.Fortress.Function.Business.dao.BankIdMapper;
import com.programme.Fortress.Function.Business.entity.BankId;
import com.programme.Fortress.Function.ToolCase.dao.SysMessageMapper;
import com.programme.Fortress.Function.ToolCase.entity.SysMessage;
import com.programme.Fortress.Other.Feign.ToATOMOSB;
import com.programme.Fortress.Util.Postman.HttpUtil;
import com.programme.Fortress.Util.Redis.RedisUtil;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component("BankAcctIdJob")
public class BankAcctIdJob {

    @Autowired
    private SysMessageMapper sysMessageMapper;
    @Autowired
    private BankIdMapper bankIdMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ToATOMOSB atomosb;

    public void execute() throws Exception {
        log.info("开始进行银行账户同步任务！");
        //统计条数模板
        SysMessage messageCount = sysMessageMapper.selectOne(new QueryWrapper<SysMessage>()
                .eq("type", "NUM")
                .eq("message_type", "SQL"));
        //查询明细模板
        SysMessage sysMessage = sysMessageMapper.selectOne(new QueryWrapper<SysMessage>()
                .eq("type", "BankAcctId")
                .eq("message_type", "SQL"));
        //分页查询模板
        SysMessage sysMessageRow = sysMessageMapper.selectOne(new QueryWrapper<SysMessage>()
                .eq("type", "ROWNUM")
                .eq("message_type", "SQL"));
        //一页一百条
        String selectSql = sysMessage.getMessage();
        log.info("银行账户信息查询SQL[{}]",selectSql);
        String selectNumSql = messageCount.getMessage()
                .replaceAll("#ID#", "*")
                .replaceAll("#SQL#", selectSql);
        log.info("拼装完成银行账户信息统计SQL[{}]",selectNumSql);
        String retrunCount = atomosb.getData(selectNumSql);
        log.info("执行银行账户信息统计返回[{}]",retrunCount);
        if(Integer.parseInt(retrunCount)==0) return;
        for (int i = 0; i < Integer.parseInt(retrunCount) % 100; i++) {
            String selectRowSql=sysMessageRow.getMessage().replaceAll("#SQL#", selectSql+" order by inputdate desc")
                    .replaceAll("#ROW#", Integer.toString(i * 100 + 1))
                    .replaceAll("#ROWNEXT#", Integer.toString(i * 100 + 100));
            log.info("拼装完成银行账户信息SQL[{}]"+selectRowSql);
            String retrunMsg = atomosb.getData(selectNumSql);
            log.info("执行银行账户信息返回[{}]"+retrunMsg);
            List<BankAcctId> bankAcctList = (List<BankAcctId>) JSONArray.parse(retrunMsg);
            for (BankAcctId bankAcctId : bankAcctList) {
                if ("1".equals(bankAcctId.getL_balance())) {
                    redisUtil.hset("ATOM:"+bankAcctId.getAgentid()+":B", bankAcctId.getAcctid(), bankAcctId);
                }
                if ("1".equals(bankAcctId.getDetailed())) {
                    redisUtil.hset("ATOM:"+bankAcctId.getAgentid()+":D", bankAcctId.getAcctid(), bankAcctId);
                }
                if ("1".equals(bankAcctId.getL_detailed())) {
                    redisUtil.hset("ATOM:"+bankAcctId.getAgentid()+":LD", bankAcctId.getAcctid(), bankAcctId);
                }
                if ("1".equals(bankAcctId.getStatement())) {
                    redisUtil.hset("ATOM:"+bankAcctId.getAgentid()+":S", bankAcctId.getAcctid(), bankAcctId);
                }
            }
        }
    }
}
