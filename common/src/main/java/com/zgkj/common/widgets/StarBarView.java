package com.zgkj.common.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zgkj.common.R;

/**
 * Descr:   自定义星级评分控件
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2017/12/11.
 */

public class StarBarView extends View {


    // 星星间的间距
    private int starSpacing = 0;

    // 星星的个数
    private int starCount = 5;

    // 星星的高度和宽度，因为星星一般为正方形，所以直接定义星星的尺寸即可
    private int starSize;

    // 实际对应星星的评分值
    private float starMark;


    // 底色
    private Drawable starEmptyDrawable;

    // 亮色
    private Bitmap starFillBitmap;

    // 是否支持触摸
    private boolean isTouch;


    // 创建一个画笔对象
    private Paint mPaint;


    // 是否需要进行整数评分
    private boolean integerMark;


    // 创建星星评分改变的监听接口对象
    private OnStarChangedLisrener mOnStarChangedLisrener;



    public StarBarView(Context context) {
        super(context);
        init(context, null);
    }



    public StarBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }




    public StarBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    /**
     * 初始化数据
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {

        setEnabled(false);
        // 加载属性资源
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StarBarView);

        // 强制转换为Int
        starSpacing = (int) typedArray.getDimension(R.styleable.StarBarView_starSpacing, 0);
        starSize = (int) typedArray.getDimension(R.styleable.StarBarView_starSize, 20);
        starCount = typedArray.getInteger(R.styleable.StarBarView_starCount, 5);
        starEmptyDrawable = typedArray.getDrawable(R.styleable.StarBarView_starEmpty);
        // 获取资源并转换为bitmap
        starFillBitmap = drawableToBitmap(typedArray.getDrawable(R.styleable.StarBarView_starFill));

        // 默认支持触摸
        isTouch = typedArray.getBoolean(R.styleable.StarBarView_isTouch, true);

        // 默认支持整数评分
        integerMark = typedArray.getBoolean(R.styleable.StarBarView_integerMark, true);


        // 回收资源
        typedArray.recycle();

        // 初始化画笔对象
        mPaint = new Paint();
        // 设置画笔抗锯齿
        mPaint.setAntiAlias(true);
        // 设置画笔防抖动
        mPaint.setDither(true);
        // 为画笔对象设置一个Bitmap类型的着色器
        mPaint.setShader(new BitmapShader(starFillBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));

    }

    /**
     * 设置是否需要整数评分
     *
     * @param integerMark
     */
    public void setIntegerMark(boolean integerMark) {
        this.integerMark = integerMark;
    }

    /**
     * 设置显示星星的评分
     *
     * @param mark
     */
    public void setStarMark(float mark) {
        // 如果需要显示整数评分
        if (integerMark) {
            starMark = (float) Math.ceil(mark);
        } else {
            starMark = Math.round(mark * 10) * 1.0f / 10;
        }

        // 回调监听接口对象
        if (mOnStarChangedLisrener != null) {

            mOnStarChangedLisrener.onStarChanged(starMark);
        }

        // 刷新界面显示
        invalidate();
    }

    /**
     * 获取显示星星的评分
     *
     * @return
     */
    public float getStarMark() {

        return starMark;
    }


    /**
     * 重写测量View的方法
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 重新测量view的宽度和高度
        setMeasuredDimension(starSize * starCount + starSpacing * (starCount - 1), starSize);
    }


    /**
     * 重写绘制View的方法
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 校验数据
        if (starEmptyDrawable == null || starFillBitmap == null) {
            return;
        }

        // 绘制星星控件的底色
        for (int i = 0; i < starCount; i++) {

            // 左上右下
            // 设置绘制位置
            starEmptyDrawable.setBounds(
                    (starSize + starSpacing) * i,
                    0,
                    (starSize + starSpacing) * i + starSize,
                    starSize);

            // 绘制
            starEmptyDrawable.draw(canvas);
        }


        // 如果当前评分大于1
        if (starMark > 1) {

            // 绘制一个星星尺寸大小的矩形框
            canvas.drawRect(0, 0, starSize, starSize, mPaint);

            if (starMark - (int) starMark == 0) {

                for (int i = 1; i < starMark; i++) {
                    // 平移画布
                    canvas.translate(starSize + starSpacing, 0);
                    // 绘制一个矩形框
                    canvas.drawRect(0, 0, starSize, starSize, mPaint);
                }
            } else {

                for (int i = 1; i < starMark - 1; i++) {
                    // 平移画布
                    canvas.translate(starSize + starSpacing, 0);
                    canvas.drawRect(0, 0, starSize, starSize, mPaint);
                }
                // 再向后平移一个画布的位置，用来绘制小数点渲染的星星颜色
                canvas.translate(starSize + starSpacing, 0);
                canvas.drawRect(0, 0, starSize * (Math.round((starMark - (int) (starMark)) * 10) * 1.0f / 10), starSize, mPaint);

            }
        } else {
            canvas.drawRect(0, 0, starSize * starMark, starSize, mPaint);
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (isTouch){

            int x = (int) event.getX();

            // 对x方向上的值进行限制
            if (x < 0) {
                x = 0;
            }
            if (x > getMeasuredWidth()) {
                x = getMeasuredWidth();
            }

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:       // 按下
                    setStarMark(x*1.0f / (getMeasuredWidth()*1.0f/starCount));
                    break;
                case MotionEvent.ACTION_MOVE:       // 滑动
                    setStarMark(x*1.0f / (getMeasuredWidth()*1.0f/starCount));
                    break;
                case MotionEvent.ACTION_UP:
                    setStarMark(x*1.0f / (getMeasuredWidth()*1.0f/starCount));
                    break;
            }
        }


        return super.onTouchEvent(event);
    }

    /**
     * 实现drawable转换为bitmap的方法
     *
     * @param drawable
     * @return bitmap
     */
    private Bitmap drawableToBitmap(Drawable drawable) {

        // 校验数据
        if (drawable == null) {
            return null;
        }

        // 创建一个返回的bitmap对象
        Bitmap bitmap = Bitmap.createBitmap(starSize, starSize, Bitmap.Config.ARGB_8888);

        // 建立对应bitmap的画布
        Canvas canvas = new Canvas(bitmap);

        // 测量drawable的尺寸
        drawable.setBounds(0, 0, starSize, starSize);

        // 吧drawable内容绘制到画布中
        drawable.draw(canvas);

        // 返回bitmap对象
        return bitmap;
    }


    /**
     * 设置初始化监听的方法
     *
     * @param onStarChangedLisrener
     */
    public void setOnStarChangedLisrener(OnStarChangedLisrener onStarChangedLisrener) {

        this.mOnStarChangedLisrener = onStarChangedLisrener;
    }


    /**
     * 实现星星点击的监听接口对象
     */
    public interface OnStarChangedLisrener {

        /**
         * 接口回调方法
         *
         * @param mark 变更后的星星评分
         */
        void onStarChanged(float mark);
    }


}
