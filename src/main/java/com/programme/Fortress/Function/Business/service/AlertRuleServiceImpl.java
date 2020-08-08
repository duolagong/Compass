package com.programme.Fortress.Function.Business.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.programme.Fortress.Function.Business.dao.AlertRuleMapper;
import com.programme.Fortress.Function.Business.entity.AlertRule;
import com.programme.Fortress.Function.ToolCase.entity.ResultBean;
import com.programme.Fortress.Function.ToolCase.entity.SysUser;
import com.programme.Fortress.Other.Exception.ExceptionHandler;
import com.programme.Fortress.Other.Exception.ResultUtil;
import com.programme.Fortress.Util.BaseValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class AlertRuleServiceImpl implements AlertRuleService {

    @Autowired
    private AlertRuleMapper alertRuleMapper;

    @Override
    public String getAlertRule() {
        List<AlertRule> configList = alertRuleMapper.selectList(null);
        return JSON.toJSONString(configList);
    }

    @Override
    public ResultBean addAlertRule(AlertRule alertRule) {
        try {
            Integer count = alertRuleMapper.selectCount(new QueryWrapper<AlertRule>().eq("type", alertRule.getType()));
            if (count>0) return ResultUtil.fail("系统类型已存在");
            alertRule.setId(alertRuleMapper.getDual());
            alertRule.setTypeStart("0");//默认是不启用
            alertRuleMapper.insert(alertRule);
            return ResultUtil.success(null,"新增系统类型成功");
        }catch (Exception e){
            log.error("新增系统类型异常",e);
            return ResultUtil.fail(e.getMessage());
        }
    }

    @Override
    public ResultBean deleteAlertRule(String id) {
        try {
            int byId = alertRuleMapper.deleteById(id);
            return ResultUtil.success(null,"删除系统类型成功");
        }catch (Exception e){
            log.error("删除系统类型异常",e);
            return ResultUtil.fail(e.getMessage());
        }
    }

    @Override
    public ResultBean upDateAlertRule(AlertRule alertRule) {
        try {
            //updateById莫名其妙的全部不校驗null值了，先这样补值
            AlertRule rule = alertRuleMapper.selectById(alertRule.getId());
            alertRule.setCreatedTime(rule.getCreatedTime());
            alertRule.setTypeStart(rule.getTypeStart());

            int byId = alertRuleMapper.updateById(alertRule);
            return ResultUtil.success(null,"修改系统类型成功");
        }catch (Exception e){
            log.error("修改系统类型异常",e);
            return ResultUtil.fail(e.getMessage());
        }
    }

    @Override
    public ResultBean startAlertRule(String id) {
        try {
            AlertRule alertRule = alertRuleMapper.selectById(id);
            alertRule.setTypeStart("1");
            int byId = alertRuleMapper.updateById(alertRule);
            return ResultUtil.success(null,"启用系统类型成功");
        }catch (Exception e){
            log.error("启用系统类型异常",e);
            return ResultUtil.fail(e.getMessage());
        }
    }

    @Override
    public ResultBean pauseAlertRule(String id) {
        try {
            AlertRule alertRule = alertRuleMapper.selectById(id);
            alertRule.setTypeStart("0");
            int byId = alertRuleMapper.updateById(alertRule);
            return ResultUtil.success(null,"停用系统类型成功");
        }catch (Exception e){
            log.error("停用系统类型异常",e);
            return ResultUtil.fail(e.getMessage());
        }
    }
}
