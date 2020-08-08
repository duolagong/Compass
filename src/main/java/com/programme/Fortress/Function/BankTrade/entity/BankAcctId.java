package com.programme.Fortress.Function.BankTrade.entity;

import lombok.Data;

@Data
public class BankAcctId {
    private String agentid; //银行

    private String branchid; //联行号

    private String acctid;//账号

    private String acctidname;//户名

    private String curcode;//币种
    /**
     * 等级
     * AA   代理户
     * GA   一般户
     * SA   二级户
     * BA  一级户
     * EA   网银户
     */
    private String level;//等级

    private String detailed;//明细
    private String l_detailed;//历史明细
    private String l_balance;//历史余额
    private String statement;//对账单

    private int offset;
    private int limit;
}
