package com.zgkj.common.utils;

import android.content.Context;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zgkj.common.app.Activity;

import java.lang.reflect.Field;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/1.
 * Descr:   控件设置相关的辅助工具类
 */

public class WidgetUtil {


    /**
     * 私有化的构造方法，禁止通过new的方式创建对象
     */
    private WidgetUtil() {

        // 抛出不支持的操作异常
        throw new UnsupportedOperationException("cannot be instantiated!");
    }


    /**
     * 设置TabLayout指示器宽度（根据Tab显示内容的宽度来设置指示器的宽度）
     *
     * @param tabLayout
     */
    public static void setTabLayoutIndicator(final TabLayout tabLayout) {
        // 校验数据
        if (tabLayout == null) {
            return;
        }
        tabLayout.post(new Runnable() {
            @Override
            public void run() {

                try {
                    LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);

                    for (int i = 0; i < linearLayout.getChildCount(); i++) {

                        View tabView = linearLayout.getChildAt(i);

                        // 拿到tabView的mTextView属性
                        // tab的字数不固定一定用反射取mTextView
                        Field textViewField = tabView.getClass().getDeclaredField("mTextView");
                        textViewField.setAccessible(true);

                        TextView textView = (TextView) textViewField.get(tabView);

                        tabView.setPadding(0, 0, 0, 0);

                        //显示内容有多宽就设置指示线为多宽，所以需要测量mTextView的宽度
                        int width = 0;
                        width = textView.getWidth();
                        if (width == 0) {
                            textView.measure(0, 0);
                            width = textView.getMeasuredWidth();
                        }
                        // 设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        layoutParams.width = width;
//                        layoutParams.leftMargin = 40;
//                        layoutParams.rightMargin = 40;
                        tabView.setLayoutParams(layoutParams);
                        // 重绘
                        tabView.invalidate();
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });


    }


    public static void setIndicator(Context context, TabLayout tabLayout, int leftMargin, int rightMargin) {
        try {
            Class<?> tablayout1 = tabLayout.getClass();
            Field tabStrip = null;

            tabStrip = tablayout1.getDeclaredField("mTabStrip");

            tabStrip.setAccessible(true);
            LinearLayout linearLayout = null;

            linearLayout = (LinearLayout) tabStrip.get(tabLayout);


            int left = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    leftMargin,
                    context.getResources().getDisplayMetrics());

            int right = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    rightMargin,
                    context.getResources().getDisplayMetrics());

            // 循环获取子控件并设置子控件的宽度
            for (int i = 0; i < linearLayout.getChildCount(); i++) {

                View tabView = linearLayout.getChildAt(i);
                tabView.setPadding(0, 0, 0, 0);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                layoutParams.leftMargin = left;
                layoutParams.rightMargin = right;
                tabView.setLayoutParams(layoutParams);
                tabView.invalidate();
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
