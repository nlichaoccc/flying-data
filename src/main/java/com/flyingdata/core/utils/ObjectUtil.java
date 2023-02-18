package com.flyingdata.core.utils;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/18
 */
public class ObjectUtil {
    public static <T> T newNoArgInstance(Class<? extends T> c) {
        try {
            return c.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
