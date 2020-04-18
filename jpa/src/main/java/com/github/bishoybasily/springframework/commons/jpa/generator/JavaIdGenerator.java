package com.github.bishoybasily.springframework.commons.jpa.generator;

import org.hibernate.engine.spi.SharedSessionContractImplementor;

import java.util.UUID;

public class JavaIdGenerator extends AbstractIdGenerator {

    @Override
    public String generate(SharedSessionContractImplementor session) {
        return UUID.randomUUID().toString();
    }

}
