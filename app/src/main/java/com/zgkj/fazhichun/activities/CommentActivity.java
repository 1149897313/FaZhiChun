package com.zgkj.fazhichun.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zgkj.common.app.ToolbarActivity;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.fragments.comment.EntireFragment;
import com.zgkj.fazhichun.fragments.comment.MineCommentFragment;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/1.
 * Descr:   用户评价界面
 */

public class CommentActivity extends ToolbarActivity {

    /**
     * UI
     */
    private TextView mTitleView;
    private FrameLayout container_layout;

    /**
     * DATA
     */
    // 定义状态变量
    public static final int TYPE_ENTIRE_COMMENT = 1;       // 全部评论
    public static final int TYPE_MINE_COMMENT = 3;         // 我的评论
    private String id;

    // 定义界面接收值的键值对象
    private static final String TYPE_KEY = "type_key";
    private int mTypeValue;


    /**
     * 显示用户评价界面
     *
     * @param context
     */
    public static void show(Context context, int typeValue, String id) {
        // 跳转并传递值
        Intent intent = new Intent(context, CommentActivity.class);
        intent.putExtra(TYPE_KEY, typeValue);
        intent.putExtra("ID", id);
        context.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mTypeValue = bundle.getInt(TYPE_KEY, 0);
        id = bundle.getString("ID");
        // 如果属性值不为0则显示评论相关的界面
        if (mTypeValue == 0) {
            return false;
        } else if (mTypeValue == TYPE_MINE_COMMENT) {//我的评论
            return true;
        } else {//商品评论
            return mTypeValue != 0 && !TextUtils.isEmpty(id);
        }
    }

    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_comment;
    }

    @Override
    protected void initWidgets() {
        super.initWidgets();
        // init UI
        mTitleView = findViewById(R.id.title);
        container_layout = findViewById(R.id.container_layout);
    }


    @Override
    protected void initDatas() {
        super.initDatas();

        // 根据属性值显示对应的碎片对象
        showFragment(mTypeValue);
    }

    /**
     * 根据其他界面传递的属性值来判断需要显示的碎片对象
     */
    private void showFragment(int typeValue) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch (typeValue) {
            case TYPE_ENTIRE_COMMENT:       // 全部评论
                mTitleView.setText("评论");
                EntireFragment entireFragment=EntireFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putString("ID",id);//
                entireFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.container_layout, entireFragment);
                break;
//            case TYPE_USER_COMMENT:         // 用户评论
//                mTitleView.setText("用户评论");
//                fragmentTransaction.replace(R.id.container_layout, CommentFragment.newInstance());
//                break;
            case TYPE_MINE_COMMENT:         // 我的评论
                mTitleView.setText("我的评论");
                fragmentTransaction.replace(R.id.container_layout, MineCommentFragment.newInstance());
                break;
            default:
                break;
        }

        fragmentTransaction.commit();

    }
}
