package com.programme.Fortress.Function.Business.service;

import com.programme.Fortress.Function.Business.dao.PayInforMapper;
import com.programme.Fortress.Function.Business.entity.PayInfor;
import com.programme.Fortress.Function.Business.entity.ReturnData;
import com.programme.Fortress.Function.ToolCase.entity.ResultBean;
import com.programme.Fortress.Function.ToolCase.service.FormatConService;
import com.programme.Fortress.Other.Exception.ResultUtil;
import com.programme.Fortress.Other.Feign.ToNBKOSB;
import com.programme.Fortress.Util.Postman.HttpUtil;
import com.programme.Fortress.Util.Redis.RedisUtil;
import com.programme.Fortress.Util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class NoticeSendServiceImpl implements NoticeSendService {

    @Autowired
    private PayInforMapper payInforMapper;
    @Autowired
    private FormatConService formatConService;
    @Autowired
    private ToNBKOSB nbkosb;

    @Override
    public ReturnData<PayInfor> getNoticeSend(HashMap hashMap) {
        ReturnData<PayInfor> returnData = new ReturnData<PayInfor>();
        if(!StringUtil.checkEmpty(hashMap.get("ordernum"))){//ordernum不为空
            String ordernums = hashMap.get("ordernum").toString().trim();
            String[] ordernumSplit = ordernums.split(",");
            hashMap.put("ordernum",ordernumSplit);
        }
        int tatlo = payInforMapper.getOrdernumInforCount(hashMap);
        if (tatlo == 0) return returnData;
        returnData.setTotal(tatlo);
        List<PayInfor> ordernumInfor = payInforMapper.getOrdernumInfor(hashMap);
        returnData.setRows(ordernumInfor);
        return returnData;
    }

    @Override
    public ResultBean noteSend1031(String ordernum) {
        try {
            PayInfor payInfor = payInforMapper.selectById(ordernum);
            String asXML="";
            if(!StringUtil.checkEmpty(payInfor.getBankfinalxml())){//Bankfinalxml非空
                asXML = payInfor.getBankfinalxml();
            }else if(!StringUtil.checkEmpty(payInfor.getBankfinalcode())){//Bankfinalcode非空
                asXML = formatConService.makeMessage1031(payInfor.getVersion(),payInfor.getSubbranchid(),payInfor.getAgentid(),
                        payInfor.getBankfinalcode(),payInfor.getBankfinalmsg(),payInfor.getOrdernum());
            /*}else if(payInfor.getErpcode().startsWith("E") && !StringUtil.checkEmpty(payInfor.getErpxml())){//ErpCode为失败和ErpXml非空
                asXML = payInfor.getErpxml();*/
            }else {
                return ResultUtil.fail("由于找不到结果记录，无法生成付款结果报文!");
            }
            log.info("准备向网银系统重推的报文[{}]",asXML);
            String resultMessage = nbkosb.sendNBKJMS(asXML);
            return ResultUtil.success(null,"重推交易结果成功!");
        } catch (DocumentException e){
            log.error("拼装1031报文异常",e);
            return ResultUtil.fail("拼装1031报文异常:"+e.getMessage());
        } catch (Exception e) {
            log.error("重推1031报文异常",e);
            return ResultUtil.fail("重推1031报文异常:"+e.getMessage());
        }
    }

}
