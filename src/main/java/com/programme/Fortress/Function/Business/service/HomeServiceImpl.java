package com.programme.Fortress.Function.Business.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.programme.Fortress.Function.Business.dao.CityMapper;
import com.programme.Fortress.Function.Business.dao.PayNoteMapper;
import com.programme.Fortress.Util.MyDateUtil;
import com.programme.Fortress.Util.Redis.RedisUtil;
import com.programme.Fortress.Util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class HomeServiceImpl implements HomeService{

    @Autowired
    private PayNoteService payNoteService;
    @Autowired
    private PayInforService payInforService;
    @Autowired
    private CityMapper cityMapper;
    @Resource
    private RedisUtil redisUtil;
    @Autowired
    private DiscoveryClient discoveryClient;

    @Override
    public Map getHomeMonitor(){
        HashMap<String, String> hashMap = new HashMap<>();
        //获取在途交易(网银区)
        Integer payProcess = payInforService.getPayProcess();
        hashMap.put("unprocessed",payProcess.toString());
        //获取系统内存
        hashMap.put("sysPressure",sysMemory());
        //获取系统交易量
        Integer tradVolume = payNoteService.getPayNoteCount(MyDateUtil.getMaxDate("yyyyMMdd"));
        hashMap.put("business",tradVolume.toString());
        //获取系统访问量
        hashMap.put("visits","30000");
        return hashMap;
    }

    public String sysMemory(){
        long useMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();
        double d = (useMemory* 1.0/ totalMemory);
        return String.format("%.2f", d) + "%";
    }

    @Override
    public JSONObject flowMapShow() {
        JSONObject jsonObj=new JSONObject();
        Map map=new HashMap();
        //获取城市坐标
        Map<Object, Object> cityPointMap = redisUtil.hmget("COM:CITY:POINT");
        HashMap<String, Object> pointMap = new HashMap<>();
        for (Object key : cityPointMap.keySet()){
            String cityPoint = cityPointMap.get(key).toString();
            String[] split = cityPoint.split("\\,", 2);
            double[] doubles = new double[]{Double.parseDouble(split[0]),Double.parseDouble(split[1])};
            pointMap.put(key.toString(),doubles);
        }
        //获取付款笔数
        ArrayList<String> cityPayNumList = redisUtil.zSCAN("COM:CITY:PNUM");
        ArrayList<String> nameList = new ArrayList<>();
        ArrayList<Double> valueList = new ArrayList<>();
        /*for (int i = 0; i < cityPayNumList.size(); i++) {
            String cityPayNum = cityPayNumList.get(i);
            String[] cityPayNumSplit = cityPayNum.split("\\|", 2);
            *//*if("局部地区".equals(cityPayNumSplit[0])) continue;*//*
            nameList.add(i,cityPayNumSplit[0]);
            valueList.add(i,Double.parseDouble(cityPayNumSplit[1]));;
        }*/
        int counter=0;
        for (String cityPayNum : cityPayNumList){
            String[] cityPayNumSplit = cityPayNum.split("\\|", 2);
            if("局部地区".equals(cityPayNumSplit[0])) continue;
            nameList.add(counter,cityPayNumSplit[0]);
            valueList.add(counter,Double.parseDouble(cityPayNumSplit[1]));
            counter++;
        }
        //获取付款金额
        ArrayList<String> cityPayAmountList = redisUtil.zSCAN("COM:CITY:PAMOUNT");
        /*map.put("BJsheng",arrayList);*/
        map.put("BJshi", nameList.toArray(new String[]{}));
        map.put("BJche", valueList.toArray(new Double[]{}));
        map.put("Target",pointMap);
        map.put("table",flowTableShow());
        jsonObj.putAll(map);
        log.info(jsonObj.toJSONString());
        return jsonObj;
    }

    public List<Map<String, Object>> flowTableShow(){
        List<Map<String, Object>> list = new ArrayList<>();
        int count = payNoteService.getPayNoteCount(MyDateUtil.getMaxDate("yyyyMMdd"));
        if(count==0) return list;
        Set<Object> cityPayNumSet = redisUtil.zGet("COM:CITY:PNUM", 0, 5);
        for(Object object:cityPayNumSet){
            Map<String, Object> objectMap = new HashMap<>();
            Object payNum = redisUtil.zItemGet("COM:CITY:PNUM", object.toString());
            objectMap.put("city",object.toString());
            int value = new Double(payNum.toString()).intValue();
            objectMap.put("paynum",value);
            Object payAmount = redisUtil.zItemGet("COM:CITY:PAMOUNT", object.toString());
            objectMap.put("payamount",Double.valueOf(payAmount.toString()));
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(2);
            objectMap.put("ratio",numberFormat.format((float) value / (float) count * 100)+"%");
            list.add(objectMap);
        }
        return list;
    }

    @Override
    public JSONObject weeklyTradShow() {
        JSONObject jsonObj=new JSONObject();
        HashMap<String, Object> weekTrad = payNoteService.getWeekTrad(MyDateUtil.getMaxDate("yyyy-MM-dd"));
        jsonObj.putAll(weekTrad);
        log.info(jsonObj.toJSONString());
        return jsonObj;
    }

    @Override
    public String personFlagShow() {
        List<Map<String,String>> personflag = payNoteService.getPersonflag(MyDateUtil.getMaxDate("yyyyMMdd"));
        String jsonString = JSON.toJSONString(personflag);
        return jsonString;
    }

    @Override
    public JSONObject interFaceflowShow() {
        JSONObject jsonObj=new JSONObject();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("legend",new String[]{"交易量", "查询量","接口访问量"});
        String nowDate = MyDateUtil.getMaxDate("yyyyMMdd");
        int[] payInts = new int[7];
        int[] queryInts = new int[7];
        int[] interfaceInts = new int[7];
        for (int i = 0; i < 7; i++) {
            String queryDate = MyDateUtil.getHistoryDate(nowDate, "yyyyMMdd", -i);
            Object payResult = redisUtil.hget("NOSB:BUSINESS:" + queryDate.substring(4), "PAY");
            payInts[6-i]=StringUtil.checkEmpty(payResult) ? 0: Integer.parseInt(payResult.toString());
            Object queryResult = redisUtil.hget("NOSB:BUSINESS:" + queryDate.substring(4), "QUERY");
            queryInts[6-i]=StringUtil.checkEmpty(queryResult) ? 0: Integer.parseInt(queryResult.toString());
            Map<Object, Object> interfaceMap = redisUtil.hmget("NOSB:INTERFACE:" + queryDate.substring(4));
            int interfaceCounts=0;
            for (Object key : interfaceMap.keySet()){
                int count=StringUtil.checkEmpty(interfaceMap.get(key)) ? 0: Integer.parseInt(interfaceMap.get(key).toString());
                interfaceCounts=interfaceCounts+count;
            }
            interfaceInts[6-i]=interfaceCounts;
        }
        hashMap.put("交易量",payInts);
        hashMap.put("查询量",queryInts);
        hashMap.put("接口访问量",interfaceInts);
        jsonObj.putAll(hashMap);
        return jsonObj;
    }
}
