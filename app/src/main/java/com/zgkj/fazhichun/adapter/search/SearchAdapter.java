package com.zgkj.fazhichun.adapter.search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zgkj.common.widgets.StarBarView;
import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.common.widgets.recycler.decoration.DividerItemDecoration;
import com.zgkj.common.widgets.text.DrawableCenterTextView;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.activities.BarberShopActivity;
import com.zgkj.fazhichun.activities.CommodityActivity;
import com.zgkj.fazhichun.activities.TypeActivity;
import com.zgkj.fazhichun.adapter.home.HomeChildAdapter;
import com.zgkj.fazhichun.entity.shop.Hairdresser;
import com.zgkj.fazhichun.entity.shop.StoreListAndProductList;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/8.
 * Descr:   门店搜索界面数据适配器的定义
 */

public class SearchAdapter extends RecyclerViewAdapter<StoreListAndProductList> {



    @Override
    protected int getItemViewType(int position, StoreListAndProductList data) {
        return R.layout.cell_home_list;
    }

    @Override
    protected ViewHolder<StoreListAndProductList> getViewHolder(View view, int viewType) {
        return new SearchViewHolder(view);
    }


    class SearchViewHolder extends RecyclerViewAdapter.ViewHolder<StoreListAndProductList> implements View.OnClickListener {
        private RelativeLayout mShopLayout;
        private ImageView mShopImageView;
        private TextView mShopNameView;
        private StarBarView mStarBarView;
        private TextView mScoreView;
        private TextView mAddressView;
        private TextView mDistanceView;
        // 显示内部项目的RecyclerView
        private RecyclerView mRecyclerView;
        // 查看其它的团购项目
        private DrawableCenterTextView mOtherPurchaseView;

        // 创建内部的适配器对象
        private SearchChildAdapter mAdapter;


        /**
         * 构造方法
         *
         * @param itemView
         */
        public SearchViewHolder(View itemView) {
            super(itemView);
            mShopLayout = itemView.findViewById(R.id.shop_layout);
            mShopImageView = itemView.findViewById(R.id.shop_image);
            mShopNameView = itemView.findViewById(R.id.shop_name);
            mStarBarView = itemView.findViewById(R.id.star_bar_view);
            mScoreView = itemView.findViewById(R.id.score);
            mAddressView = itemView.findViewById(R.id.address);
            mDistanceView = itemView.findViewById(R.id.distance);
            mRecyclerView = itemView.findViewById(R.id.recycler_view);
            mOtherPurchaseView = itemView.findViewById(R.id.other_commodity);

            // 为商家信息条目注册点击监听事件
            mShopLayout.setOnClickListener(this);


            // 为查看其它的团购项目显示控件注册点击监听事件
            mOtherPurchaseView.setOnClickListener(this);

            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext,
                    DividerItemDecoration.VERTICAL, R.drawable.shape_divider_line, 16,true));
            // 初始化适配器并注册点击事件
            mAdapter = new SearchChildAdapter(new AdapterListenerImpl<Hairdresser>() {
                @Override
                public void onItemClick(ViewHolder<Hairdresser> holder, Hairdresser data) {
                    super.onItemClick(holder, data);
                    CommodityActivity.show(mContext,String.valueOf(data.getHairdresser_id()));
                }
            });
            mRecyclerView.setAdapter(mAdapter);
        }

        @Override
        protected void onBind(StoreListAndProductList data,int position) {
            mShopNameView.setText(data.getShop_name());
            mScoreView.setText(String.valueOf(data.getShop_service_score()) + "分");
            mDistanceView.setText(String.valueOf(data.getDistance()));
            mAddressView.setText(data.getAddress());
            Picasso.get().load("".equals(data.getShop_image())?mContext.getResources().getString(R.string.none_image_url):data.getShop_image()).placeholder(R.drawable.ic_shop_bg)
                    .into(mShopImageView);

            HomeChildAdapter mChildAdapter = new HomeChildAdapter(new RecyclerViewAdapter.AdapterListenerImpl<Hairdresser>() {
                @Override
                public void onItemClick(RecyclerViewAdapter.ViewHolder<Hairdresser> holder, Hairdresser data) {
                    super.onItemClick(holder, data);
                    // 跳转到理发店商品项目列表界面
                    CommodityActivity.show(mContext,String.valueOf(data.getHairdresser_id()));
                }
            });
            mChildAdapter.replace(data.getHairdresser());
            mRecyclerView.setAdapter(mChildAdapter);
        }

        @Override
        public boolean isNeedClick() {
            return true;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.shop_layout:      // 点击了商家信息条目
                    // 跳转到理发店详情界面
                    BarberShopActivity.show(mContext,null);
                    break;
                case R.id.other_purchase:       // 查看其它团购项目
                    // 跳转到商品列表界面
                    CommodityActivity.show(mContext,null);
                    break;
            }
        }

    }


    public class SearchChildAdapter extends RecyclerViewAdapter<Hairdresser> {


        public SearchChildAdapter(AdapterListener<Hairdresser> adapterListener) {
            super(adapterListener);
        }

        @Override
        protected int getItemViewType(int position, Hairdresser data) {
            return R.layout.cell_home_child_list;
        }

        @Override
        protected ViewHolder<Hairdresser> getViewHolder(View view, int viewType) {
            return new SearchChildViewHolder(view);
        }
    }


    class SearchChildViewHolder extends RecyclerViewAdapter.ViewHolder<Hairdresser> {

        private TextView mPriceView;
        private TextView mOriginalPriceView;
        private TextView mTypeNameView;
        private TextView mSellNumberView;


        /**
         * 构造方法
         *
         * @param itemView
         */
        public SearchChildViewHolder(View itemView) {
            super(itemView);
            mPriceView = itemView.findViewById(R.id.price);
            mOriginalPriceView = itemView.findViewById(R.id.original_price);
            mTypeNameView = itemView.findViewById(R.id.type_name);
            mSellNumberView = itemView.findViewById(R.id.sell_number);
        }

        @SuppressLint("StringFormatMatches")
        @Override
        protected void onBind(Hairdresser data,int position) {
            mPriceView.setText(mContext.getResources().getString(R.string.label_price, 35));
            mOriginalPriceView.setText(mContext.getResources().getString(R.string.label_original_price, 105));
            mSellNumberView.setText(mContext.getResources().getString(R.string.label_sell_number, 12));

        }

        @Override
        public boolean isNeedClick() {
            return true;
        }

    }

    // 模拟数据请求
    private List<String> getLoadData() {
        List<String> stringList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            stringList.add("空港广场发型艺术造型" + i);
        }

        return stringList;
    }


}
