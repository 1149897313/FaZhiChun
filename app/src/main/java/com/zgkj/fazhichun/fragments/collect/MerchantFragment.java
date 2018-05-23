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
import com.zgkj.fazhichun.activities.BarberShopActivity;
import com.zgkj.fazhichun.adapter.collect.Collect;
import com.zgkj.fazhichun.adapter.collect.MerchantAdapter;
import com.zgkj.fazhichun.entity.collection.Collection;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/8.
 * Descr:   收藏界面的商家显示碎片
 */

public class MerchantFragment extends Fragment {

    /**
     * UI
     */
    private RecyclerView mRecyclerView;

    /**
     * DATA
     */
    private MerchantAdapter mAdapter;

    // 定义一个用于数据显示的数据集合
    private List<Collect> mDataList;


    // 当前显示的RecyclerView是否处于编辑状态，默认不处于编辑状态
    private boolean mIsEditState = false;


    /**
     * 显示收藏的商家订单
     *
     * @return
     */
    public static MerchantFragment newInstance(){
        MerchantFragment merchantFragment = new MerchantFragment();

        return merchantFragment;
    }


    @Override
    protected int getLayoutSourceId() {
        return R.layout.fragment_merchant;
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
        // init data
        mDataList = new ArrayList<>();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL, R.drawable.shape_divider_line, 16, false));
        // 初始化适配器对象并注册Item的点击事件
        mAdapter = new MerchantAdapter(new RecyclerViewAdapter.AdapterListenerImpl<Collection>() {
            @Override
            public void onItemClick(RecyclerViewAdapter.ViewHolder<Collection> holder, Collection data) {
                super.onItemClick(holder, data);
                if (mIsEditState){      // 如果为编辑模式
                    boolean selected = data.isSelected();
                    if (!selected){
                        // 改变为选中状态
                        data.setSelected(true);
                    }else {
                        data.setSelected(false);
                    }
                    // 刷新显示
                    mAdapter.notifyItemChanged(holder.getAdapterPosition());

                }else {
                    // 跳转到理发店详情界面
                    BarberShopActivity.show(mContext,data.getShop_id());
                }

            }
        });

        mRecyclerView.setAdapter(mAdapter);

        onGetCollection("shop");
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
                Log.i(TAG, "onSuccess:onGetCollectionDP" + response.toString());
                if (response.getCode() == 200) {
                    try {
                        Gson gson = new Gson();
                        Type type = new TypeToken<RspModel<List<Collection>>>() {
                        }.getType();
                        RspModel<List<Collection>> rspModel = gson.fromJson(response.getBody(), type);
                        Log.i(TAG, "onSuccess: onGetCollectionDP" + rspModel.toString());
                        mAdapter.replace(rspModel.getData());
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    /**
     * 是否显示选择删除条目的View
     *
     * @param editMode
     */
    public void setEditMode(int editMode){
        mAdapter.setEditMode(editMode);
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
                            onGetCollection("shop");
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
