package com.zgkj.fazhichun.adapter.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.entity.shop.Hairdresser;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/10.
 * Descr:   子项适配器类的定义
 */
public class HomeChildAdapter extends RecyclerViewAdapter<Hairdresser> {


    public HomeChildAdapter(AdapterListener<Hairdresser> adapterListener) {
        super(adapterListener);
    }


    @Override
    protected int getItemViewType(int position, Hairdresser data) {
        return R.layout.cell_home_child_list;
    }


    @Override
    protected ViewHolder<Hairdresser> getViewHolder(View view, int viewType) {
        return new HomeChildViewHolder(view);
    }


    private static class HomeChildViewHolder extends RecyclerViewAdapter.ViewHolder<Hairdresser> {
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
        public HomeChildViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            mPriceView = itemView.findViewById(R.id.price);
            mOriginalPriceView = itemView.findViewById(R.id.original_price);
            mTypeNameView = itemView.findViewById(R.id.type_name);
            mSellNumberView = itemView.findViewById(R.id.sell_number);
        }



        @SuppressLint("StringFormatMatches")
        @Override
        protected void onBind(Hairdresser data) {
            mPriceView.setText(mContext.getResources().getString(R.string.label_price, data.getFavorable_Price()));
            mOriginalPriceView.setText(mContext.getResources().getString(R.string.label_price, data.getSale_price()));
            mTypeNameView.setText(data.getHairdresser_name());
            mSellNumberView.setText(mContext.getResources().getString(R.string.label_sell_number, data.getQuantity()));
        }

        @Override
        public boolean isNeedClick() {
            return true;
        }
    }
}
