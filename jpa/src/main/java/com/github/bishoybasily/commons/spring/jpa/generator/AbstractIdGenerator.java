package com.github.bishoybasily.commons.spring.jpa.generator;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.UUIDGenerator;
import org.springframework.util.ObjectUtils;

import javax.persistence.Id;
import java.lang.reflect.Field;

public abstract class AbstractIdGenerator extends UUIDGenerator {

    @Override
    public final String generate(SharedSessionContractImplementor session, Object object) throws HibernateException {

        try {

            Field field = null;
            Class<?> cls = object.getClass();
            found:
            while (!ObjectUtils.isEmpty(cls)) {
                for (Field f : cls.getDeclaredFields()) {
                    boolean annotatedWithId = f.isAnnotationPresent(Id.class);
                    boolean isString = f.getType().getSuperclass().isAssignableFrom(String.class);
                    if (annotatedWithId && isString) {
                        field = f;
                        break found;
                    }
                }
                cls = cls.getSuperclass();
            }

            if (ObjectUtils.isEmpty(field))
                throw new RuntimeException();

            field.setAccessible(true);

            String id = (String) field.get(object);

            if (ObjectUtils.isEmpty(id))
                return generate(session);

            return id;

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    public abstract String generate(SharedSessionContractImplementor session);

}
