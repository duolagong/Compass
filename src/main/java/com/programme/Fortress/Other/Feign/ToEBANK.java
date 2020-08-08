package com.programme.Fortress.Other.Feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "ebank1")
public interface ToEBANK {

    @PostMapping(value = "/ebank/platformPaymentResultAction!getPlatformPaymentStatus.do")
    String paymentResultAction(String message);
}
