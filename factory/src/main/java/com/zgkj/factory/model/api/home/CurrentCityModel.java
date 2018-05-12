package com.zgkj.factory.model.api.home;

import com.zgkj.common.widgets.recycler.side.model.AbsIndex;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/29.
 * Descr:
 */

public class CurrentCityModel extends AbsIndex {

    // 城市的名字
    private String name;

    //悬停ItemDecoration显示的Tag
    private String suspensionTag;

    public CurrentCityModel(String suspensionTag, String indexTag) {
        this.suspensionTag = suspensionTag;
        setIndexTag(indexTag);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isNeedToPinyin() {
        return false;
    }

    @Override
    public String getTarget() {
        return name;
    }

    @Override
    public String getSuspensionTag() {
        return suspensionTag;
    }

}
