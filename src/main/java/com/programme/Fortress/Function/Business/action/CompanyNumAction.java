package com.programme.Fortress.Function.Business.action;

import com.alibaba.fastjson.JSON;
import com.programme.Fortress.Function.Business.dao.CompanyNumMapper;
import com.programme.Fortress.Function.Business.entity.CompanyNum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@Controller
public class CompanyNumAction {

    @Autowired
    private CompanyNumMapper companyNumMapper;

    @GetMapping(value = "/CompanyNum")
    public String CompanyNum(Model model){
        return "menu/CompanyNum";
    }

    @GetMapping(value = "/companyNum")
    @ResponseBody
    public String getCompanyNum(){
        List<CompanyNum> companyNums = companyNumMapper.selectList(null);
        return JSON.toJSONString(companyNums);
    }
}
