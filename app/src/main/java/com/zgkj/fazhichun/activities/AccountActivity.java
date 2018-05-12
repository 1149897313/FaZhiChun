package com.zgkj.fazhichun.activities;

import android.content.Context;
import android.content.Intent;

import com.zgkj.common.app.Activity;
import com.zgkj.common.app.Fragment;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.fragments.account.AccountTrigger;
import com.zgkj.fazhichun.fragments.account.LoginFragment;
import com.zgkj.fazhichun.fragments.account.RegisterFragment;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/8.
 * Descr:   用户的登录和注册界面
 */
public class AccountActivity extends Activity implements AccountTrigger {


    /**
     * DATA
     */
    private Fragment mCurrentFragment;
    private Fragment mLoginFragment;
    private Fragment mRegisterFragment;


    /**
     * 显示登录注册界面的方法
     *
     * @param context
     */
    public static void show(Context context) {
        context.startActivity(new Intent(context, AccountActivity.class));
    }


    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_account;
    }


    @Override
    protected void initDatas() {
        super.initDatas();

        mCurrentFragment = mLoginFragment = LoginFragment.newInstance(this);

        getSupportFragmentManager().beginTransaction()
//                .setCustomAnimations(R.anim.anim_right_in, R.anim.anim_right_out)
                .replace(R.id.container_layout, mCurrentFragment)
                .commit();

    }

    @Override
    public void onTriggerView() {

        Fragment fragment = null;

        if (mCurrentFragment == mLoginFragment) {
            if (mRegisterFragment == null) {
                mRegisterFragment = RegisterFragment.newInstance(this);
            }

            fragment = mRegisterFragment;
        } else {
            // 默认情况下已经进行了初始化无需再次初始化
            fragment = mLoginFragment;
        }

        mCurrentFragment = fragment;

        // 替换碎片的显示
        getSupportFragmentManager().beginTransaction()
//                .setCustomAnimations(R.anim.anim_right_out, R.anim.anim_right_in)
                .replace(R.id.container_layout, mCurrentFragment)
                .commit();

    }
}
