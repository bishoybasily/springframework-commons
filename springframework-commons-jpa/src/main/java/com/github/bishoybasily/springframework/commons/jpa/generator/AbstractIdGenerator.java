package com.github.bishoybasily.springframework.commons.jpa.generator;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.UUIDGenerator;
import org.springframework.util.ObjectUtils;

import javax.persistence.Id;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class AbstractIdGenerator extends UUIDGenerator {

    @Override
    public final String generate(SharedSessionContractImplementor session, Object object) throws HibernateException {

        try {

          Field field = null;
          Method method = null;

          Class<?> cls = object.getClass();
          found:
          while (!ObjectUtils.isEmpty(cls)) {

            try {
              for (Method m : cls.getDeclaredMethods()) {
                boolean annotatedWithId = m.isAnnotationPresent(Id.class);
                boolean isString = m.getReturnType().getSuperclass().isAssignableFrom(String.class);
                if (annotatedWithId && isString) {
                  method = m;
                  break found;
                }
              }
            } catch (Exception e) {
              // ignore this
            }

            try {
              for (Field f : cls.getDeclaredFields()) {
                boolean annotatedWithId = f.isAnnotationPresent(Id.class);
                boolean isString = f.getType().getSuperclass().isAssignableFrom(String.class);
                if (annotatedWithId && isString) {
                  field = f;
                  break found;
                }
              }
            } catch (Exception e) {
              // ignore this
            }

            cls = cls.getSuperclass();

          }

          if (ObjectUtils.isEmpty(method) && ObjectUtils.isEmpty(field))
            throw new RuntimeException();

          if (!ObjectUtils.isEmpty(field)) {

            field.setAccessible(true);

            String id = (String) field.get(object);

            if (ObjectUtils.isEmpty(id))
              return generate(session);

            return id;

          }

          if (!ObjectUtils.isEmpty(method)) {

            method.setAccessible(true);

            String id = (String) method.invoke(object);

            if (ObjectUtils.isEmpty(id))
              return generate(session);

            return id;

          }

          throw new IllegalStateException();

        } catch (IllegalAccessException | InvocationTargetException e) {
          throw new RuntimeException(e);
        }

    }

    public abstract String generate(SharedSessionContractImplementor session);

}
