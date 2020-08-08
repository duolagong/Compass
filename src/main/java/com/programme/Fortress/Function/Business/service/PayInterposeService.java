package com.programme.Fortress.Function.Business.service;

import com.programme.Fortress.Function.Business.entity.PayInfor;
import com.programme.Fortress.Function.Business.entity.PayInterpose;
import com.programme.Fortress.Function.Business.entity.ReturnData;
import com.programme.Fortress.Function.ToolCase.entity.ResultBean;

public interface PayInterposeService {

    ReturnData<PayInterpose> getPayInterpose(PayInfor payInfor);

    ResultBean addPayInterpose(PayInfor payInfor);

    ResultBean upDatePayInterpose(PayInterpose payInterpose);

    ResultBean getPayResult(String ordernum);
}
