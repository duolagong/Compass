package com.programme.Fortress.Function.Business.service.ErpInfor;

import com.programme.Fortress.Function.Business.dao.BankIdMapper;
import com.programme.Fortress.Function.Business.dao.ErpInforMapper;
import com.programme.Fortress.Function.Business.entity.BankId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

@Slf4j
@Service
public class ErpInforAsync {

    @Autowired
    private BankIdMapper bankIdMapper;
    @Autowired
    private ErpInforMapper erpInforMapper;

    /**
     * 获取某日期下的某银行支付时间段（异步）
     * @param payTime 日期
     * @param agentid 银行
     * @return
     */
    @Async(value="myAsyncPoolTaskExecutor")
    public Future<List<HashMap<String, String>>> getPayTime(String payTime , String agentid){
        log.info("异步查询[{}]日[{}]银行的交易信息数据",payTime,agentid);
        List<HashMap<String, String>> payTimeList = erpInforMapper.getPayTimeDistribution(payTime,
                "Z01".equals(agentid) ? new ArrayList<String>() {{ add(agentid); add("W01"); add("907");}} : new ArrayList<String>() {{ add(agentid);}});
        HashMap<String, String> bankMap = new HashMap<>();
        bankMap.put("BANK",bankIdMapper.selectById(agentid).getBankname());
        payTimeList.add(bankMap);
        return new AsyncResult<List<HashMap<String, String>>>(payTimeList);
    }
}
