package com.zgkj.fazhichun.fragments.comment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.zgkj.common.app.Fragment;
import com.zgkj.common.http.AsyncHttpPostFormData;
import com.zgkj.common.http.AsyncHttpResponse;
import com.zgkj.common.http.AsyncOkHttpClient;
import com.zgkj.common.http.AsyncResponseHandler;
import com.zgkj.common.widgets.load.LoadFactory;
import com.zgkj.common.widgets.load.core.LoadManager;
import com.zgkj.common.widgets.load.view.AbsView;
import com.zgkj.factory.model.api.RspModel;
import com.zgkj.fazhichun.App;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.entity.comment.EvaluateCount;
import com.zgkj.fazhichun.fragments.comment.entire.EntireCommentFragment;
import com.zgkj.fazhichun.fragments.comment.entire.NegativeCommentFragment;
import com.zgkj.fazhichun.fragments.comment.entire.SlideCommentFragment;
import com.zgkj.fazhichun.view.EmptyView;
import com.zgkj.fazhichun.view.LoadingView;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/20.
 * Descr:   全部评论的碎片显示对象
 */
public class EntireFragment extends Fragment implements IRefreshTabTitleText {

    /**
     * UI
     */
    private SlidingTabLayout mTabLayout;
    private ViewPager mViewPager;
    private LoadManager mLoadManager;

    /**
     * DATA
     */
    private List<Fragment> mTabFragments;
    private List<String> mTabTitleTexts;
    private MyViewPagerAdapter myViewPagerAdapter;

    private String shopId;

    @Override
    protected void initArgs(Bundle bundle) {
        shopId = bundle.getString("ID");
        super.initArgs(bundle);
    }

    /**
     * 显示全部碎片对象
     *
     * @return
     */
    public static EntireFragment newInstance() {
        EntireFragment fragment = new EntireFragment();
        return fragment;
    }

    @Override
    protected int getLayoutSourceId() {
        return R.layout.fragment_entire;
    }

    @Override
    protected void initWidgets(View rootView) {
        super.initWidgets(rootView);
        // init UI
        mTabLayout = rootView.findViewById(R.id.tab_layout);
        mViewPager = rootView.findViewById(R.id.view_pager);

        mLoadManager = LoadFactory.getInstance().register(mTabLayout, new AbsView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                mLoadManager.showStateView(LoadingView.class);
            }
        });
    }


    @Override
    protected void initDatas() {
        super.initDatas();
        mTabFragments = new ArrayList<>();
        Bundle bundle = new Bundle();
        bundle.putString("ID",shopId);
        Fragment allFragment=EntireCommentFragment.newInstance(this);
        allFragment.setArguments(bundle);
        mTabFragments.add(allFragment);
        Fragment slideFragments=SlideCommentFragment.newInstance(this);
        slideFragments.setArguments(bundle);
        mTabFragments.add(slideFragments);
        Fragment negativeFragment=NegativeCommentFragment.newInstance(this);
        negativeFragment.setArguments(bundle);
        mTabFragments.add(negativeFragment);

        // 为ViewPager设置预加载
        mViewPager.setOffscreenPageLimit(mTabFragments.size());

        mLoadManager.showSuccessView();

        mTabTitleTexts = new ArrayList<>();
        mTabTitleTexts.add(String.format(getResources().getString(R.string.title_comment_entire), "0"));
        mTabTitleTexts.add(String.format(getResources().getString(R.string.title_comment_slide), "0"));
        mTabTitleTexts.add(String.format(getResources().getString(R.string.title_comment_negative), "0"));
        // 初始化适配器对象
        myViewPagerAdapter = new MyViewPagerAdapter(getChildFragmentManager(), mTabFragments, mTabTitleTexts);
        mViewPager.setAdapter(myViewPagerAdapter);

        // 将TabLayout与ViewPager进行绑定
        mTabLayout.setViewPager(mViewPager);
        getEvaluateCount(shopId);
    }


    /**
     * 评论数量
     */
    private void evaluateCount(EvaluateCount evaluateCount) {

        String whole = String.format(getResources().getString(R.string.title_comment_entire), evaluateCount.getAll());
        mTabTitleTexts.remove(0);
        mTabTitleTexts.add(0, whole);
        String slide = String.format(getResources().getString(R.string.title_comment_slide), evaluateCount.getIs_img());
        mTabTitleTexts.remove(1);
        mTabTitleTexts.add(1, slide);
        String grade = String.format(getResources().getString(R.string.title_comment_negative), evaluateCount.getIs_low());
        mTabTitleTexts.remove(2);
        mTabTitleTexts.add(2, grade);
        // 重新设置并刷新数据
        myViewPagerAdapter.setTabTitleTexts(mTabTitleTexts);
        myViewPagerAdapter.notifyDataSetChanged();
        mTabLayout.notifyDataSetChanged();
    }

    /**
     * 获取评论数量
     *
     * @param id
     */
    private void getEvaluateCount(String id) {

        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        AsyncHttpPostFormData.addFormData("shop_id", id);
        okHttpClient.post("/v1/order-evaluate/evaluate-count", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:评论数量：" + response.toString());
                Type type = new TypeToken<RspModel<EvaluateCount>>() {
                }.getType();
                EvaluateCount evaluateCount = getAnalysis(response, type, "评论数量");
                if (evaluateCount != null) {
                    evaluateCount(evaluateCount);
                } else {
                }
            }
        });
    }

    /**
     * 数据解析
     *
     * @param response
     * @param type
     * @param log
     * @param <T>
     * @return
     */
    private <T> T getAnalysis(AsyncHttpResponse response, Type type, String log) {
        switch (response.getCode()) {
            case 200:
                try {
                    Gson gson = new Gson();
                    RspModel<T> rspModel = gson.fromJson(response.getBody(), type);
                    Log.i(TAG, "onSuccess: " + log + rspModel.toString());
                    switch (rspModel.getCode()) {
                        case 1:
                            mLoadManager.showStateView(EmptyView.class);
                            break;
                        case 200:
                            mLoadManager.showSuccessView();
                            Log.i(TAG, log + ": " + rspModel.getData());
                            return rspModel.getData();
                        default:
                            App.showMessage("data:code" + rspModel.getCode());
                            mLoadManager.showStateView(EmptyView.class);
                            break;
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                break;
            default:
                mLoadManager.showStateView(EmptyView.class);
                break;
        }
        return null;
    }

    /**
     * 各个碎片对象加载完数据后的回调接口方法通知刷新显示
     *
     * @param fragment
     * @param msgNumber
     */
    @Override
    public void RefreshTabTitleText(Fragment fragment, int msgNumber) {
//        if (fragment instanceof EntireCommentFragment){
//            String whole = String.format(getResources().getString(R.string.title_comment_entire), String.valueOf(msgNumber));
//            mTabTitleTexts.remove(0);
//            mTabTitleTexts.add(0, whole);
//        }else if (fragment instanceof SlideCommentFragment){
//            String slide = String.format(getResources().getString(R.string.title_comment_slide), String.valueOf(msgNumber));
//            mTabTitleTexts.remove(1);
//            mTabTitleTexts.add(1, slide);
//        }else if (fragment instanceof NegativeCommentFragment){
//            String grade = String.format(getResources().getString(R.string.title_comment_negative), String.valueOf(msgNumber));
//            mTabTitleTexts.remove(2);
//            mTabTitleTexts.add(2, grade);
//        }
//        // 重新设置并刷新数据
//        myViewPagerAdapter.setTabTitleTexts(mTabTitleTexts);
        myViewPagerAdapter.notifyDataSetChanged();
        mTabLayout.notifyDataSetChanged();
    }


    /**
     * 内部适配器类的定义
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


        public void setTabTitleTexts(List<String> tabTitleTexts) {
            mTabTitleTexts = tabTitleTexts;
        }

    }
}
