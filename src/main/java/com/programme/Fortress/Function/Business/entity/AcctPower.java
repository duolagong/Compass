package com.programme.Fortress.Function.Business.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("BNK_ACCTPOWER")
public class AcctPower {
    private String agentid;
    @TableId(value = "acctid", type = IdType.INPUT)
    private String acctid;
    private String querypower;
    private String paypower;
    private String billpower;
    private String version;
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime inputdate;
    private String subbranchid;
}
