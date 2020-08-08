package com.programme.Fortress.Function.Business.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.programme.Fortress.Function.Business.dao.PayInforMapper;
import com.programme.Fortress.Function.Business.entity.PayInfor;
import com.programme.Fortress.Function.Business.entity.PayNote;
import com.programme.Fortress.Function.Business.entity.ReturnData;
import com.programme.Fortress.Util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@Transactional
public class PayInforServiceImpl extends ServiceImpl<PayInforMapper, PayInfor> implements PayInforService  {

    @Autowired
    private PayInforMapper payInforMapper;
    @Autowired
    private PayNoteService  payNoteService;


    @Override
    public ReturnData<PayInfor> getPayInfor(PayInfor payInfor) {
        /*List<PayInfor> list = payInforMapper.selectList(new QueryWrapper<PayInfor>().eq("to_char(trandate,'yyyy-MM-dd')",nowDate));*/
        List<String> agentList = new ArrayList<>(Arrays.asList(payInfor.getAgentids()));
        if(agentList.contains("Z01")){
            agentList.add("W01");
            agentList.add("907");
            String[] strings = agentList.stream().toArray(String[]::new);
            log.info("优化Agentids查询条件对核心的处理[{}]",agentList.toString());
            payInfor.setAgentids(strings);
        }
        int tatlo=payInforMapper.getPayInforCount(payInfor);
        ReturnData<PayInfor> returnData=new ReturnData<PayInfor>();
        if(tatlo==0) return returnData;
        returnData.setTotal(tatlo);
        List<PayInfor> payInforList = payInforMapper.getPayInfor(payInfor);
        returnData.setRows(payInforList);
        return returnData;
    }

    @Override
    public PayInfor getOrdernumInfor(String ordernum){
        PayInfor payInfor=null;
        try {
            payInfor = payInforMapper.selectById(ordernum);
            PayNote payNote = payNoteService.getOrdernum(ordernum);
            //当表中没没有记录下来的时候，主动去拼装一个
            if (payNote == null) {
                log.info("库表中还未加载[{}]交易信息，主动拼装",ordernum);
                String tranxml = payInfor.getTranxml();
                payNote = payNoteService.getOrdernumPayNote(tranxml, ordernum);
            }
            payInfor.setPayNote(payNote);
        }catch (Exception e){
            log.error("展示"+ordernum+"交易报文时发生异常",e);
        }finally {
            return payInfor;
        }
    }

    @Override
    public String getTranXml(String ordernum){
        try {
            PayInfor payInfor = payInforMapper.selectById(ordernum);
            return StringUtil.xmlFormat(payInfor.getTranxml());
        }catch (Exception e){
            log.error("获取TranXml报错",e);
            return "发送错误:"+e.getMessage();
        }
    }

    @Override
    public Integer getPayProcess(){
        Integer count = payInforMapper.selectCount(new QueryWrapper<PayInfor>().eq("payprocess", "1"));
        return count;
    }
}
