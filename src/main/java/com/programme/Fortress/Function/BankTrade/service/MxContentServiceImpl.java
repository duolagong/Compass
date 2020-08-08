package com.programme.Fortress.Function.BankTrade.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.programme.Fortress.Function.BankTrade.entity.MxContent;
import com.programme.Fortress.Function.Business.entity.ReturnData;
import com.programme.Fortress.Function.ToolCase.dao.SysMessageMapper;
import com.programme.Fortress.Function.ToolCase.entity.SysMessage;
import com.programme.Fortress.Other.Exception.ResultUtil;
import com.programme.Fortress.Other.Feign.ToATOMOSB;
import com.programme.Fortress.Util.Postman.HttpUtil;
import com.programme.Fortress.Util.StringUtil;
import com.sun.org.apache.bcel.internal.generic.NEW;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Slf4j
@Service
public class MxContentServiceImpl implements MxContentService{

    @Autowired
    private SysMessageMapper sysMessageMapper;
    @Autowired
    private ToATOMOSB atomosb;

    @Override
    public String queryMxContent(MxContent mxContent){
        ReturnData<MxContent> returnData=new ReturnData<MxContent>();
        try {
            if(StringUtils.isEmpty(mxContent.getAcctid())) return null;
            //统计条数模板
            SysMessage messageCount = sysMessageMapper.selectOne(new QueryWrapper<SysMessage>()
                    .eq("type", "NUM")
                    .eq("message_type", "SQL"));
            //明细查询模板
            SysMessage sysMessage = sysMessageMapper.selectOne(new QueryWrapper<SysMessage>()
                    .eq("type", "MxContent")
                    .eq("message_type", "SQL"));
            //分页查询模板
            SysMessage sysMessageRow = sysMessageMapper.selectOne(new QueryWrapper<SysMessage>()
                    .eq("type", "ROWNUM")
                    .eq("message_type", "SQL"));
            String selectSql = sysMessage.getMessage()
                    .replaceAll("#ACCTID#", mxContent.getAcctid())
                    .replaceAll("#TXDATE#", mxContent.getTxdate())
                    .replaceAll("#AGENTID#", mxContent.getAgentid());
            if(!StringUtil.checkEmpty(mxContent.getBankvoucherid())){
                String bankvoucherId = mxContent.getBankvoucherid();
                String[] strings = bankvoucherId.split(",");
                for (int i = 0; i < strings.length; i++) {
                    strings[i]=mxContent.getAcctid()+strings[i];
                }
                String jsonString = JSON.toJSONString(strings);
                String bankvoucherIds=jsonString.substring(1,jsonString.length()-1).replaceAll("\"","'");
                selectSql=selectSql+" and bankvoucherid in("+bankvoucherIds+")";
            }
            if(!StringUtil.checkEmpty(mxContent.getDcflag())){
                selectSql=selectSql+" and dcflag ='"+mxContent.getDcflag()+"'";
            }
            if(!StringUtil.checkEmpty(mxContent.getRecipaccno())){
                selectSql=selectSql+" and recipaccno ='"+mxContent.getRecipaccno()+"'";
            }
            if(!StringUtil.checkEmpty(mxContent.getTxamount())){
                selectSql=selectSql+" and txamount  like'%"+mxContent.getTxamount()+"%'";
            }
            log.info("拼装银行账户交易明细查询SQL[{}]",selectSql);
            String selectNumSql = messageCount.getMessage()
                    .replaceAll("#ID#", "*")
                    .replaceAll("#SQL#", selectSql);
            log.info("拼装完成银行账户交易明细查询统计SQL[{}]",selectNumSql);
            String retrunCount = atomosb.getData(selectNumSql);
            log.info("返回银行账户交易明细查询统计返回[{}]",retrunCount);
            List<Map<String,Object>> paymentCountList = (List<Map<String,Object>>) JSONArray.parse(retrunCount);
            //校验是否存在银行账户明细
            if(paymentCountList.get(0).get("NUM").toString().equals("0")){
                returnData.setTotal(0);
                returnData.setRows(null);
            }else {
                String selectRowSql=sysMessageRow.getMessage().replaceAll("#SQL#", selectSql)
                        .replaceAll("#ROW#", mxContent.getOffset()+" + 1")
                        .replaceAll("#ROWNEXT#",mxContent.getOffset() +" + "+mxContent.getLimit());
                log.info("拼装完成银行账户交易明细查询分页SQL[{}]",selectRowSql);
                String retrunMsg = atomosb.getData(selectRowSql);
                log.info("返回的银行账户明细查询结果[{}]",retrunMsg);
                List<MxContent> mxContentList = (List<MxContent>) JSONArray.parse(retrunMsg);
                returnData.setTotal(Integer.parseInt(paymentCountList.get(0).get("NUM").toString()));
                returnData.setRows(mxContentList);
            }
        }catch (Exception e){
            log.error("查询账户明细发生异常",e);
        }
        return JSON.toJSONString(returnData);
    }

    public JSONObject madeLineChat(List<MxContent> mxContentList){
        ArrayList dlList = new ArrayList();
        ArrayList cList = new ArrayList();
        ArrayList paymentList = new ArrayList();
        ArrayList receiptList = new ArrayList();
        for (MxContent mxContent : mxContentList) {
            if("D".equals(mxContent.getDcflag())){
                dlList.add(mxContent.getTxtime());
                paymentList.add(mxContent.getTxamount());
            }else {
                cList.add(mxContent.getTxtime());
                receiptList.add(mxContent.getTxamount());
            }
        }
        JSONObject chatObject = new JSONObject(){{put("Ddate",dlList);put("Cdate",cList);put("payment",paymentList);put("receipt",receiptList);}};
        return chatObject;
    }
}
