package com.programme.Fortress.Other.Feign;

import com.alibaba.fastjson.JSON;
import com.programme.Fortress.Other.Exception.ResultUtil;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.json.Json;

@Component
@Slf4j
public class ToATOMOSBFallbackFactory implements FallbackFactory<ToATOMOSB> {
    @Override
    public ToATOMOSB create(Throwable throwable) {
        return new ToATOMOSB(){


            @Override
            public String getData(String messageSql) {
                log.error("[ToATOMOSB.getData]熔断降级处理",throwable);
                return null;
            }

            @Override
            public String visitBankAdapter(String BankAdapter, String message) {
                log.error("[ToATOMOSB.visitBankAdapter]熔断降级处理",throwable);
                return "无法连接到"+BankAdapter+",请重试!";
            }

            @Override
            public String sendOSBJMS(String message) {
                log.error("[ToATOMOSB.sendOSBJMS]熔断降级处理",throwable);
                return "调用异常:"+throwable.getMessage();
            }
        };
    }
}
