package com.github.bishoybasily.commons.spring.jpa.generator;

import org.hibernate.engine.spi.SharedSessionContractImplementor;

public class DatabaseIdGenerator extends AbstractIdGenerator {

    @Override
    public String generate(SharedSessionContractImplementor session) {
        return session.createNativeQuery("SELECT uuid()").list().iterator().next().toString();
    }

}
