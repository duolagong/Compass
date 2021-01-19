package com.programme.Fortress.Task.Job;

import com.alibaba.fastjson.JSONObject;
import com.programme.Fortress.Function.ToolCase.service.SysMemoryService;
import com.programme.Fortress.Function.ToolCase.service.SysPerformance.ThreadDetails;
import com.programme.Fortress.Util.Redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
@Component("ThreadDetailsJob")
public class ThreadDetailsJob {

    @Autowired
    private SysMemoryService sysMemoryService;
    @Autowired
    private RedisUtil redisUtil;

    public void execute() {
        try {
            log.info("开始获取网银OSB系统线性信息");
            long timeMillis = System.currentTimeMillis();
            Future<JSONObject> nbkosbThread = sysMemoryService.getNBKOSBThread(timeMillis);
            JSONObject threads = nbkosbThread.get();
            log.info("获取到的网银OSB系统线程信息[{}]", threads.toJSONString());
            ThreadDetails threadDetails = threads.toJavaObject(ThreadDetails.class);
            if(redisUtil.lGetListSize("NOSB:PERF:THREAD") >=30){
                redisUtil.lRemove("NOSB:PERF:THREAD",false);
            }
            redisUtil.lSet("NOSB:PERF:THREAD",threadDetails,true);
            log.info("网银OSB系统线程信息缓存已完成");
        } catch (Exception e) {
            log.error("获取网银OSB系统线程信息发生异常",e);
        }
    }
}
