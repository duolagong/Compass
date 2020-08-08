package com.programme.Fortress.Function.BankTrade.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.programme.Fortress.Function.BankTrade.entity.BankPayment;
import com.programme.Fortress.Function.BankTrade.service.AcctBalanceService;
import com.programme.Fortress.Function.BankTrade.service.BankPaymentService;
import com.programme.Fortress.Function.Business.dao.BankIdMapper;
import com.programme.Fortress.Function.Business.entity.BankId;
import com.programme.Fortress.Function.Business.entity.PayInfor;
import com.programme.Fortress.Function.Business.entity.PayNote;
import com.programme.Fortress.Util.MyDateUtil;
import com.programme.Fortress.Util.StringUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class BankPaymentAction {

    @Autowired
    private BankPaymentService bankPaymentService;
    @Autowired
    private BankIdMapper bankIdMapper;

    @GetMapping(value = "/BankPayment")
    public String BankPayment(Model model){
        List<BankId> bankIds = bankIdMapper.selectList(new QueryWrapper<BankId>().ne("id","Z01"));
        model.addAttribute("agentids",bankIds);
        return  "menu/BankPayment";
    }

    @PostMapping(value = "/bankPayment")
    @ResponseBody
    public String bankPayment(@RequestBody Map queryMap){
        if(StringUtil.checkEmpty(queryMap.get("dateFrom"))) {
            String nowDate = MyDateUtil.getMaxDate("yyyy-MM-dd");
            queryMap.put("dateFrom",nowDate);
            queryMap.put("dateTo",nowDate);
        }else {
            queryMap.put("dateFrom",queryMap.get("dateFrom").toString());
            queryMap.put("dateTo",queryMap.get("dateTo").toString());
        }
        return bankPaymentService.getBankPayment(queryMap);
    }

    /**
     * 测试单页面模板时使用，模拟数据
     * @param model
     * @return
     */
    @GetMapping(value = "/BankPaymentDetails")
    public String PayInforDetails(Model model){
        BankPayment bankPayment = bankPaymentService.getOrdernumInfor("551900300025","1");
        model.addAttribute("bankPayment",bankPayment);
        return "menu/BankPaymentDetails";
    }

    /**
     *获取指定Ordernum的交易信息
     */
    @GetMapping(value = "/bankPayment/{ordernum}/{payresultiswait}")
    public String getOrderNumInfor(@PathVariable("ordernum") String ordernum ,@PathVariable("payresultiswait") String payresultiswait, Model model){
        BankPayment bankPayment = bankPaymentService.getOrdernumInfor(ordernum,payresultiswait);
        model.addAttribute("bankPayment",bankPayment);
        return "menu/BankPayment::bankPaymentDetails";//更新局部
    }

    @GetMapping(value = "/bankPayment/TranXml/{ordernum}/{payresultiswait}")
    public String getTranXml(@PathVariable("ordernum") String ordernum,@PathVariable("payresultiswait") String payresultiswait,Model model){
        HashMap xmlMap = bankPaymentService.getTranXml(ordernum,payresultiswait);
        model.addAttribute("tranXml",xmlMap.get("TRANXML"));
        model.addAttribute("tranSynXml",xmlMap.get("TRANSYNXML"));
        model.addAttribute("bankXml",xmlMap.get("BANKXML"));
        model.addAttribute("bankSynXml",xmlMap.get("BANKSYNXML"));
        return "menu/BankPayment::bankPaymentTranXml";//更新局部
    }

    @PostMapping(value = "/bankPayment/Result")
    @ResponseBody
    public String getQuery1030(@RequestBody Map queryMap){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("resultXml",bankPaymentService.getResultQuery(queryMap));
        return jsonObject.toJSONString();
    }
}
