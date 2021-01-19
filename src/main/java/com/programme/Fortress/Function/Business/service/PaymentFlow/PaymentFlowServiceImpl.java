package com.programme.Fortress.Function.Business.service.PaymentFlow;

import com.alibaba.fastjson.JSONObject;
import com.programme.Fortress.Function.ToolCase.entity.ResultBean;
import com.programme.Fortress.Util.Redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

@Slf4j
@Service
public class PaymentFlowServiceImpl implements PaymentFlowService {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 收款方城市交易量展示
     * 关于收款方城市金额的展示同理
     * @return
     */
    @Override
    public JSONObject getTodayPaymentFlow() {

        ArrayList<ArrayList> paymentFlowList = new ArrayList<>(); //存储收款城市数据
        HashMap<String, Object> pointMap = new HashMap<>(); //存储收款城市坐标
        ArrayList<String> orderNameList = new ArrayList<>(); //存储页名
        //获取城市坐标
        Map<Object, Object> cityPointMap = redisUtil.hmget("COM:CITY:POINT");

        //获取付款笔数
        Set<Object> cityPayNumSet = redisUtil.zGet("COM:CITY:PNUM", 0, -1);
        //获取汇款金额
        /*Set<Object> cityPayNumSet = redisUtil.zGet("COM:CITY:PAMOUNT", 0, -1);*/
        float setSize = cityPayNumSet.size();
        int order = (int) Math.ceil(setSize / 20);
        ArrayList<String> cityPayNumList = redisUtil.zSCAN("COM:CITY:PNUM");

        Iterator<Object> iterator = cityPayNumSet.iterator();
        for (int i = 0; i < order; i++) {
            String orderName = "";
            if (i == order - 1) {
                orderName = String.format("收款方城市Top%s - %s", i * 20 + 1, (int)setSize);
            } else {
                orderName = String.format("收款方城市Top%s - %s", i * 20 + 1, i * 20 + 20);
            }
            ArrayList<Object> orderList = new ArrayList<>();
            int n = 1;
            while (iterator.hasNext()) {
                String cityName = iterator.next().toString();
                String[] split = cityPointMap.get(cityName).toString().split("\\,", 2);
                double[] doubles = new double[]{Double.parseDouble(split[0]), Double.parseDouble(split[1])};
                pointMap.put(cityName, doubles);
                //填充数据
                HashMap<String, Object> paymentNumMap = new HashMap<>();
                paymentNumMap.put("order", orderName);
                paymentNumMap.put("name", cityName);
                paymentNumMap.put("value", (Double) redisUtil.zItemGet("COM:CITY:PNUM", cityName));
                paymentNumMap.put("value1", (Double) redisUtil.zItemGet("COM:CITY:PNUM", cityName));
                orderList.add(paymentNumMap);
                if (n < 20) {
                    n++;
                } else {
                    break;
                }
            }
            orderNameList.add(orderName);
            paymentFlowList.add(orderList);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("point", pointMap);
        jsonObject.put("order", orderNameList);
        jsonObject.put("data", paymentFlowList);
        return jsonObject;
    }
}
