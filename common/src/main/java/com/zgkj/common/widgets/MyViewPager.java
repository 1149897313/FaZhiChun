package com.zgkj.common.widgets;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/2/28.
 * Descr:   当我们用ScrollView嵌套ViewPager的时候，
 *          如果每个ViewPager的页面高度不同会导致下面有一部分的空白，
 *          所以要在ViewPager改变页面的时候重置它的高度，所以要重写ViewPager
 */

public class MyViewPager extends ViewPager {

    // 定义一个集合对象用于保存ViewPager的position与对应显示View的高度
    private HashMap<Integer, Integer> mMaps = new LinkedHashMap<>();

    // 当前显示孩子的索引位置
    private int mShowChildViewPosition;


    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    /**
     * 重写测量高度的方法
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        // 遍历所有孩子的高度
        for (int i = 0; i < this.getChildCount(); i++){
            View childView = getChildAt(i);
            // 重新测量孩子的高度
            // 指定模式（MeasureSpec.UNSPECIFIED）表示当前组件，可以随便用空间不受限制
            childView.measure(widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

            // 得到孩子的高度
            int measuredHeight = childView.getMeasuredHeight();

            // 将孩子对应的索引和测量的高度保存到集合中
            mMaps.put(i, measuredHeight);
        }

        // 如果存在多个孩子，则显示高度则为当前孩子的显示高度
        if (getChildCount() > 0){
            height = getChildAt(mShowChildViewPosition).getMeasuredHeight();
        }

        // 重新测量计算ViewPager需要显示的高度
        // （MeasureSpec.EXACTLY）精确模式
        // 在这种模式下，尺寸的值是多少，那么这个组件的长或宽就是多少。
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 在切换tab的时候,重置ViewPager的高度
     *
     * @param showChildViewPosition     显示Tab的索引值
     */
    public void resetShowHeight(int showChildViewPosition){
        mShowChildViewPosition = showChildViewPosition;

        if (mMaps.size() > showChildViewPosition){
            // 得到ViewPager的布局属性对象
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
            // 如果为空则新建一个布局属性对象
            if (layoutParams == null){
                layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        mMaps.get(showChildViewPosition));
            }else {
                layoutParams.height = mMaps.get(showChildViewPosition);
            }
            // 重新设置改变高度后的属性
            setLayoutParams(layoutParams);
        }
    }




}
