package com.zgkj.fazhichun.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.zgkj.common.widgets.load.view.AbsView;
import com.zgkj.fazhichun.R;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/21.
 * Descr:   显示定位失败的View
 */

public class LocationView extends AbsView {

    @Override
    protected int getLayoutSourceId() {
            return R.layout.layout_state_location;
    }

    @Override
    protected void onViewChildClick(View view, final OnViewChildClickListener onViewChildClickListener) {
        super.onViewChildClick(view, onViewChildClickListener);
        TextView textView = view.findViewById(R.id.location);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onViewChildClickListener.onChildClick(view);
            }
        });

    }
}
