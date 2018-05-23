package com.zgkj.fazhichun.adapter.order;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.fazhichun.CropSquareTransformation;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.entity.order.OrderInfo;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/12.
 * Descr:   全部订单的适配器
 */
public class AllOrderAdapter extends RecyclerViewAdapter<OrderInfo> {


    public AllOrderAdapter(AdapterListener<OrderInfo> adapterListener) {
        super(adapterListener);
    }

    @Override
    protected int getItemViewType(int position, OrderInfo data) {
        return R.layout.cell_order_all_list;
    }

    @Override
    protected ViewHolder<OrderInfo> getViewHolder(View view, int viewType) {

        return new OrderViewHolder(view);
    }


    private static class OrderViewHolder extends ViewHolder<OrderInfo> {


        private ImageView shop_image;
        private TextView shop_name, info, type_name, goods_name, amount, price;
        private TextView mStateView;


        /**
         * 构造方法
         *
         * @param itemView
         */
        public OrderViewHolder(View itemView) {
            super(itemView);
            shop_image = itemView.findViewById(R.id.shop_image);
            shop_name = itemView.findViewById(R.id.shop_name);
            info = itemView.findViewById(R.id.info);
            type_name = itemView.findViewById(R.id.type_name);
            goods_name = itemView.findViewById(R.id.goods_name);
            amount = itemView.findViewById(R.id.amount);
            price = itemView.findViewById(R.id.price);

            mStateView = itemView.findViewById(R.id.state);
        }

        @Override
        protected void onBind(OrderInfo data,int position) {
            Picasso.get().load("".equals(data.getShop_image()) ? mContext.getResources().getString(R.string.none_image_url) : data.getShop_image()).placeholder(R.drawable.none_img).transform(new CropSquareTransformation()).error(R.drawable.image_error)
                    .into(shop_image);
            shop_name.setText(data.getShop_name());
            info.setText(data.getOrder_status());
            type_name.setText("[" + data.getHairdresser_name() + "]");
            goods_name.setText(data.getGoods_name());
            amount.setText("数量：" + data.getNum());
            price.setText("价格：" + data.getFavorable_Price());
            mStateView.setBackgroundResource(R.drawable.shape_bg_introduce_label);
            mStateView.setTextColor(mContext.getResources().getColor(R.color.textColorAccent));
            mStateView.setText("查看详情");
            //1. 待付款  2. 待使用 4.5.6.已完成 8.退款
            if ("1".equals(data.getOrder_status())) {
                info.setText("待付款");
                mStateView.setText("付款");
                mStateView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            } else if ("2".equals(data.getOrder_status())) {
                info.setText("待使用");
                mStateView.setText("使用");
                mStateView.setVisibility(View.VISIBLE);
            } else if ("4".equals(data.getOrder_status())) {//待评价
                info.setText("已完成");
                if(data.getShop_service_score()==null || TextUtils.isEmpty(data.getShop_service_score())){
                    mStateView.setText("去评价");
                    mStateView.setVisibility(View.VISIBLE);
                }else {
                    mStateView.setVisibility(View.GONE);
                }
            } else if ("5".equals(data.getOrder_status()) || "6".equals(data.getOrder_status())) {
                info.setText("已完成");
                mStateView.setText("查看详情");
                mStateView.setVisibility(View.VISIBLE);
            } else if ("8".equals(data.getOrder_status())) {
                info.setText("已退款");
                mStateView.setVisibility(View.GONE);
            }
        }

        @Override
        public boolean isNeedClick() {
            return true;
        }
    }

}
