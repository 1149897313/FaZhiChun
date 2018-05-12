package com.zgkj.common.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Descr:   Json解析辅助工具类（使用gson进行解析）
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/1/25.
 */

public class JSONUtil {

    // 单例对象
    private static final JSONUtil sInstance;

    // 创建全局的Gson对象
    private final Gson mGson;

    // 初始化单例对象
    static {
        sInstance = new JSONUtil();

    }

    /**
     * 私有化的构造方法
     */
    private JSONUtil() {

        // 初始化Gson对象
        mGson = new GsonBuilder()
                // 智能解析为NULL的数据
                .serializeNulls()
                // 设置时间格式
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                // 构建对象
                .create();
    }


    /**
     * 检查返回的json数据是否为标准的json数据
     *
     * @param json
     * @return 如果为标准的json数据格式返回true，否则为false
     */
    private static boolean isValueJson(String json) {
        if (TextUtils.isEmpty(json)) {
            return false;
        }
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonParseException e) {
            return false;
        }
    }


    public static int getInt(String json, String key, int defValue) {
        if (isValueJson(json) && !TextUtils.isEmpty(key)) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                return jsonObject.getInt(key);
            } catch (JSONException e) {
                e.printStackTrace();
                return defValue;
            }
        } else {
            return defValue;
        }
    }


    public static long getLong(String json, String key, long defValue) {
        if (isValueJson(json) && !TextUtils.isEmpty(key)) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                return jsonObject.getLong(key);
            } catch (JSONException e) {
                e.printStackTrace();
                return defValue;
            }
        } else {
            return defValue;
        }
    }

    public static double getDouble(String json, String key, long defValue) {
        if (isValueJson(json) && !TextUtils.isEmpty(key)) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                return jsonObject.getDouble(key);
            } catch (JSONException e) {
                e.printStackTrace();
                return defValue;
            }

        } else {
            return defValue;
        }
    }

    public static boolean getBoolean(String json, String key, boolean defValue) {
        if (isValueJson(json) && !TextUtils.isEmpty(json)) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                return jsonObject.getBoolean(key);
            } catch (JSONException e) {
                e.printStackTrace();
                return defValue;
            }
        } else {
            return defValue;
        }
    }


    /**
     * 将json数据转换为对象
     *
     * @param json
     * @return
     */
    public static <T> T toObject(String json, Class<T> tClass) {
        if (isValueJson(json)) {
            try {
                return sInstance.mGson.fromJson(json, tClass);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }


    /**
     * 将json数据转换为List集合对象
     *
     * @param json
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> List<T> toArrayList(String json, Class<T> tClass) {
        if (isValueJson(json)) {
            try {
                return sInstance.mGson.fromJson(json, new TypeToken<List<T>>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        } else {
            return null;
        }
    }


    /**
     * 将json数据转换为Map集合对象
     *
     * @param json
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> Map<String, T> toHashMap(String json, Class<T> tClass) {
        if (isValueJson(json)) {
            try {
                return sInstance.mGson.fromJson(json, new TypeToken<Map<String, T>>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

}
