package com.huch.common.collection;

import com.huch.common.util.MapUtil;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

/**
 *
 */
public class CollectionUtil{

    private CollectionUtil(){
    }

    /**
     * 集合是否为空
     *
     * @param collection 集合
     * @return 是否为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 集合是否不为空
     * @param collection
     * @return
     */
    public static boolean isNotEmpty(Collection<?> collection){
        return !isEmpty(collection);
    }

    /**
     * Map是否为空
     *
     * @param map 集合
     * @return 是否为空
     * @see MapUtil#isEmpty(Map)
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return MapUtil.isEmpty(map);
    }

    /**
     * Enumeration是否为空
     *
     * @param enumeration {@link Enumeration}
     * @return 是否为空
     */
    public static boolean isEmpty(Enumeration<?> enumeration) {
        return null == enumeration || false == enumeration.hasMoreElements();
    }



}
