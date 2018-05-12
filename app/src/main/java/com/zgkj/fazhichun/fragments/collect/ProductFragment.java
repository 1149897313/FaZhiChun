package com.zgkj.fazhichun.fragments.collect;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.zgkj.common.app.Fragment;
import com.zgkj.common.http.AsyncHttpPostFormData;
import com.zgkj.common.http.AsyncHttpResponse;
import com.zgkj.common.http.AsyncOkHttpClient;
import com.zgkj.common.http.AsyncResponseHandler;
import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.common.widgets.recycler.decoration.DividerItemDecoration;
import com.zgkj.factory.model.api.RspModel;
import com.zgkj.fazhichun.App;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.activities.CommodityActivity;
import com.zgkj.fazhichun.adapter.collect.Collect;
import com.zgkj.fazhichun.adapter.collect.ProductAdapter;
import com.zgkj.fazhichun.entity.collection.Collection;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/8.
 * Descr:   收藏界面的服务
 */

public class ProductFragment extends Fragment {

    /**
     * UI
     */
    private RecyclerView mRecyclerView;


    /**
     * DATA
     */
    private ProductAdapter mAdapter;

    // 定义一个储存适配数据的集合对象
    private List<Collect> mDataList;


    // 当前显示的RecyclerView是否处于编辑状态，默认不处于编辑状态
    private boolean mIsEditState = false;


    /**
     * 显示收藏的团购订单
     *
     * @return
     */
    public static ProductFragment newInstance(){
        ProductFragment fragment = new ProductFragment();

        return fragment;
    }


    @Override
    protected int getLayoutSourceId() {
        return R.layout.fragment_product;
    }


    @Override
    protected void initWidgets(View rootView) {
        super.initWidgets(rootView);
        // init UI
        mRecyclerView = rootView.findViewById(R.id.recycler_view);
    }


    @Override
    protected void initDatas() {
        super.initDatas();
        // init DATA
        mDataList = new ArrayList<>();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL, R.drawable.shape_divider_line, 16, false));

        mAdapter = new ProductAdapter(new RecyclerViewAdapter.AdapterListenerImpl<Collection>() {
            @Override
            public void onItemClick(RecyclerViewAdapter.ViewHolder<Collection> holder, Collection data) {
                super.onItemClick(holder, data);
                // 如果当前RecyclerView的显示状态为编辑状态
                if (mIsEditState){
                    boolean selected = data.isSelected();
                    if (!selected){
                        // 改变为选中状态
                        data.setSelected(true);
                    }else {
                        // 改变为未选中状态
                        data.setSelected(false);
                    }
                    // 刷新数据的显示
                    mAdapter.notifyItemChanged(holder.getAdapterPosition());

                }else {
                    // 用户点击则进入具体的服务详细信息界面
                    CommodityActivity.show(mContext,null);
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        onGetCollection("hairdresser");
    }

    /**
     * 获取收藏列表
     *
     * @param type
     */
    private void onGetCollection(String type) {

        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData asyncHttpPostFormData = new AsyncHttpPostFormData();
        asyncHttpPostFormData.addFormData("type", type);//shop是店铺-hairdresser是商品
        okHttpClient.post("/v1/collection/collection-list", asyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:onGetCollectionCP" + response.toString());
                if (response.getCode() == 200) {
                    try {
                        Gson gson = new Gson();
                        Type type = new TypeToken<RspModel<List<Collection>>>() {
                        }.getType();
                        RspModel<List<Collection>> rspModel = gson.fromJson(response.getBody(), type);
                        Log.i(TAG, "onSuccess: onGetCollectionCP" + rspModel.toString());
                        mAdapter.replace(rspModel.getData());
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    public void selectAll(boolean state){
        if (mAdapter.getDataList() != null && mAdapter.getDataList().size() > 0){
            for (int i = 0; i < mAdapter.getDataList().size(); i++){
                Collection collect = mAdapter.getDataList().get(i);
                collect.setSelected(state);
            }
            mAdapter.notifyDataSetChanged();
        }

    }

    public void setEditMode(int editMode){
        mAdapter.setEditMode(editMode);
    }


    public void setEditState(boolean isEditState){
        mIsEditState = isEditState;
    }



    /**
     * 实现删除数据的方法
     */
    public void delectData(){
        if (mAdapter.getDataList() == null || mAdapter.getDataList().size() <= 0){
            Toast.makeText(mContext, "没有可删除的数据",Toast.LENGTH_SHORT).show();
            return;
        }else {
            List<String> list=new ArrayList<>();
            for (int i = 0; i < mAdapter.getDataList().size(); i++){
                Collection collect = mAdapter.getDataList().get(i);
                if (collect.isSelected()){
                    list.add(mAdapter.getDataList().get(i).getCollection_id());
                }
            }
            onCollectionDelete(new Gson().toJson(list));
        }


    }

    //批量删除收藏
    private void onCollectionDelete(String json) {
        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        AsyncHttpPostFormData.addFormData("collection_id", json);
        okHttpClient.post("/v1/collection/collection-array-delete", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:onCollectionDelete " + response.toString());
                if (response.getCode() == 200) {
                    try {
                        Gson gson = new Gson();
                        Type type = new TypeToken<RspModel<String>>() {
                        }.getType();
                        RspModel<String> rspModel = gson.fromJson(response.getBody(), type);
                        if("true".equals(rspModel.getData())){
                            App.showMessage("移除成功！");
                            onGetCollection("hairdresser");
                        }else{
                            App.showMessage("移除失败！");
                        }

                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }



}
