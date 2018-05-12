package com.zgkj.common.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zgkj.common.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/18.
 * Descr:   自定义标签显示控件
 */
public class LabelsView extends ViewGroup {

    // label标签的背景颜色
    private int mLabelBackgroundColor;

    // label标签的字体颜色
    private int mLabelTextColor;

    // label标签的字体大小
    private float mLabelTextSize;

    // label标签文字距离左边的内边距
    private int mLabelTextPaddingLeft;

    // label标签文字距离顶部的内边距
    private int mLabelTextPaddingTop;

    // label标签文字距离右边的内边距
    private int mLabelTextPaddingRight;

    // label标签文字距离底部的内边距
    private int mLabelTextPaddingBottom;

    // label标签之间在垂直方向上的间距
    private int mLabelVerticalSpace;

    // label标签之间在水平方向上的间距
    private int mLabelHorizontalSpace;



    // 定义存放label标签文字的集合对象
    private List<Object> mLabelList;


    public LabelsView(Context context) {
        this(context, null);
    }

    public LabelsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造方法初始化数据
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public LabelsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 初始化数据
        init(context, attrs);
    }


    /**
     * 初始化自定义属性的值
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LabelsView);
        // 背景默认为白色
        mLabelBackgroundColor = typedArray.getResourceId(R.styleable.LabelsView_labelBackgroundColor, Color.WHITE);
        // 字体颜色默认为白色
        mLabelTextColor = typedArray.getColor(R.styleable.LabelsView_labelTextColor, Color.BLACK);
        // 默认字体大小为12sp
        mLabelTextSize = typedArray.getDimension(R.styleable.LabelsView_labelTextSize,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, context.getResources().getDisplayMetrics()));

        // 默认内边距为0dp
        mLabelTextPaddingLeft = typedArray.getDimensionPixelSize(R.styleable.LabelsView_labelTextPaddingLeft, 0);
        mLabelTextPaddingTop = typedArray.getDimensionPixelSize(R.styleable.LabelsView_labelTextPaddingTop, 0);
        mLabelTextPaddingRight = typedArray.getDimensionPixelSize(R.styleable.LabelsView_labelTextPaddingRight, 0);
        mLabelTextPaddingBottom = typedArray.getDimensionPixelSize(R.styleable.LabelsView_labelTextPaddingBottom, 0);
        mLabelVerticalSpace = typedArray.getDimensionPixelSize(R.styleable.LabelsView_labelVerticalSpace, 0);
        mLabelHorizontalSpace = typedArray.getDimensionPixelSize(R.styleable.LabelsView_labelHorizontalSpace, 0);

        typedArray.recycle();

        // 初始化集合对象
        mLabelList = new ArrayList<>();

        Log.i("TextSize ===", mLabelTextSize + "");
    }


    /**
     * 第一步先测量子控件的大小
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取容器中孩子的个数
        int childCount = getChildCount();
        // 获取父容器最大的宽度值
        int maxParentWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();

        // 记录内容的高度
        int contentHeight = 0;

        // 记录行的宽度
        int lineWidth = 0;

        // 记录最宽的行宽
        int maxLineWidth = 0;

        // 记录label标签高度最大的高度
        int maxLabelHeight = 0;

        // 是否是行的开头
        boolean isBegin = true;

        // 循环测量Label并计算父控件内容的宽高
        for (int i = 0; i < childCount; i++){
            View childView = getChildAt(i);
            // 测量孩子的尺寸
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);

            // 当前父容器宽度上面显示不下时则进行换行的操作
            if (maxParentWidth < lineWidth + childView.getMeasuredWidth()){
                // 先加上上一行label的最大高度
                contentHeight+= maxLabelHeight;
                // 加上水平方向上的间距
                contentHeight+= mLabelVerticalSpace;
                maxLineWidth = Math.max(maxLineWidth, lineWidth);
                // 初始化横向宽度和label的最大高度值
                maxLabelHeight = 0;
                lineWidth = 0;
                isBegin = true;
            }

            // 计算label的最大高度
            maxLabelHeight = Math.max(maxLabelHeight, childView.getMeasuredHeight());

            // 如果不是行的开头则加上label标签间水平方向上的间距
            if (!isBegin){
                lineWidth += mLabelHorizontalSpace;
            }else {
                isBegin = false;
            }
            // 最后加上孩子的宽度
            lineWidth += childView.getMeasuredWidth();
        }

        contentHeight += maxLabelHeight;
        maxLineWidth = Math.max(maxLineWidth, lineWidth);

        // 测量控件最终的宽高
        setMeasuredDimension(measureParentWidth(widthMeasureSpec, maxLineWidth),
                measureParentHeight(heightMeasureSpec, contentHeight));

    }


    /**
     * 计算父容器的宽度
     *
     * @param widthMeasureSpec
     * @param maxLineWidth
     * @return      返回计算后的父容器的宽度
     */
    private int measureParentWidth(int widthMeasureSpec, int maxLineWidth){
        int parentWidth = 0;

        // 得到测量模式
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        // 得到具体的宽度尺寸
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        // 如果测量模式为明确的值
        if (specMode == MeasureSpec.EXACTLY){
            parentWidth = specSize;
        }else {
            parentWidth = maxLineWidth + getPaddingLeft() + getPaddingRight();
            if (specMode == MeasureSpec.AT_MOST){
                parentWidth = Math.min(parentWidth, specSize);
            }
        }

        //这一句是为了支持minWidth属性
        parentWidth = Math.max(parentWidth, getSuggestedMinimumWidth());


        return parentWidth;
    }


    /**
     * 计算父容器的高度
     *
     * @param heightMeasureSpec
     * @param contentHeight
     * @return
     */
    private int measureParentHeight(int heightMeasureSpec, int contentHeight){
        int parentHeight = 0;

        // 得到测量模式
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        // 得到测量的高度尺寸
        int specSize = MeasureSpec.getSize(heightMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY){
            parentHeight = specSize;
        }else {
            parentHeight = contentHeight + getPaddingTop() + getPaddingBottom();
            if (specMode == MeasureSpec.AT_MOST){
                parentHeight = Math.min(parentHeight, specSize);
            }
        }

        //这一句是为了支持minHeight属性
        parentHeight = Math.max(parentHeight, getSuggestedMinimumHeight());

        return parentHeight;
    }



    /**
     * 第二步设置子控件在父容器中布局的位置
     *
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int contentWidth = right - left;
        int maxLabelHeight = 0;

        int width = getPaddingLeft();
        int height = getPaddingTop();

        int childCount = getChildCount();

        // 循环摆放label
        for (int i = 0; i < childCount; i++){
            View childView = getChildAt(i);

            // 当前行显示不下label时进行换行的处理
            if (contentWidth < width + childView.getMeasuredWidth() + getPaddingRight()){
                // 重置宽度值
                width = getPaddingLeft();
                // 计算高度值
                height += mLabelVerticalSpace;
                height += maxLabelHeight;
                maxLabelHeight = 0;
            }

            // 布局子控件
            childView.layout(width, height, width + childView.getMeasuredWidth(), height + childView.getMeasuredHeight());

            width += mLabelHorizontalSpace;
            width += childView.getMeasuredWidth();

            maxLabelHeight = Math.max(maxLabelHeight, childView.getMeasuredHeight());
        }
    }


    public void setLabels(List<String> labelList){

        setLabels(labelList, new LabelTextProvider<String>() {
            @Override
            public CharSequence getLabelText(TextView label, String date, int position) {
                return date;
            }
        });
    }

    /**
     * 设置需要显示的标签
     *
     * @param labelList
     * @param labelTextProvider
     * @param <T>
     */
    public <T> void setLabels(List<T> labelList, LabelTextProvider<T> labelTextProvider){
        // 先移除所有的子控件
        removeAllViews();
        // 清空数据集
        mLabelList.clear();

        // 根据数据集合的个数循环添加对应的label对象到父容器中
        if (labelList != null && labelList.size() > 0){
            mLabelList.addAll(labelList);
            int size = mLabelList.size();
            for (int i = 0; i < size; i++){
                addLabel(labelList.get(i), i, labelTextProvider);
            }
        }
    }

    /**
     * 添加一个Label标签的方法
     *
     * @param date
     * @param position
     * @param labelTextProvider
     * @param <T>
     */
    private <T> void addLabel(T date, int position, LabelTextProvider<T> labelTextProvider){
        final TextView label = new TextView(getContext());
        // 设置背景
        label.setBackgroundResource(mLabelBackgroundColor);
        // 设置内边距
        label.setPadding(mLabelTextPaddingLeft,
                mLabelTextPaddingTop,
                mLabelTextPaddingRight,
                mLabelTextPaddingBottom);
        // 设置字体颜色
        label.setTextColor(mLabelTextColor);
        // 设置字体大小
        label.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLabelTextSize);
        // 设置显示的文字
        label.setText(labelTextProvider.getLabelText(label, date, position));

        // 添加到父容器中
        addView(label);

    }



    /**
     * 定义给标签提供最终需要显示的数据的接口
     *
     * @param <T>   泛型数据
     */
    private interface LabelTextProvider<T> {

        CharSequence getLabelText(TextView label, T date, int position);
    }
}
