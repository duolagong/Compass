package com.programme.Fortress.Task;

import java.io.Serializable;

/**
 * 定时任务枚举
 */
public enum JobOperateEnum {
    START(1, "启动"),
    PAUSE(2, "暂停"),
    DELETE(3, "删除"),
    UPDATE(4,"修改");

    private final Integer value;
    private final String desc;

    JobOperateEnum(final Integer value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Serializable getValue() {
        return this.value;
    }

    // Jackson 注解为 JsonValue 返回中文 json 描述
    public String getDesc() {
        return this.desc;
    }

    public String getEnumName() {
        return name();
    }
}
