package com.programme.Fortress.Task.Single;

import com.programme.Fortress.Task.Single.TimingJob.TimingJob;
import lombok.Data;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Data
public class MyDelayedEvent implements Delayed {

    private TimingJob timingJob;
    private long time;//秒

    MyDelayedEvent(TimingJob timingJob,Long time,TimeUnit unit){
        this.timingJob = timingJob;
        this.time= System.currentTimeMillis()/1000 + (time > 0? unit.toSeconds(time): 0);//时间颗粒度转换——>秒
    }

    /**
     * 判断是否到了截止时间
     * @param unit
     * @return
     */
    @Override
    public long getDelay(TimeUnit unit) {
        return time - System.currentTimeMillis()/1000;
    }

    /**
     * 比较排序
     * @param o
     * @return
     */
    @Override
    public int compareTo(Delayed o) {
        MyDelayedEvent myDelayedEvent = (MyDelayedEvent) o;
        long diff = this.time - myDelayedEvent.time;
        if (diff <= 0) {
            return -1;
        }else {
            return 1;
        }
    }
}
