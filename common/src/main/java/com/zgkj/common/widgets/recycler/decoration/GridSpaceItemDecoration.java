package com.zgkj.common.widgets.recycler.decoration;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zgkj.common.utils.DisplayUtil;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/19.
 * Descr:   宫格布局、瀑布流布局间距设置类的定义
 */
public class GridSpaceItemDecoration extends RecyclerView.ItemDecoration {

    // 列数
    private int mSpanCount;

    // 间距值
    private int mSpace;

    // 是否需要为屏幕边缘设置间距
    private boolean mIsIncludeEdge;


    public GridSpaceItemDecoration(Context context, int spanCount, int space) {
        this(context, spanCount, space, false);
    }

    public GridSpaceItemDecoration(Context context, int spanCount, int space, boolean isIncludeEdge) {
        mSpanCount = spanCount;
        mSpace = DisplayUtil.dp2px(context, space);
        mIsIncludeEdge = isIncludeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);

        // parent.getChildAdapterPosition(view)有可能等于-1的异常
        if (position != -1) {

            int column = position % mSpanCount; // item column

            if (mIsIncludeEdge) {
                outRect.left = mSpace - column * mSpace / mSpanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * mSpace / mSpanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < mSpanCount) { // top edge
                    outRect.top = mSpace;
                }
                outRect.bottom = mSpace; // item bottom
            } else {
                outRect.left = column * mSpace / mSpanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = mSpace - (column + 1) * mSpace / mSpanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= mSpanCount) {
                    outRect.top = mSpace; // item top
                }
            }
        }


    }
}
