package com.programme.Fortress.Function.BankTrade.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.programme.Fortress.Function.BankTrade.dao.AcctBalanceMapper;
import com.programme.Fortress.Function.BankTrade.entity.AcctBalance;
import com.programme.Fortress.Function.BankTrade.entity.BankAcctId;
import com.programme.Fortress.Function.Business.dao.BankIdMapper;
import com.programme.Fortress.Function.Business.entity.BankId;
import com.programme.Fortress.Function.ToolCase.dao.SysMessageMapper;
import com.programme.Fortress.Function.ToolCase.entity.ResultBean;
import com.programme.Fortress.Function.ToolCase.entity.SysMessage;
import com.programme.Fortress.Function.ToolCase.service.FormatConService;
import com.programme.Fortress.Other.Exception.ResultUtil;
import com.programme.Fortress.Other.Feign.ToATOMOSB;
import com.programme.Fortress.Util.MyDateUtil;
import com.programme.Fortress.Util.Postman.HttpUtil;
import com.programme.Fortress.Util.Redis.RedisUtil;
import com.programme.Fortress.Util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AcctBalanceServiceImpl implements AcctBalanceService {

    @Autowired
    private AcctBalanceMapper acctBalanceMapper;
    @Autowired
    private SysMessageMapper sysMessageMapper;
    @Autowired
    private FormatConService formatConService;
    @Autowired
    private BankIdMapper bankIdMapper;
    @Autowired
    private ToATOMOSB atomosb;

    @Override
    public ResultBean getAcctBalance(String agentid,String acctid,String curcode,String txdate) {
        try {
            SysMessage sysMessage = sysMessageMapper.selectOne(new QueryWrapper<SysMessage>()
                    .eq("type", "BankAcctIdInfor")
                    .eq("message_type", "SQL"));
            String messageSql = sysMessage.getMessage()
                    .replaceAll("#AGENTID#", agentid)
                    .replaceAll("#ACCTID#", acctid);
            String retrunMsg = atomosb.getData(messageSql);
            List<BankAcctId> bankAcctIds = JSON.parseArray(retrunMsg, BankAcctId.class);
            String message1032 = formatConService.makeMessage1032("", bankAcctIds.get(0).getAgentid(), bankAcctIds.get(0).getBranchid(), acctid, curcode, txdate);
            BankId bankId = bankIdMapper.selectById(agentid);
            String retrunMsg1032 = atomosb.visitBankAdapter(bankId.getBank()+"Adapter",message1032);
            if (retrunMsg1032.startsWith("<")) retrunMsg1032=StringUtil.xmlFormat(retrunMsg1032);
            return ResultUtil.success(StringUtil.xmlFormat(retrunMsg1032),"执行查询账户余额成功!");
        }catch (Exception e){
            log.error("执行查询账户余额异常",e);
            return ResultUtil.fail("执行查询账户余额异常:"+e.getMessage());
        }
    }

    @Override
    public ResultBean getAcctBalance(AcctBalance acctBalance) {

        if(StringUtil.checkEmpty(acctBalance.getAcctid())) acctBalance.setAcctid("11030801040007689");
        if(acctBalance.getDateFrom().equals(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))){
            acctBalance.setDateFrom(LocalDateTime.now().plusDays(-7).format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            acctBalance.setDateTo(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        }else {

            long dayDifference = MyDateUtil.getDayDifference(acctBalance.getDateFrom(),acctBalance.getDateTo());
            if(Math.abs(dayDifference)>60) return ResultUtil.fail("查询间隔最大为60天!");
            acctBalance.setDateFrom(acctBalance.getDateFrom().replaceAll("\\-",""));
            acctBalance.setDateTo(acctBalance.getDateTo().replaceAll("\\-",""));
        }
        return queryBalance(acctBalance);
    }

    /**
     * 获取账户余额
     * @param acctid
     * @param dateFrom
     * @param dateTo
     * @return
     */
    private ResultBean queryBalance(AcctBalance acctBalance){
        /*List<AcctBalance> acctBalances = acctBalanceMapper.selectList(new QueryWrapper<AcctBalance>()
                .eq("acctid", acctid)
                .between("txdate", dateFrom, dateTo)
                .orderByAsc("txdate"));*/
        try {
            SysMessage sysMessage = sysMessageMapper.selectOne(new QueryWrapper<SysMessage>()
                    .eq("type", "BankBalance")
                    .eq("message_type", "SQL"));
            String messageSql = sysMessage.getMessage()
                    .replaceAll("#AGENTID#", acctBalance.getAgentid())
                    .replaceAll("#ACCTID#", acctBalance.getAcctid())
                    .replaceAll("#CURCODE#", acctBalance.getCurcode())
                    .replaceAll("#DATAFROM#", acctBalance.getDateFrom())
                    .replaceAll("#DATATO#", acctBalance.getDateTo());
            String retrunMsg = atomosb.getData(messageSql);
            if(StringUtils.isEmpty(retrunMsg) || "[]".equals(retrunMsg)) return ResultUtil.fail("未查询到账户余额数据!");
            List<Map<String,Object>> acctBalanceList = (List<Map<String,Object>>) JSONArray.parse(retrunMsg);
            log.info(acctBalanceList.toString());
            ArrayList acctBalList = new ArrayList();
            ArrayList avlBalList = new ArrayList();
            ArrayList dateList = new ArrayList();
            for (Map acctBalanceMap : acctBalanceList) {
                acctBalList.add(acctBalanceMap.get("ACCTBAL"));
                avlBalList.add(acctBalanceMap.get("AVLBAL"));
                dateList.add(acctBalanceMap.get("TXDATE"));
            }
            JSONObject balJson = new JSONObject();
            balJson.put("acctBal", acctBalList);
            balJson.put("avlBal", avlBalList);
            balJson.put("dateBal", dateList);
            balJson.put("table",acctBalanceList);
            log.info(balJson.toJSONString());
            return ResultUtil.success(balJson,"查询成功!");
        }catch (Exception e){
            log.error("获取账户余额发生错误",e);
            return ResultUtil.fail("获取账户余额发生错误:"+e.getMessage());
        }
    }

    /**
     * 发送1032请求报文
     * @param message
     */
    @Async(value="myAsyncPoolTaskExecutor")
    public void requestAcctBalance(String message) {
         try {
             atomosb.sendOSBJMS(message);
         }catch (Exception e){
             log.error("请求银行账户余额发送失败",e);
             new Exception("请求银行账户余额发送失败"+e.getMessage());
         }
    }
}
