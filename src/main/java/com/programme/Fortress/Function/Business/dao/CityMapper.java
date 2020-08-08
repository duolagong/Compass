package com.programme.Fortress.Function.Business.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.programme.Fortress.Function.Business.entity.City;

public interface CityMapper extends BaseMapper<City> {

    Long getDual();
}
