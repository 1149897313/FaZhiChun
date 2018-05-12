package com.zgkj.common.widgets.text;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/8.
 * Descr:   自定义TextView，文本内容自动调整字体大小以适应TextView的大小
 */

public class AutoFitTextView extends android.support.v7.widget.AppCompatTextView {


    // 绘制字体的画笔对象

    private Paint mTextPaint;

    // 需要显示的字体的大小
    private float mTextSize;


    public AutoFitTextView(Context context) {
        super(context);
    }

    public AutoFitTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public AutoFitTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    /**
     * 如果文本框是指定的宽度，那么将指定的文本放入文本框中
     *
     * @param text              需要显示的文本
     * @param textViewWidth     当前TextView限定的宽度值
     */
    private void refitText(String text, int textViewWidth) {
        if (text == null || textViewWidth <= 0)
            return;
        // 初始化画笔
        if(mTextPaint == null){
            mTextPaint = new Paint();
            mTextPaint.set(this.getPaint());
        }
        // 计算TextView可用的显示的文本的宽度空间
        int availableTextViewWidth = textViewWidth - getPaddingLeft() - getPaddingRight();

        // 创建一个用于测量的矩形对象
        Rect boundsRect = new Rect();

        // 测量文本的宽度和高度，并将测量的结果赋值给boundsRect
        mTextPaint.getTextBounds(text, 0, text.length(), boundsRect);
        // 得到计算后的文本的高度
        int textWidth = boundsRect.width();

        // 得到初始的字体尺寸的大小
        mTextSize = getTextSize();

        // 获取文本的宽度，和上面类似，但是是比较精准的。
        float[] charsWidthArray = new float[text.length()];

        // 如果设置的字体的宽度大于当前TextView可用的显示的文本的宽度则递减字体大小直到字体大小适应文本的宽度而止
        while (textWidth > availableTextViewWidth) {
            // 字体每次递减1.0f
            mTextSize -= 1.0f;
            mTextPaint.setTextSize(mTextSize);
            textWidth = mTextPaint.getTextWidths(text, charsWidthArray);
        }
        // 设置计算后的字体尺寸
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        refitText(this.getText().toString(), this.getWidth());
    }
}
