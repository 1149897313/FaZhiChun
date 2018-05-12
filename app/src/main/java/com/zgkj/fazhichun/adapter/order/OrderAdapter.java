package com.zgkj.fazhichun.adapter.order;

import android.view.View;
import android.widget.TextView;

import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.fazhichun.R;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/12.
 * Descr:   最近订单的适配器
 */
public class OrderAdapter extends RecyclerViewAdapter<String> {


    public OrderAdapter(AdapterListener<String> adapterListener) {
        super(adapterListener);
    }

    @Override
    protected int getItemViewType(int position, String data) {
        return R.layout.cell_order_list;
    }

    @Override
    protected ViewHolder<String> getViewHolder(View view, int viewType) {

        return new OrderViewHolder(view);
    }


    private static class OrderViewHolder extends RecyclerViewAdapter.ViewHolder<String> {

        private TextView mStateView;


        /**
         * 构造方法
         *
         * @param itemView
         */
        public OrderViewHolder(View itemView) {
            super(itemView);
            mStateView = itemView.findViewById(R.id.state);

            mStateView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        protected void onBind(String data) {

        }

        @Override
        public boolean isNeedClick() {
            return true;
        }
    }

}
