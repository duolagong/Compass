package com.programme.Fortress.Function.Business.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("BNK_MSG")
public class Msg {
    private String msg;
}
