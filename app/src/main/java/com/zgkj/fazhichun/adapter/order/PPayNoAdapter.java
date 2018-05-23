package com.zgkj.fazhichun.adapter.order;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.activities.ValidationActivity;
import com.zgkj.fazhichun.entity.order.OrderInfo;
import com.zgkj.fazhichun.entity.order.ValidateCode;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/17.
 * Descr:   订单详情 消费码
 */
public class PPayNoAdapter extends RecyclerViewAdapter<ValidateCode> {


    public PPayNoAdapter(AdapterListener<ValidateCode> adapterListener) {
        super(adapterListener);
    }

    @Override
    protected int getItemViewType(int position, ValidateCode data) {
        return R.layout.order_detail_pay_no;
    }

    @Override
    protected ViewHolder<ValidateCode> getViewHolder(View view, int viewType) {
        return new CommoditySpecificationViewHolder(view);
    }


    private static class CommoditySpecificationViewHolder extends ViewHolder<ValidateCode> {
        private TextView pay_no_number,refund,right;

        /**
         * 构造方法
         *
         * @param itemView
         */
        public CommoditySpecificationViewHolder(View itemView) {
            super(itemView);
            pay_no_number = itemView.findViewById(R.id.pay_no_number);
            refund=itemView.findViewById(R.id.refund);
            right=itemView.findViewById(R.id.right);
        }



        @Override
        protected void onBind(final ValidateCode data,int position) {
            pay_no_number.setText(data.getPay_serianlno());
            right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ValidationActivity.show(mContext,data.getRecord_id());
                }
            });
            if("1".equals(data.getOrder_status())){//1.待付款,2.已付款,4,商家验证消费,6.已完成，8 已退款
                refund.setVisibility(View.VISIBLE);
                right.setText("使用");

            }else if("8".equals(data.getOrder_status())){
                right.setText("已退款");
                right.setBackgroundColor(mContext.getResources().getColor(R.color.colorLight));
                right.setTextColor(mContext.getResources().getColor(R.color.textColorThird));
            }
        }

        @Override
        public boolean isNeedClick() {
            return true;
        }
    }



}
