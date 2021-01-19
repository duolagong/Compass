package com.programme.Fortress.Function.ToolCase.service.SysPerformance;

import lombok.Data;

@Data
public class MemoryInfo {
    private Long timestamp; //时间戳
    private Long totalMemory;   //总内存
    private Long maxMemory; //最大使用内存
    private Long freeMemory;    //空闲的内存
    private Long useMemory; //使用的内存
    private Double utilization; //使用率
}
