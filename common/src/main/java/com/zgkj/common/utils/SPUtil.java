package com.zgkj.common.utils;

import android.content.Context;
import android.content.SharedPreferences;


import com.amap.api.maps2d.model.LatLng;
import com.zgkj.common.Common;
import com.zgkj.common.app.Application;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Descr:   SharedPreferences轻量级存储类的工具类
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/1/19.
 */

public class SPUtil {

    // 定义SharedPreferences轻量级存储类的文件名
    private static final String SP_NAME = "fazhichun_data";

    /**
     * 私有化的构造方法，禁止通过new的方式创建对象
     */
    private SPUtil(){
        // 抛出不支持操作的异常
        throw new UnsupportedOperationException("cannot be instantiated!");
    }


    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key           键
     * @param object        值
     */
    public static void put(String key, Object object) {
        SharedPreferences sp = Application.getInstance()
                .getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        // 提交数据
        SharedPreferencesCompat.apply(editor);
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key               键
     * @param defaultObject     默认值
     * @return
     */
    public static Object get(String key, Object defaultObject) {

        SharedPreferences sp = Application.getInstance()
                .getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    /**
     * 获取经纬度
     * @return
     */
    public static LatLng getLatLng(){
        LatLng latLng = new LatLng(Double.parseDouble(String.valueOf(SPUtil.get(Common.Constant.LATITUDE_ID, ""))), Double.parseDouble(String.valueOf(SPUtil.get(Common.Constant.LONGITUDE_ID, ""))));
        return  latLng;
    }


    /**
     * 移除某个key值已经对应的值
     *
     * @param key           键
     */
    public static void remove(String key) {

        SharedPreferences sp = Application.getInstance()
                .getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }


    /**
     * 清除所有数据
     */
    public static void clear() {

        SharedPreferences sp = Application.getInstance()
                .getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     */
    public static boolean contains(String key) {
        SharedPreferences sp = Application.getInstance()
                .getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);

        return sp.contains(key);
    }


    /**
     * 返回所有的键值对
     */
    public static Map<String, ?> getAll() {
        SharedPreferences sp = Application.getInstance()
                .getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);

        return sp.getAll();
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     */
    private static class SharedPreferencesCompat {

        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }



        /**
         * 如果找到则使用apply执行，否则使用commit
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }









}
