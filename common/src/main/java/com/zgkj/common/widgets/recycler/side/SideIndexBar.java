package com.zgkj.common.widgets.recycler.side;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.zgkj.common.R;
import com.zgkj.common.widgets.recycler.side.utils.IndexUtil;
import com.zgkj.common.widgets.recycler.side.model.AbsIndex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/23.
 * Descr:   自定义侧边字母索引栏
 */

public class SideIndexBar extends View {

    // 定义默认的字母索引数据源
    private static final String[] DEFAULT_INDEX_LETTER = new String[]{
            "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X", "Y", "Z", "#"
    };


    // 索引数据源
    private List<String> mIndexList;

    // 当前View显示的宽度和高度
    private int mWidth;
    private int mHeight;

    // 定义索引字母Item显示的高度
    private int mIndexHegiht;

    // 定义正常显示和触摸显示的画笔绘制对象
    private Paint mPaint;
    private Paint mTouchPaint;

    // 定义索引字母显示的字体大小
    private int mIndexTextSize;

    // 定义当前View按下时显示的背景颜色
    private int mPressBackground;

    // 定义索引初始显示颜色和触摸时显示的颜色
    private int mIndexTextColor;
    private int mIndexTouchTextColor;

    // 界面悬浮的TextView;
    private TextView mOverlayTextView;


    // 创建一个触摸滑动监听接口的对象
    private OnIndexTouchChangedListener mOnIndexTouchChangedListener;


    public SideIndexBar(Context context) {
        this(context, null);
    }

    public SideIndexBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideIndexBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

        // 设置默认按下的背景颜色为透明色
        mPressBackground = Color.TRANSPARENT;
        // 设置默认初始和触摸时字体显示的颜色
        mIndexTextColor = Color.BLACK;
        mIndexTouchTextColor = Color.CYAN;

        // 设置默认的字体大小为12
        mIndexTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                16,
                context.getResources().getDisplayMetrics());

        // 加载自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SideIndexBar);
        mPressBackground = typedArray.getColor(R.styleable.SideIndexBar_pressBackground, mPressBackground);
        mIndexTextColor = typedArray.getColor(R.styleable.SideIndexBar_indexTextColor, mIndexTextColor);
        mIndexTouchTextColor = typedArray.getColor(R.styleable.SideIndexBar_indexTouchTextColor, mIndexTouchTextColor);
        mIndexTextSize = typedArray.getDimensionPixelSize(R.styleable.SideIndexBar_indexTextSize, mIndexTextSize);

        // 回收资源
        typedArray.recycle();

        // 初始化画笔
        initPaint();


    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mPaint = new Paint();
        // 设置画笔抗锯齿
        mPaint.setAntiAlias(true);
        // 设置画笔防抖动
        mPaint.setDither(true);
        // 设置画笔颜色
        mPaint.setColor(mIndexTextColor);
        // 设置画笔的尺寸
        mPaint.setTextSize(mIndexTextSize);

        mTouchPaint = new Paint();
        mTouchPaint.setAntiAlias(true);
        mTouchPaint.setDither(true);
        mTouchPaint.setColor(mIndexTouchTextColor);
        mTouchPaint.setTextSize(mIndexTextSize);
    }


    /**
     * 设置索引数据
     *
     * @param indexList
     * @return
     */
    public SideIndexBar setIndexList(List<String> indexList) {
        if (indexList == null || indexList.size() <= 0) {

            mIndexList = Arrays.asList(DEFAULT_INDEX_LETTER);
        } else {
            // 否则根据数据源生成字母索引
            mIndexList = indexList;
        }
        // 索引数据发生改变需要重新计算Index绘制的高度
        computeIndexHeight();

        return this;
    }


    /**
     * 重写测量的方法
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 获取宽度和高度的测量模式和尺寸
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // 最终测量出的宽度和高度
        int measureWidth = 0;
        int measureHeight = 0;

        // 得到每个index字母合适的宽度
        Rect indexBounds = new Rect();      // 存放每个index字母的矩形区域
        for (int i = 0; i < mIndexList.size(); i++) {
            String indexText = mIndexList.get(i);
            mPaint.getTextBounds(indexText, 0, indexText.length(), indexBounds);
            //循环结束后得到index数据项最大的宽度
            measureWidth = Math.max(indexBounds.width(), measureWidth);
            // 循环结束后得到Index数据项最大的高度值
            measureHeight = Math.max(indexBounds.height(), measureHeight);
        }
        // 得到index集合数据总的高度
        measureHeight *= mIndexList.size();

        // 根据测量模式得到View的宽度
        switch (widthMode) {
            case MeasureSpec.EXACTLY:       // 精确模式（指定了具体的数值）
                measureWidth = widthSize;
                break;
            case MeasureSpec.AT_MOST:       // 最大模式--wrap_content
                // widthSize此时是父控件能给子View分配的最大空间
                measureWidth = Math.min(measureWidth, widthSize);
                break;
            case MeasureSpec.UNSPECIFIED:   // 未指定模式
                break;
            default:
                break;

        }

        // 根据测量模式得到View的高度
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                measureHeight = heightSize;
                break;
            case MeasureSpec.AT_MOST:
                measureHeight = Math.min(measureHeight, heightSize);
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
            default:
                break;
        }
        // 设置计算后的View需要显示的宽度和高度
        setMeasuredDimension(measureWidth, measureHeight);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

        // 如果数据源为空
        if (mIndexList == null || mIndexList.size() <= 0) {
            return;
        }
        // 计算Index需要绘制的高度
        computeIndexHeight();
    }

    /**
     * 重写绘制View的方法
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 距离顶部的基准点（支持padding）
        int paddingTop = getPaddingTop();
        // 每个要绘制的index的文本对象
        String indexText;
        for (int i = 0; i < mIndexList.size(); i++) {
            indexText = mIndexList.get(i);
            // 获得画笔的FontMetrics，用来计算baseLine。因为drawText的y坐标，代表的是绘制的文字的baseLine的位置
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
            int baseLine = (int) (mIndexHegiht / 2 + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom);
            // 居中绘制文本
            canvas.drawText(indexText, (mWidth - mPaint.measureText(indexText)) / 2,
                    paddingTop + mIndexHegiht * i + baseLine,
                    mPaint);
        }
    }


    /**
     * 重写触摸事件方法
     *
     * @param event
     * @return
     */
    @SuppressLint("ResourceAsColor")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:       // 按下事件
                // 改变背景颜色
                setBackgroundColor(mPressBackground);
            case MotionEvent.ACTION_MOVE:       // 滑动事件
                // 得到触摸的点
                float y = event.getY();
                // 将触摸的坐标点转换为对应的索引值
                int position = (int) ((y - getPaddingTop()) / mIndexHegiht);
                // 限制索引越界
                if (position < 0) {
                    position = 0;
                } else if (position >= mIndexList.size()) {
                    position = mIndexList.size() - 1;
                }
                // 进行事件的回调
                if (position >= 0 && position < mIndexList.size()
                        && mOnIndexTouchChangedListener != null) {
                    if (mOverlayTextView != null) {
                        mOverlayTextView.setText(mIndexList.get(position));
                        mOverlayTextView.setVisibility(View.VISIBLE);
                    }
                    mOnIndexTouchChangedListener.onIndexTouchChanged(mIndexList.get(position), position);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            default:
                // 还原背景色为透明状态
                setBackgroundColor(android.R.color.transparent);
                // 隐藏悬浮TextView
                if (mOverlayTextView != null) {
                    mOverlayTextView.setVisibility(View.GONE);
                }
                break;
        }
        return true;
    }

    /**
     * 计算Index的绘制高度（数据源发生改变或者控件size改变时）
     */
    private void computeIndexHeight() {
        mIndexHegiht = (mHeight - getPaddingTop() - getPaddingBottom()) / mIndexList.size();
        // 重绘
        invalidate();
    }

    /**
     * 设置悬浮的TextView
     *
     * @param textView
     */
    public SideIndexBar setOverlayTextView(TextView textView) {
        mOverlayTextView = textView;
        return this;
    }

    /**
     * 初始化触摸监听接口
     *
     * @param onIndexTouchChangedListener
     */
    public SideIndexBar setOnIndexTouchChangedListener(OnIndexTouchChangedListener onIndexTouchChangedListener) {
        mOnIndexTouchChangedListener = onIndexTouchChangedListener;
        return this;
    }


    /**
     * 定义触摸改变的监听接口
     */
    public interface OnIndexTouchChangedListener {

        /**
         * 回调方法
         *
         * @param indexText
         * @param position
         */
        void onIndexTouchChanged(String indexText, int position);
    }


}
