package com.programme.Fortress.Function.BankTrade.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.programme.Fortress.Function.Business.entity.PayNote;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class BankPayment {
    private String ordernum;

    private String bankaccounttype;

    private String totalamount;

    private String inputdate;

    private String payresultiswait;

    private String version;

    private String agentid;

    private String subbranchid;

    private String transnoid;

    private String banksyncode;
    private String banksynmsg;
    private String banksynxml;
    private Date banksyndate;

    private String bankfinalcode;
    private String bankfinalmsg;
    private String bankfinalxml;
    private Date bankfinaldate;

    public PayNote payNote; //报文明细
}
