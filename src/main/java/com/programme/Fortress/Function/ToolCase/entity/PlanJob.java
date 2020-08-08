package com.programme.Fortress.Function.ToolCase.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
@Data
@TableName("BNK_PLAN_JOB")
public class PlanJob implements Serializable {

    @TableId(value = "id", type = IdType.INPUT)
    private long id;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * cron表达式
     */
    private String cronExpression;

    /**
     * 服务名称
     */
    private String beanName;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 状态 1.启动 2.暂停
     */
    private Integer status;

    /**
     * 是否删除 0.否 1.是
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleteFlag;

    /**
     * 创建人id
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer creatorId;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;

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
