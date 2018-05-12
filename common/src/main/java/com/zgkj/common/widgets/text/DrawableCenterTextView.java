package com.zgkj.common.widgets.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;

import com.zgkj.common.R;

/**
 * Descr:   自定义使带有drawable图标的文本控件居中(只限定为drawableRight属性)
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2017/12/8.
 */

public class DrawableCenterTextView extends android.support.v7.widget.AppCompatTextView {

    private Drawable[] mDrawableIcons;

    private int mDrawableLeftIconWidth;
    private int mDrawableLeftIconHeight;

    private int mDrawableTopIconWidth;
    private int mDrawableTopIconHeight;

    private int mDrawableRightIconWidth;
    private int mDrawableRightIconHeight;

    private int mDrawableBottomIconWidth;
    private int mDrawableBottomIconHeight;


    public DrawableCenterTextView(Context context) {
        this(context, null);
    }

    public DrawableCenterTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawableCenterTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // 初始化数组
        mDrawableIcons = new Drawable[4];

        // 加载配置属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DrawableCenterTextView);

        mDrawableLeftIconWidth = typedArray.getDimensionPixelOffset(R.styleable.DrawableCenterTextView_drawableLeftIconWidth, 0);
        mDrawableLeftIconHeight = typedArray.getDimensionPixelOffset(R.styleable.DrawableCenterTextView_drawableLeftIconHeight, 0);

        mDrawableTopIconWidth = typedArray.getDimensionPixelOffset(R.styleable.DrawableCenterTextView_drawableTopIconWidth, 0);
        mDrawableTopIconHeight = typedArray.getDimensionPixelOffset(R.styleable.DrawableCenterTextView_drawableTopIconHegiht, 0);

        mDrawableRightIconWidth = typedArray.getDimensionPixelOffset(R.styleable.DrawableCenterTextView_drawableRightIconWidth, 0);
        mDrawableRightIconHeight = typedArray.getDimensionPixelOffset(R.styleable.DrawableCenterTextView_drawableRightIconHeight, 0);

        mDrawableBottomIconWidth = typedArray.getDimensionPixelOffset(R.styleable.DrawableCenterTextView_drawableBottomIconWidth, 0);
        mDrawableBottomIconHeight = typedArray.getDimensionPixelOffset(R.styleable.DrawableCenterTextView_drawableBottomIconHeight, 0);

        Drawable leftDrawableIcon = typedArray.getDrawable(R.styleable.DrawableCenterTextView_drawableLeftIcon);
        if (leftDrawableIcon != null) {
            mDrawableIcons[0] = leftDrawableIcon;
            if (mDrawableLeftIconWidth == 0) {
                mDrawableLeftIconWidth = leftDrawableIcon.getIntrinsicWidth();
            }
            if (mDrawableLeftIconHeight == 0) {
                mDrawableLeftIconHeight = leftDrawableIcon.getIntrinsicHeight();
            }
        }
        Drawable topDrawableIcon = typedArray.getDrawable(R.styleable.DrawableCenterTextView_drawableTopIcon);
        if (topDrawableIcon != null) {
            mDrawableIcons[1] = topDrawableIcon;
            if (mDrawableTopIconWidth == 0) {
                mDrawableTopIconWidth = topDrawableIcon.getIntrinsicWidth();
            }
            if (mDrawableTopIconHeight == 0) {
                mDrawableTopIconHeight = topDrawableIcon.getIntrinsicHeight();
            }
        }
        Drawable rightDrawableIcon = typedArray.getDrawable(R.styleable.DrawableCenterTextView_drawableRightIcon);
        if (rightDrawableIcon != null) {
            mDrawableIcons[2] = rightDrawableIcon;
            if (mDrawableRightIconWidth == 0) {
                mDrawableRightIconWidth = rightDrawableIcon.getIntrinsicWidth();
            }
            if (mDrawableRightIconHeight == 0) {
                mDrawableRightIconHeight = rightDrawableIcon.getIntrinsicHeight();
            }

        }
        Drawable bottomDrawableIcon = typedArray.getDrawable(R.styleable.DrawableCenterTextView_drawableBottomIcon);
        if (bottomDrawableIcon != null) {
            mDrawableIcons[3] = bottomDrawableIcon;
            if (mDrawableBottomIconWidth == 0) {
                mDrawableBottomIconWidth = bottomDrawableIcon.getIntrinsicWidth();
            }
            if (mDrawableBottomIconHeight == 0) {
                mDrawableBottomIconHeight = bottomDrawableIcon.getIntrinsicHeight();
            }
        }

        typedArray.recycle();

        // 设置居中
        setGravity(Gravity.CENTER);
    }


    /**
     * 重写画布的绘制方法
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {

        int drawablePadding = getCompoundDrawablePadding();
        translateText(canvas, drawablePadding);

        super.onDraw(canvas);

        float centerX = (getWidth() - getPaddingLeft() - getPaddingRight()) / 2;
        float centerY = (getHeight() - getPaddingTop() - getPaddingBottom()) / 2;

        // 测量文本的宽度和高度
        float textWidth = getPaint().measureText(getText().toString().isEmpty() ? getHint().toString() : getText().toString());
        Paint.FontMetrics fontMetrics = getPaint().getFontMetrics();
        float textHeight = fontMetrics.descent - fontMetrics.ascent;

        // 如果左边的drawable不为空
        if (mDrawableIcons[0] != null) {
            int left = (int) (centerX - drawablePadding - mDrawableLeftIconWidth - textWidth / 2);
            int top = (int) (centerY - mDrawableLeftIconHeight / 2);
            mDrawableIcons[0].setBounds(
                    left,
                    top,
                    left + mDrawableLeftIconWidth,
                    top + mDrawableLeftIconHeight
            );
            // 保存画布
            canvas.save();
            // 绘制drawable
            mDrawableIcons[0].draw(canvas);
            // 重置画布
            canvas.restore();
        }

        // 如果右边的drawable不为空
        if (mDrawableIcons[2] != null){
            int left = (int) (centerX + textWidth / 2 + drawablePadding);
            int top = (int) (centerY - mDrawableRightIconHeight / 2);
            mDrawableIcons[2].setBounds(
                    left,
                    top,
                    left + mDrawableRightIconWidth,
                    top + mDrawableRightIconHeight
            );

            // 保存画布
            canvas.save();
            // 绘制drawable
            mDrawableIcons[2].draw(canvas);
            // 重置画布
            canvas.restore();
        }

        // 如果上面的drawable对象不为空
        if (mDrawableIcons[1] != null){
            int left = (int) (centerX - mDrawableTopIconWidth / 2);
            int bottom = (int) (centerY - textHeight / 2 - drawablePadding);
            mDrawableIcons[1].setBounds(
                    left,
                    bottom - mDrawableTopIconHeight,
                    left + mDrawableTopIconWidth,
                    bottom
            );
            canvas.save();
            mDrawableIcons[1].draw(canvas);
            canvas.restore();
        }

        // 如果下面的drawable不为空
        if (mDrawableIcons[3] != null){
            int left = (int) (centerX - mDrawableBottomIconWidth / 2);
            int top = (int) (centerY + textHeight / 2 + drawablePadding);
            mDrawableIcons[3].setBounds(
                    left,
                    top,
                    left + mDrawableBottomIconWidth,
                    top + mDrawableBottomIconHeight
            );

            canvas.save();
            mDrawableIcons[3].draw(canvas);
            canvas.restore();
        }
    }


    /**
     * 通过drawable的尺寸计算文本平移的距离
     *
     * @param canvas
     * @param drawablePadding
     */
    private void translateText(Canvas canvas, int drawablePadding) {

        int translateWidth = 0;
        int translateHeight = 0;

        // 如果左边和右边的drawable对象不为空
        if (mDrawableIcons[0] != null && mDrawableIcons[2] != null) {
            translateWidth = (mDrawableLeftIconWidth - mDrawableRightIconWidth) / 2;
        } else if (mDrawableIcons[0] != null) {
            translateWidth = (mDrawableLeftIconWidth + drawablePadding) / 2;
        } else if (mDrawableIcons[2] != null) {
            translateWidth = -(mDrawableRightIconWidth + drawablePadding) / 2;
        }

        // 如果上面和下面的drawable对象不为空
        if (mDrawableIcons[1] != null && mDrawableIcons[3] != null) {
            translateHeight = (mDrawableTopIconHeight - mDrawableBottomIconHeight) / 2;
        } else if (mDrawableIcons[1] != null) {
            translateHeight = (mDrawableTopIconHeight + drawablePadding) / 2;
        } else if (mDrawableIcons[3] != null) {
            translateHeight = -(mDrawableBottomIconHeight + drawablePadding) / 2;
        }
        // 平移画布
        canvas.translate(translateWidth, translateHeight);
    }


}
