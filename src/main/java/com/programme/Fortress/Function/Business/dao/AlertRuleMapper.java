package com.programme.Fortress.Function.Business.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.programme.Fortress.Function.Business.entity.AlertRule;

public interface AlertRuleMapper extends BaseMapper<AlertRule> {
    Long getDual();
}
