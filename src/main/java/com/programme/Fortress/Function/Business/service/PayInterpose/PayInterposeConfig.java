package com.programme.Fortress.Function.Business.service.PayInterpose;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 付款干预配置类
 */

@Configuration
public class PayInterposeConfig {

    @Resource(name = "EBankInterposeStrategy")
    private PayInterposeStrategy eBankInterposeStrategy;

    @Resource(name = "ATOMInterposeStrategy")
    private PayInterposeStrategy atomInterposeStrategy;

    @Bean
    public Map<Integer, PayInterposeStrategy> giftInfoStrategyServiceMap() {
        Map<Integer, PayInterposeStrategy> strategyMap = new ConcurrentHashMap<>();
        strategyMap.put(1, eBankInterposeStrategy);
        strategyMap.put(2, atomInterposeStrategy);
        return strategyMap;
    }
}
