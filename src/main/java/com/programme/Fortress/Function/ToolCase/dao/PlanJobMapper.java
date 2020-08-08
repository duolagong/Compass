package com.programme.Fortress.Function.ToolCase.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.programme.Fortress.Function.ToolCase.entity.PlanJob;

import java.util.Map;

public interface PlanJobMapper extends BaseMapper<PlanJob> {

    Long getDual();
}
