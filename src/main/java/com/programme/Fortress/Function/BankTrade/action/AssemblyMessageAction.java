package com.programme.Fortress.Function.BankTrade.action;

import com.programme.Fortress.Function.BankTrade.entity.AssemblyMessage;
import com.programme.Fortress.Function.BankTrade.service.AssemblyMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class AssemblyMessageAction {

    @Autowired
    private AssemblyMessageService assemblyMessageService;

    @PostMapping(value = "/assemblyMessage",produces ={"application/json"})
    @ResponseBody
    public Object getAssemblyMessage(@RequestBody AssemblyMessage assemblyMessage) {
        assemblyMessage.setVersion("02");
        return assemblyMessageService.getAssemblyMessage(assemblyMessage);
    }

}
