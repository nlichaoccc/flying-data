package com.flyingdata.core.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/18
 */
public class ObjectUtil {
    public static <T> T newNoArgInstance(Class<? extends T> c) {
        if (c == null) {
            return null;
        }
        try {
            return c.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static String join2String(List<Object> objects, String delimiter) {
        if (objects == null || objects.isEmpty()) {
            return "";
        }
        return objects.stream().map(Object::toString).collect(Collectors.joining(delimiter));
    }
}
