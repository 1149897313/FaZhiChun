package com.zgkj.common.widgets.recycler.decoration;


import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zgkj.common.utils.DisplayUtil;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/19.
 * Descr:   定义线性管理布局器间距设置的定义
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {


    // 定义间距
    private int mSpace;


    /**
     * 构造方法，初始化数据
     *
     * @param context
     * @param space
     */
    public SpaceItemDecoration(Context context, int space){
        mSpace = DisplayUtil.dp2px(context, space);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager){

            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            if (linearLayoutManager.getOrientation() == LinearLayoutManager.VERTICAL){
                int position = parent.getChildAdapterPosition(view);
                if (position != -1){
                    outRect.top = mSpace;
                    if (position == 0){
                        outRect.top = 0;
                    }
                }

            }else if (linearLayoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL){
                int position = parent.getChildAdapterPosition(view);
                if (position != -1){

                    outRect.left = mSpace;
                    if (position == 0){
                        outRect.left = 0;
                    }
                }
            }
        }

    }
}
