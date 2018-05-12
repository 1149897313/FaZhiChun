package com.zgkj.fazhichun.adapter.barbershop;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.entity.shop.BarberInfo;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/16.
 * Descr:   理发店页面理发师图片适配器的定义
 */
public class ShopBarberAdapter extends RecyclerViewAdapter<BarberInfo> {


    public ShopBarberAdapter(AdapterListener<BarberInfo> adapterListener) {
        super(adapterListener);
    }

    @Override
    protected int getItemViewType(int position, BarberInfo data) {
        return R.layout.cell_barbershop_barber_list;
    }

    @Override
    protected ViewHolder<BarberInfo> getViewHolder(View view, int viewType) {
        return new ShopBarberViewHolder(view);
    }



    private static class ShopBarberViewHolder extends RecyclerViewAdapter.ViewHolder<BarberInfo> {
        /**
         * UI
         */
        private ImageView mBarberImageView;
        private TextView mBarberNameView;
        private TextView mBarberJobView;

        /**
         * 构造方法
         *
         * @param itemView
         */
        public ShopBarberViewHolder(View itemView) {
            super(itemView);
            mBarberImageView = itemView.findViewById(R.id.barber_image);
            mBarberNameView = itemView.findViewById(R.id.barber_name);
            mBarberJobView = itemView.findViewById(R.id.barber_job);

        }

        @Override
        protected void onBind(BarberInfo data) {
            Picasso.get().load(data.getPic_url()).placeholder(R.drawable.none_img)
                    .into(mBarberImageView);
            mBarberNameView.setText(data.getBarber_name());
            mBarberJobView.setText(data.getPosition());
        }

        @Override
        public boolean isNeedClick() {
            return true;
        }
    }
}
