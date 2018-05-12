package com.zgkj.common.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.zgkj.common.R;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/5.
 * Descr:   带有Toolbar标题栏的碎片抽象基类，在Activity中没有使用Toolbar的情况下使用过
 */

public abstract class ToolbarFragment extends Fragment {

    // 定义标题栏对象
    protected Toolbar mToolbar;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置当前Fragment可以调用onCreateOptionsMenu()方法
        setHasOptionsMenu(true);
    }


    @Override
    protected void initWidgets(View rootView) {
        super.initWidgets(rootView);

        // 初始化标题栏对象
        initToolbar((Toolbar) rootView.findViewById(R.id.toolbar));
    }

    /**
     * 初始化Toolbar标题栏
     *
     * @param toolbar Toolbar
     */
    public void initToolbar(Toolbar toolbar) {
        mToolbar = toolbar;
        if (toolbar != null) {
            // 设置无Title
            mToolbar.setTitle("");
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            mToolbar.inflateMenu(getMenuSourceId());
        }
    }




    /**
     * 重写创建菜单的方法
     *
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (getMenuSourceId() != 0){
            inflater.inflate(getMenuSourceId(), menu);
        }
    }

    /**
     * 加载菜单资源文件
     *
     * @return      菜单资源文件的Id
     */
    protected abstract int getMenuSourceId();
}
