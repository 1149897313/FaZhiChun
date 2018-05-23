package com.zgkj.fazhichun.adapter.type;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.squareup.picasso.Picasso;
import com.zgkj.common.Common;
import com.zgkj.common.utils.SPUtil;
import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.fazhichun.CropSquareTransformation;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.entity.shop.Goods;

import java.text.DecimalFormat;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/18.
 * Descr:   Type界面对应的适配器的定义
 */
public class TypeAdapter extends RecyclerViewAdapter<Goods> {


    public TypeAdapter(AdapterListener<Goods> adapterListener) {
        super(adapterListener);
    }

    @Override
    protected int getItemViewType(int position, Goods data) {
        return R.layout.cell_type_list;
    }

    @Override
    protected ViewHolder<Goods> getViewHolder(View view, int viewType) {
        return new TypeViewHolder(view);
    }



    DecimalFormat df=new DecimalFormat("#.##");

    private class TypeViewHolder extends RecyclerViewAdapter.ViewHolder<Goods> {

        private ImageView mShopImageView;
        private TextView shop_name,type,type_name;
        private TextView price,original_price,distanceText;

        /**
         * 构造方法
         *
         * @param itemView
         */
        public TypeViewHolder(View itemView) {
            super(itemView);
            mShopImageView=itemView.findViewById(R.id.shop_image);
            shop_name=itemView.findViewById(R.id.shop_name);
            type=itemView.findViewById(R.id.type);
            type_name=itemView.findViewById(R.id.type_name);
            price=itemView.findViewById(R.id.price);
            original_price=itemView.findViewById(R.id.original_price);
            distanceText=itemView.findViewById(R.id.distance);
        }

        @Override
        protected void onBind(Goods data,int position) {
            Picasso.get().load("".equals(data.getPic_url()) ? mContext.getResources().getString(R.string.none_image_url) : data.getPic_url()).placeholder(R.drawable.none_img).transform(new CropSquareTransformation()).error(R.drawable.image_error)
                    .into(mShopImageView);
            shop_name.setText(data.getShop_name());
            type.setText("["+data.getHairdresser_category_name()+"]");
            type_name.setText(data.getHairdresser_name());
            price.setText("￥"+data.getFavorable_Price());
            original_price.setText("￥"+data.getSale_price());
            distanceText.setText(df.format(data.getDistance()/1000.00d)+"KM");
        }

        @Override
        public boolean isNeedClick() {
            return true;
        }
    }
}
