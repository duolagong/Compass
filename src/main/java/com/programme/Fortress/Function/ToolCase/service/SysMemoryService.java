package com.programme.Fortress.Function.ToolCase.service;

import com.alibaba.fastjson.JSONObject;
import com.programme.Fortress.Function.ToolCase.entity.ResultBean;

import java.util.concurrent.Future;

public interface SysMemoryService {

    /**
     * 获取初始化系统内存
     * @return
     */
    ResultBean getSysMemoryInit();

    /**
     * 读取剩余内存
     * @param timestamp
     * @return
     */
    ResultBean getFreeMemory(Long timestamp);

    /**
     * 读取已使用内存
     * @param timestamp
     * @return
     */
    ResultBean getUseMemory(Long timestamp);

    /**
     * 读取内存使用率
     * @param timestamp
     * @return
     */
    ResultBean getMemoryUtilization(Long timestamp);

    /**
     * 读取线程信息
     * @param timestamp
     * @return
     */
    ResultBean getThreadDetails(Long timestamp);

    /**
     * 异步获取网银OSB内存信息
     * @param timestamp
     * @return
     */
    Future<JSONObject> getNBKOSBMemory(Long timestamp);

    /**
     * 异步获取网银OSB线程信息
     * @param timestamp
     * @return
     */
    Future<JSONObject> getNBKOSBThread(Long timestamp);
}
