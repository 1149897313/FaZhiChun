package com.zgkj.fazhichun.fragments.comment.entire;

import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zgkj.common.app.Fragment;
import com.zgkj.common.widgets.LabelsView;
import com.zgkj.common.widgets.load.LoadFactory;
import com.zgkj.common.widgets.load.core.LoadManager;
import com.zgkj.common.widgets.load.view.AbsView;
import com.zgkj.common.widgets.recycler.decoration.DividerItemDecoration;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.adapter.comment.CommentAdapter;
import com.zgkj.fazhichun.fragments.comment.IRefreshTabTitleText;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/1.
 * Descr:   晒图显示碎片的定义
 */

public class SlideCommentFragment extends Fragment {


    /**
     * UI
     */
    private NestedScrollView mNestedScrollView;
    private LabelsView mLabelsView;
    private RecyclerView mRecyclerView;

    /**
     * DATA
     */
    //创建一个改变TabText并刷新显示内容的接口对象
    private IRefreshTabTitleText mIRefreshTabTitleText;

    // 创建一个加载状态的管理对象
    private LoadManager mLoadManager;

    // 创建适配器对象
    private CommentAdapter mCommentAdapter;


    /**
     * 显示晒图碎片对象
     *
     * @param refreshTabTitleText
     * @return
     */
    public static SlideCommentFragment newInstance(IRefreshTabTitleText refreshTabTitleText) {
        SlideCommentFragment slideCommentFragment = new SlideCommentFragment();
        slideCommentFragment.setIRefreshTabTitleText(refreshTabTitleText);

        return slideCommentFragment;
    }


    /**
     * 初始化接口对象
     *
     * @param refreshTabTitleText
     */
    public void setIRefreshTabTitleText(IRefreshTabTitleText refreshTabTitleText) {
        mIRefreshTabTitleText = refreshTabTitleText;
    }


    @Override
    protected int getLayoutSourceId() {
        return R.layout.fragment_comment_slide;
    }


    @Override
    protected void initWidgets(View rootView) {
        super.initWidgets(rootView);
        // init UI
        mNestedScrollView = rootView.findViewById(R.id.nested_scroll_view);
        mLabelsView = rootView.findViewById(R.id.labels_view);
        mRecyclerView = rootView.findViewById(R.id.recycler_view);


    }

    @Override
    protected void initDatas() {
        super.initDatas();

        mLoadManager = LoadFactory.getInstance().register(mNestedScrollView, new AbsView.OnReloadListener() {
            @Override
            public void onReload(View view) {
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoadManager.showSuccessView();
            }
        }, 1000);


        List<String> list = new ArrayList<>();
        list.add("高逼格发型师  25");
        list.add("西伯利亚大沙漠环境  30");
        list.add("技师专业  20");
        list.add("服务如冬天里的一把火  20");
        list.add("优惠多多  20");

        mLabelsView.setLabels(list);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL, R.drawable.shape_divider_line, 16));
        mCommentAdapter = new CommentAdapter();

        mRecyclerView.setAdapter(mCommentAdapter);

        loadData();

    }


    private void loadData(){
        List<String> list = new ArrayList<>();

        for (int i = 0; i < 2; i++){

            list.add("a");
        }

//        mCommentAdapter.replace(list);
        if (mIRefreshTabTitleText != null){
            mIRefreshTabTitleText.RefreshTabTitleText(this, list.size());
        }


    }



}
