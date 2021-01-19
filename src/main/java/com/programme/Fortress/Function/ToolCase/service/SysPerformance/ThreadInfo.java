package com.programme.Fortress.Function.ToolCase.service.SysPerformance;

import lombok.Data;

@Data
public class ThreadInfo {

    private Long threadId;  //线程ID
    private String threadName;  //线程名称
    private String start;   //线程状态
}
