package com.zgkj.fazhichun.adapter.collect;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zgkj.common.widgets.StarBarView;
import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.entity.collection.Collection;

import org.w3c.dom.Text;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/9.
 * Descr:   收藏界面商家列表的适配器类
 */

public class MerchantAdapter extends RecyclerViewAdapter<Collection> {


    // 定义显示模式的常量
    private static final int MODE_UNCHECK = 0;
    // 默认为未选中
    private int mEditMode = MODE_UNCHECK;

    /**
     * 构造方法，初始化监听器
     *
     * @param adapterListener
     */
    public MerchantAdapter(AdapterListener<Collection> adapterListener) {
        super(adapterListener);
    }

    @Override
    protected int getItemViewType(int position, Collection data) {
        return R.layout.cell_merchant_list;
    }


    @Override
    protected ViewHolder<Collection> getViewHolder(View view, int viewType) {

        return new MerchantViewHolder(view);
    }

    /**
     * 设置显示模式
     *
     * @param editMode
     */
    public void setEditMode(int editMode) {
        mEditMode = editMode;
        // 刷新数据的显示
        notifyDataSetChanged();
    }


    /**
     * 定义内部的ViewHolder类，用于显示控件创建的复用
     */
    private class MerchantViewHolder extends RecyclerViewAdapter.ViewHolder<Collection> {
        private ImageView mSelectView;
        private ImageView mShopImageView;
        private TextView mShopNameView;
        private TextView mDistanceView;
        private StarBarView mScoreView;
        private TextView type;
        private TextView shop_score;
        /**
         * 构造方法
         *
         * @param itemView
         */
        public MerchantViewHolder(View itemView) {
            super(itemView);
            mSelectView = itemView.findViewById(R.id.select);
            mShopImageView = itemView.findViewById(R.id.shop_image);
            mShopNameView = itemView.findViewById(R.id.shop_name);
            mDistanceView = itemView.findViewById(R.id.distance);
            mScoreView=itemView.findViewById(R.id.shop_star_bar_view);
            shop_score=itemView.findViewById(R.id.shop_score);
            type=itemView.findViewById(R.id.type);
        }

        @Override
        protected void onBind(Collection data,int position) {
            // 如果为true则显示用于选择Item删除条目的View，否则则隐藏
            if (mEditMode != MODE_UNCHECK) {
                mSelectView.setVisibility(View.VISIBLE);
                // 如果为选中状态
                if (data.isSelected()) {
                    mSelectView.setImageResource(R.drawable.ic_payment_checked);
                } else {
                    mSelectView.setImageResource(R.drawable.ic_payment_unchecked);
                }
            } else {
                mSelectView.setVisibility(View.GONE);
            }

            mShopNameView.setText(data.getShop_name());
//           mDistanceView.setText(String.valueOf(data.getDistance()));
            mScoreView.setStarMark(data.getShop_service_score());
            shop_score.setText(String.valueOf(data.getShop_service_score()) + "分");
            type.setText(data.getCategory_name());
//            mAddressView.setText(data.get);
            Picasso.get().load("".equals(data.getShop_image())?mContext.getResources().getString(R.string.none_image_url):data.getShop_image()).placeholder(R.drawable.ic_shop_bg)
                    .into(mShopImageView);
        }

        @Override
        public boolean isNeedClick() {
            return true;
        }
    }


}
