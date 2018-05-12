package com.zgkj.common.widgets.recycler.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;

import com.zgkj.common.widgets.recycler.callback.ISuspension;

import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/28.
 * Descr:   定义可实现悬浮停靠的ItemDecoration
 */

public class SuspensionItemDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;


    // 定义存储标题显示数据的集合对象
    private List<? extends ISuspension> mDataList;


    // 定义一个绘制背景色的画笔对象
    private Paint mPaint;

    // 定义一个绘制文字的画笔对象
    private TextPaint mTextPaint;

    // 定义用于存放测量文字的Rect
    private Rect mTextBounds;


    // 悬浮Header的高度
    private int mSuspensionHeaderHeight;

    // 悬浮Header的背景颜色
    private int mSuspensionHeaderBgColor;

    // 悬浮Header显示文字的颜色
    private int mSuspensionHeaderTextColor;

    // 悬浮Header显示文字的大小
    private int mSuspensionHeaderTextSize;

    // 悬浮Header显示文字距离左边的距离
    private int mSupensionHeaderTextLeftSpace;

    private static int COLOR_TITLE_BG = Color.parseColor("#EDEDED");
    private static int COLOR_TITLE_FONT = Color.parseColor("#FF999999");

    /**
     * 构造方法
     *
     * @param context
     * @param dataList
     */
    public SuspensionItemDecoration(Context context, List<? extends ISuspension> dataList) {
        mContext = context;
        mDataList = dataList;
        init(context);
    }

    /**
     * 初始化数据
     */
    private void init(Context context) {

        mSuspensionHeaderHeight = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                20,
                context.getResources().getDisplayMetrics());

        mSuspensionHeaderBgColor = COLOR_TITLE_BG;
        mSuspensionHeaderTextColor = COLOR_TITLE_FONT;
        mSuspensionHeaderTextSize = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                12,
                context.getResources().getDisplayMetrics());

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(mSuspensionHeaderBgColor);

        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        mTextPaint.setTextSize(mSuspensionHeaderTextSize);
        mTextPaint.setColor(mSuspensionHeaderTextColor);

        mTextBounds = new Rect();

    }


    public SuspensionItemDecoration setSuspensionHeaderHeight(int suspensionHeaderHeight) {
        mSuspensionHeaderHeight = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                suspensionHeaderHeight,
                mContext.getResources().getDisplayMetrics());
        return this;
    }


    public SuspensionItemDecoration setSuspensionHeaderBgColor(@ColorRes int suspensionHeaderBgColor) {
        mSuspensionHeaderBgColor = mContext.getResources().getColor(suspensionHeaderBgColor);
        mPaint.setColor(mSuspensionHeaderBgColor);
        return this;
    }


    public SuspensionItemDecoration setSuspensionHeaderTextColor(@ColorRes int suspensionHeaderTextColor) {
        mSuspensionHeaderTextColor = mContext.getResources().getColor(suspensionHeaderTextColor);
        mTextPaint.setColor(mSuspensionHeaderTextColor);
        return this;
    }

    public SuspensionItemDecoration setSuspensionHeaderTextSize(int suspensionHeaderTextSize) {
        mSuspensionHeaderTextSize = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                suspensionHeaderTextSize,
                mContext.getResources().getDisplayMetrics());
        mTextPaint.setTextSize(mSuspensionHeaderTextSize);
        return this;
    }

    public SuspensionItemDecoration setSupensionHeaderTextLeftSpace(int supensionHeaderTextLeftSpace){
        mSupensionHeaderTextLeftSpace = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                supensionHeaderTextLeftSpace,
                mContext.getResources().getDisplayMetrics());
        return this;
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        // 获取孩子的个数
        final int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View childView = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childView.getLayoutParams();
            // 得到孩子在当前布局中的索引位置
            int childPosition = layoutParams.getViewLayoutPosition();

            // 数组越界的判定
            if (mDataList == null || mDataList.size() <= 0
                    || childPosition < 0 || childPosition > mDataList.size() -1
                    || !mDataList.get(childPosition).isSuspension()){
                continue;
            }
            // RecyclerView的item的position在重置时可能为-1
            if (childPosition > -1){
                if (childPosition == 0) {
                    // 绘制悬浮的Title
                    drawTitleArea(canvas, childView, layoutParams, left, right, childPosition);
                } else {
                    // 如果文本内容不为空，同时与前一个Item项的文本内容不相同则代表是新的字母分类，也需要绘制
                    // 此时childPosition肯定是>=1的
                    if (mDataList.get(childPosition).getSuspensionTag() != null
                            && !mDataList.get(childPosition).getSuspensionTag().equals(mDataList.get(childPosition - 1).getSuspensionTag())) {
                        // 绘制悬浮的Title
                        drawTitleArea(canvas, childView, layoutParams, left, right, childPosition);
                    }
                }
            }
        }
    }

    /**
     * 绘制悬浮Title的背景和文字（最新绘制，绘制在最底层）
     *
     * @param canvas
     * @param childView
     * @param layoutParams
     * @param left
     * @param right
     * @param position
     */
    private void drawTitleArea(Canvas canvas, View childView, RecyclerView.LayoutParams layoutParams,
                               int left, int right, int position) {
        // 绘制背景，绘制在ChildView的顶部
        canvas.drawRect(left,
                childView.getTop() - layoutParams.topMargin - mSuspensionHeaderHeight,
                right,
                childView.getTop() - layoutParams.topMargin,
                mPaint);

        // 获取到需要绘制的文本对象
        String suspensionTag = mDataList.get(position).getSuspensionTag();

        // 测量需要绘制文本的尺寸
        mTextPaint.getTextBounds(suspensionTag, 0, suspensionTag.length(), mTextBounds);

        // 绘制文本，垂直居中绘制（绘制点是以左下角为坐标原点）
        canvas.drawText(suspensionTag,
                mSupensionHeaderTextLeftSpace,
                childView.getTop() - layoutParams.topMargin - mSuspensionHeaderHeight / 2 + mTextBounds.height() / 2,
                mTextPaint);
    }

    /**
     * 最后调用，绘制在最上层
     *
     * @param canvas
     * @param parent
     * @param state
     */
    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(canvas, parent, state);
        // 转换为线性布局管理器
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) parent.getLayoutManager();
        // 拿到第一个可见的Item的索引值
        int position = linearLayoutManager.findFirstVisibleItemPosition();

        // 校验数据
        if (mDataList == null || mDataList.size() <= 0
                || position < 0 || position > mDataList.size() - 1
                || !mDataList.get(position).isSuspension()) {
            return;
        }
        // 获取childView时出现bug，有时候childView为空，所以将 child = parent.getChildAt(i)---> parent.findViewHolderForLayoutPosition(pos).itemView
        View childView = parent.findViewHolderForLayoutPosition(position).itemView;

        // 获取需要需要绘制的文本对象
        String suspensionTag = mDataList.get(position).getSuspensionTag();

        // 定义一个内部的标识变量，标识当前画布canvas是否位移过
        boolean flag = false;
        // 判断索引值，防止数组越界（但是一般不会出现这种情况）
        if ((position + 1) < mDataList.size()) {

            // 如果当前第一个Item的tag不等于其后面一个Item的tag，说明悬浮的View需要切换了
            if (!TextUtils.isEmpty(suspensionTag)
                    && !suspensionTag.equals(mDataList.get(position + 1).getSuspensionTag())) {

                // 当第一个Item在屏幕中还剩下的高度小于悬浮Title的高度时，就应该做悬浮Title显示的“交换动画”了
                if (childView.getTop() + childView.getHeight() < mSuspensionHeaderHeight) {
                    // 每次绘制前保存当前画布canvas的状态
                    canvas.save();

                    // 改变标识变量的状态
                    flag = true;

                    // 进行画布的平移操作
                    // 上滑时将画布canvas上移（y为负数），所以后面canvas绘制出来的Rect和Text都上移了，有种切换动画的感觉
                    canvas.translate(0, childView.getTop() + childView.getHeight() - mSuspensionHeaderHeight);
                }
            }
        }

        // 绘制悬浮的title背景区域和文字
        canvas.drawRect(parent.getPaddingLeft(),
                parent.getPaddingTop(),
                parent.getRight() - parent.getPaddingRight(),
                parent.getPaddingTop() + mSuspensionHeaderHeight,
                mPaint);

        // 测量需要绘制的文字
        mTextPaint.getTextBounds(suspensionTag, 0, suspensionTag.length(), mTextBounds);

        // 绘制文本，垂直居中绘制（绘制点是以左下角为坐标原点）
        canvas.drawText(suspensionTag,
                mSupensionHeaderTextLeftSpace,
                parent.getPaddingTop() + mSuspensionHeaderHeight / 2 + mTextBounds.height() / 2,
                mTextPaint);

        // 恢复画布到之前保存的状态
        if (flag) {
            canvas.restore();
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        // 拿到View在RecyclerView中的索引位置
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        int position = layoutParams.getViewLayoutPosition();
        // 越界检查
        if (mDataList == null || mDataList.size() <= 0
                || position < 0 || position > mDataList.size() -1){
            return;
        }
        // 如果满足条件
        if (position > -1){
            // 拿到当前索引位置的数据对象
            ISuspension suspension = mDataList.get(position);
            if (suspension.isSuspension()) {

                if (position == 0) {
                    outRect.set(0, mSuspensionHeaderHeight, 0, 0);
                } else {

                    // 如果文本对象不为空，且跟前一个Item的tag不相同，说明是新的分类，也需要悬浮的Title
                    if (suspension.getSuspensionTag() != null
                            && !suspension.getSuspensionTag().equals(mDataList.get(position - 1).getSuspensionTag())) {
                        outRect.set(0, mSuspensionHeaderHeight, 0, 0);
                    }
                }
            }
        }



    }
}
