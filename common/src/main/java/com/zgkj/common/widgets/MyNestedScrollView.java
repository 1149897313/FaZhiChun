package com.zgkj.common.widgets;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/1.
 * Descr:   重写NestedScrollView控件解决嵌套ViewPager时上下滑动不流畅的问题
 */

public class MyNestedScrollView extends NestedScrollView {


    public MyNestedScrollView(Context context) {
        super(context);
    }

    public MyNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    // 记录用户按下时的坐标点
    private float mDownPosX = 0;
    private float mDownPosY = 0;


    /**
     * 重写触摸事件的拦截方法
     * 默认返回值为false，表示不拦截TouchEvent，返回true则表示拦截TouchEvent，不再分发给子类
     *
     * @param event
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:       // 按下
                mDownPosX = x;
                mDownPosY = y;

                break;
            case MotionEvent.ACTION_MOVE:       // 滑动

                // 计算滑动距离的绝对值
                final float deltaX = Math.abs(x - mDownPosX);
                final float deltaY = Math.abs(y - mDownPosY);

                // 如果是上下滑动则拦截触摸事件，不下发给子类
                if (deltaY > deltaX){
                    return true;
                }
                break;
        }


        return super.onInterceptTouchEvent(event);
    }
}
