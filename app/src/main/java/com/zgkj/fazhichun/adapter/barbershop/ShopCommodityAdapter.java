package com.zgkj.fazhichun.adapter.barbershop;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.entity.shop.Hairdresser;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/16.
 * Descr: 店内服务/商品列表
 */
public class ShopCommodityAdapter extends RecyclerViewAdapter<Hairdresser> {


    public ShopCommodityAdapter(AdapterListener<Hairdresser> adapterListener) {
        super(adapterListener);
    }

    @Override
    protected int getItemViewType(int position, Hairdresser data) {
        return R.layout.cell_barbershop_commodity_list;
    }

    @Override
    protected ViewHolder<Hairdresser> getViewHolder(View view, int viewType) {
        return new ShopCommodityViewHolder(view);
    }


    private static class ShopCommodityViewHolder extends RecyclerViewAdapter.ViewHolder<Hairdresser> {
        /**
         * UI
         */
        private ImageView mTypeImage;
        private TextView mTypeView;
        private TextView mTypeNameView;
        private TextView mPriceView;
        private TextView mOriginalPriceView;
        private TextView mSellNumberView;


        /**
         * 构造方法
         *
         * @param itemView
         */
        public ShopCommodityViewHolder(View itemView) {
            super(itemView);
            mTypeImage = itemView.findViewById(R.id.type_image);
            mTypeView = itemView.findViewById(R.id.type);
            mTypeNameView = itemView.findViewById(R.id.type_name);
            mPriceView = itemView.findViewById(R.id.price);
            mOriginalPriceView = itemView.findViewById(R.id.original_price);
            mSellNumberView = itemView.findViewById(R.id.sell_number);
        }

        @Override
        protected void onBind(Hairdresser data,int position) {

            Picasso.get().load(data.getPic_url()).placeholder(R.drawable.none_img)
                    .into(mTypeImage);
            mTypeView.setText("["+data.getHairdresser_category_name()+"]");
            mTypeNameView.setText(data.getHairdresser_name());
            mPriceView.setText(String.valueOf("￥"+data.getFavorable_Price()));
            mOriginalPriceView.setText(String.valueOf("门市价："+data.getSale_price()));
            mSellNumberView.setText("已售:"+data.getQuantity());
        }

        @Override
        public boolean isNeedClick() {
            return true;
        }
    }





}
