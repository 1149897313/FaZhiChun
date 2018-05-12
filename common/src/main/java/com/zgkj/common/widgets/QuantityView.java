package com.zgkj.common.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zgkj.common.R;
import com.zgkj.common.utils.ToastUtil;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/13.
 * Descr:   实现自定义数量加减的View
 */

public class QuantityView extends LinearLayout implements View.OnClickListener, TextWatcher {

    // 减号
    private TextView mMinusView;

    // 加号
    private TextView mPlusView;

    // 文本输入框
    private EditText mQuantityView;


    // 商品的库存总量，默认库存的数量为1
    private int mInventory = 1;

    // 用户购买的数量，默认购买的数量为1
    private int mQuantity = 1;

    // 定义一个数量发生改变的监听器
    private OnQuantityChangeListener mOnQuantityChangeListener;


    public QuantityView(Context context) {
        this(context, null);
    }

    public QuantityView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuantityView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }

    private void init(Context context, AttributeSet attrs) {
        // 加载自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QuantityView);
        // getDimensionPixelOffset() 方法加载的值默认为像素值

        int buttonWidth = typedArray.getDimensionPixelOffset(R.styleable.QuantityView_buttonWidth, 20);
        int buttonHeight = typedArray.getDimensionPixelOffset(R.styleable.QuantityView_buttonHeight, 20);
        int editBoxWidth = typedArray.getDimensionPixelOffset(R.styleable.QuantityView_editBoxWidth, 20);
        int buttonTextSize = typedArray.getDimensionPixelSize(R.styleable.QuantityView_buttonTextSize, 12);
        int editBoxTextSize = typedArray.getDimensionPixelSize(R.styleable.QuantityView_editBoxTextSize, 12);
        int leftAndRightSpace = typedArray.getDimensionPixelOffset(R.styleable.QuantityView_leftAndRightSpace, 2);

        // 回收资源
        typedArray.recycle();

        // 加载显示界面的布局文件
        View view = LayoutInflater.from(context).inflate(R.layout.layout_quantity, this);

        // 初始化显示控件
        mMinusView = view.findViewById(R.id.minus);
        mPlusView = view.findViewById(R.id.plus);
        mQuantityView = view.findViewById(R.id.quantity);


        // 设置显示控件的自定义属性
        LayoutParams buttonParams = new LayoutParams(buttonWidth, buttonHeight);
        mMinusView.setLayoutParams(buttonParams);
        mPlusView.setLayoutParams(buttonParams);

        // 设置高度与Button的高度一致
        LayoutParams editBoxParams = new LayoutParams(editBoxWidth, buttonHeight);
        editBoxParams.leftMargin = leftAndRightSpace;
        editBoxParams.rightMargin = leftAndRightSpace;
        mQuantityView.setLayoutParams(editBoxParams);

        // 设置显示控件的字体
        mMinusView.setTextSize(TypedValue.COMPLEX_UNIT_PX, buttonTextSize);
        mPlusView.setTextSize(TypedValue.COMPLEX_UNIT_PX, buttonTextSize);

        mQuantityView.setTextSize(TypedValue.COMPLEX_UNIT_PX, editBoxTextSize);

        // 为按钮注册点击事件
        mMinusView.setOnClickListener(this);
        mPlusView.setOnClickListener(this);

        // 为文本输入框控件注册文本的改变监听事件
        mQuantityView.addTextChangedListener(this);

        // 默认显示数量为1
        mQuantityView.setText(mQuantity + "");
        // 设置光标在末尾处
        mQuantityView.setSelection(mQuantityView.getText().toString().length());
        mQuantityView.clearFocus();


    }

    /**
     * 获取数量
     * @return
     */
    public int getValue() {
        return mQuantity;
    }

    /**
     * 设置商品库存总量
     *
     * @param inventory
     */
    public void setInventory(int inventory) {
        mInventory = inventory;
    }


    /**
     * 初始化监听器的方法
     *
     * @param onQuantityChangeListener
     */
    public void setOnQuantityChangeListener(OnQuantityChangeListener onQuantityChangeListener) {
        mOnQuantityChangeListener = onQuantityChangeListener;
    }


    /**
     * 显示控件点击事件的回调方法
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.minus) {       // 如果用户点击了减号
            if (mQuantity > 1) {
                // 数量减1
                mQuantity--;
                // 改变文本输入框的显示
                mQuantityView.setText(mQuantity + "");
            }
        } else if (v.getId() == R.id.plus) {  // 如果用户点击了加号
            // 如果当前数量小于库存总量
            if (mQuantity < mInventory) {
                // 数量加1
                mQuantity++;

                // 改变文本输入框的显示
                mQuantityView.setText(mQuantity + "");
            }
        }
        // 设置光标在末尾处
        mQuantityView.setSelection(mQuantityView.getText().toString().length());

        // 设置文本输入框失去焦点
        mQuantityView.clearFocus();

        // 回调接口监听方法
        if (mOnQuantityChangeListener != null) {
            mOnQuantityChangeListener.onQuantityChanged(mQuantity);
        }

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String string = s.toString();
        // 如果输入的内容为空或者为空格则直接返回
        if (TextUtils.isEmpty(string)) {
            return;
        } else {
            // 转换为Int类型
            try {
                mQuantity = Integer.parseInt(string);
                if (mQuantity > mInventory || mQuantity == 0) {
                    // 提示用户输入正确的数字
                    ToastUtil.getInstance()
                            .setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary))
                            .setTextColor(getContext().getResources().getColor(R.color.textColorLight))
                            .show(getContext().getResources().getString(R.string.prompt_quantity_view_illegal), Toast.LENGTH_SHORT);

                    mQuantity = 1;
                    mQuantityView.setText(mQuantity + "");
                    // 设置光标在末尾处
                    mQuantityView.setSelection(mQuantityView.getText().toString().length());
                }

                // 回调接口监听方法
                if (mOnQuantityChangeListener != null) {
                    mOnQuantityChangeListener.onQuantityChanged(mQuantity);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                // 提示用户输入正确的数字
                ToastUtil.getInstance()
                        .setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary))
                        .setTextColor(getContext().getResources().getColor(R.color.textColorLight))
                        .show(getContext().getResources().getString(R.string.prompt_quantity_view_illegal), Toast.LENGTH_SHORT);
                mQuantity = 1;
                mQuantityView.setText(mQuantity + "");
                // 设置光标在末尾处
                mQuantityView.setSelection(mQuantityView.getText().toString().length());
            }
        }

    }


    /**
     * 定义一个数量改变的回调方法
     */
    public interface OnQuantityChangeListener {

        /**
         * 用户选择的数量改变的回调方法
         *
         * @param quantity 用于最终选择的数量
         */
        void onQuantityChanged(int quantity);
    }
}
