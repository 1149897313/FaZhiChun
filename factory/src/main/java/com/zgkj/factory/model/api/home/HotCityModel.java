package com.zgkj.factory.model.api.home;

import com.zgkj.common.widgets.recycler.side.model.AbsIndex;

import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/29.
 * Descr:
 */

public class HotCityModel extends AbsIndex {

    // 城市的名字
    private String name;

    private List<HotCityModel> hotCityModelList;
    //悬停ItemDecoration显示的Tag
    private String suspensionTag;

    public HotCityModel(){

    }

    public HotCityModel(String name) {
        this.name = name;
    }

    public HotCityModel(String suspensionTag, String indexTag) {
        this.suspensionTag = suspensionTag;
        setIndexTag(indexTag);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<HotCityModel> getHotCityModelList() {
        return hotCityModelList;
    }

    public void setHotCityModelList(List<HotCityModel> hotCityModelList) {
        this.hotCityModelList = hotCityModelList;
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
        return "热门城市";
    }
}
