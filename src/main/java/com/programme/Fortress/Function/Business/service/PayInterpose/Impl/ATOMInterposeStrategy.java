package com.programme.Fortress.Function.Business.service.PayInterpose.Impl;

import com.programme.Fortress.Function.Business.entity.PayInfor;
import com.programme.Fortress.Function.Business.service.PayInterpose.PayInterposeStrategy;
import com.programme.Fortress.Function.ToolCase.entity.ResultBean;
import com.programme.Fortress.Other.Exception.ResultUtil;
import com.programme.Fortress.Other.Feign.ToNBKOSB;
import com.programme.Fortress.Util.MyDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ATOMInterposeStrategy implements PayInterposeStrategy {

    @Autowired
    private ToNBKOSB nbkosb;

    @Override
    public ResultBean confirmIntermediateState(PayInfor payInfor) {
        Document document= DocumentHelper.createDocument();
        Element root = document.addElement("Root");
        root.addAttribute("Target","ATOM");
        root.addAttribute("RecCount","1");
        root.addElement("TxSerial").addText(MyDateUtil.getMaxDate("yyyyMMdd24HHmmss"));
        Element record = root.addElement("Record");
        record.addElement("Version").addText(payInfor.getVersion());
        record.addElement("OrderNum").addText(payInfor.getOrdernum());
        record.addElement("SubBranchId").addText(payInfor.getSubbranchid());
        record.addElement("AgentId").addText(payInfor.getAgentid());
        log.info("拼装成的查询1030报文[{}]",document.asXML());
        String assemble1030 = nbkosb.sendAssemble1030(document.asXML());
        log.info("发送1030请求报文返回[{}]",assemble1030);
        return ResultUtil.success(null,"成功");
    }
}
