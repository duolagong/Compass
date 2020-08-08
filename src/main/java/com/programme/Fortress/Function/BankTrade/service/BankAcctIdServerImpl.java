package com.programme.Fortress.Function.BankTrade.service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.programme.Fortress.Function.BankTrade.entity.BankAcctId;
import com.programme.Fortress.Function.BankTrade.entity.MxContent;
import com.programme.Fortress.Function.Business.entity.ReturnData;
import com.programme.Fortress.Function.ToolCase.dao.SysMessageMapper;
import com.programme.Fortress.Function.ToolCase.entity.SysMessage;
import com.programme.Fortress.Other.Exception.ResultUtil;
import com.programme.Fortress.Other.Feign.ToATOMOSB;
import com.programme.Fortress.Util.Postman.HttpUtil;
import com.programme.Fortress.Util.Redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class BankAcctIdServerImpl implements BankAcctIdServer {

    @Autowired
    private SysMessageMapper sysMessageMapper;
    @Autowired
    private ToATOMOSB atomosb;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<Object> getBankAcctId(String agentId) {
        List<Object> acctList=null;
        try {
            acctList = redisUtil.lGet("AOSB:" + agentId + ":ACC", 0, -1);
            if (acctList== null || acctList.size()==0) {
                SysMessage sysMessage = sysMessageMapper.selectOne(new QueryWrapper<SysMessage>()
                        .eq("type", "SelectBankAcctId")
                        .eq("message_type", "SQL"));
                String messageSql = sysMessage.getMessage()
                        .replaceAll("#AGENTID#", agentId);
                log.info("拼装成的银行账户信息查询SQL[{}]",messageSql);
                String retrunMsg = atomosb.getData(messageSql);
//                String retrunMsg = HttpUtil.httpXml(redisUtil.hget("COM:CIRCLE", "atomosb") + "/HTKG/SuperOSBEntry", messageSql, 30);
                log.info("执行银行账户信息返回[{}]"+retrunMsg);
                acctList = (List<Object>) JSONArray.parse(retrunMsg);
                //查出数据放入缓存
                cacheBankAcctId(acctList,agentId);
            }
            return acctList;
        } catch (Exception e) {
            log.error("获取银行账户信息失败",e);
            acctList.add("获取账户异常,请重试");
            return acctList;
        }
    }

    @Override
    public Object getBankAcctId(BankAcctId bankAcctId) {
        ReturnData<BankAcctId> returnData=new ReturnData<BankAcctId>();
        try {
            //统计条数模板
            SysMessage messageCount = sysMessageMapper.selectOne(new QueryWrapper<SysMessage>()
                    .eq("type", "NUM")
                    .eq("message_type", "SQL"));
            //明细查询模板
            SysMessage sysMessage = sysMessageMapper.selectOne(new QueryWrapper<SysMessage>()
                    .eq("type", "BankAcctId")
                    .eq("message_type", "SQL"));
            //分页查询模板
            SysMessage sysMessageRow = sysMessageMapper.selectOne(new QueryWrapper<SysMessage>()
                    .eq("type", "ROWNUM")
                    .eq("message_type", "SQL"));
            String messageSql = sysMessage.getMessage();
            if(!StringUtils.isEmpty(bankAcctId.getAgentid())){
                messageSql=messageSql + " and f_yhbh like'%"+bankAcctId.getAgentid()+"'";
            }
            if(!StringUtils.isEmpty(bankAcctId.getAcctid())){
                messageSql=messageSql + " and f_zhbh ='"+bankAcctId.getAcctid()+"'";
            }
            log.info("拼装成的银行账户信息查询SQL[{}]",messageSql);
            String selectNumSql = messageCount.getMessage()
                    .replaceAll("#ID#", "*")
                    .replaceAll("#SQL#", messageSql);
            log.info("拼装完成银行账户信息查询统计SQL[{}]",selectNumSql);
            String retrunCount = atomosb.getData(selectNumSql);
            log.info("返回银行账户信息查询统计返回[{}]",retrunCount);
            List<Map<String,Object>> paymentCountList = (List<Map<String,Object>>) JSONArray.parse(retrunCount);
            //校验是否存在银行账户明细
            if(paymentCountList.get(0).get("NUM").toString().equals("0")) {
                returnData.setTotal(0);
                returnData.setRows(null);
            }else {
                String selectRowSql=sysMessageRow.getMessage().replaceAll("#SQL#", messageSql)
                        .replaceAll("#ROW#", bankAcctId.getOffset()+" + 1")
                        .replaceAll("#ROWNEXT#",bankAcctId.getOffset() +" + "+bankAcctId.getLimit());
                log.info("拼装成的银行账户信息查询分页SQL[{}]",selectRowSql);
                String retrunMsg = atomosb.getData(selectRowSql);
                log.info("执行银行账户信息返回[{}]"+retrunMsg);
                List<BankAcctId> acctList = (List<BankAcctId>) JSONArray.parse(retrunMsg);
                returnData.setTotal(Integer.parseInt(paymentCountList.get(0).get("NUM").toString()));
                returnData.setRows(acctList);
            }

            return returnData;
        }catch (Exception e){
            log.error("获取银行账户信息失败",e);
            returnData.setTotal(0);
            returnData.setRows(null);
            return returnData;
        }
    }

    @Async(value="myAsyncPoolTaskExecutor")
    public void cacheBankAcctId(List acctList,String agentId){
        for (Object ob:acctList){
            redisUtil.lSet("AOSB:" + agentId + ":ACC", ob, TimeUnit.HOURS.toSeconds(1));
        }
        log.info("已将[{}]银行账户信息缓存完成",agentId);
    }
}
