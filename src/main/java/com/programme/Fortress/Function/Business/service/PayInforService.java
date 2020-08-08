package com.programme.Fortress.Function.Business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.programme.Fortress.Function.Business.entity.PayInfor;
import com.programme.Fortress.Function.Business.entity.ReturnData;


public interface PayInforService extends IService<PayInfor> {

    ReturnData<PayInfor> getPayInfor(PayInfor payInfor);

    PayInfor getOrdernumInfor(String ordernum);

    String getTranXml(String ordernum);

    Integer getPayProcess();
}
