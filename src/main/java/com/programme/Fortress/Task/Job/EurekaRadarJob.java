package com.programme.Fortress.Task.Job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.programme.Fortress.Function.Business.dao.AlertRuleMapper;
import com.programme.Fortress.Function.Business.entity.AlertRule;
import com.programme.Fortress.Util.Remind.SMSAlert;
import com.programme.Fortress.Util.Eureka.EurekaUtil;
import com.programme.Fortress.Util.Redis.RedisUtil;
import com.programme.Fortress.Util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("EurekaRadarJob")
public class EurekaRadarJob {

    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private AlertRuleMapper alertRuleMapper;
    @Autowired
    private SMSAlert smsAlert;

    public void execute(){
        log.info("生态网扫描——获取历史在线名单");
        Map<Object, Object> lastMap = redisUtil.hmget("COM:RADAR");
        log.info("生态网扫描——扫描生态网在线系统名单");
        List<String> inList = discoveryClient.getServices();
        log.info("开始检测是否存在系统丢失");
        for (Object key : lastMap.keySet()){
            if("compass".equals(key.toString())) continue;//不对本身进行校验
            int value = (int)redisUtil.hget("COM:RADAR", key.toString());
            if(!inList.contains(key)){
                log.info("生态网警告:[{}]系统丢失",key);
                redisUtil.hincr("COM:RADAR",key.toString(),1);
                if(redisUtil.hHasKey("COM:ALERT", key.toString())) continue;
                //获取该系统预警规则
                AlertRule rule = alertRuleMapper.selectOne(new QueryWrapper<AlertRule>().eq("type", key.toString()));
                if(StringUtil.checkEmpty(rule)){//如果没维护该系统的预警规则，系统自动维护一条
                    AlertRule alertRule = new AlertRule();
                    alertRule.setId(alertRuleMapper.getDual());
                    alertRule.setType(key.toString());
                    alertRule.setTypeName("SYS_"+key.toString());
                    alertRule.setRule(30);//默认30分钟
                    alertRule.setOperate(1);//默认短信预警
                    alertRule.setTypeStart("1");//"1"启用，"0"不启用
                    int insert = alertRuleMapper.insert(alertRule);
                    continue;//维护完成后跳出本次循环
                }else if("0".equals(rule.getTypeStart())){
                    continue;//未启用也不进行预警，直接跳出本次循环
                }
                if (value > rule.getRule()){
                    log.info("系统预警,[{}]系统已确认失联！",key);
                    log.info("向管理员反映[{}]系统失联问题!",key);
                    //异步操作
                    smsAlert.AssemblyAlertSMS(key.toString(),key+"系统失联,请检查!",true);
                    /*redisUtil.hset("Alert",key.toString(), LocalDateTime.now().toString());//加入提醒*/
                    log.info("已将[{}]系统踢出生态网！",key);
                }
            }else if(value > 0){
                log.info("[{}]系统已经上线，准备清空该系统异常累计数",key);
                redisUtil.hdel("COM:ALERT",key.toString());
                redisUtil.hset("COM:RADAR",key.toString(),0);
                log.info("[{}]系统的异常累计数已清空",key);
            }
        }
        log.info("开始检测是否存在新入网系统");
        for (String key :inList){
            if("compass".equals(key)) continue;//不对本身进行校验
            if(!lastMap.containsKey(key)){
                log.info("[{}]系统申请加入生态网",key);
                redisUtil.hset("COM:RADAR",key,0);
                log.info("已批准[{}]系统加入生态网",key);
                List<ServiceInstance> instances = discoveryClient.getInstances(key);
                redisUtil.hset("COM:CIRCLE", key, instances.get(0).getUri());
                log.info("[{}]系统完成生态网登记", key);
            }
        }
        log.info("完成生态网扫描");
    }
}
