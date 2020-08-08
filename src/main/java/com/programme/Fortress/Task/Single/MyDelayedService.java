package com.programme.Fortress.Task.Single;

public interface MyDelayedService {

    void init();

    void put(MyDelayedEvent myDelayedEvent);

    boolean remove(MyDelayedEvent myDelayedEvent);
}
