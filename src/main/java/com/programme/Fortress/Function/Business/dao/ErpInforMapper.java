package com.programme.Fortress.Function.Business.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.programme.Fortress.Function.Business.entity.ErpInfor;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ErpInforMapper extends BaseMapper<ErpInfor> {
    List<HashMap<String,String>> getDayTrad(@Param("dateFrom") String dateFrom,@Param("dateTo") String dateTo,@Param("list") List<String> list);

    List<HashMap<String,String>> getPayTimeDistribution(@Param("payDate") String dateFrom,@Param("list") List<String> list);

    int getTradVolume(String queryDate);
}
