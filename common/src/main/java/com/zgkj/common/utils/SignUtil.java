package com.zgkj.common.utils;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/11.
 * Descr:  参数签名
 */
public class SignUtil {

    /**
     * 参数签名的操作
     *
     * @param map
     * @param key
     * @return
     */
    public static String getSign(Map<String, String> map, String key){
        String mapHex = MD5Util.encode(formatSort(map));

        StringBuffer stringBuffer = new StringBuffer(mapHex);
        // 拼接Key
        stringBuffer.append(key);

        // 进行第二次MD5
        String md5Hex = MD5Util.encode(stringBuffer.toString());
        Log.e("sign", "createSign: 参数："+map+"sign"+md5Hex);
        return md5Hex;
    }


    /**
     * 参数排序并进行拼接
     *
     * @param map
     * @return
     */
    private static String formatSort(Map<String, String> map){
        Map<String, String> tempMap = map;

        List<Map.Entry<String, String>> entryList = new ArrayList<>(tempMap.entrySet());

        // 进行排序
        Collections.sort(entryList, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                // 参数名进行比较
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        // 创建一个可变的字符串对象
        StringBuffer stringBuffer = new StringBuffer();

        for (Map.Entry<String, String> entry : entryList){

            if (!TextUtils.isEmpty(entry.getKey())){

                String value = entry.getValue();

                // 通过&拼接字符串
                stringBuffer.append(value).append("&");
            }
        }
        int length = stringBuffer.length();
        if (length > 1){
            // 去掉尾部的&
            stringBuffer = stringBuffer.deleteCharAt(length - 1);
        }

        return stringBuffer.toString();
    }

    /**
     * 最新修改
     * @param parameters
     * @param key
     * @return
     */
    public static String createSign(Map<String,Object> parameters, String key){
        StringBuffer sb = new StringBuffer();
        StringBuffer sbkey = new StringBuffer();

        SortedMap<String,Object> sort=new TreeMap<String,Object>(parameters);
        Set es = sort.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext())
        {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            Object v = entry.getValue();
            //空值不传递，不参与签名组串
            if(null != v && !"".equals(v)) {
                sb.append(k + "=" + v + "&");
                sbkey.append(k + "=" + v + "&");
            }
        }
        if (sbkey.length() > 1){
            // 去掉尾部的&
            sbkey=sbkey.deleteCharAt(sbkey.length() - 1).append(key);
        }
        //MD5加密,结果转换为小写字符
        String sign = MD5Util.encode(String.valueOf(sbkey));
        Log.e("sign", "createSign: 参数："+sbkey+"：sign："+sign);
        return sign;
    }
}
