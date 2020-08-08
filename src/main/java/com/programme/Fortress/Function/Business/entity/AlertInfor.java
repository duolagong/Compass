package com.programme.Fortress.Function.Business.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.lang.reflect.Type;

@Data
@TableName("BNK_ALERT_INFOR")
public class AlertInfor {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    /**
     * 类型
     */
    private String type;
    /**
     * 类型名称
     */
    private String typeName;
    /**
     * 提醒用户类型
     */
    private String userType;
    /**
     * 提醒消息
     */
    @TableField(value = "alert",strategy = FieldStrategy.IGNORED)
    private String alert;

    @TableField(fill = FieldFill.INSERT)
    private String createdTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updatedTime;
}
