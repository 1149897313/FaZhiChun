package com.zgkj.fazhichun.adapter.barber;

import android.media.Image;
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
 * Date:    2018/2/28.
 * Descr:   发型师列表的适配器
 */

public class BarberAdapter extends RecyclerViewAdapter<BarberInfo> {


    public BarberAdapter(AdapterListener<BarberInfo> adapterListener) {
        super(adapterListener);
    }

    @Override
    protected int getItemViewType(int position, BarberInfo data) {
        return R.layout.cell_barber_list;
    }


    @Override
    protected ViewHolder<BarberInfo> getViewHolder(View view, int viewType) {
        return new HairstylistViewHolder(view);
    }


    private static class HairstylistViewHolder extends RecyclerViewAdapter.ViewHolder<BarberInfo> implements View.OnClickListener {


        private ImageView barber_image;
        private TextView barber_name;
        private TextView barber_job;
        /**
         * 构造方法
         *
         * @param itemView
         */
        public HairstylistViewHolder(View itemView) {
            super(itemView);
            barber_image=itemView.findViewById(R.id.barber_image);
            barber_name=itemView.findViewById(R.id.barber_name);
            barber_job=itemView.findViewById(R.id.barber_job);
        }

        @Override
        protected void onBind(BarberInfo data) {
            Picasso.get().load("".equals(data.getPic_url())?mContext.getResources().getString(R.string.none_image_url):data.getPic_url()).placeholder(R.drawable.none_img)
                    .into(barber_image);
            barber_name.setText(data.getBarber_name());
            barber_job.setText(data.getPosition());
        }

        @Override
        public boolean isNeedClick() {
            return true;
        }

        @Override
        public void onClick(View v) {

        }
    }
}