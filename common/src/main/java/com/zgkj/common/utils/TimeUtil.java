package com.zgkj.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Descr:   日期时间处理的辅助工具类
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/1/22.   yyyy年MM月dd日 hh时mm分ss秒SSS毫秒
 */

public class TimeUtil {

    /**
     * 私有化的构造方法，禁止通过new的方式创建对象
     */
    private TimeUtil() {

        // 抛出不支持的操作异常
        throw new UnsupportedOperationException("cannot be instantiated!");
    }


    /**
     * 获取当前系统的时间戳（10位）
     *
     * @return 以字符串的数据格式返回时间戳
     */
    public static String getStringStamp() {

        long stamp = System.currentTimeMillis() / 1000L;

        return String.valueOf(stamp);
    }


    /**
     * 获取当前系统的时间戳（10位）
     *
     * @return 以长整型的数据格式返回时间戳
     */
    public static long getLongStamp() {

        return System.currentTimeMillis() / 1000L;

    }


    /**
     * 将时间戳转换为时间
     *
     * @param stamp 字符串数据类型的时间戳对象
     * @param type  转换时间的类型
     * @return
     */
    public static String stamp2Time(String stamp, String type) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(type, Locale.CHINA);
        Date date = new Date(Long.parseLong(stamp) * 1000L);

        return dateFormat.format(date);
    }


    /**
     * 将时间戳转换为时间
     *
     * @param stamp 长整型数据类型的时间戳对象
     * @param type  转换时间的类型
     * @return
     */
    public static String stamp2Time(long stamp, String type) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(type, Locale.CHINA);
        Date date = new Date(stamp * 1000L);

        return dateFormat.format(date);
    }


    /**
     * 将时间戳转换为Date
     *
     * @param stamp 字符串数据类型的时间戳对象
     * @return 返回Date的对象
     */
    public static Date stamp2Date(String stamp) {

        return new Date(Long.parseLong(stamp) * 1000L);
    }

    /**
     * 将时间戳转换为Date
     *
     * @param stamp 长整型数据类型的时间戳对象
     * @return
     */
    public static Date stamp2Date(long stamp) {

        return new Date(stamp * 1000L);
    }


    /**
     * 通过时间戳计算年龄（以当前系统时间为参照）
     *
     * @param stamp 字符串数据类型的时间戳对象（出生日期的时间戳）
     * @return 返回字符串数据类型的年龄
     */
    public static String stamp2Age(String stamp) {
        // 将出生日期的时间戳对象转换为Date
        Date birthDate = stamp2Date(stamp);

        // 获取当前系统的日历对象
        Calendar calendar = Calendar.getInstance();

        // 如果当前系统的日期小于出生日期则抛出异常
        // 直接返回空
        if (calendar.before(birthDate)) {
            throw new IllegalArgumentException("The date of birth is not legal.");
        }

        // 获取当前系统时间点的年月日
        int nowYear = calendar.get(Calendar.YEAR);
        int nowMonth = calendar.get(Calendar.MONTH);
        int nowDay = calendar.get(Calendar.DAY_OF_MONTH);   // 一个月中的某一天

        // 将出生日期赋值给日历对象并获取出生日期的年月日
        calendar.setTime(birthDate);
        int oldYear = calendar.get(Calendar.YEAR);
        int oldMonth = calendar.get(Calendar.MONTH);
        int oldDay = calendar.get(Calendar.DAY_OF_MONTH);

        // 进行比较计算
        int age = nowYear - oldYear;
        if (nowMonth <= oldMonth) {
            // 月份相同比较月份中的天数
            if (nowMonth == oldMonth) {
                if (nowDay < oldDay) {
                    age--;
                }
            } else {
                age--;
            }
        }
        return String.valueOf(age);
    }


    /**
     * 通过时间戳计算年龄（以当前系统时间为参照）
     *
     * @param stamp 长整型数据类型的时间戳对象（出生日期的时间戳）
     * @return 返回字符串数据类型的年龄
     */
    public static String stamp2Age(long stamp) {
        // 将出生日期的时间戳对象转换为Date
        Date oldDate = stamp2Date(stamp);

        // 获取当前系统的日历对象
        Calendar calendar = Calendar.getInstance();

        // 如果当前系统的日期小于出生日期则抛出异常
        // 直接返回空
        if (calendar.before(oldDate)) {
            throw new IllegalArgumentException("The date of birth is not legal.");
        }

        // 获取当前系统时间点的年月日
        int nowYear = calendar.get(Calendar.YEAR);
        int nowMonth = calendar.get(Calendar.MONTH);
        int nowDay = calendar.get(Calendar.DAY_OF_MONTH);   // 一个月中的某一天

        // 将出生日期赋值给日历对象并获取出生日期的年月日
        calendar.setTime(oldDate);
        int oldYear = calendar.get(Calendar.YEAR);
        int oldMonth = calendar.get(Calendar.MONTH);
        int oldDay = calendar.get(Calendar.DAY_OF_MONTH);

        // 进行比较计算
        int age = nowYear - oldYear;
        if (nowMonth <= oldMonth) {
            // 月份相同比较月份中的天数
            if (nowMonth == oldMonth) {
                if (nowDay < oldDay) {
                    age--;
                }
            } else {
                age--;
            }
        }
        return String.valueOf(age);
    }


    /**
     * 通过时间戳计算年龄
     *
     * @param oldStamp
     * @param nowStamp
     * @return
     */
    public static String stamp2Age(String oldStamp, String nowStamp) {
        Date oldDate = stamp2Date(oldStamp);
        Date nowDate = stamp2Date(nowStamp);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        if (calendar.before(oldDate)) {
            throw new IllegalArgumentException("The date of birth is not legal.");
        }
        // 获取当前系统时间点的年月日
        int nowYear = calendar.get(Calendar.YEAR);
        int nowMonth = calendar.get(Calendar.MONTH);
        int nowDay = calendar.get(Calendar.DAY_OF_MONTH);   // 一个月中的某一天

        // 将出生日期赋值给日历对象并获取出生日期的年月日
        calendar.setTime(oldDate);
        int oldYear = calendar.get(Calendar.YEAR);
        int oldMonth = calendar.get(Calendar.MONTH);
        int oldDay = calendar.get(Calendar.DAY_OF_MONTH);

        // 进行比较计算
        int age = nowYear - oldYear;
        if (nowMonth <= oldMonth) {
            // 月份相同比较月份中的天数
            if (nowMonth == oldMonth) {
                if (nowDay < oldDay) {
                    age--;
                }
            } else {
                age--;
            }
        }
        return String.valueOf(age);
    }


}
