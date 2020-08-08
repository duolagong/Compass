package com.programme.Fortress.Task.Single.TimingJob;

import com.programme.Fortress.Function.Business.dao.PayInforMapper;
import com.programme.Fortress.Function.Business.entity.PayInfor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NBKResultTask implements TimingJob {

    @Autowired
    private PayInforMapper payInforMapper;

    @Override
    public void executeTask() {

    }

    @Override
    public void executeTask(String Ordernum) {
        PayInfor payInfor = payInforMapper.selectById(Ordernum);
        if("1".equals(payInfor.getPayprocess())){
            if ("2000".equals(payInfor.getTxcode())) {

            }
        }else {
            log.info("[{}]交易已处理完成，无需干预状态！",Ordernum);
        }
    }
}
