package com.programme.Fortress.Other.Feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "NBKOSB")
public interface ToNBKOSB {

    @PostMapping(value = "/NBHTKG/Interface/ToErpServer")
    String sendOSBJMS(String messageSql);

    @PostMapping(value = "/NBHTKG/Interface/ToNBKServer")
    String sendNBKJMS(String message);

    @PostMapping(value = "/NBHTKG/MobileMessage")
    String sendSMS(String message);

    @PostMapping(value = "/NBHTKG/TypeStrategy/Assemble1030")
    String sendAssemble1030(String message);
}
