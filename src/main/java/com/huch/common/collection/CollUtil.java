package com.huch.common.collection;

import com.huch.common.util.ArrayUtil;

import java.util.*;

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
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return MapUtil.isEmpty(map);
    }

    /**
     * Iterable是否为空
     *
     * @param iterable Iterable对象
     * @return 是否为空
     * @see IterUtil#isEmpty(Iterable)
     */
    public static boolean isEmpty(Iterable<?> iterable) {
        return IterUtil.isEmpty(iterable);
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

    /**
     * 创建一个 ArrayList
     * @param values
     * @param <T>
     * @return
     */
    public static <T> ArrayList<T> createArrayList(T... values){
        if(ArrayUtil.isEmpty(values)){
            return new ArrayList<T>();
        }
        ArrayList<T> list = new ArrayList<>(values.length);
        for (T t : values) {
            list.add(t);
        }
        return list;
    }

    /**
     * 创建一个 ArrayList
     * @param iterator
     * @param <T>
     * @return
     */
    public static <T> ArrayList<T> createArrayList(Iterator<T> iterator){
        ArrayList<T> list = new ArrayList<>();
        if(null ==  iterator){
            return list;
        }
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }

    /**
     * 创建一个 ArrayList
     * @param iterable
     * @param <T>
     * @return
     */
    public static <T> ArrayList<T> createArrayList(Iterable<T> iterable) {
        return createArrayList(iterable.iterator());
    }

}
