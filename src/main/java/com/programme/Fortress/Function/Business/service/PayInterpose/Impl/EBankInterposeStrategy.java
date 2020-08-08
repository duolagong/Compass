package com.programme.Fortress.Function.Business.service.PayInterpose.Impl;

import com.alibaba.fastjson.JSONObject;
import com.programme.Fortress.Function.Business.entity.PayInfor;
import com.programme.Fortress.Function.Business.service.PayInterpose.PayInterposeStrategy;
import com.programme.Fortress.Function.ToolCase.entity.ResultBean;
import com.programme.Fortress.Function.ToolCase.service.FormatConService;
import com.programme.Fortress.Other.Exception.ResultUtil;
import com.programme.Fortress.Other.Feign.ToEBANK;
import com.programme.Fortress.Other.Feign.ToNBKOSB;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EBankInterposeStrategy implements PayInterposeStrategy {

    @Autowired
    private FormatConService formatConService;
    @Autowired
    private ToNBKOSB nbkosb;
    @Autowired
    private ToEBANK nbk;

    @Override
    public ResultBean confirmIntermediateState(PayInfor payInfor) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ordernum",payInfor.getOrdernum());
        jsonObject.put("version",payInfor.getVersion());
        jsonObject.put("trandate",payInfor.getTrandate());
        String resultAction = nbk.paymentResultAction(jsonObject.toJSONString());
        JSONObject resultJson = JSONObject.parseObject(resultAction);
        if(payInfor.getOrdernum().equals(resultJson.getString("ordernum"))){
            if (resultJson.getString("respcode").startsWith("P")) return ResultUtil.fail(resultJson.getString("respmsg"));
            if (resultJson.getString("respcode").startsWith("M")) {
                resultJson.put("respcode", "E0001");
                resultJson.put("respmsg", "判断指令发送失败,请重新支付");
            }
            try {
                String asXML = formatConService.makeMessage1031(payInfor.getVersion(), payInfor.getSubbranchid(), payInfor.getAgentid(),
                        resultJson.getString("respcode"), resultJson.getString("respmsg"), resultJson.getString("ordernum"));
                return ResultUtil.success(null,nbkosb.sendOSBJMS(asXML));
            }catch (Exception e){
                log.error("向网银OSB同步1031状态失败",e);
                return ResultUtil.fail("向网银OSB同步1031状态失败:"+e.getMessage());
            }
        }else {
            return ResultUtil.fail("同步资管状态,反馈交易号不一致！");
        }
    }
}
