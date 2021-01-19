package com.programme.Fortress.Function.BankTrade.service;

import com.alibaba.fastjson.JSONObject;
import com.programme.Fortress.Function.BankTrade.entity.BankAcctId;

import java.util.List;
import java.util.concurrent.Future;

public interface BankAcctIdServer {

    List<Object> getBankAcctId(String agentId);

    Object getBankAcctId(BankAcctId bankAcctId);

    Future<BankAcctId> getBankAcctId(String acctId, String agentId);
}
