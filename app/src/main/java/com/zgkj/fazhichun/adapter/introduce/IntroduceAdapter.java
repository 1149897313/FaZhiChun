package com.zgkj.fazhichun.adapter.introduce;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.entity.shop.BarberService;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/20.
 * Descr:   理发师介绍界面适配器对象的定义
 */
public class IntroduceAdapter extends RecyclerViewAdapter<BarberService> {

    @Override
    protected int getItemViewType(int position, BarberService data) {
        return R.layout.cell_introduce_list;
    }

    @Override
    protected ViewHolder<BarberService> getViewHolder(View view, int viewType) {
        return new IntroduceViewHolder(view);
    }


    private static class IntroduceViewHolder extends RecyclerViewAdapter.ViewHolder<BarberService> {


        private ImageView type_image;
        private TextView serviceName;
        private TextView price;
        /**
         * 构造方法
         *
         * @param itemView
         */
        public IntroduceViewHolder(View itemView) {
            super(itemView);
            type_image=itemView.findViewById(R.id.type_image);
            serviceName=itemView.findViewById(R.id.type_name);
            price=itemView.findViewById(R.id.price);

        }

        @Override
        protected void onBind(BarberService data) {
            Picasso.get().load("".equals(data.getPic_url())?mContext.getResources().getString(R.string.none_image_url):data.getPic_url()).placeholder(R.drawable.none_img)
                    .into(type_image);
            serviceName.setText(data.getHairdresser_name());
            price.setText("￥"+data.getFavorable_Price());
        }

        @Override
        public boolean isNeedClick() {
            return false;
        }
    }
}
