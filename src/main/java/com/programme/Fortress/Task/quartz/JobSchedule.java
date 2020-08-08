package com.programme.Fortress.Task.quartz;

import com.programme.Fortress.Task.service.QuartzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class JobSchedule implements CommandLineRunner {

    @Autowired
    private QuartzService taskService;

    /**
     * 任务调度开始
     * @param strings
     * @throws Exception
     */
    @Override
    public void run(String... strings) throws Exception {
        taskService.timingTask();
        log.info("加载系统自动任务完成！");
    }
}
