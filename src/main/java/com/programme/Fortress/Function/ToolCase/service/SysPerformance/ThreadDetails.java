package com.programme.Fortress.Function.ToolCase.service.SysPerformance;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ThreadDetails {
    private Integer total;
    private Long timestamp; //时间戳
    private ArrayList<ThreadInfo> threadInfo;
}
