package com.zgkj.fazhichun.adapter.wallet;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zgkj.common.widgets.CircleImageView;
import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.common.widgets.text.ExpandableTextView;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.entity.wallet.Withdraw;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/18.
 * Descr:   提现记录
 */
public class WithdrawalRecordAdapter extends RecyclerViewAdapter<Withdraw> {


    public WithdrawalRecordAdapter(AdapterListener<Withdraw> adapterListener) {
        super(adapterListener);
    }

    @Override
    protected int getItemViewType(int position, Withdraw data) {
        return R.layout.item_withdraw_record;
    }

    @Override
    protected ViewHolder<Withdraw> getViewHolder(View view, int viewType) {
        return new MessageeViewHolder(view);
    }


    private static class MessageeViewHolder extends ViewHolder<Withdraw> {

        /**
         * UI
         */
        private TextView mTimeView,mMoneyView,status;
        private ImageView resubmit;

        /**
         * 构造方法
         *
         * @param itemView
         */
        public MessageeViewHolder(View itemView) {
            super(itemView);
            mTimeView = itemView.findViewById(R.id.time);
            mMoneyView = itemView.findViewById(R.id.money);
            status=itemView.findViewById(R.id.status);
            resubmit=itemView.findViewById(R.id.resubmit);
        }

        @Override
        protected void onBind(Withdraw data,int position) {
            mTimeView.setText(data.getCreate_time());
            mMoneyView.setText("-￥"+data.getAmount());
            switch (data.getWithdraw_status()){//1:审核中2：成功3：失败
                case 1:
                    status.setText("审核中");
                    status.setTextColor(mContext.getResources().getColor(R.color.textColorBlue));
                    resubmit.setVisibility(View.GONE);
                    break;
                case 2:
                    status.setText("提现成功");
                    status.setTextColor(mContext.getResources().getColor(R.color.alertNormal));
                    resubmit.setVisibility(View.GONE);
                    break;
                case 3:
                    status.setText("提现失败");
                    status.setTextColor(mContext.getResources().getColor(R.color.textBuyVoucherColor));
                    resubmit.setVisibility(View.VISIBLE);
                    break;
            }
        }

        @Override
        public boolean isNeedClick() {
            return true;
        }
    }
}
