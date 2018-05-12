package com.zgkj.common.widgets.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zgkj.common.R;

/**
 * Descr:   自定义一个带删除按钮的文本编辑框
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2017/12/26.
 */

public class ClearTextIconEditText extends AppCompatEditText implements View.OnTouchListener, TextWatcher {



    // 定义一个删除图标的Drawable对象
    private Drawable mClearTextIconDrawable;

    // 定义存放当前用户输入文字信息的对象
    private CharSequence mText;


    public ClearTextIconEditText(Context context) {
        this(context, null);

    }

    // 构造方法，初始化数据
    public ClearTextIconEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 初始化数据
        init(context, attrs);
    }


    /**
     * 实现初始化数据的方法
     */
    private void init(Context context, AttributeSet attrs){

        // 加载资源
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClearTextIconEditText);

        mClearTextIconDrawable = typedArray.getDrawable(R.styleable.ClearTextIconEditText_clearIconDrawable);

        // 释放资源
        typedArray.recycle();

        if (mClearTextIconDrawable != null){
            // 计算绘制的矩形区域
            mClearTextIconDrawable.setBounds(0, 0,
                    mClearTextIconDrawable.getIntrinsicWidth(), mClearTextIconDrawable.getIntrinsicHeight());
        }

        // 为当前文本控件注册触摸事件
        this.setOnTouchListener(this);

        // 为当前文本控件注册文本监听事件
        this.addTextChangedListener(this);

        // 调用管理删除按钮显示与隐藏的方法
        managerClearButton();

    }


    /**
     * 设置传入删除图标资源id的方法
     * @param resId
     */
    public void setClearTextDrawable(int resId){

        mClearTextIconDrawable = getResources().getDrawable(resId);

        // 计算绘制的矩形区域
        mClearTextIconDrawable.setBounds(0, 0,
                mClearTextIconDrawable.getIntrinsicWidth(), mClearTextIconDrawable.getIntrinsicHeight());

        managerClearButton();
    }




    /**
     * 实现管理清除文本内容按钮的方法
     */
    private void managerClearButton(){

        // 如果文本框的显示内容为空则不显示清除按钮
        if(TextUtils.isEmpty(this.getText().toString())){
            hideClearButton();
        }else {
            showClearButton();
        }

    }







    /**
     * 实现显示清除按钮的方法
     */
    private void showClearButton(){

        this.setCompoundDrawables(this.getCompoundDrawables()[0], this.getCompoundDrawables()[1],
                mClearTextIconDrawable, this.getCompoundDrawables()[3]);
    }

    /**
     * 实现隐藏清除按钮的方法
     */
    private void hideClearButton(){

        this.setCompoundDrawables(this.getCompoundDrawables()[0], this.getCompoundDrawables()[1],
                null, this.getCompoundDrawables()[3]);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        // 如果删除按钮图标为空则直接返回
        if (this.getCompoundDrawables()[2] == null){
            return false;
        }

        // 如果用户不是按下的触摸操作则直接返回
        if (event.getAction() != MotionEvent.ACTION_UP){
            return false;
        }

        // 如果用户触摸了删除图标按钮的位置
        if (event.getX() > this.getWidth() - this.getPaddingRight() - mClearTextIconDrawable.getIntrinsicWidth()
                && event.getX() < this.getWidth() - this.getPaddingRight()){

            // 将文本框输入框的内容清空
            if (!TextUtils.isEmpty(this.getText().toString())){
                this.setText("");
            }

            // 隐藏删除按钮图标
            hideClearButton();
        }

        return super.onTouchEvent(event);
    }


    @Override
    public void setEnabled(boolean enabled) {


        if (enabled == false){
            // 如果当前控件不可编辑则隐藏删除按钮
            hideClearButton();
        }else {
            // 否则判断显示
            if (mText != null){
                if (mText.length() > 0){
                    showClearButton();
                }
            }
        }
        super.setEnabled(enabled);
    }



    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        mText = s;

        if (s.length() > 0){
            this.showClearButton();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


}
