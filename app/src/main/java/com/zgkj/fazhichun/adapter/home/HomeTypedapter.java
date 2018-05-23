package com.zgkj.fazhichun.adapter.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zgkj.common.widgets.CircleImageView;
import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.entity.HairdresserType;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/2/28.
 * Descr:   理发店图片适配器的定义
 */

public class HomeTypedapter extends RecyclerViewAdapter<HairdresserType> {


    public HomeTypedapter(AdapterListener<HairdresserType> adapterListener) {
        super(adapterListener);
    }

    @Override
    protected int getItemViewType(int position, HairdresserType data) {
        return R.layout.home_type;
    }


    @Override
    protected ViewHolder<HairdresserType> getViewHolder(View view, int viewType) {
        return new ShopImageViewHolder(view);
    }


    private static class ShopImageViewHolder extends ViewHolder<HairdresserType> {

        private CircleImageView mImageView;
        private TextView name;

        /**
         * 构造方法
         *
         * @param itemView
         */
        public ShopImageViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image);
            name=itemView.findViewById(R.id.name);
        }

        /**
         * 设置绑定数据
         *
         * @param data 被绑定的数据对象
         */
        @Override
        protected void onBind(HairdresserType data,int position) {
            Picasso.get().load("".equals(data.getType_icon())?mContext.getResources().getString(R.string.none_image_url):data.getType_icon()).placeholder(R.drawable.none_img).noFade().error(R.drawable.image_error)
                    .into(mImageView);
            name.setText(data.getHairdresser_name());
        }

        @Override
        public boolean isNeedClick() {
            return true;
        }
    }
}
