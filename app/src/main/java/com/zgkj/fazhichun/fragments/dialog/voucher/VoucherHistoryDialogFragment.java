package com.zgkj.fazhichun.fragments.dialog.voucher;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.zgkj.common.utils.DisplayUtil;
import com.zgkj.fazhichun.R;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/16.
 * Descr:   定义查看代金券历史订单详情的dialog
 */

public class VoucherHistoryDialogFragment extends DialogFragment {


    /**
     * 创建一个dialog对象
     * @return
     */
    public static VoucherHistoryDialogFragment newInstance(){
        VoucherHistoryDialogFragment voucherHistoryDialogFragment =
                new VoucherHistoryDialogFragment();

        return voucherHistoryDialogFragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 设置无标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 加载布局视图
        View view = inflater.inflate(R.layout.dialog_voucher_history, container, false);

        return view;
    }

    @SuppressLint("NewApi")
    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);

        View decorView = window.getDecorView();
        decorView.setBackground(new ColorDrawable(Color.TRANSPARENT));
        decorView.setPadding(DisplayUtil.dp2px(getContext(), 16), 0, DisplayUtil.dp2px(getContext(), 16), 0);

    }
}
