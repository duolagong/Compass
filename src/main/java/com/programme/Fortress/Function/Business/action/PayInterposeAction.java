package com.programme.Fortress.Function.Business.action;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.programme.Fortress.Function.Business.dao.BankIdMapper;
import com.programme.Fortress.Function.Business.dao.CompanyNumMapper;
import com.programme.Fortress.Function.Business.entity.*;
import com.programme.Fortress.Function.Business.service.PayInterpose.PayInterposeService;
import com.programme.Fortress.Util.MyDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
public class PayInterposeAction {

    @Autowired
    private CompanyNumMapper companyNumMapper;
    @Autowired
    private BankIdMapper bankIdMapper;
    @Autowired
    private PayInterposeService payInterposeService;

    /**
     * 菜单
     */
    @GetMapping(value = "/PayInterpose")
    public String PayInterpose(Model model) {
        List<CompanyNum> versions = companyNumMapper.selectList(new QueryWrapper<CompanyNum>().isNotNull("f_mbdz").or().eq("f_qybh", "21"));
        model.addAttribute("versions", versions);
        List<BankId> bankIds = bankIdMapper.selectList(null);
        model.addAttribute("agentids", bankIds);
        return "menu/PayInterpose";
    }

    /**
     * 添加人工干预数据
     *
     * @param payInfor
     * @return
     */
    @PostMapping(value = "/payInterpose")
    @ResponseBody
    public Object addPayInterpose(@RequestBody PayInfor payInfor, @CookieValue(value = "COMPASS_TOKEN", required = true) String cookie) {
        return payInterposeService.addPayInterpose(payInfor, cookie);
    }

    /**
     * 修改付款交易状态
     *
     * @param payInterpose
     * @return
     */
    @PutMapping(value = "/payInterpose")
    @ResponseBody
    public Object upDatePayInterpose(@RequestBody PayInterpose payInterpose) {
        return payInterposeService.upDatePayInterpose(payInterpose);
    }

    /**
     * 依据条件获取
     */
    @PostMapping(value = "/payInterpose/Table")
    @ResponseBody
    public String getPayInterpose(@RequestBody PayInfor payInfor, @CookieValue(value = "COMPASS_TOKEN", required = true) String cookie) {
        if (payInfor.getDateFrom() == null || "".equals(payInfor.getDateFrom())) {
            payInfor.setDateFrom(MyDateUtil.getMaxDate("yyyy-MM-dd"));
            payInfor.setDateTo(MyDateUtil.getMaxDate("yyyy-MM-dd"));
        }
        ReturnData<PayInterpose> payInforList = payInterposeService.getPayInterpose(payInfor, cookie);
        return JSON.toJSONString(payInforList);
    }

    @GetMapping(value = "/payInterpose/{ordernum}")
    @ResponseBody
    public Object getPayResult(@PathVariable("ordernum") String ordernum) {
        return payInterposeService.getPayResult(ordernum);
    }
}
