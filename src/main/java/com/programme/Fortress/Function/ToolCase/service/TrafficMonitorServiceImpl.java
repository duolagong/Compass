package com.programme.Fortress.Function.ToolCase.service;

import com.alibaba.fastjson.JSONObject;
import com.programme.Fortress.Function.ToolCase.entity.ResultBean;
import com.programme.Fortress.Function.ToolCase.service.TrafficMonitor.NBKOSBVisits;
import com.programme.Fortress.Other.Exception.ResultUtil;
import com.programme.Fortress.Util.Redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class TrafficMonitorServiceImpl implements TrafficMonitorService {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public ResultBean initTrafficMonitor() {
        JSONObject jsonObject = new JSONObject();
        try {
            List<Object> objectList = redisUtil.lGet("NOSB:VISITS:MINUTE", 0, -1);
            if(objectList.size()== 0) return ResultUtil.fail("当前未检测到数据，请检查本功能是否开启");
            ArrayList queryList = new ArrayList<>();
            ArrayList interfaceList = new ArrayList<>();
            //1440
            /*long timeMillis = System.currentTimeMillis();*/
            for (int i = 0; i < objectList.size(); i++) {
                NBKOSBVisits nbkosbVisits = (NBKOSBVisits) objectList.get(i);
                //生成返回给前台的数据
                if (i == 0) jsonObject.put("startTime", nbkosbVisits.getTimestamp());
                if (i == objectList.size() - 1) jsonObject.put("endTime", nbkosbVisits.getTimestamp());
                HashMap<String, Object> queryMap = new HashMap<>();
                long[] queryValue = {nbkosbVisits.getTimestamp(), nbkosbVisits.getQueryCount()};
                queryMap.put("value", queryValue);
                queryList.add(queryMap);
                HashMap<String, Object> interfaceMap = new HashMap<>();
                long[] interfaceValue = {nbkosbVisits.getTimestamp(), nbkosbVisits.getInterfaceCount()};
                interfaceMap.put("value", interfaceValue);
                interfaceList.add(interfaceMap);

                //下面是模拟数据
                /*int nextInt = new Random().nextInt(100);
                int nextInt2 = new Random().nextInt(100);
                long[] freeMemorys = {nbkosbVisits.getTimestamp(), nextInt};
                queryMap.put("value", freeMemorys);
                long[] freeMemorys2 = {nbkosbVisits.getTimestamp(), nextInt2};
                interfaceMap.put("value", freeMemorys2);
                queryList.add(queryMap);
                interfaceList.add(interfaceMap);*/
            }
            jsonObject.put("initData", queryList);
            jsonObject.put("initData2", interfaceList);
            /*jsonObject.put("startTime", timeMillis);
            jsonObject.put("endTime", timeMillis + 1439 * 60 * 1000);*/
        } catch (Exception e) {
            log.error("初始化访问量异常", e);
        }
        return ResultUtil.success(jsonObject);
    }
}
