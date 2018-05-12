package com.zgkj.common.widgets.recycler.side.utils;

import android.text.TextUtils;

import com.github.promeg.pinyinhelper.Pinyin;
import com.zgkj.common.widgets.recycler.side.model.AbsIndex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/23.
 * Descr:   定义SideIndexBar数据的处理工具类（主要做数据的排序和装换为拼音）
 */

public class IndexUtil {

    /**
     * 添加排序后的源数据到对应的索引列表数据集合中
     *
     * @param sourceDataList
     * @return
     */
    public static List<String> getIndexListData(List<? extends AbsIndex> sourceDataList) {

        // 校验数据
        if (sourceDataList == null || sourceDataList.size() <= 0) {
            return null;
        }

        // 将汉字转换为拼音
        transformPinyin(sourceDataList);

        // 截取拼音首字母
        transformTag(sourceDataList);

        // 排序
        sortSourceData(sourceDataList);

        List<String> indexList = new ArrayList<>();

        String indexTag;
        for (int i = 0; i < sourceDataList.size(); i++) {
            AbsIndex absIndex = sourceDataList.get(i);
            if (absIndex != null) {
                indexTag = sourceDataList.get(i).getIndexTag();
                if (!TextUtils.isEmpty(indexTag)) {
                    // 如果集合中不存在当前的索引目标则将目标对象添加到索引集合中
                    if (!indexList.contains(indexTag)) {
                        indexList.add(indexTag);
                    }
                }
            }
        }
        return indexList;
    }


    /**
     * 将城市的名字转换为拼音
     *
     * @param dataList
     * @return
     */
    private static void transformPinyin(List<? extends AbsIndex> dataList) {
        // 校验数据
        if (dataList == null || dataList.size() <= 0) {
            return;
        }
        // 循环的将汉字转换为拼音对象
        for (int i = 0; i < dataList.size(); i++) {
            // 获取对应索引位置的的对象
            AbsIndex absIndex = dataList.get(i);
            if (absIndex != null) {
                // 如果需要将城市名转换为拼音
                if (absIndex.isNeedToPinyin()) {
                    // 拿到需要转换的对象
                    String target = absIndex.getTarget();
                    if (TextUtils.isEmpty(target)) {
                        continue;
                    } else {
                        // 创建一个可变的字符串操作对象
                        StringBuilder pinYinSB = new StringBuilder();
                        for (int j = 0; j < target.length(); j++) {
                            char charAt = target.charAt(j);
                            String pinyin = Pinyin.toPinyin(charAt);
                            pinYinSB.append(pinyin.toUpperCase());
                        }
                        absIndex.setIndexPinyin(pinYinSB.toString());
                    }
                }
            }

        }
    }

    /**
     * 截取拼音对象的首字母
     *
     * @param dataList
     * @return
     */
    private static void transformTag(List<? extends AbsIndex> dataList) {
        // 校验数据
        if (dataList == null || dataList.size() <= 0) {
            return;
        }
        // 循环截取拼音的首字母
        for (int i = 0; i < dataList.size(); i++) {
            AbsIndex absIndex = dataList.get(i);
            if (absIndex != null) {
                if (absIndex.isNeedToPinyin()) {
                    String pinyin = absIndex.getIndexPinyin();
                    String letter = pinyin.substring(0, 1);
                    // 对首字母进行正则匹配判断
                    if (letter.matches("[A-Z]")) {
                        absIndex.setIndexTag(letter);
                    } else {
                        absIndex.setIndexTag("#");
                    }
                }
            }
        }
    }


    /**
     * 对源数据进行排序的操作
     *
     * @param dataList
     * @return
     */
    private static void sortSourceData(List<? extends AbsIndex> dataList) {
        if (dataList == null || dataList.size() <= 0) {
            return;
        }
        Collections.sort(dataList, new Comparator<AbsIndex>() {
            @Override
            public int compare(AbsIndex t1, AbsIndex t2) {
                // 返回0表示前者与后者相同
                // 返回1表示前者比后者大
                // 返回-1表示前者比后者小
                if (!t1.isNeedToPinyin()) {
                    return 0;
                } else if (!t2.isNeedToPinyin()) {
                    return 0;
                } else if ("#".equals(t1.getIndexTag())) {
                    return 1;
                } else if ("#".equals(t2.getIndexTag())) {
                    return -1;
                }
                return t1.getIndexPinyin().compareTo(t2.getIndexPinyin());
            }
        });
    }

}
