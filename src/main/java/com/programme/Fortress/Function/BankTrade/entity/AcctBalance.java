package com.programme.Fortress.Function.BankTrade.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
public class AcctBalance {

    private String acctid;

    private String agentid;

    private String acctbal;

    private String avlbal;

    private String frzamt;

    private String curcode;

    private String txdate;

    private String dateFrom;

    private String dateTo;
}
