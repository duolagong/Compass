package com.programme.Fortress.Other.XQuery;

import net.sf.saxon.s9api.Processor;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class XQueryFactory implements FactoryBean<Processor>, InitializingBean {

    private Processor processor;

    @Override
    public Processor getObject() throws Exception {
        return processor;
    }

    @Override
    public Class<?> getObjectType() {
        return Processor.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        processor = new Processor(false);
    }
}
