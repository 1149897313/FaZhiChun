package com.zgkj.fazhichun.adapter.member;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zgkj.common.widgets.StarBarView;
import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.activities.BarberShopActivity;
import com.zgkj.fazhichun.activities.CommodityActivity;
import com.zgkj.fazhichun.activities.TypeActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/2.
 * Descr:   美发会员商家适配器
 */

public class MemberAdapter extends RecyclerViewAdapter<String> {

    // 创建上下文对象用于界面的跳转
    private Context mContext;


    public MemberAdapter(Context context) {
        mContext = context;
    }


    @Override
    protected int getItemViewType(int position, String data) {
        return R.layout.cell_member_list;
    }

    @Override
    protected ViewHolder<String> getViewHolder(View view, int viewType) {
        return new MemberViewHolder(view);
    }


    class MemberViewHolder extends RecyclerViewAdapter.ViewHolder<String> implements View.OnClickListener {
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
        private TextView mOtherPurchaseView;

        // 创建内部的适配器对象
        private MemberChildAdapter mAdapter;


        /**
         * 构造方法
         *
         * @param itemView
         */
        public MemberViewHolder(View itemView) {
            super(itemView);
            mShopLayout = itemView.findViewById(R.id.shop_layout);
            mShopImageView = itemView.findViewById(R.id.shop_image);
            mShopNameView = itemView.findViewById(R.id.shop_name);
            mStarBarView = itemView.findViewById(R.id.star_bar_view);
            mScoreView = itemView.findViewById(R.id.score);
            mAddressView = itemView.findViewById(R.id.address);
            mDistanceView = itemView.findViewById(R.id.distance);
            mRecyclerView = itemView.findViewById(R.id.recycler_view);
            mOtherPurchaseView = itemView.findViewById(R.id.other_purchase);

            // 为商家信息条目注册点击监听事件
            mShopLayout.setOnClickListener(this);

            // 为查看其它的团购项目显示控件注册点击监听事件
            mOtherPurchaseView.setOnClickListener(this);

            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            mAdapter = new MemberChildAdapter(new AdapterListenerImpl<String>() {
                @Override
                public void onItemClick(ViewHolder<String> holder, String data) {
                    super.onItemClick(holder, data);
                    // 跳转到显示具体的理发类型项目的详细信息界面
                    TypeActivity.show(mContext);
                }
            });
            mRecyclerView.setAdapter(mAdapter);

        }

        @SuppressLint("StringFormatMatches")
        @Override
        protected void onBind(String data) {
            mShopImageView.setBackgroundResource(R.color.orange_400);
            mDistanceView.setText(itemView.getContext().getResources().getString(R.string.label_distance, 45.3));

            // 为适配器设置数据
            mAdapter.replace(getLoadData());

        }

        @Override
        public boolean isNeedClick() {
            return true;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.shop_layout:
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


    class MemberChildAdapter extends RecyclerViewAdapter<String> {


        public MemberChildAdapter(AdapterListener<String> adapterListener) {
            super(adapterListener);
        }

        @Override
        protected int getItemViewType(int position, String data) {
            return R.layout.cell_member_child_list;
        }

        @Override
        protected ViewHolder<String> getViewHolder(View view, int viewType) {
            return new MemberChildViewHolder(view);
        }
    }


    class MemberChildViewHolder extends RecyclerViewAdapter.ViewHolder<String> {
        private Context mContext;
        private TextView mPriceView;
        private TextView mOriginalPriceView;
        private TextView mTypeNameView;
        private TextView mSellNumberView;

        /**
         * 构造方法
         *
         * @param itemView
         */
        public MemberChildViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            mPriceView = itemView.findViewById(R.id.price);
            mOriginalPriceView = itemView.findViewById(R.id.original_price);
            mTypeNameView = itemView.findViewById(R.id.type_name);
            mSellNumberView = itemView.findViewById(R.id.sell_number);


        }

        @SuppressLint("StringFormatMatches")
        @Override
        protected void onBind(String data) {

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
