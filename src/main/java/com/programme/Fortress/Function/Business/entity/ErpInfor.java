package com.programme.Fortress.Function.Business.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("BNK_ERP_INFOR")
public class ErpInfor {
    @TableId(value = "txserial")
    private String txserial;
    private String version;
    private String subbranchid;
    private String txmoment;
    private String reccount;
    private String inputdate;
    private String agentid;
    private String payresultiswait;
    private String tranxml;

}
