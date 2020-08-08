package com.programme.Fortress.Function.Business.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.programme.Fortress.Function.Business.entity.PayInfor;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface PayInforMapper extends BaseMapper<PayInfor> {

    List<PayInfor> getPayInfor(PayInfor payInfor);

    int getPayInforCount(PayInfor payInfor);

    List<HashMap<String,Object>> getInforNote(@Param("nowDate") String nowDate);

    int getInforNoteCount(@Param("nowDate") String nowDate);

    int getOrdernumInforCount(HashMap hashMap);

    List<PayInfor> getOrdernumInfor(HashMap hashMap);
}
