package com.programme.Fortress.Function.ToolCase.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import weblogic.messaging.ID;

import java.time.LocalDateTime;

@Data
@TableName("BNK_USER")
public class SysUser {
    @TableId(value = "Id", type = IdType.INPUT)
    private Long id;
    /**
     * 用户号
     */
    private String userId;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 微信编号
     */
    @TableField(value = "wechat",strategy = FieldStrategy.IGNORED)
    private String wechat;
    /**
     * 账户类型
     */
    private String userType;
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
    /**
     * 用户号
     */
    @TableField(value = "password",strategy = FieldStrategy.IGNORED)
    private String password;

}
