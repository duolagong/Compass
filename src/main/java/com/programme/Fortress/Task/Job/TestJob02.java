package com.programme.Fortress.Task.Job;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Component("TestJob02")
@Transactional
public class TestJob02 {
    public void execute() {
        System.out.println("-------------------TestJob02任务执行开始-------------------"+new Date());
    }
}
