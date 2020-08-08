package com.programme.Fortress.Function.Business.action;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.programme.Fortress.Function.Business.dao.BankIdMapper;
import com.programme.Fortress.Function.Business.dao.CompanyNumMapper;
import com.programme.Fortress.Function.Business.dao.PayInforMapper;
import com.programme.Fortress.Function.Business.entity.BankId;
import com.programme.Fortress.Function.Business.entity.CompanyNum;
import com.programme.Fortress.Function.Business.entity.PayInfor;
import com.programme.Fortress.Function.Business.entity.ReturnData;
import com.programme.Fortress.Function.Business.service.PayInforService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
public class PayInforAction {

    @Autowired
    private PayInforService payInforService;
    @Autowired
    private CompanyNumMapper companyNumMapper;
    @Autowired
    private BankIdMapper bankIdMapper;
    @Autowired
    private PayInforMapper payInforMapper;

    /**
     * 菜单
     */
    @GetMapping(value = "/PayInfor")
    public String PayInfor(Model model){
        List<CompanyNum> versions = companyNumMapper.selectList(new QueryWrapper<CompanyNum>().isNotNull("f_mbdz").or().eq("f_qybh","21"));
        List<BankId> bankIds = bankIdMapper.selectList(null);
        model.addAttribute("versions",versions);
        model.addAttribute("agentids",bankIds);
        return "menu/PayInfor";
    }

    @GetMapping(value = "/PayInforDetails")
    public String PayInforDetails(Model model){
        PayInfor payInfor = payInforService.getOrdernumInfor("562010700004");
        model.addAttribute("payInfor",payInfor);
        return "menu/PayInforDetails";
    }

    /**
     *获取指定Ordernum的交易信息
     */
    @GetMapping(value = "/payInfor/{ordernum}")
    public String getOrderNumInfor(@PathVariable("ordernum") String ordernum ,Model model){
        PayInfor payInfor = payInforService.getOrdernumInfor(ordernum);
        model.addAttribute("payInfor",payInfor);
        return "menu/PayInfor::payInforDetails";//更新局部
    }

    @GetMapping(value = "/payInfor/TranXml/{ordernum}")
    public String getTranXml(@PathVariable("ordernum") String ordernum,Model model){
        model.addAttribute("tranXml",payInforService.getTranXml(ordernum));
        return "menu/PayInfor::payInforTranXml";//更新局部
    }

    /**
     * 依据条件获取
     */
    @PostMapping(value = "/payInfor")
    @ResponseBody
    public String getPayInfor(@RequestBody PayInfor payInfor){
        if(payInfor.getDateFrom()==null || "".equals(payInfor.getDateFrom())) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String format = timeFormatter.format(now);
            payInfor.setDateFrom(format);
            payInfor.setDateTo(format);
        }
        ReturnData<PayInfor> payInforList = payInforService.getPayInfor(payInfor);
        return JSON.toJSONString(payInforList);
    }
}
