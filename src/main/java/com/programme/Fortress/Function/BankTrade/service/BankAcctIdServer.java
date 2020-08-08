package com.programme.Fortress.Function.BankTrade.service;

import com.programme.Fortress.Function.BankTrade.entity.BankAcctId;

import java.util.List;

public interface BankAcctIdServer {

    List<Object> getBankAcctId(String agentId);

    Object getBankAcctId(BankAcctId bankAcctId);
}
