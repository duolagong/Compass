package com.programme.Fortress.Other.Feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "NBKOSB")
public interface ToNBKOSB {

    @PostMapping(value = "/NBHTKG/Interface/ToErpServer")
    String sendOSBJMS(String messageSql);


    @PostMapping(value = "/NBHTKG/Interface/ToNBKServer")
    String sendNBKJMS(String message);

    /**
     * 发送短信
     * @param message
     * @return
     */
    @PostMapping(value = "/NBHTKG/MobileMessage")
    String sendSMS(String message);

    @PostMapping(value = "/NBHTKG/TypeStrategy/Assemble1030")
    String sendAssemble1030(String message);

    /**
     * 获取NBKOSB内存详情
     * @return json格式
     */
    @PostMapping(value = "/Compass/OSBMemory")
    String getNbkOSBMemory(Long timestamp);

    /**
     * 获取NBKOSB线程使用情况
     * @return json格式
     */
    @PostMapping(value = "/Compass/OSBThread")
    String getNbkOSBThread(Long timestamp);

    /**
     * 获取NBKOSB访问量
     * @return json格式
     */
    @PostMapping(value = "Compass/OSBVisits")
    String getNbkOSBVisits(Long timestamp);
}
