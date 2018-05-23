package com.zgkj.fazhichun.adapter.coupons;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zgkj.common.utils.TimeUtil;
import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.entity.coupon.Coupon;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/05.04.
 * Descr:  订单优惠券
 */
public class CouponsOrderAdapter extends RecyclerViewAdapter<Coupon> {



    public CouponsOrderAdapter(AdapterListener<Coupon> adapterListener) {
        super(adapterListener);
    }

    @Override
    protected int getItemViewType(int position, Coupon data) {
        return R.layout.coupons_order_item;
    }

    @Override
    protected ViewHolder<Coupon> getViewHolder(View view, int viewType) {
        return new CouponsViewHolder(view);
    }




    private static class CouponsViewHolder extends ViewHolder<Coupon> {


        private TextView money,type;
        private TextView usable_range,date;
        private ImageView mSelectView;

        /**
         * 构造方法
         *
         * @param itemView
         */
        public CouponsViewHolder(View itemView) {
            super(itemView);
            money=itemView.findViewById(R.id.money);
            type=itemView.findViewById(R.id.type);
            usable_range=itemView.findViewById(R.id.usable_range);
            date=itemView.findViewById(R.id.date);
            mSelectView=itemView.findViewById(R.id.select);
        }

        @Override
        protected void onBind(Coupon data,int position) {
            money.setText(data.getCoupon_money());
//            type.setText("");
            usable_range.setText("使用范围："+data.getUse_range());
            date.setText("有效期："+ TimeUtil.stamp2Time(data.getEnd_time(),"yyyy年MM月dd日"));
            if("0".equals(data.getIs_used())){//0:未使用，1：已使用
            }else if("1".equals(data.getIs_used())){
            }else if("2".equals(data.getIs_used())){
            }
            if (data.isSelected()) {
                mSelectView.setImageResource(R.drawable.ic_payment_checked);
            } else {
                mSelectView.setImageResource(R.drawable.ic_payment_unchecked);
            }
        }

        @Override
        public boolean isNeedClick() {
            return true;
        }
    }
}