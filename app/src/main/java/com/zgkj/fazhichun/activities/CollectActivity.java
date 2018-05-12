package com.zgkj.fazhichun.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.zgkj.common.app.Fragment;
import com.zgkj.common.app.ToolbarActivity;
import com.zgkj.common.http.AsyncHttpPostFormData;
import com.zgkj.common.http.AsyncHttpResponse;
import com.zgkj.common.http.AsyncOkHttpClient;
import com.zgkj.common.http.AsyncResponseHandler;
import com.zgkj.factory.model.api.RspModel;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.fragments.collect.MerchantFragment;
import com.zgkj.fazhichun.fragments.collect.ProductFragment;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/5.
 * Descr:   我的收藏的显示界面
 */

public class CollectActivity extends ToolbarActivity implements View.OnClickListener {

    /**
     * UI
     */
    private SlidingTabLayout mTabLayout;
    private ViewPager mViewPager;
    private LinearLayout bottom;
    private LinearLayout bottom_left;
    private ImageView selectAll;
    private TextView mDeleteView; // 删除按钮

    /**
     * UI
     */
    private MerchantFragment mMerchantFragment;
    private ProductFragment mProductFragment;
    private List<Fragment> mTabFragments;
    private List<String> mTabTitleTexts;

    // 定义当前显示模式的常量
    private static final int MODE_CHECK = 0;
    private static final int MODE_EDIT = 1;

    // 编辑模控制变量，默认为编辑模式
    private int mEditMode = MODE_CHECK;

    // 当前显示界面的索引
    private int mCurrentPosition = 0;


    /**
     * 显示我的收藏界面
     *
     * @param context
     */
    public static void show(Context context) {
        context.startActivity(new Intent(context, CollectActivity.class));
    }

    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_collect;
    }


    @Override
    protected void initWidgets() {
        super.initWidgets();
        // init UI
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);
        bottom=findViewById(R.id.bottom);
        bottom_left=findViewById(R.id.bottom_left);
        selectAll=findViewById(R.id.select_all);
        mDeleteView = findViewById(R.id.delete);
    }

    @Override
    protected void initDatas() {
        super.initDatas();

        mTabFragments = new ArrayList<>();

        mMerchantFragment = MerchantFragment.newInstance();
        mProductFragment = ProductFragment.newInstance();
        mTabFragments.add(mMerchantFragment);
        mTabFragments.add(mProductFragment);

        mTabTitleTexts = new ArrayList<>();
        mTabTitleTexts.add(getResources().getString(R.string.label_collect_merchant));
        mTabTitleTexts.add(getResources().getString(R.string.label_collect_product));

        mViewPager.setOffscreenPageLimit(mTabFragments.size());
        mViewPager.setAdapter(new MyViewPagerAdapter(
                getSupportFragmentManager(),
                mTabFragments,
                mTabTitleTexts));

        // 绑定
        mTabLayout.setViewPager(mViewPager);

        // 为ViewPager注册滑动监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                    if (mCurrentPosition != position) {
                        if (mEditMode == MODE_EDIT) {
                            updateEditMode();
                        }
                        mCurrentPosition = position;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // 为删除按钮注册点击监听事件
        mDeleteView.setOnClickListener(this);
        bottom_left.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_collect, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            updateEditMode();
        }
        return super.onOptionsItemSelected(item);
    }


    private boolean selectAllMerchant=false,selectAllProduct=false;//默认未选中

    private void selectAllstate(boolean state){
        if(state){
            selectAll.setImageResource(R.drawable.ic_payment_checked);
        }else{
            selectAll.setImageResource(R.drawable.ic_payment_unchecked);
        }
    }

    /**
     * 点击事件的回调方法
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bottom_left:
                if (mTabLayout.getCurrentTab() == 0) {//店铺编辑
                    selectAllMerchant=!selectAllMerchant;
                    selectAllstate(selectAllMerchant);
                    mMerchantFragment.selectAll(selectAllMerchant);
                }else{//产品编辑
                    selectAllProduct=!selectAllProduct;
                    selectAllstate(selectAllProduct);
                    mProductFragment.selectAll(selectAllProduct);
                }

                break;
            case R.id.delete: // 如果用户点击了删除按钮
                if (mTabLayout.getCurrentTab() == 0) {
                    mMerchantFragment.delectData();
                } else if (mTabLayout.getCurrentTab() == 1) {
                    mProductFragment.delectData();
                }
                break;
        }
    }


    private void updateEditMode() {
        Menu menu = mToolbar.getMenu();
        MenuItem menuItem = menu.getItem(0);
        mEditMode = mEditMode == MODE_CHECK ? MODE_EDIT : MODE_CHECK;
        if (mEditMode == MODE_EDIT) {    // 如果为编辑模式
            // 改变菜单显示为取消
            menuItem.setTitle(getResources().getString(R.string.action_cancel));
            // 显示底部的删除按钮
            bottom.setVisibility(View.VISIBLE);
            selectAll.setImageResource(R.drawable.ic_payment_unchecked);
            if (mTabLayout.getCurrentTab() == 0) {
                mMerchantFragment.setEditMode(mEditMode);
                mMerchantFragment.setEditState(true);
                selectAllstate(selectAllMerchant);
            } else if (mTabLayout.getCurrentTab() == 1) {
                mProductFragment.setEditMode(mEditMode);
                mProductFragment.setEditState(true);
                selectAllstate(selectAllProduct);
            }
        } else {
            // 改变菜单显示为编辑
            menuItem.setTitle(getResources().getString(R.string.action_edit));
            // 隐藏底部的删除按钮
            bottom.setVisibility(View.GONE);
            if (mTabLayout.getCurrentTab() == 0) {
                mMerchantFragment.setEditMode(mEditMode);
                mMerchantFragment.setEditState(false);
            } else if (mTabLayout.getCurrentTab() == 1) {
                mProductFragment.setEditMode(mEditMode);
                mProductFragment.setEditState(false);
            }
        }

    }


    /**
     * 定义内部的适配器类
     */
    private static class MyViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> mTabFragments;
        private List<String> mTabTitleTexts;


        public MyViewPagerAdapter(FragmentManager fm, List<Fragment> tabFragments, List<String> tabTitleTexts) {
            super(fm);
            mTabFragments = tabFragments;
            mTabTitleTexts = tabTitleTexts;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return mTabFragments.get(position);
        }

        @Override
        public int getCount() {
            return mTabFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabTitleTexts.get(position);
        }
    }


}
