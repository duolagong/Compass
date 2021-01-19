package com.programme.Fortress.Function.Business.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.programme.Fortress.Function.Business.entity.AcctPower;

import java.util.HashMap;
import java.util.List;

public interface AcctPowerMapper extends BaseMapper<AcctPower> {
    List<HashMap> getSubbranchId();
}
