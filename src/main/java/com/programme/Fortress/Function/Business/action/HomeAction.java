package com.programme.Fortress.Function.Business.action;

import com.alibaba.fastjson.JSONObject;
import com.programme.Fortress.Function.Business.dao.BankIdMapper;
import com.programme.Fortress.Function.Business.entity.BankId;
import com.programme.Fortress.Function.Business.service.HomeService;
import com.programme.Fortress.Util.Redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(value = "/Home")
public class HomeAction {

    @Autowired
    private HomeService homeService;
    @Autowired
    private BankIdMapper bankIdMapper;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 监控首页
     * @param model
     * @return
     */
    @GetMapping
    public String HomeMonitor(Model model){
        Map homeMonitor = homeService.getHomeMonitor();
        log.info("监控首页数据展示[{}]",homeMonitor);
        model.addAttribute("visits",homeMonitor.get("visits"));
        model.addAttribute("business",homeMonitor.get("business"));
        model.addAttribute("unprocessed",homeMonitor.get("unprocessed"));
        model.addAttribute("sysPressure",homeMonitor.get("sysPressure"));
        return "menu/Home";
    }

    /**
     * 七日走势
     * @return
     */
    @GetMapping(value = "/weeklyTrad")
    public @ResponseBody JSONObject weeklyTradShow(){
        return homeService.weeklyTradShow();
    }

    /**
     * 监控首页-饼形图（公私比例）
     * @return
     */
    @GetMapping(value = "/personFlag")
    public @ResponseBody String personFlagShow(){
        return homeService.personFlagShow();
    }

    /**
     * 监控首页-折线图（系统流量）
     * @return
     */
    @GetMapping(value = "/interFaceflow")
    public @ResponseBody JSONObject interFaceflowShow(){
        return homeService.interFaceflowShow();
    }

    @GetMapping(value = "test")
    public @ResponseBody String setRedis(){
        List<BankId> bankIds = bankIdMapper.selectList(null);
        for (BankId bankId : bankIds){
            HashMap<Object, Object> hashMap = new HashMap<>();
            hashMap.put("bank",bankId.getBank());
            hashMap.put("name",bankId.getBankname());
            boolean b = redisUtil.lSet("ATOM:ICBC:ACCT", hashMap,true);
            log.info("结果[{}]"+b);
        }
        List<Object> objectList = redisUtil.lGet("ATOM:ICBC:ACCT", 0, -1);
        String jsonString = JSONObject.toJSONString(objectList);
        return jsonString;
    }

}
