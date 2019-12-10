package com.huch.common.collection;

import com.huch.common.util.MapUtil;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

/**
 * @author huchanghua
 * @create 2019-12-10-21:44
 */
public class CollUtil{


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
     * @see com.huch.common.util.MapUtil#isEmpty(Map)
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

    /**
     * 以 conjunction 为分隔符将集合转换为字符串<br>
     * 如果集合元素为数组、{@link Iterable}或{@link Iterator}，则递归组合其为字符串
     *
     * @param <T> 集合元素类型
     * @param iterable {@link Iterable}
     * @param conjunction 分隔符
     * @return 连接后的字符串
     * @see IterUtil#join(Iterable, CharSequence)
     */
    public static <T> String join(Iterable<T> iterable, CharSequence conjunction) {
        return IterUtil.join(iterable, conjunction);
    }

    /**
     * 以 conjunction 为分隔符将集合转换为字符串<br>
     * 如果集合元素为数组、{@link Iterable}或{@link Iterator}，则递归组合其为字符串
     *
     * @param <T> 集合元素类型
     * @param iterator 集合
     * @param conjunction 分隔符
     * @return 连接后的字符串
     * @see IterUtil#join(Iterator, CharSequence)
     */
    public static <T> String join(Iterator<T> iterator, CharSequence conjunction) {
        return IterUtil.join(iterator, conjunction);
    }
}
