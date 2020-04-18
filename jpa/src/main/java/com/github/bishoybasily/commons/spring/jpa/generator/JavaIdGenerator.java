package com.github.bishoybasily.commons.spring.jpa.generator;

import org.hibernate.engine.spi.SharedSessionContractImplementor;

import java.util.UUID;

public class JavaIdGenerator extends AbstractIdGenerator {

    @Override
    public String generate(SharedSessionContractImplementor session) {
        return UUID.randomUUID().toString();
    }

}
