package com.zgkj.common.widgets.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;

import com.zgkj.common.utils.DisplayUtil;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/9.
 * Descr:   自定义dialog弹窗控件的封装（构建这模式实现）
 */

public class CustomDialog extends Dialog {

    // 上下文对象
    private Context mContext;

    // dialog需要显示的View
    private View mContentView;

    // dialog显示的宽度和高度
    private int mWidth;
    private int mHeight;


    // 显示的动画样式
    @StyleRes
    private int mAnimResId;

    // Dialog显示的位置
    private int mGravity;


    // 是否可以点击返回按钮关闭dialog
    private boolean mCancelable = false;

    // 是否可以点击dialog显示区域外部的屏幕区域关闭dialog
    private boolean mCanceledOnTouchOutside = false;


    private CustomDialog(Builder builder) {
        super(builder.mContext);
        init(builder);
    }

    private CustomDialog(Builder builder, int themeResId) {
        super(builder.mContext, themeResId);
        init(builder);
    }


    /**
     * 初始化数据
     *
     * @param builder
     */
    private void init(Builder builder) {
        mContext = builder.mContext;
        mContentView = builder.mContentView;
        mWidth = builder.mWidth;
        mHeight = builder.mHeight;
        mAnimResId = builder.mAnimResId;
        mGravity = builder.mGravity;
        mCancelable = builder.mCancelable;
        mCanceledOnTouchOutside = builder.mCanceledOnTouchOutside;
    }


    /**
     * 重写创建Dialog的方法，设置配置的相关属性值
     *
     * @param savedInstanceState
     */
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(mContentView);
        setCancelable(mCancelable);
        setCanceledOnTouchOutside(mCanceledOnTouchOutside);

        // 设置显示属性
        Window window = getWindow();
        View decorView = window.getDecorView();
        decorView.setPadding(0, 0, 0, 0);
        decorView.setBackground(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = mWidth;
        layoutParams.height = mHeight;
        layoutParams.gravity = mGravity;
        window.setAttributes(layoutParams);
        // 设置显示动画
        if (mAnimResId != -1){
            window.setWindowAnimations(mAnimResId);
        }
    }


    /**
     * 将dp值转换为px值，保证尺寸大小不变
     *
     * @param context
     * @param dpValue
     * @return
     */
    private static int dp2px(Context context, float dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5f);
    }


    /**
     * 定义内部的构建者类
     */
    public static final class Builder {

        // 上下文对象
        private Context mContext;

        // 需要显示的View对象
        private View mContentView;

        // dialog显示的宽度和高度
        private int mWidth;
        private int mHeight;

        // 显示的主题样式的StyleId
        @StyleRes
        private int mThemeResId = -1;

        // 设置dialog动画显示动画的样式，默认无显示动画
        @StyleRes
        private int mAnimResId = -1;

        // 设置dialog的显示位置，默认为居中显示
        private int mGravity = Gravity.CENTER;


        // 是否可以点击返回按钮关闭dialog
        private boolean mCancelable = false;

        // 是否可以点击dialog显示区域外部的屏幕区域关闭dialog
        private boolean mCanceledOnTouchOutside = false;


        /**
         * 构造函数，初始化上下文对象
         *
         * @param context
         */
        public Builder(Context context) {
            mContext = context;
        }


        public Builder setContentView(@LayoutRes int layoutResId) {
            mContentView = LayoutInflater.from(mContext).inflate(layoutResId, null);
            return this;
        }

        public Builder setContentView(View contentView) {
            mContentView = contentView;
            return this;
        }


        public Builder setDialogWidthAndDP(int dpWidth) {
            mWidth = dp2px(mContext, dpWidth);
            return this;
        }

        public Builder setDialogWidthAndPX(int pxWidth) {
            mWidth = pxWidth;
            return this;
        }

        public Builder setDialogWidthAndDimenRes(@DimenRes int dimenResWidth) {
            // getDimensionPixelOffset(@DimenRes int id) 方法是获取某个dimen的值。
            // 如果DimenResId对应资源的单位是dp或sp，则需要将其乘以density。
            // 如果是px，则不乘。
            mWidth = mContext.getResources().getDimensionPixelOffset(dimenResWidth);
            return this;
        }

        public Builder setDialogHeightAndDP(int dpHeight) {
            mHeight = dp2px(mContext, dpHeight);
            return this;
        }

        public Builder setDialogHeightAndPX(int pxHeight) {
            mHeight = pxHeight;
            return this;
        }


        public Builder setDialogHeightAndDimenRes(@DimenRes int dimenResHeight) {
            mHeight = mContext.getResources().getDimensionPixelOffset(dimenResHeight);
            return this;
        }


        /**
         * 设置显示主题样式的资源ID
         *
         * @param themeResId
         * @return
         */
        public Builder setThemeStyle(@StyleRes int themeResId) {
            mThemeResId = themeResId;
            return this;
        }


        /**
         * 设置显示的动画样式
         *
         * @return
         */
        public Builder setAnimStyle(@StyleRes int animResId){
            mAnimResId = animResId;
            return this;
        }


        /**
         * 设置dialog显示的位置
         *
         * @param gravity
         * @return
         */
        public Builder setGravity(int gravity){
            mGravity = gravity;
            return this;
        }



        /**
         * @param cancelable
         * @return
         */
        public Builder setCancelable(boolean cancelable) {
            mCancelable = cancelable;
            return this;
        }


        public Builder setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
            mCanceledOnTouchOutside = canceledOnTouchOutside;
            return this;
        }


        /**
         * 为item显示控件注册点击监听事件
         *
         * @param itemId
         * @param onClickListener
         * @return
         */
        public Builder addOnItemClickListener(@IdRes int itemId, View.OnClickListener onClickListener) {
            mContentView.findViewById(itemId).setOnClickListener(onClickListener);
            return this;
        }


        /**
         * 创建一个自定义的CustomDialog的对象
         *
         * @return
         */
        public CustomDialog create() {
            if (mThemeResId == -1) {
                return new CustomDialog(this);
            } else {
                return new CustomDialog(this, mThemeResId);
            }
        }

    }


}
