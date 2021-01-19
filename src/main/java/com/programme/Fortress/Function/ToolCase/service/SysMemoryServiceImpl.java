package com.programme.Fortress.Function.ToolCase.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.programme.Fortress.Function.ToolCase.entity.ResultBean;
import com.programme.Fortress.Function.ToolCase.service.SysPerformance.MemoryInfo;
import com.programme.Fortress.Other.Exception.ResultUtil;
import com.programme.Fortress.Other.Feign.ToNBKOSB;
import com.programme.Fortress.Util.Redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SysMemoryServiceImpl implements SysMemoryService {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ToNBKOSB nbkosb;

    @Override
    public ResultBean getSysMemoryInit() {
        List<Object> memoryList = redisUtil.lGet("NOSB:PERF:MEMORY", 0, -1);
        if(memoryList.size()== 0) return ResultUtil.fail("当前未检测到数据，请检查本功能是否开启");
        ArrayList useMemoryList = new ArrayList();
        ArrayList utilizationsList = new ArrayList();
        JSONObject jsonObject = new JSONObject();
        for (int i = 0; i < memoryList.size(); i++) {
            MemoryInfo memoryInfo = (MemoryInfo) memoryList.get(i);
            Long timestamp = memoryInfo.getTimestamp();
            Double utilization = memoryInfo.getUtilization();
            //填充初始时间和结束时间
            if(i == 0) jsonObject.put("startTime", timestamp);
            if(i == memoryList.size() - 1) jsonObject.put("endTime", timestamp);
            //生成返回给前台的数据
            long[] useMemorys = {memoryInfo.getTimestamp(), memoryInfo.getUseMemory()/1024/1024};
            long[] utilizations = {memoryInfo.getTimestamp(), memoryInfo.getUtilization().longValue()};
            HashMap<Object, Object> useMemoryMap = new HashMap<>();
            HashMap<Object, Object> utilizationMap = new HashMap<>();
            useMemoryMap.put("value", useMemorys);
            utilizationMap.put("value", utilizations);
            useMemoryList.add(useMemoryMap);
            utilizationsList.add(utilizationMap);
        }
        jsonObject.put("freeMemoryData", useMemoryList);
        jsonObject.put("utilizationsData", utilizationsList);
        return ResultUtil.success(jsonObject);
    }

    @Override
    public ResultBean getFreeMemory(Long timestamp) {
        long size = redisUtil.lGetListSize("NOSB:PERF:MEMORY");
        if (size==0) return ResultUtil.fail("当前无网银OSB空闲内存信息，请检查是否开启任务!");
        List<Object> memoryList = redisUtil.lGet("NOSB:PERF:MEMORY", size-1, size);
        MemoryInfo memoryInfo =(MemoryInfo) memoryList.get(0);
        long[] strings = {timestamp, memoryInfo.getFreeMemory()/1024/1024};
        log.info("返回给前端{}", strings);
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("value", strings);
        return ResultUtil.success(hashMap);
    }

    @Override
    public ResultBean getUseMemory(Long timestamp) {
        long size = redisUtil.lGetListSize("NOSB:PERF:MEMORY");
        if (size==0) return ResultUtil.fail("当前无网银OSB内存使用信息，请检查是否开启任务!");
        List<Object> memoryList = redisUtil.lGet("NOSB:PERF:MEMORY", size-1, size);
        MemoryInfo memoryInfo =(MemoryInfo) memoryList.get(0);
        long[] strings = {timestamp, memoryInfo.getUseMemory()/1024/1024};
        log.info("返回给前端{}", strings);
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("value", strings);
        return ResultUtil.success(hashMap);
    }

    /**
     * 读取内存使用率
     *
     * @param timestamp
     * @return
     */
    @Override
    public ResultBean getMemoryUtilization(Long timestamp) {
        long size = redisUtil.lGetListSize("NOSB:PERF:MEMORY");
        if (size==0) return ResultUtil.fail("当前无网银OSB内存使用率信息，请检查是否开启任务!");
        List<Object> memoryList = redisUtil.lGet("NOSB:PERF:MEMORY", size - 1, size);
        MemoryInfo memoryInfo = (MemoryInfo)memoryList.get(0);
        long[] strings = {timestamp, memoryInfo.getUtilization().longValue()};
        log.info("返回给前端{}", strings);
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("value", strings);
        return ResultUtil.success(hashMap);
    }

    /**
     * 读取线程信息
     *
     * @param timestamp
     * @return
     */
    @Override
    public ResultBean getThreadDetails(Long timestamp) {
        return null;
    }

    /**
     * 异步获取网银OSB内存信息
     * @param timestamp
     * @return
     */
    @Async(value = "myAsyncPoolTaskExecutor")
    @Override
    public Future<JSONObject> getNBKOSBMemory(Long timestamp) {
        return new AsyncResult<JSONObject>(JSON.parseObject(nbkosb.getNbkOSBMemory(timestamp)));
    }

    /**
     * 异步获取网银OSB线程信息
     * @param timestamp
     * @return
     */
    @Async(value = "myAsyncPoolTaskExecutor")
    @Override
    public Future<JSONObject> getNBKOSBThread(Long timestamp) {
        return new AsyncResult<JSONObject>(JSON.parseObject(nbkosb.getNbkOSBThread(timestamp)));
    }
}
