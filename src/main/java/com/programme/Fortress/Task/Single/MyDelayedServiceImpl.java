package com.programme.Fortress.Task.Single;

import com.programme.Fortress.Task.Single.TimingJob.NBKResultTask;
import com.programme.Fortress.Task.Single.TimingJob.TimingJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class MyDelayedServiceImpl implements MyDelayedService, CommandLineRunner {

    DelayQueue<MyDelayedEvent> queue = new DelayQueue<MyDelayedEvent>();

    @Override
    public void run(String... args) throws Exception {
        init();
    }

    @Override
    public void init() {
        log.info("初始化延时队列");
        Executors.newSingleThreadExecutor().execute(new Thread(this::motionGet));
    }

    @Override
    public void put(MyDelayedEvent myDelayedEvent) {
        queue.put(myDelayedEvent);
    }

    @Override
    public boolean remove(MyDelayedEvent myDelayedEvent) {
        return queue.remove(myDelayedEvent);
    }

    /**
     * 处理队列中自动任务
     */
    public void motionGet() {
        for (; ; ) {
            try {
                MyDelayedEvent myDelayedEvent = queue.take();
                myDelayedEvent.getTimingJob().executeTask();
                log.info(myDelayedEvent.getTimingJob().getClass().getName()+"-任务已成为");
            }catch (Exception e){
                log.error("延时队列处理任务异常",e);
            }
        }
    }
}
