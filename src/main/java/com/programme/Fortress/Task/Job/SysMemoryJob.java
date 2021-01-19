package com.programme.Fortress.Task.Job;

import com.alibaba.fastjson.JSONObject;
import com.programme.Fortress.Function.ToolCase.service.SysMemoryService;
import com.programme.Fortress.Function.ToolCase.service.SysPerformance.MemoryInfo;
import com.programme.Fortress.Function.ToolCase.service.SysPerformance.ThreadDetails;
import com.programme.Fortress.Function.ToolCase.service.SysPerformance.ThreadInfo;
import com.programme.Fortress.Other.Exception.ResultUtil;
import com.programme.Fortress.Other.Feign.ToNBKOSB;
import com.programme.Fortress.Util.Redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.rmi.runtime.Log;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component("SysMemoryJob")
public class SysMemoryJob {

    @Autowired
    private SysMemoryService sysMemoryService;
    @Autowired
    private RedisUtil redisUtil;

    public void execute() {
        log.info("开始获取网银OSB系统内存使用情况");
        long timeMillis = System.currentTimeMillis();
        try {
            Future<JSONObject> nbkosbMemory = sysMemoryService.getNBKOSBMemory(timeMillis);
            Future<JSONObject> nbkosbThread = sysMemoryService.getNBKOSBThread(timeMillis);
            //整理系统内存信息
            JSONObject memorys = nbkosbMemory.get();
            log.info("获取到的网银OSB系统内存[{}]", memorys.toJSONString());
            MemoryInfo memoryInfo = memorys.toJavaObject(MemoryInfo.class);
            Long totalMemory = memorys.getLong("totalMemory");
            Long maxMemory = memorys.getLong("maxMemory");
            Long freeMemory = memorys.getLong("freeMemory");
            Long useMemory = totalMemory - freeMemory;
            Double utilization = new BigDecimal(useMemory * 1.0 / maxMemory * 100).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
            log.info("当前网银OSB系统内存使用率：{}", utilization + "%");
            memoryInfo.setUseMemory(useMemory);
            memoryInfo.setUtilization(utilization);
            if(redisUtil.lGetListSize("NOSB:PERF:MEMORY") >= 60){
                redisUtil.lRemove("NOSB:PERF:MEMORY",false);
            }
            redisUtil.lSet("NOSB:PERF:MEMORY",memoryInfo, TimeUnit.HOURS.toSeconds(1),true);
            log.info("网银OSB系统内存信息缓存完成");
        } catch (Exception e) {
                log.error("入网系统性能状况获取发生异常",e);
        }
    }
}
