package com.zgkj.fazhichun.adapter.barbershop;

import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.fazhichun.R;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/2/28.
 * Descr:   理发店图片适配器的定义
 */

public class ShopImageAdapter extends RecyclerViewAdapter<String> {


    public ShopImageAdapter(AdapterListener<String> adapterListener) {
        super(adapterListener);
    }

    @Override
    protected int getItemViewType(int position, String data) {
        return R.layout.cell_barbershop_shop_image_list;
    }


    @Override
    protected ViewHolder<String> getViewHolder(View view, int viewType) {
        return new ShopImageViewHolder(view);
    }


    private static class ShopImageViewHolder extends RecyclerViewAdapter.ViewHolder<String> {

        private ImageView mShopImageView;

        /**
         * 构造方法
         *
         * @param itemView
         */
        public ShopImageViewHolder(View itemView) {
            super(itemView);
            mShopImageView = itemView.findViewById(R.id.shop_image);
        }

        /**
         * 设置绑定数据
         *
         * @param data 被绑定的数据对象
         */
        @Override
        protected void onBind(String data,int position) {
            Picasso.get().load(data).placeholder(R.drawable.none_img)
                    .into(mShopImageView);
        }

        @Override
        public boolean isNeedClick() {
            return true;
        }
    }
}
