package com.programme.Fortress.Util;

public class LogUtil {

    public void getStamp(String time){
        System.out.println(
                "  _____  _                 _       _      _____               \n"
                        +" |  __ \\(_)               | |     | |    / ____|              \n"
                        +" | |  | |_ ___ _ __   __ _| |_ ___| |__ | |     ___  _ __ ___ \n"
                        +" | |  | | / __| '_ \\ / _` | __/ __| '_ \\| |    / _ \\| '__/ _ \\\n"
                        +" | |__| | \\__ \\ |_) | (_| | || (__| | | | |___| (_) | | |  __/\n"
                        +" |_____/|_|___/ .__/ \\__,_|\\__\\___|_| |_|\\_____\\___/|_|  \\___|\n"
                        +"              | |                                             \n"
                        +"              |_|                                            \n"
                        +"                                ...服务端启动完成"+time);
    }
}