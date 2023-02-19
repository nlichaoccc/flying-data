package com.flyingdata.core.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author nlichaoccc
 * @since 2023/2/19
 */
public class ListUtil {
    public static <T> List<List<T>> splitList(List<T> list, int batchSize) {
        List<List<T>> tNewList = new ArrayList<>();
        int priIndex = 0;
        int lastPriIndex = 0;
        int insertTimes = list.size() / batchSize;
        List<T> subList;
        for (int i = 0; i <= insertTimes; i++) {
            priIndex = batchSize * i;
            lastPriIndex = priIndex + batchSize;
            if (i == insertTimes) {
                subList = list.subList(priIndex, list.size());
            } else {
                subList = list.subList(priIndex, lastPriIndex);
            }
            if (subList.size() > 0) {
                tNewList.add(subList);
            }
        }
        return tNewList;
    }

}
