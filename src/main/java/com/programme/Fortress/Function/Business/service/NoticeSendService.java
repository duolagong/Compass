package com.programme.Fortress.Function.Business.service;

import com.programme.Fortress.Function.Business.entity.PayInfor;
import com.programme.Fortress.Function.Business.entity.ReturnData;
import com.programme.Fortress.Function.ToolCase.entity.ResultBean;

import java.util.HashMap;
import java.util.Map;

public interface NoticeSendService {

    ReturnData<PayInfor> getNoticeSend(HashMap hashMap);

    ResultBean noteSend1031(String ordernum);
}
