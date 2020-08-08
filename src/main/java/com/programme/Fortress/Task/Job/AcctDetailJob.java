package com.programme.Fortress.Task.Job;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component("AccountDetailJob")
public class AcctDetailJob {

    public void execute() {
        System.out.println("-------------------AccountDetailJob任务执行开始-------------------"+new Date());
    }
}
