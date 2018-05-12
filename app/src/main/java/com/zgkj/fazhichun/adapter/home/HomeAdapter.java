package com.zgkj.fazhichun.adapter.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.zgkj.fazhichun.entity.shop.Hairdresser;
import com.zgkj.fazhichun.entity.shop.StoreListAndProductList;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/2.
 * Descr:   主页碎片的适配器的定义
 */

public class HomeAdapter extends RecyclerViewAdapter<StoreListAndProductList> {

    @Override
    protected int getItemViewType(int position, StoreListAndProductList data) {
        return R.layout.cell_home_list;
    }

    public HomeAdapter(AdapterListener<StoreListAndProductList> adapterListener) {
        super(adapterListener);
    }

    @Override
    protected ViewHolder<StoreListAndProductList> getViewHolder(View view, int viewType) {
        return new HomeViewHolder(view);
    }

    private class HomeViewHolder extends RecyclerViewAdapter.ViewHolder<StoreListAndProductList> implements View.OnClickListener {
        private Context mContext;
        private RelativeLayout mShopLayout;
        private ImageView mShopImageView;
        private TextView mShopNameView;
        private StarBarView mStarBarView;
        private TextView mScoreView;
        private TextView mTypeView;
        private ImageView mRecommendView;
        private TextView mAddressView;
        private TextView mDistanceView;
        private RecyclerView mRecyclerView;
        private DrawableCenterTextView mOtherCommodityView;

        /**
         * 构造方法
         *
         * @param itemView
         */
        public HomeViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            mShopLayout = itemView.findViewById(R.id.shop_layout);
            mShopImageView = itemView.findViewById(R.id.shop_image);
            mShopNameView = itemView.findViewById(R.id.shop_name);
            mStarBarView = itemView.findViewById(R.id.star_bar_view);
            mScoreView = itemView.findViewById(R.id.score);
            mTypeView = itemView.findViewById(R.id.type);
            mRecommendView = itemView.findViewById(R.id.recommend);
            mAddressView = itemView.findViewById(R.id.address);
            mDistanceView = itemView.findViewById(R.id.distance);
            mRecyclerView = itemView.findViewById(R.id.recycler_view);
            mOtherCommodityView = itemView.findViewById(R.id.other_commodity);

            mOtherCommodityView.setOnClickListener(this);

            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext,
                    DividerItemDecoration.VERTICAL, R.drawable.shape_divider_line, 16, true));


        }

        @Override
        protected void onBind(final StoreListAndProductList data) {
            mShopNameView.setText(data.getShop_name());
            mScoreView.setText(String.valueOf(data.getShop_service_score()) + "分");
            mDistanceView.setText(String.valueOf(data.getDistance()));
            mAddressView.setText(data.getAddress());
            Picasso.get().load("".equals(data.getShop_image())?mContext.getResources().getString(R.string.none_image_url):data.getShop_image()).placeholder(R.drawable.ic_shop_bg)
                    .into(mShopImageView);

            if(data.getHairdresser()==null || "[]".equals(data.getHairdresser()) || data.getHairdresser().toString()=="[]"){
                mOtherCommodityView.setVisibility(View.GONE);
            }else {
                mOtherCommodityView.setVisibility(View.VISIBLE);

               final HomeChildAdapter mChildAdapter = new HomeChildAdapter(new RecyclerViewAdapter.AdapterListenerImpl<Hairdresser>() {
                    @Override
                    public void onItemClick(RecyclerViewAdapter.ViewHolder<Hairdresser> holder, Hairdresser data) {
                        super.onItemClick(holder, data);
                        // 跳转到理发店商品项目列表界面
                        CommodityActivity.show(mContext,data.getHairdresser_id());
                    }
                });
                mRecyclerView.setAdapter(mChildAdapter);

                final int number=data.getHairdresser().size();
                mChildAdapter.replace(data.getHairdresser().subList(0,number>2?2:number));

                if(number>2){
                    mOtherCommodityView.setText(mContext.getResources().getString(R.string.label_other_service, String.valueOf(number-2)));
                    mOtherCommodityView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mChildAdapter.add(data.getHairdresser().subList(2,number));
                            mOtherCommodityView.setVisibility(View.GONE);
                        }
                    });
                }else{
                    mOtherCommodityView.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public boolean isNeedClick() {
            return true;
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.shop_layout:
                    break;
                case R.id.other_commodity:

                    break;
                default:
                    break;
            }

        }
    }


}
