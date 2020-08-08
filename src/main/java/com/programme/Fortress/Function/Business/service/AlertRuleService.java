package com.programme.Fortress.Function.Business.service;

import com.programme.Fortress.Function.Business.entity.AlertRule;
import com.programme.Fortress.Function.ToolCase.entity.ResultBean;
import org.springframework.stereotype.Service;

public interface AlertRuleService {

    String getAlertRule();

    ResultBean addAlertRule(AlertRule alertRule);

    ResultBean deleteAlertRule(String id);

    ResultBean upDateAlertRule(AlertRule alertRule);

    ResultBean startAlertRule(String id);

    ResultBean pauseAlertRule(String id);
}
