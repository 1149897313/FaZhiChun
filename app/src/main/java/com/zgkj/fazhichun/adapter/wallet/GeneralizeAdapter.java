package com.zgkj.fazhichun.adapter.wallet;

import android.view.View;
import android.widget.TextView;

import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.entity.wallet.Brokerage;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/18.
 * Descr:   推荐奖励规则
 */
public class GeneralizeAdapter extends RecyclerViewAdapter<Brokerage> {


    @Override
    protected int getItemViewType(int position, Brokerage data) {
        return R.layout.item_generalize;
    }

    @Override
    protected ViewHolder<Brokerage> getViewHolder(View view, int viewType) {
        return new MessageeViewHolder(view);
    }


    private static class MessageeViewHolder extends ViewHolder<Brokerage> {

        /**
         * UI
         */
        private TextView number,content;

        /**
         * 构造方法
         *
         * @param itemView
         */
        public MessageeViewHolder(View itemView) {
            super(itemView);
            number=itemView.findViewById(R.id.number);
            content = itemView.findViewById(R.id.content);
        }

        @Override
        protected void onBind(Brokerage data,int position) {
            number.setText("0"+(1+position));
        }

        @Override
        public boolean isNeedClick() {
            return false;
        }
    }
}
