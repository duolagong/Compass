package com.programme.Fortress.Function.Business.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName(value = "BNK_CITY")
public class City {
    @TableId(value = "id", type = IdType.INPUT)
    private long id;
    private String code;
    private String name;
    private String point;
    @TableField(strategy = FieldStrategy.IGNORED)
    private String area;
}
