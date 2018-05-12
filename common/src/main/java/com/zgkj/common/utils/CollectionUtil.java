package com.zgkj.common.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Descr:   集合操作的辅助工具类
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/1/25.
 */

public class CollectionUtil {


    /**
     * 私有化的构造方法，禁止通过new的方式创建对象
     */
    private CollectionUtil() {

        // 抛出不支持的操作异常
        throw new UnsupportedOperationException("cannot be instantiated!");
    }


    /**
     * 判断集合对象是否为空
     *
     * @param tCollection 需要判断的集合对象
     * @param <T>         泛型的数据类型
     * @return 返回true表示为空，返回false表示不为空
     */
    public static <T> boolean isEmpty(Collection<T> tCollection) {

        return tCollection == null || tCollection.size() == 0;
    }


    /**
     * 判断数组对象是否为空
     *
     * @param ts  需要判断的数组对象
     * @param <T> 泛型的数据类型
     * @return 返回true表示为空，返回false表示不为空
     */
    public static <T> boolean isEmpty(T[] ts) {

        return ts == null || ts.length == 0;
    }


    /**
     * List集合对象转换为数组对象
     *
     * @param tList
     * @param tClass
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(List<T> tList, Class<T> tClass) {
        // 如果集合对象为空则直接返回NULL
        if (isEmpty(tList)) {
            return null;
        }
        int size = tList.size();
        try {
            // 创建一个指定类型和长度的数组对象
            T[] ts = (T[]) Array.newInstance(tClass, size);
            // 将集合转换为指定类型的数组并返回
            return tList.toArray(ts);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Set集合转换为数组对象
     *
     * @param tSet
     * @param tClass
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Set<T> tSet, Class<T> tClass) {
        if (isEmpty(tSet)) {
            return null;
        }
        int size = tSet.size();
        try {
            T[] ts = (T[]) Array.newInstance(tClass, size);

            return tSet.toArray(ts);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 数组对象转换为ArrayList集合
     *
     * @param ts
     * @param <T>
     * @return
     */
    public static <T> ArrayList<T> toArrayList(T[] ts) {
        if (isEmpty(ts)) {
            return null;
        }
        // 创建一个返回的ArrayList集合对象
        ArrayList<T> tArrayList = new ArrayList<>();

        // 将数组对象里面的数据添加到ArrayList集合中
        Collections.addAll(tArrayList, ts);

        return tArrayList;
    }


    /**
     * 数组对象转换为HashSet集合
     *
     * @param ts
     * @param <T>
     * @return
     */
    public static <T> HashSet<T> toHashSet(T[] ts) {
        if (isEmpty(ts)) {
            return null;
        }
        HashSet<T> tHashSet = new HashSet<>();
        Collections.addAll(tHashSet, ts);

        return tHashSet;
    }

}
