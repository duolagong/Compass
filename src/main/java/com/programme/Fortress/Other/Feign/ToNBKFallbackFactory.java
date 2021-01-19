package com.programme.Fortress.Other.Feign;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ToNBKFallbackFactory implements FallbackFactory<ToNBKOSB> {
    @Override
    public ToNBKOSB create(Throwable throwable) {
        return new ToNBKOSB() {
            @Override
            public String sendOSBJMS(String messageSql) {
                return null;
            }

            @Override
            public String sendNBKJMS(String message) {
                return null;
            }

            @Override
            public String sendSMS(String message) {
                return null;
            }

            @Override
            public String sendAssemble1030(String message) {
                return null;
            }

            @Override
            public String getNbkOSBMemory(Long timestamp) {
                return null;
            }

            @Override
            public String getNbkOSBThread(Long timestamp) {
                return null;
            }


            @Override
            public String getNbkOSBVisits(Long timestamp) {
                return null;
            }
        };
    }
}
