package com.programme.Fortress.Other.Listener;

import com.programme.Fortress.Gateway.GatewayServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Netty启动监听
 */
/*@Component*/
public class InitListener implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${spring.netty.port}")
    private int nettyPort;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        GatewayServer nettyServer = new GatewayServer();
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    nettyServer.start(nettyPort);
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

