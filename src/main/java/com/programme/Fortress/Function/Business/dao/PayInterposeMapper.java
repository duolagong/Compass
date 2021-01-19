package com.programme.Fortress.Function.Business.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.programme.Fortress.Function.Business.entity.PayInfor;
import com.programme.Fortress.Function.Business.entity.PayInterpose;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PayInterposeMapper extends BaseMapper<PayInterpose> {

    List<PayInterpose> getPayInfor(PayInfor payInfor);

    int getPayInforCount(PayInfor payInfor);
}
