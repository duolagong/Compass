package com.programme.Fortress.Function.BankTrade.action;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.programme.Fortress.Function.BankTrade.entity.AcctBalance;
import com.programme.Fortress.Function.BankTrade.entity.MxContent;
import com.programme.Fortress.Function.BankTrade.service.AcctBalanceService;
import com.programme.Fortress.Function.BankTrade.service.MxContentService;
import com.programme.Fortress.Function.Business.dao.BankIdMapper;
import com.programme.Fortress.Function.Business.entity.BankId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@Controller
public class MxContentAction {

    @Autowired
    private MxContentService mxContentService;
    @Autowired
    private BankIdMapper bankIdMapper;

    /**
     * 账户明细
     * @param model
     * @return
     */
    @GetMapping(value = "/MxContent")
    public String Mxcontent(Model model){
        List<BankId> bankIds = bankIdMapper.selectList(new QueryWrapper<BankId>().ne("id","Z01"));
        model.addAttribute("agentids",bankIds);
        return "menu/MxContent";
    }

    @PostMapping(value = "/mxContent")
    @ResponseBody
    public String getMxContent(@RequestBody MxContent mxContent){
        return mxContentService.queryMxContent(mxContent);
    }
}
