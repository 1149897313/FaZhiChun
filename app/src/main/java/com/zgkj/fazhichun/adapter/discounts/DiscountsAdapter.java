package com.zgkj.fazhichun.adapter.discounts;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.fazhichun.R;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/2/28.
 * Descr:   优惠项目适配器的定义
 */

public class DiscountsAdapter extends RecyclerViewAdapter<String> {




    public DiscountsAdapter(AdapterListener<String> adapterListener) {
        super(adapterListener);
    }

    @Override
    protected int getItemViewType(int position, String data) {
        return R.layout.cell_discounts_list;
    }


    @Override
    protected ViewHolder<String> getViewHolder(View view, int viewType) {
        return new DiscountsViewHolder(view);
    }



    private static class DiscountsViewHolder extends RecyclerViewAdapter.ViewHolder<String> {
        private ImageView mShopImageView;
        private TextView mTypeNameView;
        private TextView mPriceView;
        // 门市价
        private TextView mOriginalPrice;
        private TextView mSellNumberView;

        /**
         * 构造方法
         *
         * @param itemView
         */
        public DiscountsViewHolder(View itemView) {
            super(itemView);
            mShopImageView = itemView.findViewById(R.id.shop_image);
            mTypeNameView = itemView.findViewById(R.id.type_name);
            mPriceView = itemView.findViewById(R.id.price);
            mOriginalPrice = itemView.findViewById(R.id.original_price);
            mSellNumberView = itemView.findViewById(R.id.sell_number);

        }

        @SuppressLint("StringFormatMatches")
        @Override
        protected void onBind(String data,int position) {
            mShopImageView.setBackgroundResource(R.color.orange_400);
            mTypeNameView.setText(data);
            mPriceView.setText(mItemView.getContext().getResources().getString(R.string.label_price, 34));
            mOriginalPrice.setText(mItemView.getContext().getResources().getString(R.string.label_original_price, 34));
            mSellNumberView.setText(mItemView.getContext().getResources().getString(R.string.label_sell_number, 20));

        }

        @Override
        public boolean isNeedClick() {
            return true;
        }
    }
}
