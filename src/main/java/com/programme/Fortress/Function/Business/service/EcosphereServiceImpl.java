package com.programme.Fortress.Function.Business.service;

import com.alibaba.fastjson.JSONObject;
import com.programme.Fortress.Util.Redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class EcosphereServiceImpl implements EcosphereService{

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public String getEcosphere(){
        Map<Object, Object> radarMap = redisUtil.hmget("COM:RADAR");
        Map<Object, Object> ecosphere = redisUtil.hmget("COM:CIRCLE");
        //拼装流向数据
        List<Map<String, String>> linkList = new ArrayList<>();//流向数据
        List<Map<String, String>> nodeList = new ArrayList<>();//流向信息
        for (Object key:radarMap.keySet()){
            int value = (int)radarMap.get(key.toString());
            HashMap<String, String> linkMap = new HashMap<>();
            linkMap.put("source",key.toString());
            linkMap.put("target","注册中心");
            linkList.add(linkMap);
            HashMap<String, String> nodeMap = new HashMap<>();
            nodeMap.put("name",key.toString());
            nodeMap.put("img",key.toString());
            if(value>0) nodeMap.put("alarm",""+value);
            Object address = ecosphere.get(key.toString());
            nodeMap.put("address",address==null ? "无地址" : address.toString());
            nodeList.add(nodeMap);
        }
        JSONObject json = new JSONObject();
        json.put("links",linkList);
        json.put("nodes",nodeList);
        /*json.put("textarea","");//表格要展示的数据（）*/
        return json.toJSONString();
    }
}
