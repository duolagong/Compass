package com.programme.Fortress.Function.Business.service;

import com.alibaba.fastjson.JSON;
import com.programme.Fortress.Function.Business.dao.CityMapper;
import com.programme.Fortress.Function.Business.dao.MsgMapper;
import com.programme.Fortress.Function.Business.entity.City;
import com.programme.Fortress.Function.Business.entity.Msg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class CityServiceImpl implements CityService {

    @Autowired
    private MsgMapper msgMapper;
    @Autowired
    private CityMapper cityMapper;

    @Override
    public void setCity(){
        List<Msg> msgs = msgMapper.selectList(null);
        Msg msg = msgs.get(0);
        String msgMsg = msg.getMsg();
        log.info("msg的长度："+msgMsg.length());
        List<City> citys = JSON.parseArray(msgMsg, City.class);
        for (City city :citys){
            city.setId(cityMapper.getDual());
            int insert = cityMapper.insert(city);//插入数据
        }
    }
}