package com.programme.Fortress.Function.Business.service;

import com.alibaba.fastjson.JSON;
import com.programme.Fortress.Function.Business.entity.PayNote;

import java.util.HashMap;
import java.util.List;

public interface PayNoteService {

    Integer getPayNoteCount(String queryDate);

    PayNote getOrdernum(String ordernum);

    JSON insertPayNote(String tranxml, String ordernum);

    void insertPayNote(PayNote payNote);

    PayNote getOrdernumPayNote(String tranxml,String ordernum);

    List getPersonflag(String nowDate);

    HashMap<String, Object> getWeekTrad(String payDate);
}
