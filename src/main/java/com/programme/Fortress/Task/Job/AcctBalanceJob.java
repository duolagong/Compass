package com.programme.Fortress.Task.Job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netflix.ribbon.proxy.annotation.Http;
import com.programme.Fortress.Function.BankTrade.entity.AcctBalance;
import com.programme.Fortress.Function.BankTrade.service.AcctBalanceService;
import com.programme.Fortress.Function.ToolCase.entity.SysMessage;
import com.programme.Fortress.Function.ToolCase.service.FormatConService;
import com.programme.Fortress.Other.Feign.ToATOMOSB;
import com.programme.Fortress.Util.MyDateUtil;
import com.programme.Fortress.Util.Postman.HttpUtil;
import com.programme.Fortress.Util.Redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component("AcctBalanceJob")
public class AcctBalanceJob {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ToATOMOSB atomosb;
    @Autowired
    private FormatConService formatConService;
    @Autowired
    private AcctBalanceService acctBalanceService;

    public void execute() {
        log.info("开始进行账户余额查询任务！");
        String nowDate = MyDateUtil.getMaxDate("yyyy-MM-dd");
        //获取账户列表
        List<Object> acctList = redisUtil.lGet("ATOM:ICBC:ACCT", 0, -1);
        if(StringUtils.isEmpty(acctList)){
            String selectRowSql = "select acct.f_zhbh as \"agentId\",acct.f_lhh as \"branchId\",acct.f_bz as \"curCode\" from bnk_khzh acct where f_yhbh='CWGS-666666102'";
            try {
                String retrunMsg = atomosb.getData(selectRowSql);
//                String retrunMsg = HttpUtil.httpXml(redisUtil.hget("COM:CIRCLE", "atomosb") + "/HTKG/SuperOSBEntry", selectRowSql, 30);
                log.info("银行账户同步返回[{}]", retrunMsg);
                List<Map<String ,Object>> bankAcctList = (List<Map<String ,Object>>) JSONArray.parse(retrunMsg);
                log.info(bankAcctList.toString());
                log.info("银行账户存储---------->Redis[{}]", retrunMsg);
                for (Map map : bankAcctList) {
                    redisUtil.lSet("ATOM:ICBC:ACCT", map, TimeUnit.MINUTES.toSeconds(30));
                }
            } catch (Exception e) {
                log.error("同步银行账户信息失败", e);
            }
            acctList = redisUtil.lGet("ATOM:ICBC:ACCT", 0, -1);
        }
        for (Object object : acctList){
            JSONObject jsonObject = JSON.parseObject(object.toString());
            try {
                String message1032 = formatConService.makeMessage1032("", jsonObject.getString("agentId"), jsonObject.getString("branchId"),
                        jsonObject.getString("acctId"), jsonObject.getString("curCode"), jsonObject.getString(""));
                acctBalanceService.requestAcctBalance(message1032);//异步
            }catch (Exception e){
                log.error("请求余额"+object.toString()+"发生异常",e);
            }
        }
    }
}
