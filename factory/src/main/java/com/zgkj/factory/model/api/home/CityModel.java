package com.zgkj.factory.model.api.home;

import com.zgkj.common.widgets.recycler.side.model.AbsIndex;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/29.
 * Descr:   定位城市的Model
 */

public class CityModel extends AbsIndex {

    private String name;


    @Override
    public boolean isNeedToPinyin() {
        return true;
    }

    @Override
    public String getTarget() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
