package com.programme.Fortress.Function.Business.service.ErpInfor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.programme.Fortress.Function.Business.dao.BankIdMapper;
import com.programme.Fortress.Function.Business.dao.ErpInforMapper;
import com.programme.Fortress.Function.Business.entity.BankId;
import com.programme.Fortress.Function.Business.service.ErpInfor.ErpInforAsync;
import com.programme.Fortress.Function.Business.service.ErpInfor.ErpInforService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

@Slf4j
@Service
public class ErpInforServiceImpl implements ErpInforService {
    @Autowired
    private ErpInforMapper erpInforMapper;
    @Autowired
    private BankIdMapper bankIdMapper;
    @Autowired
    private ErpInforAsync erpInforAsync;

    @Override
    public Integer getTradVolume(String queryDate){
        return erpInforMapper.getTradVolume(queryDate);
    }

    /**
     * 获取提交日期的银行汇款时段
     * @param payTime
     * @return
     */
    @Override
    public HashMap<String, Object> getPayTimeDistribution(String payTime){
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            List<BankId> bankIds = bankIdMapper.selectList(new QueryWrapper<BankId>().isNotNull("id").orderByAsc("id"));
            ArrayList<String> bankList = new ArrayList<>(); //存储银行名单
            ArrayList<int[]> agentIdTimeList = new ArrayList<>(); //存储交易时间数组

            ArrayList<List<HashMap<String, String>>> banksFutureList = new ArrayList<>();//存储异步银行交易时间段
            List<Future<List<HashMap<String, String>>>> futures = new ArrayList<>();//存储异步返回Future
            for (BankId bankId : bankIds) {
                futures.add(erpInforAsync.getPayTime(payTime, bankId.getId()));
            }
            for (Future future : futures){
                List<HashMap<String, String>> mapList = (List<HashMap<String, String>>)future.get();
                log.info("存储的异步返回数据[{}]",mapList);
                String bank = mapList.get(mapList.size() - 1).get("BANK");
                mapList.remove(mapList.size() - 1);
                if(mapList.size()>0){
                    bankList.add(bank);
                    banksFutureList.add(mapList);
                }
            }
            //格式List<int[bankX(序号))，timeY(时间序号))，numZ(笔数))]>
            for (int bankX = 0; bankX < banksFutureList.size(); bankX++) {
                int mapTimeY = 0;//累计Map
                for (int timeY = 0; timeY < 25; timeY++) {
                    if (banksFutureList.get(bankX).size() <= mapTimeY) break;
                    int numZ = Integer.parseInt(banksFutureList.get(bankX).get(mapTimeY).get("PAYTIME"));
                    if (timeY == numZ) {
                        agentIdTimeList.add(bankX + timeY, new int[]{bankX, timeY, Integer.parseInt(String.valueOf(banksFutureList.get(bankX).get(mapTimeY).get("TIMENUM")))});
                        mapTimeY++;
                    } else {
                        agentIdTimeList.add(bankX + timeY, new int[]{bankX, timeY, 0});
                    }
                }
            }
            hashMap.put("bankId",bankList.toArray(new String[bankList.size()]));
            hashMap.put("payTimeData",agentIdTimeList.toArray(new Object[agentIdTimeList.size()]));
        }catch (Exception e){
            log.error("获取提交日期的银行汇款时段信息异常",e);
        }
        return hashMap;
    }

}
