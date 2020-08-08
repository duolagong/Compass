package com.programme.Fortress.Function.Business.action;

import com.programme.Fortress.Function.Business.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/City")
public class CityAction {

    @Autowired
    private CityService cityService;

    @GetMapping
    public String setCity(){
        cityService.setCity();
        return "ok";
    }


}
