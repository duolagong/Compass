package com.programme.Fortress.Function.Business.service.PayInterpose;

import com.programme.Fortress.Function.Business.entity.PayInfor;
import com.programme.Fortress.Function.ToolCase.entity.ResultBean;

public interface PayInterposeStrategy {

    ResultBean confirmIntermediateState(PayInfor payInfor);
}
