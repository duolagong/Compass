package com.programme.Fortress.Function.Business.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface HomeService {

    Map getHomeMonitor();

    JSONObject flowMapShow();

    JSONObject weeklyTradShow();

    String personFlagShow();

    JSONObject interFaceflowShow();
}
