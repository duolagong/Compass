package com.programme.Fortress.Function.ToolCase.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("BNK_SYS_MESSAGE")
public class SysMessage {

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    /**
     * 报文类型
     */
    private String type;
    /**
     * 类型来源
     */
    private String typeSource;
    /**
     * 类型名称
     */
    private String typeName;
    /**
     * 报文
     */
    private String message;
    /**
     * 报文格式
     */
    private String messageType;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
}
