package com.zgkj.common.widgets.recycler.callback;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/23.
 * Descr:   定义一个分类悬停的接口
 */

public interface ISuspension {

    /**
     * 是否需要悬停显示Title
     *
     * @return
     */
    boolean isSuspension();


    /**
     * 获取悬停的Title
     *
     * @return
     */
    String getSuspensionTag();


}
