package com.zgkj.fazhichun.fragments.main;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
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
import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.common.widgets.recycler.decoration.SpaceItemDecoration;
import com.zgkj.common.widgets.text.TextDrawable;
import com.zgkj.factory.model.api.RspModel;
import com.zgkj.factory.model.api.home.Banner;
import com.zgkj.fazhichun.App;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.activities.AccountActivity;
import com.zgkj.fazhichun.activities.BarberShopActivity;
import com.zgkj.fazhichun.activities.CommodityActivity;
import com.zgkj.fazhichun.activities.SearchActivity;
import com.zgkj.fazhichun.activities.TypeActivity;
import com.zgkj.fazhichun.adapter.home.HomeAdapter;
import com.zgkj.fazhichun.entity.HairdresserType;
import com.zgkj.fazhichun.entity.ShopList;
import com.zgkj.fazhichun.entity.SortList;
import com.zgkj.fazhichun.entity.shop.StoreListAndProductList;
import com.zgkj.fazhichun.fragments.dialog.location.SelectCityListFragment;
import com.zgkj.fazhichun.holder.NetWorkImageHolderView;
import com.zgkj.fazhichun.observer.location.LocationStateObserver;
import com.zgkj.fazhichun.view.LoadingView;
import com.zgkj.fazhichun.view.LocationView;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/2/27.
 * Descr:   定义主页显示的碎片
 */

public class HomeFragment extends Fragment implements View.OnClickListener, LocationStateObserver.OnLocationStateListener, OnItemClickListener {

    /**
     * UI
     */
    private LinearLayout mCityLayout;
    private TextView mCityView;
    private TextView mSearchView;
    private NestedScrollView mNestedScrollView;
    private TextDrawable mHaircutView;        // 剪发
    private TextDrawable mBlowView;           // 洗吹
    private TextDrawable mHairDyeView;        // 染发
    private TextDrawable mPermView;           // 烫发
    private ConvenientBanner convenientBanner;

    private RecyclerView mRecyclerView;

    /**
     * DATA
     */
    private HomeAdapter mHomeAdapter;

    // 创建一个加载管理对象
    private LoadManager mLoadManager;

    // 定位操作的监听接口对象
    private OnLocationHandleListener mOnLocationHandleListener;

    /**
     * 显示主页碎片对象
     *
     * @return
     */
    public static HomeFragment newInstance(OnLocationHandleListener onLocationHandleListener) {
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setOnLocationHandleListener(onLocationHandleListener);

        return homeFragment;
    }


    /**
     * 设置监听
     *
     * @param onLocationHandleListener
     */
    private void setOnLocationHandleListener(OnLocationHandleListener onLocationHandleListener) {
        mOnLocationHandleListener = onLocationHandleListener;
    }

    @Override
    protected int getLayoutSourceId() {
        return R.layout.fragment_home;
    }


    @Override
    protected void initWidgets(View rootView) {
        super.initWidgets(rootView);
        // init UI
        mCityLayout = rootView.findViewById(R.id.city_layout);
        mCityView = rootView.findViewById(R.id.city);
        mSearchView = rootView.findViewById(R.id.search);
        mNestedScrollView = rootView.findViewById(R.id.nested_scroll_view);
        mHaircutView = rootView.findViewById(R.id.haircut);
        mBlowView = rootView.findViewById(R.id.blow);
        mHairDyeView = rootView.findViewById(R.id.hair_dye);
        mPermView = rootView.findViewById(R.id.perm);
        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        convenientBanner = rootView.findViewById(R.id.comvenientbanner);

        // 为城市切换的显示控件注册点击事件
        mCityLayout.setOnClickListener(this);

        // 为标题栏搜索按钮注册点击事件
        mSearchView.setOnClickListener(this);

        mHaircutView.setOnClickListener(this);
        mBlowView.setOnClickListener(this);
        mHairDyeView.setOnClickListener(this);
        mPermView.setOnClickListener(this);

    }

    @Override
    protected void initDatas() {
        super.initDatas();

        // 初始化页面加载管理器
        mLoadManager = LoadFactory.getInstance().register(mNestedScrollView, new AbsView.OnViewChildClickListener() {
            @Override
            public void onChildClick(View view) {
                // 显示加载中
                mLoadManager.showStateView(LoadingView.class);
                mCityView.setText("正在定位...");
                // 启动重新定位
                mOnLocationHandleListener.onStartLocation();
            }
        });


        // 初始化显示
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(mContext, 10));
        mHomeAdapter = new HomeAdapter(new RecyclerViewAdapter.AdapterListenerImpl<StoreListAndProductList>() {
            @Override
            public void onItemClick(RecyclerViewAdapter.ViewHolder<StoreListAndProductList> holder, StoreListAndProductList data) {
                super.onItemClick(holder, data);
                // 跳转到理发店详情界面
                BarberShopActivity.show(mContext,data.getShop_id());//data.getShop_id()
            }
        });

        mRecyclerView.setAdapter(mHomeAdapter);

        // 启动定位
        if (mOnLocationHandleListener != null) {
            mOnLocationHandleListener.onStartLocation();
        }


        // 注册一个定位状态的观察者
        LocationStateObserver.getInstance().addObserver(this);

        // 请求数据
        mLoadManager.showSuccessView();

        mCityView.setText("重庆市");
        mCityView.setEnabled(true);

        onGetBanner();
//        onGetHairdresserList();
        onHot();

    }

    /**
     * 图片轮播
     *
     * @param imageUrl
     */
    private void showBanner(List<Banner> imageUrl) {

        //开始自动翻页
        convenientBanner.setPages(new CBViewHolderCreator() {
            @Override
            public NetWorkImageHolderView createHolder() {
                return new NetWorkImageHolderView();
            }
        }, imageUrl)
                //设置指示器是否可见
                .setPointViewVisible(true)
                //设置自动切换（同时）
                .startTurning(3000)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                //设置指示器的方向（左、中、右）
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                //设置点击监听事件
                .setOnItemClickListener(this)
                //设置手动影响（设置了该项无法手动切换）
                .setManualPageable(true);

        //设置翻页的效果，不需要翻页效果可用不设
        //setPageTransformer(Transformer.DefaultTransformer);   // 集成特效之后会有白屏现象，新版已经分离，如果要集成特效的例子可以看Demo的点击响应。
    }

    // 开始自动翻页
    @Override
    public void onResume() {
        super.onResume();
        convenientBanner.startTurning(3000);
    }

    // 停止自动翻页
    @Override
    public void onPause() {
        super.onPause();
        convenientBanner.stopTurning();
    }


    /**
     * 分类列表列表
     */
    private void onGetHairdresserType() {
        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        okHttpClient.post("/v1/hairdresser-type/show", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:onGetHairdresserType " + response.toString());
                if (response.getCode() == 200) {
                    try {
                        Gson gson = new Gson();
                        Type type = new TypeToken<RspModel<HairdresserType>>() {
                        }.getType();
                        RspModel<HairdresserType> rspModel = gson.fromJson(response.getBody(), type);
                        Log.i(TAG, "onGetHairdresserType: " + rspModel.toString());

                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 获取首页banner
     */
    private void onGetBanner() {

        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        okHttpClient.post("/v1/banner/show", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:onGetBanner" + response.toString());
                if (response.getCode() == 200) {
                    try {
                        Gson gson = new Gson();
                        Type type = new TypeToken<RspModel<List<Banner>>>() {
                        }.getType();
                        RspModel<List<Banner>> rspModel = gson.fromJson(response.getBody(), type);
                        showBanner(rspModel.getData());

                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 获取分类列表列表
     */
    private void onGetHairdresserList() {

        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        okHttpClient.post("/v1/hairdresser-type/show", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:onGetSoltList" + response.toString());
                if (response.getCode() == 200) {
                    try {
                        Gson gson = new Gson();
                        Type type = new TypeToken<RspModel<List<SortList>>>() {
                        }.getType();
                        Log.i(TAG, "onSuccess:onGetHairdresserList " + response.getBody());
                        RspModel<List<SortList>> rspModel = gson.fromJson(response.getBody(), type);
                        Log.i(TAG, "onSuccess:onGetHairdresserList " + rspModel.toString());

                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 获取首页热门
     */
    private void onHot() {
        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        AsyncHttpPostFormData.addFormData("page", "1");
        AsyncHttpPostFormData.addFormData("lng", "0");
        AsyncHttpPostFormData.addFormData("lat", "0");
        AsyncHttpPostFormData.addFormData("shop_name", "");
        okHttpClient.post("/shops/1", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:onHot " + response.toString());
                if (response.getCode() == 200) {
                    try {
                        Gson gson = new Gson();
                        Type type = new TypeToken<RspModel<ShopList>>() {
                        }.getType();
                        RspModel<ShopList> rspModel = gson.fromJson(response.getBody(), type);
                        Log.i(TAG, "onBackValue: " + rspModel.toString());
                        //商家列表
                        mHomeAdapter.replace(rspModel.getData().getShop_list());

                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 点击事件的回调方法
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.city_layout:     // 点击了切换城市
                showCityPickerDialogFragment();
                break;
            case R.id.search:       // 标题栏搜索按钮
                // 跳转到搜索界面
                SearchActivity.show(mContext);
                break;
            case R.id.haircut:      // 剪发
                TypeActivity.show(mContext);
                break;
            case R.id.blow:         // 洗吹
                TypeActivity.show(mContext);
                break;
            case R.id.hair_dye:     // 染发
                TypeActivity.show(mContext);
                break;
            case R.id.perm:         // 烫发
                AccountActivity.show(mContext);
                break;
            default:
                break;
        }

    }


    /**
     * 显示切换城市
     */
    private void showCityPickerDialogFragment() {
//        CityPickerDialogFragment cityPickerDialogFragment = CityPickerDialogFragment.newInstance();
//        if (!cityPickerDialogFragment.isAdded()) {
//            cityPickerDialogFragment.show(getChildFragmentManager(), cityPickerDialogFragment.getClass().getName());
//        }
        SelectCityListFragment selectCityListFragment = SelectCityListFragment.newInstance();
        if (!selectCityListFragment.isAdded()) {
            selectCityListFragment.show(getChildFragmentManager(), selectCityListFragment.getClass().getName());
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOnLocationHandleListener != null) {
            mOnLocationHandleListener = null;
        }
        // 取消监听
        LocationStateObserver.getInstance().removeObserver();
    }


    /**
     * 定位成功的回调
     */
    @Override
    public void onLocationSuccess() {
        // 请求数据
        mLoadManager.showSuccessView();

        mCityView.setText("重庆市");
        mCityView.setEnabled(true);

        // 停止定位
        mOnLocationHandleListener.onStopLocation();

    }


    /**
     * 定位失败的回调
     */
    @Override
    public void onLocationFailure() {
        // 显示加载失败的界面
        mLoadManager.showStateView(LocationView.class);

        mCityView.setText("地址出错了");

        // 停止定位
        mOnLocationHandleListener.onStopLocation();
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(mContext, "点击了第" + position + "个", Toast.LENGTH_SHORT).show();
    }

    /**
     * 定义一个定位操作的监听接口
     */
    public interface OnLocationHandleListener {

        /**
         * 启动定位
         */
        void onStartLocation();

        /**
         * 停止定位
         */
        void onStopLocation();
    }
}
