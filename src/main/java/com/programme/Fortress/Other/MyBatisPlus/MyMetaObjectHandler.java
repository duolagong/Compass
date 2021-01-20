package com.programme.Fortress.Other.MyBatisPlus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        System.out.println("自动新增");
        setFieldValByName("createdTime", LocalDateTime.now(), metaObject);
        setFieldValByName("updatedTime", LocalDateTime.now(), metaObject);
        setFieldValByName("deleteFlag", 0, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        setFieldValByName("updatedTime", LocalDateTime.now(), metaObject);
    }
}
