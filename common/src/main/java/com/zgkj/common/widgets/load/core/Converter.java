package com.zgkj.common.widgets.load.core;

import com.zgkj.common.widgets.load.view.AbsView;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/20.
 * Descr:   转换器接口
 */

public interface Converter<T> {

    Class<? extends AbsView> map(T t);
}
