package com.yuefor.customruleview.util;

import java.util.List;
import java.util.Map;

/**
 * 集合或者数组的非空判断
 *
 * @author ZHT
 */
public class CollectionUtils {

    public static boolean isNotEmpty(Object[] array) {
        if (array == null)
            return false;
        return array.length != 0;
    }

    public static boolean isEmpty(Object[] array) {
        return !isNotEmpty(array);
    }

    public static boolean isNotEmpty(List<?> list) {
        if (list == null)
            return false;
        return !list.isEmpty();
    }

    public static boolean isEmpty(List<?> list) {
        return !isNotEmpty(list);
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        if (map == null)
            return false;
        return !map.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return !isNotEmpty(map);
    }

}
