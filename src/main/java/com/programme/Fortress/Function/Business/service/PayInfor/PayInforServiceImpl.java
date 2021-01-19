package com.programme.Fortress.Function.Business.service.PayInfor;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.programme.Fortress.Function.Business.dao.PayInforMapper;
import com.programme.Fortress.Function.Business.entity.PayInfor;
import com.programme.Fortress.Function.Business.entity.PayNote;
import com.programme.Fortress.Function.Business.entity.ReturnData;
import com.programme.Fortress.Function.Business.service.PayInfor.PayInforService;
import com.programme.Fortress.Function.Business.service.PayNoteService;
import com.programme.Fortress.Util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@Transactional
public class PayInforServiceImpl extends ServiceImpl<PayInforMapper, PayInfor> implements PayInforService {

    @Autowired
    private PayInforMapper payInforMapper;
    @Autowired
    private PayNoteService payNoteService;

    @Override
    public PayInfor fillPayInforDate(PayInfor payInfor) {
        if (payInfor.getDateFrom() == null || "".equals(payInfor.getDateFrom())) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String format = timeFormatter.format(now);
            payInfor.setDateFrom(format);
            payInfor.setDateTo(format);
        }
        return payInfor;
    }

    @Override
    public PayInfor fillPayInforAgentid(PayInfor payInfor) {
        List<String> agentList = new ArrayList<>(Arrays.asList(payInfor.getAgentids()));
        if (agentList.contains("Z01")) {
            agentList.add("W01");
            agentList.add("907");
            String[] strings = agentList.stream().toArray(String[]::new);
            payInfor.setAgentids(strings);
        }
        return payInfor;
    }

    @Override
    public ReturnData<PayInfor> getPayInfor(PayInfor payInfor) {
        /*List<PayInfor> list = payInforMapper.selectList(new QueryWrapper<PayInfor>().eq("to_char(trandate,'yyyy-MM-dd')",nowDate));*/
        List<String> agentList = new ArrayList<>(Arrays.asList(payInfor.getAgentids()));
        if (agentList.contains("Z01")) {
            agentList.add("W01");
            agentList.add("907");
            String[] strings = agentList.stream().toArray(String[]::new);
            log.info("优化Agentids查询条件对核心的处理[{}]", agentList.toString());
            payInfor.setAgentids(strings);
        }

        int tatlo = payInforMapper.getPayInforCount(payInfor);
        ReturnData<PayInfor> returnData = new ReturnData<PayInfor>();
        if (tatlo == 0) return returnData;
        returnData.setTotal(tatlo);
        List<PayInfor> payInforList = payInforMapper.getPayInfor(payInfor);
        returnData.setRows(payInforList);
        return returnData;
    }

    @Override
    public PayInfor getOrdernumInfor(String ordernum) {
        PayInfor payInfor = null;
        try {
            payInfor = payInforMapper.selectById(ordernum);
            PayNote payNote = payNoteService.getOrdernum(ordernum);
            //当表中没没有记录下来的时候，主动去拼装一个
            if (payNote == null) {
                log.info("库表中还未加载[{}]交易信息，主动拼装", ordernum);
                String tranxml = payInfor.getTranxml();
                payNote = payNoteService.getOrdernumPayNote(tranxml, ordernum);
            }
            payInfor.setPayNote(payNote);
        } catch (Exception e) {
            log.error("展示" + ordernum + "交易报文时发生异常", e);
        } finally {
            return payInfor;
        }
    }

    @Override
    public String getTranXml(String ordernum) {
        try {
            PayInfor payInfor = payInforMapper.selectById(ordernum);
            return StringUtil.xmlFormat(payInfor.getTranxml());
        } catch (Exception e) {
            log.error("获取TranXml报错", e);
            return "发送错误:" + e.getMessage();
        }
    }

    @Override
    public Integer getPayProcess() {
        Integer count = payInforMapper.selectCount(new QueryWrapper<PayInfor>().eq("payprocess", "1"));
        return count;
    }

    @Override
    public JSONObject getVersionChart(PayInfor payInfor, Boolean type) {
        List<HashMap> versionInfoMap = null;
        if (type) {
            versionInfoMap = payInforMapper.getVersionChart(payInfor);
        } else {
            versionInfoMap = payInforMapper.getVersionnNumChart(payInfor);
        }
        ArrayList<String> nameList = new ArrayList<String>();
        ArrayList<BigDecimal> totalList = new ArrayList<>();
        ArrayList<Long> numList = new ArrayList<Long>();
        for (HashMap infoMap : versionInfoMap) {
            nameList.add(infoMap.get("NAME").toString());
            if (type) totalList.add(new BigDecimal(infoMap.get("TOTAL").toString()));
            numList.add(Long.valueOf(infoMap.get("NUM").toString()));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", nameList);
        if (type) jsonObject.put("total", totalList);
        jsonObject.put("num", numList);
        return jsonObject;
    }

    @Override
    public JSONObject getPersonflagChart(PayInfor payInfor) {
        List<HashMap> personflagList = payInforMapper.getPersonflagChart(payInfor);
        ArrayList<String> nameList = new ArrayList<>();
        ArrayList<Long> persionList = new ArrayList<>();
        ArrayList<Long> noPersionList = new ArrayList<>();
        for (HashMap infoMap : personflagList) {
            nameList.add(infoMap.get("NAME").toString());
            persionList.add(Long.valueOf(infoMap.get("PERSON").toString()));
            noPersionList.add(Long.valueOf(infoMap.get("NOPERSON").toString()));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", nameList);
        jsonObject.put("person", persionList);
        jsonObject.put("noperson", noPersionList);
        return jsonObject;
    }

    @Override
    public JSONObject getPayDateTrendChart(PayInfor payInfor, Boolean type) {
        List<HashMap> totalTrendList = null;
        if (type) {
            totalTrendList = payInforMapper.getPayDateTrendChart(payInfor);
        } else {
            totalTrendList = payInforMapper.getNumTrendChart(payInfor);
        }
        ArrayList<String> nameList = new ArrayList<>();
        ArrayList<BigDecimal> totalList = new ArrayList<>();
        ArrayList<Long> numList = new ArrayList<>();
        for (HashMap infoMap : totalTrendList) {
            nameList.add(infoMap.get("PAYDATE").toString());
            if (type) totalList.add(new BigDecimal(infoMap.get("TOTAL").toString()));
            numList.add(Long.valueOf(infoMap.get("NUM").toString()));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("paydate", nameList);
        if (type) jsonObject.put("total", totalList);
        jsonObject.put("num", numList);
        return jsonObject;
    }
}
