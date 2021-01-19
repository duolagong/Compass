package com.programme.Fortress.Function.Business.action;

import com.alibaba.fastjson.JSONObject;
import com.programme.Fortress.Function.Business.service.ErpInfor.ErpInforService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@Slf4j
@Controller
@RequestMapping(value = "/TradeAnalysis")
public class TradeAnalysisAction {

    @Autowired
    private ErpInforService erpInforService;

    /**
     * 汇款时段
     * @param model
     * @return
     */
    @GetMapping
    public String TradeAnalysis(Model model){
        model.addAttribute("msg","");
        return "menu/TradeAnalysis";
    }

    @GetMapping(value = "/{queryDate}",produces = {"application/json"})
    public @ResponseBody JSONObject interFaceflowShow(@PathVariable("queryDate") String queryDate){
        JSONObject jsonObj=new JSONObject();
        HashMap<String, Object> hashMap = erpInforService.getPayTimeDistribution(queryDate);
        jsonObj.putAll(hashMap);
        log.info(jsonObj.toJSONString());
        return jsonObj;
    }
}
