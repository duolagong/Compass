package com.programme.Fortress.Function.Business.service.PayInterpose;

import com.programme.Fortress.Function.Business.entity.PayInfor;
import com.programme.Fortress.Function.Business.entity.PayInterpose;
import com.programme.Fortress.Function.ToolCase.entity.ResultBean;
import com.programme.Fortress.Other.Exception.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 付款干预控制类
 */

@Service
public class PayInterposeContext {

    @Autowired
    private final Map<Integer,PayInterposeStrategy> strategyMap = new ConcurrentHashMap<>();

    public ResultBean confirmIntermediateState(int subjectId, PayInfor payInfor) {
        PayInterposeStrategy interposeStrategy = strategyMap.get(subjectId);
        if(ObjectUtils.isEmpty(interposeStrategy)) return ResultUtil.fail("获取策略服务为空,请检查代码！");
        return interposeStrategy.confirmIntermediateState(payInfor);
    }
}
