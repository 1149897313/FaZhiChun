package com.zgkj.fazhichun.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zgkj.common.app.ToolbarActivity;
import com.zgkj.common.utils.AccountManagers;
import com.zgkj.common.utils.AppUtil;
import com.zgkj.common.widgets.load.LoadFactory;
import com.zgkj.common.widgets.load.core.LoadManager;
import com.zgkj.common.widgets.load.view.AbsView;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.fragments.dialog.phone.CallPhoneDialogFragment;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/25.
 * Descr:   用户设置界面
 */
public class SettingActivity extends ToolbarActivity implements View.OnClickListener {

    /**
     * UI
     */
    private LinearLayout mContentLayout;
    private RelativeLayout mPersonalLayout;
    private RelativeLayout mPhoneLayout;
    private TextView mPhoneView;
    private RelativeLayout mGeneralLayout;
    private RelativeLayout mAboutLayout;
    private TextView mTelephoneView;
    private ImageView mTelephoneIconView;
    private RelativeLayout mUpdateLayout;
    private TextView mLogoutView;

    /**
     * DATA
     */
    private LoadManager mLoadManager;


    /**
     * 显示设置界面
     *
     * @param context
     */
    public static void show(Context context) {
        context.startActivity(new Intent(context, SettingActivity.class));
    }


    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_setting;
    }


    @Override
    protected void initWidgets() {
        super.initWidgets();
        // init UI
        mContentLayout = findViewById(R.id.content_layout);
        mPersonalLayout = findViewById(R.id.personal_layout);
        mPhoneLayout = findViewById(R.id.phone_layout);
        mPhoneView = findViewById(R.id.phone);
        mGeneralLayout = findViewById(R.id.general_layout);
        mAboutLayout = findViewById(R.id.about_layout);
        mTelephoneView = findViewById(R.id.telephone);
        mTelephoneIconView = findViewById(R.id.telephone_icon);
        mUpdateLayout = findViewById(R.id.update_layout);
        mLogoutView = findViewById(R.id.logout);


        mPersonalLayout.setOnClickListener(this);
        mPhoneLayout.setOnClickListener(this);
        mGeneralLayout.setOnClickListener(this);
        mAboutLayout.setOnClickListener(this);
        mTelephoneIconView.setOnClickListener(this);
        mUpdateLayout.setOnClickListener(this);
        mLogoutView.setOnClickListener(this);

        mLoadManager = LoadFactory.getInstance().register(mContentLayout, new AbsView.OnReloadListener() {
            @Override
            public void onReload(View view) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoadManager.showSuccessView();
            }
        }, 500);


    }



    /**
     * 显示控件的点击事件的回调方法
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.personal_layout:      // 个人信息
                PersonalActivity.show(mContext);
                break;
            case R.id.phone_layout:

                break;
            case R.id.general_layout:

                break;
            case R.id.about_layout:

                break;
            case R.id.telephone_icon:
                showCallPhoneDialogFragment("40088888888");
                break;
            case R.id.update_layout:


                break;
            case R.id.logout:// 退出登录
                // 退出应用程序
//                AppUtil.finishAll();
                AccountManagers.logOut();
                AccountActivity.show(mContext,false);
                break;
            default:
                break;
        }


    }

    /**
     * 显示拨打商家电话的dialog
     */
    private void showCallPhoneDialogFragment(String phone) {
        CallPhoneDialogFragment callPhoneDialogFragment = CallPhoneDialogFragment.newInstance(phone);
        if (!callPhoneDialogFragment.isAdded()) {
            callPhoneDialogFragment.show(getSupportFragmentManager(), callPhoneDialogFragment.getClass().getName());
        }

    }
}
