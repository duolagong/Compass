package com.programme.Fortress.Function.Business.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("BNK_PAY_INTERPOSE")
public class PayInterpose implements Serializable {
    @TableId(value = "ordernum", type = IdType.INPUT)
    private String ordernum;
    private String version;
    @TableField(value = "txcode")
    private String txcode;
    @TableField(value = "agentid")
    private String agentid;
    @TableField(value = "subbranchid")
    private String subbranchid;
    @TableField(value = "txamount")
    private String txamount;
    @TableField(value = "bankfinalcode",strategy = FieldStrategy.IGNORED)
    private String bankFinalCode;
    @TableField(value = "bankfinalmsg",strategy = FieldStrategy.IGNORED)
    private String bankFinalMsg;
    /**
     * 创建时间
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    /**
     * 提交人id
     */
    @TableField(strategy = FieldStrategy.IGNORED)
    private Integer submitId;
    /**
     * 提交人
     */
    @TableField(strategy = FieldStrategy.IGNORED)
    private String submitName;
    /**
     * 修改时间
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
}
