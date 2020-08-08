package com.programme.Fortress.Function.BankTrade.service;
import com.programme.Fortress.Function.BankTrade.entity.BankPayment;
import org.dom4j.DocumentException;

import java.util.HashMap;
import java.util.Map;

public interface BankPaymentService {

    String getBankPayment(Map queryMap);

    HashMap getTranXml(String ordernum , String payresultiswait);

    BankPayment getOrdernumInfor(String ordernum ,String payresultiswait);

    String getResultQuery(Map queryMap);

    String getResultQueryXml(String txCode,String agentId,String orderNum ,String subBranchId) throws Exception;
}
