package com.zgkj.fazhichun.adapter.commodity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.entity.shop_service.ShopService;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/19.
 * Descr:   服务详情页面商家服务适配器的定义
 */
public class CommodityServiceAdapter extends RecyclerViewAdapter<ShopService> {


    @Override
    protected int getItemViewType(int position, ShopService data) {
        return R.layout.cell_commodity_service_list;
    }

    @Override
    protected ViewHolder<ShopService> getViewHolder(View view, int viewType) {
        return new CommodityServiceViewHolder(view);
    }



    private static class CommodityServiceViewHolder extends RecyclerViewAdapter.ViewHolder<ShopService> {
        private ImageView mServiceImageView;
        private TextView mServiceNameView;


        /**
         * 构造方法
         *
         * @param itemView
         */
        public CommodityServiceViewHolder(View itemView) {
            super(itemView);
            mServiceImageView = itemView.findViewById(R.id.service_image);
            mServiceNameView = itemView.findViewById(R.id.service_name);
        }

        @Override
        protected void onBind(ShopService data) {
            Picasso.get().load(data.getService_url()).placeholder(R.drawable.none_img)
                    .into(mServiceImageView);
            mServiceNameView.setText(data.getService_name());
        }

        @Override
        public boolean isNeedClick() {
            return false;
        }
    }

}
