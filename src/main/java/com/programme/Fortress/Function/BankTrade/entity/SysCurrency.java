package com.programme.Fortress.Function.BankTrade.entity;

import lombok.Data;

@Data
public class SysCurrency {

    private String agentid; //银行

    private String curcode; //系统币种

    private String bankcurcode;//银行币种
}
