package com.programme.Fortress.Function.Business.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.programme.Fortress.Function.Business.entity.AlertInfor;

public interface AlertInforMapper extends BaseMapper<AlertInfor> {
    Long getDual();
}
