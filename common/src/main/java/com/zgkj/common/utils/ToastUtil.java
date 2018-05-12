package com.zgkj.common.utils;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.widget.TextViewCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.zgkj.common.app.Application;

import java.lang.reflect.Field;

/**
 * Descr:   Toast吐司工具类
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/1/19.
 */

public class ToastUtil {

    // 单例对象
    private static volatile ToastUtil sInstance;

    // 默认的背景颜色
    private static final int DEFAULT_COLOR = 0xFEFFFFFF;

    // 创建一个Handler对象，保证提示的内容在主线程中执行
    private Handler mHandler;

    // 创建一个Toast对象
    private Toast mToast;

    // 消息提示的位置，默认为顶部垂直居中
    private int mGravity = Gravity.CENTER;
    // X方向上的偏移量
    private int mOffsetX = 0;
    // Y方向上的偏移量
    private int mOffsetY = 0;

    // 背景颜色
    private int mBgColor = DEFAULT_COLOR;
    // 背景资源
    private int mBgResource = -1;

    // 字体颜色
    private int mTextColor;
    // 字体大小
    private int mTextSize = -1;

    // 显示动画
    private int mAnimId = -1;


    /**
     * 私有化的构造方法，初始化数据
     */
    private ToastUtil() {
        // 如果handler对象为空则进行初始化
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
    }

    /**
     * 获取单例对象
     *
     * @return
     */
    public static ToastUtil getInstance() {
        if (sInstance == null) {
            synchronized (ToastUtil.class) {
                if (sInstance == null) {
                    sInstance = new ToastUtil();
                }
            }
        }
        return sInstance;
    }


    /**
     * 显示一条信息
     *
     * @param text
     * @param duration
     */
    public void show(@NonNull final CharSequence text, final int duration) {
        showText(text, duration);
    }

    /**
     * 显示一条信息
     *
     * @param resId
     * @param duration
     */
    public void show(@StringRes final int resId, final int duration) {
        showText(Application.getInstance().getString(resId), duration);
    }

    /**
     * 设置消息的显示位置
     *
     * @param gravity
     * @param offsetX
     * @param offsetY
     */
    public ToastUtil setGravity(int gravity, int offsetX, int offsetY) {
        mGravity = gravity;
        mOffsetX = offsetX;
        mOffsetY = (int) (offsetY * Application.getInstance().getResources().getDisplayMetrics().density);
        return this;
    }

    /**
     * 设置背景颜色
     *
     * @param backgroundColor
     */
    public ToastUtil setBackgroundColor(@ColorInt final int backgroundColor) {

        mBgColor = backgroundColor;

        return this;
    }

    /**
     * 设置背景资源
     *
     * @param backgroundResource
     */
    public ToastUtil setBackgroundResource(@DrawableRes final int backgroundResource) {

        mBgResource = backgroundResource;

        return this;
    }

    /**
     * 设置消息的字体颜色
     *
     * @param textColor
     */
    public ToastUtil setTextColor(@ColorInt final int textColor) {
        mTextColor = textColor;
        return this;
    }


    /**
     * 设置显示字体的大小
     *
     * @param textSize
     * @return
     */
    public ToastUtil setTextSize(int textSize){
        mTextSize = textSize;
        return this;
    }


    /**
     * 设置显示动画
     *
     * @param animId
     * @return
     */
    public ToastUtil setAnim(int animId){

        mAnimId = animId;

        return this;
    }


    /**
     * 设置Toast的背景
     *
     * @param textView
     */
    private void setToastBackground(final TextView textView) {
        View view = mToast.getView();
        if (mBgResource != -1) {
            // 如果背景资源不为-1
            view.setBackgroundResource(mBgResource);
            textView.setBackgroundColor(Color.TRANSPARENT);
        } else if (mBgColor != DEFAULT_COLOR) {
            // 如果背景颜色不为默认的颜色值
            Drawable viewDrawable = view.getBackground();
            Drawable textDrawable = textView.getBackground();
            if (viewDrawable != null && textDrawable != null) {
                viewDrawable.setColorFilter(new PorterDuffColorFilter(mBgColor, PorterDuff.Mode.SRC_IN));
                textView.setBackgroundColor(Color.TRANSPARENT);
            } else if (viewDrawable != null) {
                viewDrawable.setColorFilter(new PorterDuffColorFilter(mBgColor, PorterDuff.Mode.SRC_IN));
            } else if (textDrawable != null) {
                textDrawable.setColorFilter(new PorterDuffColorFilter(mBgColor, PorterDuff.Mode.SRC_IN));
            } else {
                view.setBackgroundColor(mBgColor);
            }
        }
    }

    /**
     * 设置Toast显示字体的大小
     *
     * @param textView
     */
    private void setToastTextSize(TextView textView){
        if (textView != null && mTextSize != -1){
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize);
        }
    }


    /**
     * 设置Toast的显示动画（通过反射机制修改动画属性值）
     *
     * @param
     */
    private void setToastAnim(Toast toast){
        if (toast != null || mAnimId != -1){
            try {
                // 根据变量名获取对应的字段
                Field mTNField = toast.getClass().getDeclaredField("mTN");
                // 设置字段权限为公共的（暴力反射）
                mTNField.setAccessible(true);
                // 在反射类的对象上获取此字段的对象（即mNT字段的对象）
                Object mTNObject = mTNField.get(toast);

                // 根据变量名mParams获取对应的字段
                Field paramsField = mTNObject.getClass().getDeclaredField("mParams");
                paramsField.setAccessible(true);
                // 得到mParams字段对应的对象
                WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) paramsField.get(mTNObject);
                // 设置自定义的动画
                layoutParams.windowAnimations = mAnimId;

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 安全的显示一条信息
     *
     * @param text
     * @param duration
     */
    private void showText(final CharSequence text, final int duration) {
        // 在UI线程中执行显示
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                // 先取消显示
                cancel();
                // 得到一个Toast对象
                mToast = Toast.makeText(Application.getInstance(), text, duration);
                TextView textView = mToast.getView().findViewById(android.R.id.message);
                TextViewCompat.setTextAppearance(textView, android.R.style.TextAppearance);
                // 设置显示位置
                mToast.setGravity(mGravity, mOffsetX, mOffsetY);
                // 设置字体颜色
                textView.setTextColor(mTextColor);
                // 设置字体大小
                setToastTextSize(textView);
                // 设置背景颜色
                setToastBackground(textView);
                // 设置动画
                setToastAnim(mToast);

                // 显示Toast
                mToast.show();
            }
        });
    }


    /**
     * 实现取消显示Toast的方法
     */
    private void cancel() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }
}
