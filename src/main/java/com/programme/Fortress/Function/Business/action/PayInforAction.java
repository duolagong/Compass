package com.programme.Fortress.Function.Business.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.programme.Fortress.Function.Business.dao.BankIdMapper;
import com.programme.Fortress.Function.Business.dao.CompanyNumMapper;
import com.programme.Fortress.Function.Business.dao.PayInforMapper;
import com.programme.Fortress.Function.Business.entity.BankId;
import com.programme.Fortress.Function.Business.entity.CompanyNum;
import com.programme.Fortress.Function.Business.entity.PayInfor;
import com.programme.Fortress.Function.Business.entity.ReturnData;
import com.programme.Fortress.Function.Business.service.AcctPowerService;
import com.programme.Fortress.Function.Business.service.PayInfor.PayInforService;
import com.programme.Fortress.Other.Exception.ResultUtil;
import com.programme.Fortress.Util.MyDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
    @Autowired
    private AcctPowerService acctPowerService;

    /**
     * 菜单
     */
    @GetMapping(value = "/PayInfor")
    public String PayInfor(Model model) {
        List<CompanyNum> versions = companyNumMapper.selectList(new QueryWrapper<CompanyNum>().isNotNull("f_mbdz").or().eq("f_qybh", "21"));
        List<BankId> bankIds = bankIdMapper.selectList(null);
        List<HashMap> subbranchIds = acctPowerService.getSubbranchId();
        model.addAttribute("versions", versions);
        model.addAttribute("agentids", bankIds);
        model.addAttribute("subbranchIds", subbranchIds);
        return "menu/PayInfor/PayInfor";
    }

    @GetMapping(value = "/PayInforDetails")
    public String PayInforDetails(Model model) {
        PayInfor payInfor = payInforService.getOrdernumInfor("562010700004");
        model.addAttribute("payInfor", payInfor);
        return "menu/PayInfor/PayInforDetails";
    }

    /**
     * 获取指定Ordernum的交易信息
     */
    @GetMapping(value = "/payInfor/{ordernum}")
    public String getOrderNumInfor(@PathVariable("ordernum") String ordernum, Model model) {
        PayInfor payInfor = payInforService.getOrdernumInfor(ordernum);
        model.addAttribute("payInfor", payInfor);
        return "menu/PayInfor/PayInfor::payInforDetails";//更新局部
    }

    @GetMapping(value = "/payInfor/TranXml/{ordernum}")
    public String getTranXml(@PathVariable("ordernum") String ordernum, Model model) {
        model.addAttribute("tranXml", payInforService.getTranXml(ordernum));
        return "menu/PayInfor/PayInfor::payInforTranXml";//更新局部
    }


    /**
     * 依据条件获取
     */
    @PostMapping(value = "/payInfor")
    @ResponseBody
    public String getPayInfor(@RequestBody PayInfor payInfor) {
        payInfor = payInforService.fillPayInforDate(payInfor);
        ReturnData<PayInfor> payInforList = payInforService.getPayInfor(payInfor);
        return JSON.toJSONString(payInforList);
    }

    @PostMapping(value = "/payInfor/versionNumChart")
    @ResponseBody
    public Object getVersionNumChart(@RequestBody PayInfor payInfor) {
        payInfor = payInforService.fillPayInforDate(payInfor);
        return ResultUtil.success(payInforService.getVersionChart(payInfor, false), "绘制成功");
    }

    @PostMapping(value = "/payInfor/versionChart")
    @ResponseBody
    public Object getVersionChart(@RequestBody PayInfor payInfor) {
        payInfor = payInforService.fillPayInforAgentid(payInfor);
        if (payInfor.getDateFrom() == null || "".equals(payInfor.getDateFrom())) {
            payInfor = payInforService.fillPayInforDate(payInfor);
        } else {
            long dayDifference = MyDateUtil.getDayDifference(payInfor.getDateFrom(), payInfor.getDateTo());
            if (Math.abs(dayDifference) > 31) return ResultUtil.fail("目前设置的最大查询间隔为一个月!");
        }
        return ResultUtil.success(payInforService.getVersionChart(payInfor, true), "绘制成功");
    }

    @PostMapping(value = "/payInfor/personflagChart")
    @ResponseBody
    public Object getPersonflagChart(@RequestBody PayInfor payInfor) {
        payInfor = payInforService.fillPayInforAgentid(payInfor);
        if (payInfor.getDateFrom() == null || "".equals(payInfor.getDateFrom())) {
            payInfor = payInforService.fillPayInforDate(payInfor);
        } else {
            long dayDifference = MyDateUtil.getDayDifference(payInfor.getDateFrom(), payInfor.getDateTo());
            if (Math.abs(dayDifference) > 93) return ResultUtil.fail("目前设置的最大查询间隔为一个季度!");
        }
        return ResultUtil.success(payInforService.getPersonflagChart(payInfor), "绘制成功");
    }

    @PostMapping(value = "/payInfor/numTrendChart")
    @ResponseBody
    public Object getNumTrendChart(@RequestBody PayInfor payInfor) {
        payInfor = payInforService.fillPayInforDate(payInfor);
        return ResultUtil.success(payInforService.getPayDateTrendChart(payInfor, false), "绘制成功");
    }

    @PostMapping(value = "/payInfor/payDateTrendChart")
    @ResponseBody
    public Object getTotalTrendChart(@RequestBody PayInfor payInfor) {
        payInfor = payInforService.fillPayInforAgentid(payInfor);
        if (payInfor.getDateFrom() == null || "".equals(payInfor.getDateFrom())) {
            payInfor = payInforService.fillPayInforDate(payInfor);
            return ResultUtil.success(payInforService.getPayDateTrendChart(payInfor, false), "绘制成功");
        } else {
            long dayDifference = MyDateUtil.getDayDifference(payInfor.getDateFrom(), payInfor.getDateTo());
            if (Math.abs(dayDifference) > 31) return ResultUtil.fail("目前设置的最大查询间隔为一个月!");
            return ResultUtil.success(payInforService.getPayDateTrendChart(payInfor, true), "绘制成功");
        }
    }
}
