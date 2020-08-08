package com.programme.Fortress.Task.Job;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Component("TestJob01")
@Transactional
public class TestJob01 {

    public void execute() {
        System.out.println("-------------------TestJob01任务执行开始-------------------"+new Date());
    }
}
