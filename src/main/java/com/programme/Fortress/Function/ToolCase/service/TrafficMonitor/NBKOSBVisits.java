package com.programme.Fortress.Function.ToolCase.service.TrafficMonitor;

import lombok.Data;

@Data
public class NBKOSBVisits {

    private Long timestamp; //时间戳
    //private Long payCount; //交易量
    private Long queryCount; //查询量
    private Long interfaceCount; //接口访问量
}
