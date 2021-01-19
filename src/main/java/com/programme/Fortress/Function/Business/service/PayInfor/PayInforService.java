package com.programme.Fortress.Function.Business.service.PayInfor;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.programme.Fortress.Function.Business.entity.PayInfor;
import com.programme.Fortress.Function.Business.entity.ReturnData;
import com.programme.Fortress.Function.ToolCase.entity.ResultBean;


public interface PayInforService extends IService<PayInfor> {

    PayInfor fillPayInforDate(PayInfor payInfor);

    PayInfor fillPayInforAgentid(PayInfor payInfor);

    ReturnData<PayInfor> getPayInfor(PayInfor payInfor);

    PayInfor getOrdernumInfor(String ordernum);

    String getTranXml(String ordernum);

    Integer getPayProcess();

    JSONObject getVersionChart(PayInfor payInfor, Boolean type);

    JSONObject getPersonflagChart(PayInfor payInfor);

    JSONObject getPayDateTrendChart(PayInfor payInfor, Boolean type);
}
