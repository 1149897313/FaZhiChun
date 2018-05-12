package com.zgkj.common.app;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.zgkj.common.R;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/2/27.
 * Descr:   ToolbarActivity基类的定义
 */

public abstract class ToolbarActivity extends Activity {

    // 定义标题栏对象
    protected Toolbar mToolbar;


    @Override
    protected void initWidgets() {
        super.initWidgets();
        // 初始化标题栏对象
        initToolbar((Toolbar) findViewById(R.id.toolbar));
    }

    /**
     * 初始化Toolbar标题栏
     *
     * @param toolbar Toolbar
     */
    public void initToolbar(Toolbar toolbar) {
        mToolbar = toolbar;
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        initTitleNeedBack();
    }


    /**
     * 设置Toolbar标题栏的显示属性
     */
    protected void initTitleNeedBack() {
        // 设置左上角的返回按钮为实际的返回效果
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // 设置显示返回按钮
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            // 设置不显示标题栏的title
            actionBar.setDisplayShowTitleEnabled(false);

        }

    }


}
