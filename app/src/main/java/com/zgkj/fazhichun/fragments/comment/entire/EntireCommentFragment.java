package com.zgkj.fazhichun.fragments.comment.entire;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.zgkj.common.app.Fragment;
import com.zgkj.common.http.AsyncHttpPostFormData;
import com.zgkj.common.http.AsyncHttpResponse;
import com.zgkj.common.http.AsyncOkHttpClient;
import com.zgkj.common.http.AsyncResponseHandler;
import com.zgkj.common.widgets.LabelsView;
import com.zgkj.common.widgets.load.LoadFactory;
import com.zgkj.common.widgets.load.core.LoadManager;
import com.zgkj.common.widgets.load.view.AbsView;
import com.zgkj.common.widgets.recycler.decoration.DividerItemDecoration;
import com.zgkj.factory.model.api.RspModel;
import com.zgkj.fazhichun.App;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.adapter.comment.CommentAdapter;
import com.zgkj.fazhichun.entity.comment.Comment;
import com.zgkj.fazhichun.entity.comment.EvaluateLabel;
import com.zgkj.fazhichun.fragments.comment.IRefreshTabTitleText;
import com.zgkj.fazhichun.view.EmptyView;
import com.zgkj.fazhichun.view.NetErrorView;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/1.
 * Descr:   全部评论显示碎片的定义
 */

public class EntireCommentFragment extends Fragment {

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
    private String shopId;

    @Override
    protected void initArgs(Bundle bundle) {
        shopId = bundle.getString("ID");
        super.initArgs(bundle);
    }

    /**
     * 显示全部评论的碎片对象
     *
     * @param refreshTabTitleText
     * @return
     */
    public static EntireCommentFragment newInstance(IRefreshTabTitleText refreshTabTitleText) {
        EntireCommentFragment entireCommentFragment = new EntireCommentFragment();
        entireCommentFragment.setIRefreshTabTitleText(refreshTabTitleText);

        return entireCommentFragment;
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
        return R.layout.fragment_comment_entire;
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
        list.add("干净卫生  20");
        list.add("高大上  7");
        list.add("高逼格发型师  25");
        list.add("西伯利亚大沙漠环境  30");
        list.add("技师专业  20");
        list.add("服务如冬天里的一把火  20");
        list.add("淘气  20");
        list.add("优惠多多  20");

        mLabelsView.setLabels(list);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL, R.drawable.shape_divider_line, 16));
        mCommentAdapter = new CommentAdapter();

        mRecyclerView.setAdapter(mCommentAdapter);

        getEvaluateLabel(shopId,"all");
    }

    /**
     * 获取评论标签
     * @param id
     * @param type all全部-is_img有图-is_low低分-new最新
     */
    private void getEvaluateLabel(String id,String type) {

        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        AsyncHttpPostFormData.addFormData("shop_id", id);
        AsyncHttpPostFormData.addFormData("type", type);
        okHttpClient.post("/v1/order-evaluate/evaluate-label", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:评论标签：" + response.toString());
                Type type = new TypeToken<RspModel<EvaluateLabel>>() {
                }.getType();
                EvaluateLabel evaluateCount = getAnalysis(response, type, "评论标签");
                if (evaluateCount != null) {
                } else {
                }
            }
        });
    }


    private void getCommentList(String page,String shop_id,String type) {

        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        AsyncHttpPostFormData.addFormData("page", page);
        AsyncHttpPostFormData.addFormData("shop_id", shop_id);
        AsyncHttpPostFormData.addFormData("type", type);
        okHttpClient.post("/v1/order-evaluate/my-evaluate-list", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                mLoadManager.showStateView(NetErrorView.class);
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:我的评论列表" + response.toString());
                Type type = new TypeToken<RspModel<Comment>>() {
                }.getType();
                Comment evaluateCount = getAnalysis(response, type, "我的评论列表");
                if (evaluateCount != null) {
                } else {
                    mLoadManager.showStateView(EmptyView.class);
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



    private void loadData() {
        List<String> list = new ArrayList<>();

        for (int i = 0; i < 15; i++) {

            list.add("a");
        }

//        mCommentAdapter.replace(list);
        if (mIRefreshTabTitleText != null) {
            mIRefreshTabTitleText.RefreshTabTitleText(this, list.size());
        }


    }


}
