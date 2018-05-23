package com.zgkj.fazhichun.fragments.comment;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zgkj.common.app.Fragment;
import com.zgkj.common.http.AsyncHttpPostFormData;
import com.zgkj.common.http.AsyncHttpResponse;
import com.zgkj.common.http.AsyncOkHttpClient;
import com.zgkj.common.http.AsyncResponseHandler;
import com.zgkj.common.widgets.load.LoadFactory;
import com.zgkj.common.widgets.load.core.LoadManager;
import com.zgkj.common.widgets.load.view.AbsView;
import com.zgkj.common.widgets.recycler.decoration.DividerItemDecoration;
import com.zgkj.factory.model.api.RspModel;
import com.zgkj.fazhichun.App;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.adapter.comment.MineCommentAdapter;
import com.zgkj.fazhichun.entity.HairdresserType;
import com.zgkj.fazhichun.entity.comment.Comment;
import com.zgkj.fazhichun.view.EmptyView;
import com.zgkj.fazhichun.view.NetErrorView;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/28.
 * Descr:   我的评论内容显示碎片对象
 */
public class MineCommentFragment extends Fragment {


    /**
     * UI
     */
    private SmartRefreshLayout refresh_layout;
    private RecyclerView mRecyclerView;


    /**
     * DATA
     */
    private LoadManager mLoadManager;
    // 创建适配器对象
    private MineCommentAdapter mMineCommentAdapter;


    /**
     * 显示我的评论碎片对象
     *
     * @return
     */
    public static MineCommentFragment newInstance(){
        MineCommentFragment fragment = new MineCommentFragment();
        return fragment;
    }



    @Override
    protected int getLayoutSourceId() {
        return R.layout.fragment_mine_comment;
    }


    @Override
    protected void initWidgets(View rootView) {
        super.initWidgets(rootView);
        // init ui
        refresh_layout = rootView.findViewById(R.id.refresh_layout);
        mRecyclerView = rootView.findViewById(R.id.recycler_view);

        // 初始化界面状态加载管理器
        mLoadManager = LoadFactory.getInstance().register(refresh_layout, new AbsView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                // 重新请求数据
            }
        });


        mLoadManager.showSuccessView();


        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL, R.drawable.shape_divider_line, 16, false));

        mMineCommentAdapter = new MineCommentAdapter();

        mRecyclerView.setAdapter(mMineCommentAdapter);

        setRefresh();

    }

    private int page = 1;

    /**
     * 刷新
     */
    private void setRefresh() {

        getMyComment(page);
        // 下拉刷新控件
        refresh_layout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                //全局设置主题颜色
                layout.setPrimaryColorsId(R.color.colorAccent, android.R.color.white);
                //指定为经典Header，默认是 贝塞尔雷达Header
                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });

        // 下拉刷新
        refresh_layout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getMyComment(page);
                refreshlayout.finishRefresh();
            }
        });

        // 滑到底部加载更多数据的监听接口
        refresh_layout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refresh_layout.finishLoadmore();
                page++;
                getMyComment(page);
            }
        });
    }


    /**
     * 订单我的评论列表查询接口
     * @param page
     */
    private void getMyComment(final int page) {

        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        AsyncHttpPostFormData.addFormData("page", page);
        okHttpClient.post("/v1/order-evaluate/my-evaluate-list", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                mLoadManager.showStateView(NetErrorView.class);
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:我的评论列表" + response.toString());
                Type type = new TypeToken<RspModel<List<Comment>>>() {
                }.getType();
                List<Comment> list = getAnalysis(response, type, "我的评论列表");
                if (list != null && !"[]".equals(list.toString())) {
                    if(page==1) {
                        mMineCommentAdapter.replace(list);
                    }else{
                        mMineCommentAdapter.add(list);
                    }
                }else{
                    if(page==1){
                        mLoadManager.showStateView(EmptyView.class);
                    }
                }
            }
        });
    }


    /**
     * 数据解析
     *
     * @param response
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
}
