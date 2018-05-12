package com.zgkj.common.widgets.recycler.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zgkj.common.utils.DisplayUtil;

/**
 * Descr:   自定义分割线
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2017/12/27.
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {


    // 水平布局
    public static final int HORIZONTAL = LinearLayoutManager.HORIZONTAL;

    // 垂直布局
    public static final int VERTICAL = LinearLayoutManager.VERTICAL;

    // 定义分割线的Drawable对象
    private Drawable mDrawableDivider;

    // 定义布局方向的控制变量
    private int mOrientation;

    // 是否绘制底部的分割线
    private boolean mIsDrawBottomDivider;

    // 设置分割线距离边缘的偏移量
    private int mMarginOffsets;


    /**
     * 构造方法，初始化数据
     *
     * @param context
     * @param orientation
     * @param resId
     */
    public DividerItemDecoration(Context context, int orientation, @DrawableRes int resId) {
        this(context, orientation, resId, 0);
    }


    public DividerItemDecoration(Context context, int orientation, @DrawableRes int resId, int marginOffsets) {
        this(context, orientation, resId, marginOffsets, false);
    }


    public DividerItemDecoration(Context context, int orientation, @DrawableRes int resId, boolean isDrawBottomDivider) {
        this(context, orientation, resId, 0, isDrawBottomDivider);
    }

    public DividerItemDecoration(Context context, int orientation, @DrawableRes int resId, int marginOffsets, boolean isDrawBottomDivider) {
        // 初始化分割线的资源对象
        mDrawableDivider = context.getResources().getDrawable(resId);
        mIsDrawBottomDivider = isDrawBottomDivider;
        mMarginOffsets = DisplayUtil.dp2px(context, marginOffsets);
        setOrientation(orientation);
    }


    /**
     * 初始化布局方向控制变量
     *
     * @param orientation
     */
    private void setOrientation(int orientation) {

        // 如果布局方向不为垂直方向也不为水平方向则抛出异常
        if (orientation != VERTICAL && orientation != HORIZONTAL) {

            throw new IllegalArgumentException("invalid orientation");
        }

        // 否则则初始化布局方向的控制变量
        mOrientation = orientation;
    }


    /**
     * 重写绘制的方法
     *
     * @param canvas
     * @param parent
     * @param state
     */
    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        // 根据布局方向调用不同的绘制方法
        if (mOrientation == VERTICAL) {
            // 垂直方向
            drawVerticalDivider(canvas, parent);
        } else if (mOrientation == HORIZONTAL) {
            // 水平方向
            drawHorizontalDivider(canvas, parent);
        }

    }


    /**
     * 垂直方向上绘制分割线的方法
     *
     * @param canvas
     * @param parent
     */
    private void drawVerticalDivider(Canvas canvas, RecyclerView parent) {
        // 获取RecyclerView的左右内边距，即为分割线左右的绘制位置
        int lineLeft = parent.getPaddingLeft();
        int lineRight = parent.getWidth() - parent.getPaddingRight();

        // 计算RecyclerView孩子的数量
        int childCount = parent.getChildCount();
        if (!mIsDrawBottomDivider) {
            childCount--;
        }

        for (int i = 0; i < childCount; i++) {

            // 得到当前索引的孩子对象
            View childView = parent.getChildAt(i);

            // 得到孩子控件的布局参数对象
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childView.getLayoutParams();

            // 计算分割线上下的绘制位置
            int lineTop = childView.getBottom() + layoutParams.bottomMargin;
            int lineBottom = lineTop + mDrawableDivider.getIntrinsicHeight();

            // 设置分割线的绘制位置
            mDrawableDivider.setBounds(lineLeft + mMarginOffsets, lineTop, lineRight - mMarginOffsets, lineBottom);

            // 绘制
            mDrawableDivider.draw(canvas);
        }
    }


    /**
     * 水平方向上绘制分割线的方法
     *
     * @param canvas
     * @param parent
     */
    private void drawHorizontalDivider(Canvas canvas, RecyclerView parent) {

        // 获取RecyclerView的上线内边距，即为分割线上下的绘制位置
        int lineTop = parent.getPaddingTop();
        int lineBottom = parent.getHeight() - parent.getPaddingBottom();

        // 计算RecyclerView孩子的数量
        int childCount = parent.getChildCount();
        if (!mIsDrawBottomDivider) {
            childCount--;
        }

        for (int i = 0; i < childCount; i++) {
            // 得到当前索引位置的孩子的对象
            View childView = parent.getChildAt(i);

            // 得到孩子控件的布局参数对象
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childView.getLayoutParams();

            // 计算分割线左右的绘制位置
            int lineLeft = childView.getRight() + layoutParams.rightMargin;
            int lineRight = lineLeft + mDrawableDivider.getIntrinsicWidth();

            // 设置分割线的绘制位置
            mDrawableDivider.setBounds(lineLeft, lineTop, lineRight, lineBottom);

            // 绘制到画布上
            mDrawableDivider.draw(canvas);
        }
    }


    /**
     * 重写Item偏移量的方法
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if (mOrientation == VERTICAL) {
            outRect.set(0, 0, 0, mDrawableDivider.getIntrinsicHeight());
        } else if (mOrientation == HORIZONTAL) {
            outRect.set(0, 0, mDrawableDivider.getIntrinsicWidth(), 0);
        }

    }
}
