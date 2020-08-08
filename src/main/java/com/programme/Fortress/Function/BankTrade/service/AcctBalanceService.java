package com.programme.Fortress.Function.BankTrade.service;

import com.programme.Fortress.Function.BankTrade.entity.AcctBalance;
import com.programme.Fortress.Function.ToolCase.entity.ResultBean;

public interface AcctBalanceService {

    ResultBean getAcctBalance(String agentid,String acctid,String curcode,String txdate);

    ResultBean getAcctBalance(AcctBalance acctBalance);

    void requestAcctBalance(String message);
}
