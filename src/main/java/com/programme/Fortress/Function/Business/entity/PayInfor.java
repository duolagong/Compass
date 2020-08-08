package com.programme.Fortress.Function.Business.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("BNK_PAY_INFOR")
public class PayInfor {
    @TableId(value = "ordernum")
    private String ordernum;
    private String version;
    private String agentid;
    private String personflag;
    private String txmoment;
    private String tranxml;
    private String erpcode;
    private String erpmsg;
    private String erpxml;
    private LocalDateTime erpdate;
    private String atomcode;
    private String atommsg;
    private String atomxml;
    private LocalDateTime atomdate;
    private String osbcode;
    private String osbmsg;
    private String osbxml;
    private LocalDateTime osbdate;
    private String banksyncode;
    private String banksynmsg;
    private String banksynxml;
    private LocalDateTime banksyndate;
    private String bankfinalcode;
    private String bankfinalmsg;
    private String bankfinalxml;
    private LocalDateTime bankfinaldate;
    private String payprocess;
    private String erpsyncode;
    private String erpsynmsg;
    private String erpsynxml;
    private String sendatomtag;
    private String sendatomosbtag;
    private String subbranchid;
    private String txamount;
    private LocalDateTime trandate;
    private LocalDateTime erpsyndate;
    private String nbkcode;
    private String nbkmsg;
    private String nbkxml;
    private LocalDateTime nbkdate;
    private String txcode;

    //用于处理前台传过来的非表内数据
    @TableField(exist = false)
    private String[] versions;
    @TableField(exist = false)
    private String[] agentids;
    @TableField(exist = false)
    private String dateFrom;
    @TableField(exist = false)
    private String dateTo;
    @TableField(exist = false)
    public int limit; //每页的条数
    @TableField(exist = false)
    public int offset; //数据库查询索引
    @TableField(exist = false)
    public PayNote payNote; //报文明细

}
