package com.zgkj.fazhichun.fragments.comment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;
import com.zgkj.common.app.Fragment;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.fragments.comment.entire.EntireCommentFragment;
import com.zgkj.fazhichun.fragments.comment.entire.NegativeCommentFragment;
import com.zgkj.fazhichun.fragments.comment.entire.SlideCommentFragment;

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


    /**
     * DATA
     */
    private List<Fragment> mTabFragments;
    private List<String> mTabTitleTexts;

    private MyViewPagerAdapter myViewPagerAdapter;


    /**
     * 显示全部碎片对象
     *
     * @return
     */
    public static EntireFragment newInstance(){
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
    }


    @Override
    protected void initDatas() {
        super.initDatas();

        mTabFragments = new ArrayList<>();
        mTabFragments.add(EntireCommentFragment.newInstance(this));
        mTabFragments.add(SlideCommentFragment.newInstance(this));
        mTabFragments.add(NegativeCommentFragment.newInstance(this));

        mTabTitleTexts = new ArrayList<>();
        mTabTitleTexts.add(String.format(getResources().getString(R.string.title_comment_entire), "0"));
        mTabTitleTexts.add(String.format(getResources().getString(R.string.title_comment_slide), "0"));
        mTabTitleTexts.add(String.format(getResources().getString(R.string.title_comment_negative), "0"));

        // 为ViewPager设置预加载
        mViewPager.setOffscreenPageLimit(mTabFragments.size());

        // 初始化适配器对象
        myViewPagerAdapter = new MyViewPagerAdapter(getChildFragmentManager(), mTabFragments, mTabTitleTexts);
        mViewPager.setAdapter(myViewPagerAdapter);

        // 将TabLayout与ViewPager进行绑定
        mTabLayout.setViewPager(mViewPager);
    }


    /**
     * 各个碎片对象加载完数据后的回调接口方法通知刷新显示
     *
     * @param fragment
     * @param msgNumber
     */
    @Override
    public void RefreshTabTitleText(Fragment fragment, int msgNumber) {
        if (fragment instanceof EntireCommentFragment){
            String whole = String.format(getResources().getString(R.string.title_comment_entire), String.valueOf(msgNumber));
            mTabTitleTexts.remove(0);
            mTabTitleTexts.add(0, whole);
        }else if (fragment instanceof SlideCommentFragment){
            String slide = String.format(getResources().getString(R.string.title_comment_slide), String.valueOf(msgNumber));
            mTabTitleTexts.remove(1);
            mTabTitleTexts.add(1, slide);
        }else if (fragment instanceof NegativeCommentFragment){
            String grade = String.format(getResources().getString(R.string.title_comment_negative), String.valueOf(msgNumber));
            mTabTitleTexts.remove(2);
            mTabTitleTexts.add(2, grade);
        }
        // 重新设置并刷新数据
        myViewPagerAdapter.setTabTitleTexts(mTabTitleTexts);
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


        public void setTabTitleTexts(List<String> tabTitleTexts){
            mTabTitleTexts = tabTitleTexts;
        }

    }
}
