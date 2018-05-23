package com.zgkj.fazhichun.adapter.collect;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.entity.collection.Collection;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/9.
 * Descr:   收藏界面团购列表的适配器类
 */

public class ProductAdapter extends RecyclerViewAdapter<Collection> {


    // 定义编辑模式
    private static final int MODE_UNCHECK = 0;
    // 默认为未选中状态
    private int mEditMode = MODE_UNCHECK;


    /**
     * 构造方法，初始化监听器
     *
     * @param adapterListener
     */
    public ProductAdapter(AdapterListener<Collection> adapterListener) {
        super(adapterListener);
    }

    @Override
    protected int getItemViewType(int position, Collection data) {
        return R.layout.cell_product_list;
    }


    @Override
    protected ViewHolder<Collection> getViewHolder(View view, int viewType) {
        return new ProductViewHolder(view);
    }


    /**
     * 设置编辑模式
     *
     * @param editMode
     */
    public void setEditMode(int editMode) {
        mEditMode = editMode;
        // 刷新数据显示
        notifyDataSetChanged();
    }


    /**
     * 定义内部的ViewHolder类，用于显示控件初始化的复用，避免重复创建
     */
    private class ProductViewHolder extends RecyclerViewAdapter.ViewHolder<Collection> {
        private ImageView mSelectView;
        private ImageView mShopImageView;
        private TextView mShopNameView;
        private TextView mTypeNameView,goods_name;
        private TextView mPriceView;
        private TextView mOriginalPriceView;
        private TextView mDistanceView;


        /**
         * 构造方法
         *
         * @param itemView
         */
        public ProductViewHolder(View itemView) {
            super(itemView);
            mSelectView = itemView.findViewById(R.id.select);
            mShopImageView = itemView.findViewById(R.id.shop_image);
            mShopNameView = itemView.findViewById(R.id.shop_name);
            goods_name=itemView.findViewById(R.id.goods_name);
            mTypeNameView = itemView.findViewById(R.id.type_name);
            mPriceView = itemView.findViewById(R.id.price);
            mOriginalPriceView = itemView.findViewById(R.id.original_price);
            mDistanceView = itemView.findViewById(R.id.distance);
        }


        @Override
        protected void onBind(Collection data,int position) {

            if (mEditMode != MODE_UNCHECK) {
                // 显示是否选中的View
                mSelectView.setVisibility(View.VISIBLE);
                // 如果为选中状态
                if (data.isSelected()) {
                    mSelectView.setImageResource(R.drawable.ic_payment_checked);
                } else {
                    mSelectView.setImageResource(R.drawable.ic_payment_unchecked);
                }

            } else {
                // 隐藏
                mSelectView.setVisibility(View.GONE);
            }

            mShopNameView.setText(data.getShop_name());
            mTypeNameView.setText("["+data.getCategory_name()+"]");
            goods_name.setText(data.getHairdresser_name());
            mPriceView.setText("￥"+data.getFavorable_Price());
            mOriginalPriceView.setText("门市价："+data.getSale_price());

            Picasso.get().load("".equals(data.getPic_url())?mContext.getResources().getString(R.string.none_image_url):data.getPic_url()).placeholder(R.drawable.ic_shop_bg)
                    .into(mShopImageView);

        }

        @Override
        public boolean isNeedClick() {
            return true;
        }
    }
}
