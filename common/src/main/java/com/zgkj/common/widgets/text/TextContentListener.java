package com.zgkj.common.widgets.text;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.TextView;

/*
 * Descr:   文本内容变化的监听器，判断多个文本内容是否为空
 * Author:  bozaixing.
 * Date:    2017-12-31.
 * Email:   654152983@qq.com.
 */
public class TextContentListener<T extends TextView> implements TextWatcher {

    /**
     * 存放需要监听的泛型控件对象
     */
    private T[] mViewList;

    /**
     * 文本内容变化的监听器
     */
    private OnTextChangedListener mListener;

    /**
     * 构造方法，初始化参数
     *
     * @param onTextChangedListener
     * @param ts
     */
    public TextContentListener(OnTextChangedListener onTextChangedListener, T... ts) {
        this.mListener = onTextChangedListener;
        mViewList = ts;
        // 添加监听
        for (T t : mViewList){
            t.addTextChangedListener(this);
        }
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


    }

    @Override
    public void afterTextChanged(Editable editable) {

        if (mViewList != null && mViewList.length > 0) {
            // 判断是否为空
            boolean empty = isEmpty(mViewList);
            if (empty) {
                if (this.mListener != null) {
                    mListener.onAfterTextChanged(true);
                }
            } else {
                if (this.mListener != null) {
                    mListener.onAfterTextChanged(false);
                }
            }
        }
    }

    /**
     * 判断一个数组里面的值是否全部为空
     * 不为空返回true,为空则返回false
     *
     * @param viewList
     * @return
     */
    private boolean isEmpty(T[] viewList) {

        for (T view : viewList) {
            String text = view.getText().toString();
            if (TextUtils.isEmpty(text)) {
                return false;
            }
        }

        return true;
    }


    public interface OnTextChangedListener {


        void onAfterTextChanged(boolean isEmpty);


    }

}
