package com.programme.Fortress.Function.Business.action;

import com.alibaba.fastjson.JSONObject;
import com.programme.Fortress.Function.Business.service.PaymentFlow.PaymentFlowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class PaymentFlowAction {

    @Autowired
    private PaymentFlowService paymentFlowService;

    @GetMapping(value = "/PaymentFlow")
    public String FlowMap(Model model){
        model.addAttribute("msg","");
        return "menu/PaymentFlow";
    }

    /**
     * 汇款流向(普通版)
     * @return
     */
    /*@GetMapping(value = "/flowMap")
    public @ResponseBody JSONObject flowMapShow(){
        return homeService.flowMapShow();
    }*/

    /**
     *  汇款流向
     * @return
     */
    @GetMapping(value = "/paymentFlow")
    @ResponseBody
    public JSONObject getPaymentFlow(){
        JSONObject todayPaymentFlow = paymentFlowService.getTodayPaymentFlow();
        return todayPaymentFlow;
    }
}
