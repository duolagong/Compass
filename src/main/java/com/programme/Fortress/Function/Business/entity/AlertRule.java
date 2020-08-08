package com.programme.Fortress.Function.Business.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("BNK_ALERT_RULE")
public class AlertRule {
    @TableId(value = "id", type = IdType.INPUT)
    private long id;
    /**
     * 规则类型
     */
    private String type;
    /**
     * 类型名称
     */
    private String typeName;
    /**
     * 规则
     */
    private int rule;
    /**
     * 操作
     */
    private int operate;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    /**
     * 开关
     */
    private String typeStart;
}
