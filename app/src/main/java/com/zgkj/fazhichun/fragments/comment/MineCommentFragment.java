package com.zgkj.fazhichun.fragments.comment;

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
import com.zgkj.common.widgets.load.LoadFactory;
import com.zgkj.common.widgets.load.core.LoadManager;
import com.zgkj.common.widgets.load.view.AbsView;
import com.zgkj.common.widgets.recycler.decoration.DividerItemDecoration;
import com.zgkj.factory.model.api.RspModel;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.adapter.comment.MineCommentAdapter;
import com.zgkj.fazhichun.entity.HairdresserType;

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
    private NestedScrollView mNestedScrollView;
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
        mNestedScrollView = rootView.findViewById(R.id.nested_scroll_view);
        mRecyclerView = rootView.findViewById(R.id.recycler_view);

        // 初始化界面状态加载管理器
        mLoadManager = LoadFactory.getInstance().register(mNestedScrollView, new AbsView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                // 重新请求数据
            }
        });


        // 模拟网络请求
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoadManager.showSuccessView();
            }
        }, 1000);




        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL, R.drawable.shape_divider_line, 16, false));

        mMineCommentAdapter = new MineCommentAdapter();

        mRecyclerView.setAdapter(mMineCommentAdapter);

    }

    @Override
    protected void initDatas() {
        super.initDatas();

        // 模拟的假数据
        List<String> list = new ArrayList<>();

        for (int i = 0; i < 30; i++){

            list.add("a");
        }

        mMineCommentAdapter.replace(list);
    }

    /**
     *我的评论
     * @param page
     */
    private void getMIneComment(String page) {
        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        AsyncHttpPostFormData.addFormData("page",page);
        okHttpClient.post("/v1/order-evaluate/my-evaluate-list", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:getMIneComment " + response.toString());
                if (response.getCode() == 200) {
                    try {
                        Gson gson = new Gson();
                        Type type = new TypeToken<RspModel<HairdresserType>>() {
                        }.getType();
                        RspModel<HairdresserType> rspModel = gson.fromJson(response.getBody(), type);
                        Log.i(TAG, "getMIneComment: " + rspModel.toString());

                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
