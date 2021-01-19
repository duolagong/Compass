package com.programme.Fortress.Other.Feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "ATOMOSB",fallbackFactory = ToATOMOSBFallbackFactory.class)
public interface ToATOMOSB {

    @PostMapping(value = "/HTKG/SuperOSBEntry")
    String getData(String messageSql);

    @PostMapping(value = "/HTKG/{BankAdapter}")
    String visitBankAdapter(@PathVariable("BankAdapter")String BankAdapter , String message);

    @PostMapping(value = "/HTKG/Interface/ToMyServer")
    String sendOSBJMS(String message);

    @PostMapping(value = "/HTKG/ERPNoticeReceiver")
    String sendOSBNotice(String message);
}
