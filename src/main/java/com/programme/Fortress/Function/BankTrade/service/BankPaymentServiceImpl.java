package com.programme.Fortress.Function.BankTrade.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.programme.Fortress.Function.BankTrade.entity.BankPayment;
import com.programme.Fortress.Function.Business.dao.BankIdMapper;
import com.programme.Fortress.Function.Business.entity.BankId;
import com.programme.Fortress.Function.Business.entity.PayNote;
import com.programme.Fortress.Function.Business.entity.ReturnData;
import com.programme.Fortress.Function.Business.service.PayNoteService;
import com.programme.Fortress.Function.ToolCase.dao.SysMessageMapper;
import com.programme.Fortress.Function.ToolCase.entity.SysMessage;
import com.programme.Fortress.Other.Feign.ToATOMOSB;
import com.programme.Fortress.Util.MyDateUtil;
import com.programme.Fortress.Util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class BankPaymentServiceImpl implements BankPaymentService{
    @Autowired
    private SysMessageMapper sysMessageMapper;
    @Autowired
    private PayNoteService payNoteService;
    @Autowired
    private BankIdMapper bankIdMapper;
    @Autowired
    private ToATOMOSB atomosb;

    @Override
    public String getBankPayment(Map queryMap){
        try {
            ReturnData<BankPayment> returnData=new ReturnData<BankPayment>();
            //统计条数模板
            SysMessage messageCount = sysMessageMapper.selectOne(new QueryWrapper<SysMessage>()
                    .eq("type", "NUM")
                    .eq("message_type", "SQL"));
            //查询明细模板
            SysMessage sysMessage = sysMessageMapper.selectOne(new QueryWrapper<SysMessage>()
                    .eq("type", "BankPayment")
                    .eq("message_type", "SQL"));
            //分页查询模板
            SysMessage sysMessageRow = sysMessageMapper.selectOne(new QueryWrapper<SysMessage>()
                    .eq("type", "ROWNUM")
                    .eq("message_type", "SQL"));
            String selectSql = sysMessage.getMessage()
                    .replaceAll("#DATAFROM#", queryMap.get("dateFrom").toString())
                    .replaceAll("#DATATO#", queryMap.get("dateTo").toString());
            if(!StringUtil.checkEmpty(queryMap.get("ordernum"))){
                String ordernums = queryMap.get("ordernum").toString();
                String[] strings = ordernums.split(",");
                String jsonString = JSON.toJSONString(strings);
                ordernums=jsonString.substring(1,jsonString.length()-1).replaceAll("\"","'");
                selectSql=selectSql+" and ordernum in("+ordernums+")";
            }
            if(!StringUtil.checkEmpty(queryMap.get("bankaccounttype"))){
                selectSql=selectSql+" and bankaccounttype ='"+queryMap.get("bankaccounttype").toString()+"'";
            }
            if(!StringUtil.checkEmpty(queryMap.get("agentid"))){
                selectSql=selectSql+" and agentid like'%"+queryMap.get("agentid").toString()+"'";
            }
            if(!StringUtil.checkEmpty(queryMap.get("payresultiswait"))){
                selectSql=selectSql+" and payresultiswait ='"+queryMap.get("payresultiswait").toString()+"'";
            }
            if(!StringUtil.checkEmpty(queryMap.get("txamount"))){
                selectSql=selectSql+" and totalamount  like'%"+queryMap.get("txamount").toString()+"%'";
            }
            log.info("拼装银行账户付款查询SQL[{}]",selectSql);
            String selectNumSql = messageCount.getMessage()
                    .replaceAll("#ID#", "*")
                    .replaceAll("#SQL#", selectSql);
            log.info("拼装完成银行账户付款统计SQL[{}]",selectNumSql);
            String retrunCount = atomosb.getData(selectNumSql);
            log.info("执行银行账户付款统计返回[{}]",retrunCount);
            List<Map<String,Object>> paymentCountList = (List<Map<String,Object>>) JSONArray.parse(retrunCount);
            //校验是否存在付款交易
            if(paymentCountList.get(0).get("NUM").toString().equals("0")){
                returnData.setTotal(0);
                returnData.setRows(null);
            } else {
                String selectRowSql=sysMessageRow.getMessage().replaceAll("#SQL#", selectSql+" order by inputdate desc")
                        .replaceAll("#ROW#", queryMap.get("offset").toString()+" + 1")
                        .replaceAll("#ROWNEXT#",queryMap.get("offset").toString() +" + "+queryMap.get("limit").toString());
                log.info("拼装完成银行账户付款明细SQL[{}]",selectRowSql);
                String retrunMsg = atomosb.getData(selectRowSql);
                log.info("执行完成银行账户付款明细返回[{}]",retrunMsg);
                List<Map<String,Object>> acctBalanceList = (List<Map<String,Object>>) JSONArray.parse(retrunMsg);
                log.info("银行账户付款明细返回-格式转换后数据[{}]",acctBalanceList.toString());
                ArrayList<BankPayment> bankPaymentList = new ArrayList<>();
                for (Map map : acctBalanceList){
                    bankPaymentList.add(JSON.parseObject(JSON.toJSONString(map), BankPayment.class));
                }
                returnData.setTotal(Integer.parseInt(paymentCountList.get(0).get("NUM").toString()));
                returnData.setRows(bankPaymentList);
            }
            log.info(JSON.toJSONString(returnData));
            return JSON.toJSONString(returnData);
        }catch (Exception e){
            log.error("向核心OSB查询付款数据异常",e);
            return null;
        }
    }

    @Override
    public BankPayment getOrdernumInfor(String ordernum,String payresultiswait){
        BankPayment bankPayment=null;
        try {
            SysMessage sysMessage = sysMessageMapper.selectOne(new QueryWrapper<SysMessage>()
                    .eq("type", "BankPaymentDetails")
                    .eq("message_type", "SQL"));
            String messageSql = sysMessage.getMessage().replaceAll("#ORDERNUM#", ordernum);
            if ("3".equals(payresultiswait)){//异常交易特殊处理
                messageSql=messageSql.replaceAll("#FUNCCODE#","000");
            }else {
                messageSql=messageSql.replaceAll("#FUNCCODE#","100");
            }
            log.info("拼装完成银行账户付款明细SQL[{}]",messageSql);
            String retrunMsg = atomosb.getData(messageSql);
            log.info("执行银行账户付款明细[{}]",retrunMsg);
            List<Map<String,Object>> bankPaymentList = (List<Map<String,Object>>) JSONArray.parse(retrunMsg);
            Map<String, Object> bankPaymentMap = bankPaymentList.get(0);
            PayNote payNote = payNoteService.getOrdernumPayNote(bankPaymentMap.get("TRANXML").toString(), ordernum);
            bankPaymentMap.remove("TRANXML");
            bankPayment = JSON.parseObject(JSON.toJSONString(bankPaymentMap),BankPayment.class);
            bankPayment.setPayNote(payNote);
            log.info("反馈银行账户付款明细[{}]",bankPayment);
            return bankPayment;
        }catch (Exception e){
            log.error("展示"+ordernum+"交易报文时发生异常",e);
        }finally {
            return bankPayment;
        }
    }

    @Override
    public HashMap getTranXml(String ordernum,String payresultiswait){
        HashMap<String, String> xmlMap = new HashMap<>();
        try {
            String messageSql="";
            if("3".equals(payresultiswait)){
                messageSql="select tranxml,bankxml,'null' as transynxml, 'null' as banksynxml from esb_payment where ordernum = '"+ordernum+"'";
            }else {
                messageSql = "select b.tranxml,b.bankxml,a.tranxml as transynxml,a.bankxml as banksynxml from esb_payment a inner join esb_payment b on a.txserial = b.txserial where b.ordernum = '" + ordernum + "' and a.funccode = '100'";
            }
            log.info("拼装完成银行付款报文SQL[{}]",messageSql);
            String retrunMsg = atomosb.getData(messageSql);
            log.info("执行银行付款报文查询[{}]",retrunMsg);
            List<Map<String,Object>> bankPaymentList = (List<Map<String,Object>>) JSONArray.parse(retrunMsg);
            //填充
            xmlMap.put("TRANXML",StringUtil.xmlFormat(bankPaymentList.get(0).get("TRANXML").toString()));
            xmlMap.put("BANKXML",StringUtil.xmlFormat(bankPaymentList.get(0).get("BANKXML").toString()));
            if (StringUtil.checkEmpty(bankPaymentList.get(0).get("TRANSYNXML"))){
                xmlMap.put("TRANSYNXML","无");
            }else {
                xmlMap.put("TRANSYNXML",StringUtil.xmlFormat(bankPaymentList.get(0).get("TRANSYNXML").toString()));
            }
            if (StringUtil.checkEmpty(bankPaymentList.get(0).get("BANKSYNXML"))){
                xmlMap.put("BANKSYNXML","无");
            }else {
                xmlMap.put("BANKSYNXML",StringUtil.xmlFormat(bankPaymentList.get(0).get("BANKSYNXML").toString()));
            }
        }catch (Exception e){
            xmlMap.put("TRANXML",e.getMessage());
            xmlMap.put("BANKXML",e.getMessage());
            xmlMap.put("TRANSYNXML",e.getMessage());
            xmlMap.put("BANKSYNXML",e.getMessage());
            log.error("展示"+ordernum+"交易报文时发生异常",e);
        }
        return xmlMap;
    }

    @Override
    public String getResultQuery(Map queryMap){
        String retrunMsg="";
        try {
            String resultQueryXml = getResultQueryXml(queryMap.get("version").toString(),queryMap.get("txcode").toString(), queryMap.get("agentid").toString(),
                    queryMap.get("ordernum").toString(), queryMap.get("subbranchid").toString());
            log.info("拼装完成查询[{}]交易结果的报文[{}]",queryMap.get("ordernum"),resultQueryXml);
            if(!resultQueryXml.startsWith("<")) return resultQueryXml;
            String agentId = queryMap.get("agentid").toString();
            BankId bankId = bankIdMapper.selectById(agentId.substring(agentId.length() - 3));
            retrunMsg = atomosb.visitBankAdapter(bankId.getBank()+"Adapter",resultQueryXml);
            if (retrunMsg.startsWith("<")) retrunMsg=StringUtil.xmlFormat(retrunMsg);
            log.info("结果查询反馈报文[{}]",retrunMsg);
            return retrunMsg;
        }catch (Exception e){
            retrunMsg=e.toString();
            log.error("交易结果查询发生异常",e);
        }finally {
            return retrunMsg;
        }
    }

    @Override
    public String getResultQueryXml(String version,String txCode, String agentId, String orderNum, String subBranchId) throws Exception {
        SysMessage sysMessage = sysMessageMapper.selectOne(new QueryWrapper<SysMessage>()
                .eq("type", txCode)
                .eq("message_type", "XML"));
        Document document = DocumentHelper.parseText(sysMessage.getMessage());
        //拼装报文头
        Element rootElement = document.getRootElement();
        rootElement.element("Head").element("Version").setText(version);
        rootElement.element("Head").element("TxSerial").setText(MyDateUtil.getMaxDate("yyyyMMddhhmmss"));
        rootElement.element("Head").element("TxMoment").setText(MyDateUtil.getMaxDate("yyyyMMddhhmmss"));
        rootElement.element("Head").element("SubBranchId").setText(subBranchId);
        rootElement.element("Head").element("AgentId").setText(agentId.length()==3 ? "CWGS-666666"+agentId : agentId);
        //拼装报文体
        rootElement.element("Body").element("Record").element("OrderNum").setText(orderNum);
        //生成1030报文
        return StringUtil.xmlFormat(document.asXML());
    }

    @Async
    public void sendOSBNotice(String queryXml){
        atomosb.sendOSBNotice(queryXml);
    }
}
