package com.programme.Fortress.Function.BankTrade.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.programme.Fortress.Function.BankTrade.entity.AssemblyMessage;
import com.programme.Fortress.Function.BankTrade.entity.BankAcctId;
import com.programme.Fortress.Function.ToolCase.dao.SysMessageMapper;
import com.programme.Fortress.Function.ToolCase.entity.ResultBean;
import com.programme.Fortress.Function.ToolCase.entity.SysMessage;
import com.programme.Fortress.Other.Exception.ResultUtil;
import com.programme.Fortress.Other.XQuery.XQueryFactory;
import com.programme.Fortress.Util.MyDateUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.saxon.s9api.*;
import org.dom4j.DocumentHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.transform.stream.StreamSource;
import java.lang.reflect.Type;
import java.util.concurrent.Future;

@Slf4j
@Service
public class AssemblyMessageServiceImpl implements AssemblyMessageService {

    @Autowired
    private XQueryFactory xQueryFactory;
    @Autowired
    private SysMessageMapper sysMessageMapper;
    @Autowired
    private BankAcctIdServer bankAcctIdServer;

    /**
     * 按照模板拼装报文
     *
     * @param assemblyMessage
     * @return
     */
    @Override
    public ResultBean getAssemblyMessage(AssemblyMessage assemblyMessage) {
        try {
            //异步获取账户信息
            Future<BankAcctId> bankAcctIdInfo = bankAcctIdServer.getBankAcctId(assemblyMessage.getAcctId(), assemblyMessage.getAgentId());
            SysMessage sysMessage = sysMessageMapper.selectOne(new QueryWrapper<SysMessage>()
                    .eq("type", assemblyMessage.getTxCode())
                    .eq("type_source", "COMPASS"));
            Processor processor = xQueryFactory.getObject();
            XQueryCompiler queryCompiler = processor.newXQueryCompiler();
            XQueryEvaluator queryEvaluator = queryCompiler.compile(sysMessage.getMessage()).load();
            queryEvaluator.setExternalVariable(new QName("version"), new XdmAtomicValue(assemblyMessage.getVersion()));
            queryEvaluator.setExternalVariable(new QName("txSerial"), new XdmAtomicValue(MyDateUtil.getMaxDate("yyyyMMddhhmmss")));
            queryEvaluator.setExternalVariable(new QName("txMoment"), new XdmAtomicValue(MyDateUtil.getMaxDate("yyyyMMddhhmmss")));
            queryEvaluator.setExternalVariable(new QName("acctId"), new XdmAtomicValue(assemblyMessage.getAcctId()));
            queryEvaluator.setExternalVariable(new QName("curCode"), new XdmAtomicValue(assemblyMessage.getCurCode()));
            queryEvaluator.setExternalVariable(new QName("txDate"), new XdmAtomicValue(assemblyMessage.getTxDate()));
            BankAcctId bankAcctId = bankAcctIdInfo.get();
            queryEvaluator.setExternalVariable(new QName("subBranchId"), new XdmAtomicValue(bankAcctId !=null ?bankAcctId.getAgentid().substring(6,10) : ""));
            queryEvaluator.setExternalVariable(new QName("branchId"), new XdmAtomicValue(bankAcctId != null ? bankAcctId.getBranchid().toString() : ""));
            queryEvaluator.setExternalVariable(new QName("agentId"), new XdmAtomicValue(bankAcctId !=null ? bankAcctId.getAgentid().toString() : assemblyMessage.getAgentId()));
            return ResultUtil.success(queryEvaluator.evaluate().toString(), "报文拼装成功");
        } catch (Exception e) {
            log.info("拼装报文发生异常", e);
            return ResultUtil.fail(e.getMessage());
        }
    }
}
