package com.programme.Fortress.Function.ToolCase.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.programme.Fortress.Function.ToolCase.dao.SysMessageMapper;
import com.programme.Fortress.Function.ToolCase.entity.SysMessage;
import com.programme.Fortress.Util.DecryptUtil;
import com.programme.Fortress.Util.MyDateUtil;
import com.programme.Fortress.Util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FormatConServiceImpl implements FormatConService {

    @Autowired
    private SysMessageMapper sysMessageMapper;

    @Override
    public String formatConvert(Map map) {
        String inputTypr = map.get("inputTypr").toString();
        String leftArea = map.get("leftArea").toString();
        if ("1".equals(inputTypr)) {
            log.info("准备进行报文转换");
            try {
                String xmlFormat = StringUtil.xmlFormat(leftArea);
                log.info("转换后的报文:" + xmlFormat);
                return xmlFormat;
            } catch (Exception e) {
                log.error("转换报文发生异常", e);
                return e.getMessage();
            }
        } else if ("2".equals(inputTypr)) {
            log.info("准备进行数据信息解密");
            try {
                String filterMessage = DecryptUtil.signVerifyText(new StringUtil().filterMessage(leftArea));
                log.info("数据信息解密后:" + filterMessage);
                return filterMessage;
            } catch (Exception e) {
                e.printStackTrace();
                return e.getMessage();
            }
        } else {
            log.info("未查询到选择的类型");
            return "未查询到选择的类型";
        }
    }

    /**
     * 拼装1031查询报文
     * @param version
     * @param subBranchId
     * @param agentId
     * @param respCode
     * @param respMsg
     * @param orderNum
     * @return
     * @throws Exception
     */
    @Override
    public String makeMessage1031(String version,String subBranchId,String agentId,
                String respCode,String respMsg,String orderNum) throws Exception {
        //拼装1031报文
        List<SysMessage> sysMessages = sysMessageMapper.selectList(new QueryWrapper<SysMessage>()
                .eq("type", "1031").eq("type_source", "OSB"));
        Document document = DocumentHelper.parseText(sysMessages.get(0).getMessage());
        Element rootElement = document.getRootElement();
        //拼装报文头
        rootElement.element("Head").element("Version").setText(version);
        rootElement.element("Head").element("SubBranchId").setText(subBranchId);
        rootElement.element("Head").element("TxSerial").setText(MyDateUtil.getMaxDate("yyyyMMdd24HHmmss"));
        rootElement.element("Head").element("TxMoment").setText(MyDateUtil.getMaxDate("yyyyMMdd24HHmmss"));
        rootElement.element("Head").element("AgentId").setText(agentId);
        //拼装报文体
        rootElement.element("Body").element("Record").element("RespCode").setText(respCode);
        rootElement.element("Body").element("Record").element("RespMsg").setText(respMsg);
        rootElement.element("Body").element("Record").element("OrderNum").setText(orderNum);

        log.info("生产的1031报文[{}]",document.asXML());
        return document.asXML();
    }

    /**
     * 拼装1032查询报文
     * @param subBranchId
     * @param agentId
     * @param branchId
     * @param acctId
     * @param curCode
     * @param txDate
     * @return
     * @throws Exception
     */
    @Override
    public String makeMessage1032(String subBranchId,String agentId,
                                  String branchId,String acctId,String curCode,String txDate) throws Exception {
        List<SysMessage> sysMessages = sysMessageMapper.selectList(new QueryWrapper<SysMessage>()
                .eq("type", "1032").eq("type_source", "OSB"));
        Document document = DocumentHelper.parseText(sysMessages.get(0).getMessage());
        Element rootElement = document.getRootElement();
        rootElement.element("Head").element("SubBranchId").setText(subBranchId);
        rootElement.element("Head").element("TxSerial").setText(MyDateUtil.getMaxDate("yyyyMMdd24HHmmss"));
        rootElement.element("Head").element("TxMoment").setText(MyDateUtil.getMaxDate("yyyyMMdd24HHmmss"));
        rootElement.element("Head").element("AgentId").setText(agentId);
        rootElement.element("Body").element("BranchId").setText(branchId);
        rootElement.element("Body").element("AcctId").setText(acctId);
        rootElement.element("Body").element("CurCode").setText(curCode);
        rootElement.element("Body").element("TxDate").setText(txDate);
        log.info("生产的1032报文[{}]",document.asXML());
        return document.asXML();
    }
}
