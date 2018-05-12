package com.zgkj.fazhichun.view;

import android.content.Context;
import android.view.View;

import com.zgkj.common.widgets.load.view.AbsView;
import com.zgkj.fazhichun.R;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/20.
 * Descr:   显示空数据的View
 */

public class EmptyView extends AbsView {


    @Override
    protected int getLayoutSourceId() {
        return R.layout.layout_state_empty;
    }


}
