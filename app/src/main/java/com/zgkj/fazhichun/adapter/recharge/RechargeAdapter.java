package com.zgkj.fazhichun.adapter.recharge;

import android.view.View;

import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.fazhichun.R;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/24.
 * Descr:   购买代金券界面优惠条目的适配器的定义
 */
public class RechargeAdapter extends RecyclerViewAdapter<String> {



    @Override
    protected int getItemViewType(int position, String data) {
        return R.layout.cell_recharge_list;
    }

    @Override
    protected ViewHolder<String> getViewHolder(View view, int viewType) {
        return new RechargeViewHolder(view);
    }



    private static class RechargeViewHolder extends RecyclerViewAdapter.ViewHolder<String> {


        /**
         * 构造方法
         *
         * @param itemView
         */
        public RechargeViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(String data,int position) {

        }

        @Override
        public boolean isNeedClick() {
            return false;
        }
    }






}
