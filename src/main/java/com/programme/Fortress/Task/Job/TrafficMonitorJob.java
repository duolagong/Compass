package com.programme.Fortress.Task.Job;

import com.alibaba.fastjson.JSONObject;
import com.programme.Fortress.Function.ToolCase.service.TrafficMonitor.NBKOSBVisits;
import com.programme.Fortress.Other.Feign.ToNBKOSB;
import com.programme.Fortress.Util.MyDateUtil;
import com.programme.Fortress.Util.Redis.RedisUtil;
import com.programme.Fortress.Util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.DateUtils;

import java.util.concurrent.TimeUnit;

/**
 * 日访问量汇总(ZSET)
 * NOSB:VISITS:DAY:MMDD:
 *      PAY  支付量
 *      QUERY   查询量
 *      INTERFACE    接口调用
 * 24小时访问量汇总(LIST)
 * NOSB:VISITS:MINUTE:
 *      {PAY,QUERY,INTERFACE}
 *      ...
 */
@Slf4j
@Component("TrafficMonitorJob")
public class TrafficMonitorJob {

    @Autowired
    private ToNBKOSB nbkosb;
    @Autowired
    private RedisUtil redisUtil;

    public void execute() {
        log.info("开始获取网银OSB系统访问量");
        try {

            long timeMillis = System.currentTimeMillis(); //获取时间戳
            String nowDay = MyDateUtil.getMaxDate("yyyyMMdd");
            //设置日访问量的KEY值
            String dayVisits = "NOSB:VISITS:DAY:" + nowDay;
            String osbVisits = nbkosb.getNbkOSBVisits(timeMillis);
            log.info("距离上阶段OSB的系统访问量[{}]", osbVisits);
            NBKOSBVisits nbkosbVisits = JSONObject.parseObject(osbVisits, NBKOSBVisits.class);
            if (redisUtil.hasKey("NOSB:VISITS:MINUTE") && redisUtil.lGetListSize("NOSB:VISITS:MINUTE") >= 1440) {
                redisUtil.lRemove("NOSB:VISITS:MINUTE", false);
            }
            redisUtil.lSet("NOSB:VISITS:MINUTE", nbkosbVisits, TimeUnit.HOURS.toSeconds(24),true);
            log.info("已将OSB当前阶段的系统访问量缓存");
            //处理查询量的相关数据
            Double queryCount = nbkosbVisits.getQueryCount().doubleValue();
            if (queryCount != 0) {
                if (StringUtil.checkEmpty(redisUtil.zItemGet(dayVisits, "QUERY"))) {
                    redisUtil.zSet(dayVisits, "QUERY", queryCount);
                    redisUtil.expire(dayVisits, TimeUnit.DAYS.toSeconds(8));
                } else {
                    redisUtil.zAdd(dayVisits, "QUERY", queryCount);
                }
                log.info("已将OSB日查询量的相关数据缓存[{}]", queryCount);
            } else {
                log.info("未查询到OSB日查询量，无需缓存处理");
            }
            //处理接口访问量的相关数据
            Double interfaceCount = nbkosbVisits.getInterfaceCount().doubleValue();
            if (interfaceCount != 0) {
                if (StringUtil.checkEmpty(redisUtil.zItemGet(dayVisits, "INTERFACE"))) {
                    redisUtil.zSet(dayVisits, "INTERFACE", interfaceCount);
                    redisUtil.expire(dayVisits,TimeUnit.DAYS.toSeconds(8));
                } else {
                    redisUtil.zAdd(dayVisits, "INTERFACE", interfaceCount);
                }
                log.info("已将OSB日接口访问量的相关数据缓存[{}]", interfaceCount);
            } else {
                log.info("未查询到OSB日接口访问量，无需缓存处理");
            }
        } catch (Exception e) {
            log.error("获取网银OSB系统访问量发生异常", e);
        }
        log.info("结束获取网银OSB系统访问量");
    }
}
