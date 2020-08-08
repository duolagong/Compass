package com.programme.Fortress.Other.Redis;

import java.util.concurrent.TimeUnit;

public abstract class Status {
    /**
     * 过期时间相关枚举
     */
    public static enum ExpireEnum{
        //未读消息的有效期为8天
        UNREAD_MSG(8L, TimeUnit.DAYS);

        /**
         * 过期时间
         */
        private Long time;
        /**
         * 时间单位
         */
        private TimeUnit timeUnit;

        ExpireEnum(Long time, TimeUnit timeUnit) {
            this.time = time;
            this.timeUnit = timeUnit;
        }

        public Long getTime() {
            return time;
        }

        public TimeUnit getTimeUnit() {
            return timeUnit;
        }
    }
}
