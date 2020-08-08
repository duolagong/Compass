package com.programme.Fortress.Function.Business.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("BNK_BANKID")
public class BankId {
    @TableId
    private String id;
    private String bank;
    private String bankname;
}
