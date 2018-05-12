package com.zgkj.fazhichun.fragments.dialog.portrait;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zgkj.fazhichun.R;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/15.
 * Descr:   更新头像提示的dialog
 */

public class UpdatePortraitDialogFragment extends DialogFragment implements View.OnClickListener {


    /**
     * 定义更新头像的回调接口
     */
    private OnUpdatePortraitListener mOnUpdatePortraitListener;


    /**
     * 显示更新头像的dialog
     *
     * @param onUpdatePortraitListener
     * @return
     */
    public static UpdatePortraitDialogFragment newInstance(OnUpdatePortraitListener onUpdatePortraitListener){
        UpdatePortraitDialogFragment updatePortraitDialogFragment = new UpdatePortraitDialogFragment();
        updatePortraitDialogFragment.setOnItemViewClickListener(onUpdatePortraitListener);

        return updatePortraitDialogFragment;
    }


    /**
     * 初始化接口监听对象
     *
     * @param onUpdatePortraitListener
     */
    private void setOnItemViewClickListener(OnUpdatePortraitListener onUpdatePortraitListener){
        mOnUpdatePortraitListener = onUpdatePortraitListener;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 设置无标题栏
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 加载界面布局
        View view = inflater.inflate(R.layout.dialog_update_portrait, container, false);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 拍一张
        TextView cameraView = view.findViewById(R.id.camera);
        // 相册选择
        TextView galleryView = view.findViewById(R.id.gallery);
        // 取消
        TextView cancelView = view.findViewById(R.id.cancel);

        // 注册点击事件
        cameraView.setOnClickListener(this);
        galleryView.setOnClickListener(this);
        cancelView.setOnClickListener(this);

    }


    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.BOTTOM;
        window.setAttributes(layoutParams);

        View decorView = window.getDecorView();
        decorView.setPadding(0, 0, 0, 0);
        decorView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP){
                    close();
                }
                return true;
            }
        });
    }

    /**
     * 点击事件的回调方法
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera:       // 拍一张
                camera();
                break;
            case R.id.gallery:      // 相册选择
                gallery();
                break;
            case R.id.cancel:       // 取消
                close();
                break;
            default:
                break;
        }
    }

    /**
     * 拍一张
     */
    private void camera(){
        if (mOnUpdatePortraitListener != null){
            mOnUpdatePortraitListener.onCamera();
            close();
        }

    }

    /**
     * 相册选择
     */
    private void gallery(){
        if (mOnUpdatePortraitListener != null){
            mOnUpdatePortraitListener.onGallery();
            close();
        }
    }


    /**
     * 关闭dialog
     */
    private void close(){
        if (getDialog() != null && getDialog().isShowing()){
            dismiss();
        }
    }

    /**
     * 定义一个item点击事件的接口回调监听
     */
    public interface OnUpdatePortraitListener {

        void onCamera();

        void onGallery();
    }


}
