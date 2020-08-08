package com.programme.Fortress.Function.BankTrade.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.sql.Clob;
@Data
public class MxContent {

    private String bankvoucherid;
    private String acctid;
    private String acctidname;
    private String agentid;
    private String txdate;
    private String txtime;
    private String txamount;
    private String dcflag;
    private String recipaccno;
    private String recipname;
    private String tranxml;
    private String abstractstr;
    private String purpose;

    private int offset;
    private int limit;
}
