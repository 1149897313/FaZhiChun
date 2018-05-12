package com.zgkj.common.utils;

import java.security.MessageDigest;

/**
 * Descr:   MD5编码工具类
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/1/19.
 */

public class MD5Util {

    /**
     * 私有化的构造方法，禁止通过new的方式创建对象
     */
    private MD5Util() {

        // 抛出不支持的操作异常
        throw new UnsupportedOperationException("cannot be instantiated!");
    }

    /**
     * 字符串类型的数据进行MD5编码
     *
     * @param string
     * @return
     */
    public final static String encode(String string) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest digest = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            digest.update(string.getBytes());
            // 获得密文
            byte[] md = digest.digest();
            // 把密文转换成十六进制的字符串形式
            int length = md.length;
            char chars[] = new char[length * 2];
            int index = 0;
            for (int i = 0; i < length; i++) {
                byte byte0 = md[i];
                chars[index++] = hexDigits[byte0 >>> 4 & 0xf];
                chars[index++] = hexDigits[byte0 & 0xf];
            }
            return new String(chars);
        } catch (Exception e) {
            return null;
        }
    }


}
