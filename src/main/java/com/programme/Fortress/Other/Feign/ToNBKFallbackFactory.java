package com.programme.Fortress.Other.Feign;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.bcel.internal.generic.NEW;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ToNBKFallbackFactory implements FallbackFactory<ToEBANK> {
    @Override
    public ToEBANK create(Throwable throwable) {
        return new ToEBANK(){

            @Override
            public String paymentResultAction(String message) {
                log.error("[ToEBANK.paymentResultAction]熔断降级处理",throwable);
                JSONObject jsonMessage = JSON.parseObject(message);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ordernum",jsonMessage.get("ordernum"));
                jsonObject.put("respcode","P9999");
                jsonObject.put("respmsg","与网银服务器连接异常");
                return jsonObject.toJSONString();
            }
        };
    }
}
