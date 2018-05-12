package com.zgkj.fazhichun.adapter.type;

import android.view.View;
import android.widget.TextView;

import com.zgkj.common.utils.TimeUtil;
import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.entity.coupon.Coupon;

import org.w3c.dom.Text;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/05.04.
 * Descr:  我的优惠券
 */
public class CouponsAdapter extends RecyclerViewAdapter<Coupon> {



    public CouponsAdapter(AdapterListener<Coupon> adapterListener) {
        super(adapterListener);
    }

    @Override
    protected int getItemViewType(int position, Coupon data) {
        return R.layout.coupons_item;
    }

    @Override
    protected ViewHolder<Coupon> getViewHolder(View view, int viewType) {
        return new CouponsViewHolder(view);
    }




    private static class CouponsViewHolder extends RecyclerViewAdapter.ViewHolder<Coupon> {


        private TextView money,type;
        private TextView usable_range,date;
        private TextView state,coupon_type;
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
            state=itemView.findViewById(R.id.state);
            coupon_type=itemView.findViewById(R.id.coupon_type);
        }

        @Override
        protected void onBind(Coupon data) {
            money.setText(data.getCoupon_money());
//            type.setText("");
            usable_range.setText("使用范围："+data.getUse_range());
            date.setText("有效期："+ TimeUtil.stamp2Time(data.getEnd_time(),"yyyy年MM月dd日"));
            if("0".equals(data.getIs_used())){//0:未使用，1：已使用
                state.setText("立即使用");
                state.setVisibility(View.VISIBLE);
                coupon_type.setVisibility(View.GONE);
            }else if("1".equals(data.getIs_used())){
                coupon_type.setText("已使用");
                state.setVisibility(View.GONE);
                coupon_type.setVisibility(View.VISIBLE);
            }else if("2".equals(data.getIs_used())){
                coupon_type.setText("已过期");
                state.setVisibility(View.GONE);
                coupon_type.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public boolean isNeedClick() {
            return true;
        }
    }
}