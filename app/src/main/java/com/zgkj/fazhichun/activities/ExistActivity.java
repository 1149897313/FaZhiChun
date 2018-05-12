package com.zgkj.fazhichun.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.zgkj.common.app.Fragment;
import com.zgkj.common.app.ToolbarActivity;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.fragments.exist.EvaluateOrderFragment;
import com.zgkj.fazhichun.fragments.exist.ObligationOrderFragment;
import com.zgkj.fazhichun.fragments.exist.RefundOrderFragment;
import com.zgkj.fazhichun.fragments.exist.UsedOrderFragment;
import com.zgkj.fazhichun.fragments.exist.ExistOrderFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/12.
 * Descr:   全部存在订单的显示界面
 */
public class ExistActivity extends ToolbarActivity {

    /**
     * UI
     */
    private SlidingTabLayout mTabLayout;
    private ViewPager mViewPager;

    /**
     * DATA
     */
    private List<String> mTabTitleTexts;
    private List<Fragment> mTabFragments;

    private static String TYPE_NAME = "type_name";
    private int mTypeValue;

    /**
     * 显示全部订单界面
     * @param context
     */
    public static void show(Context context, int typeValue){
        Intent intent = new Intent(context, ExistActivity.class);
        intent.putExtra(TYPE_NAME, typeValue);
        context.startActivity(intent);
    }


    @Override
    protected boolean initArgs(Bundle bundle) {
        mTypeValue = bundle.getInt(TYPE_NAME, -1);
        return mTypeValue != -1;
    }

    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_exist;
    }

    @Override
    protected void initWidgets() {
        super.initWidgets();
        // init UI
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);
    }

    @Override
    protected void initDatas() {
        super.initDatas();

        mTabTitleTexts = new ArrayList<>();
        mTabTitleTexts.add("全部");
        mTabTitleTexts.add("待付款");
        mTabTitleTexts.add("待使用");
        mTabTitleTexts.add("待评价");
        mTabTitleTexts.add("退款");


        mTabFragments = new ArrayList<>();
        mTabFragments.add(ExistOrderFragment.newInstance());
        mTabFragments.add(ObligationOrderFragment.newInstance());
        mTabFragments.add(UsedOrderFragment.newInstance());
        mTabFragments.add(EvaluateOrderFragment.newInstance());
        mTabFragments.add(RefundOrderFragment.newInstance());

        mViewPager.setOffscreenPageLimit(mTabFragments.size());
        mViewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(),
                mTabTitleTexts, mTabFragments));


        // 将tablayout与Viewpager进行绑定
        mTabLayout.setViewPager(mViewPager);

        // 根据需求切换页面
        changeViewPager(mViewPager, mTypeValue);
    }



    /**
     * 切换ViewPager页面视图显示的方法
     *
     * @param viewPager
     * @param typeValue
     */
    private void changeViewPager(ViewPager viewPager, int typeValue){
        viewPager.setCurrentItem(typeValue);
    }


    private static class MyViewPagerAdapter extends FragmentPagerAdapter {

        private List<String> mTabTitleTexts;
        private List<Fragment> mTabFragments;

        public MyViewPagerAdapter(FragmentManager fm, List<String> tabTitleTexts, List<Fragment> tabFragments) {
            super(fm);
            mTabTitleTexts = tabTitleTexts;
            mTabFragments = tabFragments;

        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return mTabFragments.get(position);
        }

        @Override
        public int getCount() {
            return mTabFragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mTabTitleTexts.get(position);
        }
    }







}
