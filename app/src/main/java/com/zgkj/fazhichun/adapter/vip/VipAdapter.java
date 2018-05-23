package com.zgkj.fazhichun.adapter.vip;

import android.view.View;

import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/2.
 * Descr:   美发VIP商家适配器
 */

public class VipAdapter extends RecyclerViewAdapter<String> {



    @Override
    protected int getItemViewType(int position, String data) {
        return 0;
    }

    @Override
    protected ViewHolder<String> getViewHolder(View view, int viewType) {
        return null;
    }

    private static class VipViewHolder extends RecyclerViewAdapter.ViewHolder<String> {

        /**
         * 构造方法
         *
         * @param itemView
         */
        public VipViewHolder(View itemView) {
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
