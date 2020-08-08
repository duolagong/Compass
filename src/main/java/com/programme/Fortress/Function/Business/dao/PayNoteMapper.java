package com.programme.Fortress.Function.Business.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.programme.Fortress.Function.Business.entity.PayNote;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface PayNoteMapper extends BaseMapper<PayNote> {

    List<Map<String,String>> getPersonflag(@Param("nowDate") String nowDate);

    Integer getPayNoteCount(String queryDate);

    List<HashMap<String,String>> getDayTrad(@Param("dateFrom") String dateFrom, @Param("dateTo") String dateTo, @Param("list") List<String> list);
}
