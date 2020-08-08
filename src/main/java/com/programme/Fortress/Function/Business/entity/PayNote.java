package com.programme.Fortress.Function.Business.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("BNK_PAY_NOTE")
public class PayNote {
    @TableId(value = "ordernum", type = IdType.INPUT)
    private String orderNum;
    private String version;
    @TableField(value = "txcode")
    private String txCode;
    @TableField(value = "subbranchid")
    private String subBranchId;
    @TableField(value = "txSerial")
    private String txSerial;
    @TableField(value = "txmoment")
    private String TxMoment;
    @TableField(value = "agentid")
    private String agentId;
    @TableField(value = "espdate")
    private String espDate;
    @TableField(value = "curcode")
    private String curCode;
    @TableField(value = "hurryflag",strategy = FieldStrategy.IGNORED)
    private String hurryFlag;
    @TableField(value = "skipflag",strategy = FieldStrategy.IGNORED)
    private String skipFlag;
    @TableField(value = "areaflag",strategy = FieldStrategy.IGNORED)
    private String areaFlag;
    @TableField(value = "personflag",strategy = FieldStrategy.IGNORED)
    private String personFlag;
    @TableField(value = "txamount")
    private String txAmount;
    @TableField(value = "outbranchid",strategy = FieldStrategy.IGNORED)
    private String outBranchId;
    @TableField(value = "outbranchname",strategy = FieldStrategy.IGNORED)
    private String outBranchName;
    @TableField(value = "outacctid",strategy = FieldStrategy.IGNORED)
    private String outAcctId;
    @TableField(value = "outacctname",strategy = FieldStrategy.IGNORED)
    private String outAcctName;
    @TableField(value = "inbranchid",strategy = FieldStrategy.IGNORED)
    private String inBranchId;
    @TableField(value = "inbranchname",strategy = FieldStrategy.IGNORED)
    private String inBranchName;
    @TableField(value = "inacctid",strategy = FieldStrategy.IGNORED)
    private String inAcctId;
    @TableField(value = "inacctname",strategy = FieldStrategy.IGNORED)
    private String inAcctName;
    @TableField(value = "reccityname",strategy = FieldStrategy.IGNORED)
    private String recCityName;
    @TableField(strategy = FieldStrategy.IGNORED)
    private String purpose;
    @TableField(value = "abstractstr",strategy = FieldStrategy.IGNORED)
    private String abstractStr;
    @TableField(value = "memo",strategy = FieldStrategy.IGNORED)
    private String memo;
    @TableField(value = "budgetnum",strategy = FieldStrategy.IGNORED)
    private String budgetNum;
    @TableField(value = "contractnum",strategy = FieldStrategy.IGNORED)
    private String contractNum;
    @TableField(value = "reasonnoused",strategy = FieldStrategy.IGNORED)
    private String reasonNoUsed;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    //核心OSB
    @TableField(value = "bankaccounttype",exist = false)
    private String bankaccounttype;

}
