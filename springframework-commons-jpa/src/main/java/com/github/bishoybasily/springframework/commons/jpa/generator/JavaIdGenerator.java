package com.github.bishoybasily.springframework.commons.jpa.generator;

import org.hibernate.engine.spi.SharedSessionContractImplementor;

import java.util.UUID;

public class JavaIdGenerator extends AbstractIdGenerator {

    public final static String
            NAME = "JavaIdGenerator",
            CLASS = "com.github.bishoybasily.springframework.commons.jpa.generator.JavaIdGenerator";

    @Override
    public String generate(SharedSessionContractImplementor session) {
        return UUID.randomUUID().toString();
    }

}
