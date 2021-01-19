package com.programme.Fortress.Function.BankTrade.entity;

import lombok.Data;

@Data
public class AssemblyMessage {

    /**
     * 报文类型
     */
    private String txCode;
    /**
     * 目标系统来源
     */
    private String version;
    /**
     * 企业编号
     */
    private String subBranchId;
    /**
     * 银行编号
     */
    private String agentId;
    /**
     * 账户号
     */
    private String acctId;
    /**
     * 币种
     */
    private String curCode;
    /**
     * 日期
     */
    private String txDate;

}
