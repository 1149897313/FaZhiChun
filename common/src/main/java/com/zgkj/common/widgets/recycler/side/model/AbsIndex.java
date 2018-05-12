package com.zgkj.common.widgets.recycler.side.model;

import com.zgkj.common.widgets.recycler.callback.ISuspension;

import java.io.Serializable;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/23.
 * Descr:   索引实体对象的基类
 */

public abstract class AbsIndex implements ISuspension, Serializable {

    // 所属的分类，城市名字拼音的首字符
    private String indexTag;

    // 城市的拼音
    private String indexPinyin;

    @Override
    public boolean isSuspension() {
        return true;
    }

    @Override
    public String getSuspensionTag() {
        return indexTag;
    }

    public String getIndexTag() {
        return indexTag;
    }

    public void setIndexTag(String indexTag) {
        this.indexTag = indexTag;
    }

    public String getIndexPinyin() {
        return indexPinyin;
    }

    public void setIndexPinyin(String indexPinyin) {
        this.indexPinyin = indexPinyin;
    }

    /**
     * 显示目标是否需要被转换为拼音
     *
     * @return
     */
    public abstract boolean isNeedToPinyin();

    /**
     * 返回需要被转换为拼音的目标对象（即城市对应的的名字）
     *
     * @return
     */
    public abstract String getTarget();




}
