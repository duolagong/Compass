package com.programme.Fortress.Function.ToolCase.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.programme.Fortress.Function.ToolCase.entity.PlanJob;
import com.programme.Fortress.Function.ToolCase.entity.ResultBean;

import java.util.Map;

public interface PlanJobService extends IService<PlanJob> {

    /**
     * 新增定时任务
     */
    ResultBean add(PlanJob planJob,String cookie);

    /**
     * 启动定时任务
     */
    ResultBean start(int id);

    /**
     * 暂停定时任务
     */
    ResultBean pause(int id);

    /**
     * 删除定时任务
     */
    ResultBean delete(int id);

    /**
     *修改定时任务
     */
    ResultBean update(PlanJob planJob);

    /**
     * 启动所有定时任务
     */
    ResultBean startAllJob();

    /**
     * 暂停所有定时任务
     */
    ResultBean pauseAllJob();
}
