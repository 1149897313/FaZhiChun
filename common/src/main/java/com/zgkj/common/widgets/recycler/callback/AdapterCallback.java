package com.zgkj.common.widgets.recycler.callback;

import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;

/**
 * Descr:   RecyclerViewAdapter适配器的回掉接口
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2017-12-6.
 */

public interface AdapterCallback<T> {

    /**
     * 实现更新RecyclerView刷新数据显示的方法
     *
     * @param data      需要更新显示的泛型数据
     * @param holder    当前适配的ViewHolder对象
     */
    void update(T data, RecyclerViewAdapter.ViewHolder<T> holder);
}
