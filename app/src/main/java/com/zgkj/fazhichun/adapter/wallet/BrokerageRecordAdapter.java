package com.zgkj.fazhichun.adapter.wallet;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.entity.wallet.Brokerage;
import com.zgkj.fazhichun.entity.wallet.Withdraw;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/18.
 * Descr:   佣金记录
 */
public class BrokerageRecordAdapter extends RecyclerViewAdapter<Brokerage> {


    public BrokerageRecordAdapter(AdapterListener<Brokerage> adapterListener) {
        super(adapterListener);
    }

    @Override
    protected int getItemViewType(int position, Brokerage data) {
        return R.layout.item_brokerage_record;
    }

    @Override
    protected ViewHolder<Brokerage> getViewHolder(View view, int viewType) {
        return new MessageeViewHolder(view);
    }


    private static class MessageeViewHolder extends ViewHolder<Brokerage> {

        /**
         * UI
         */
        private TextView mName,mTimeView,mMoneyView,brokerageType;

        /**
         * 构造方法
         *
         * @param itemView
         */
        public MessageeViewHolder(View itemView) {
            super(itemView);
            mName=itemView.findViewById(R.id.name);
            mTimeView = itemView.findViewById(R.id.time);
            mMoneyView = itemView.findViewById(R.id.money);
            brokerageType=itemView.findViewById(R.id.brokerage_type);
        }

        @Override
        protected void onBind(Brokerage data,int position) {
            mName.setText(data.getNickname());
            mTimeView.setText(data.getCreate_time());
            brokerageType.setText(data.getOrder_sn());
            mMoneyView.setText("-￥"+data.getTotal_Commission());
        }

        @Override
        public boolean isNeedClick() {
            return true;
        }
    }
}
