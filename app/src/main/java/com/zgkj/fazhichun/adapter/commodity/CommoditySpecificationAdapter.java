package com.zgkj.fazhichun.adapter.commodity;

import android.view.View;
import android.widget.TextView;

import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.fazhichun.R;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/17.
 * Descr:   服务详情界面服务说明适配器的定义
 */
public class CommoditySpecificationAdapter extends RecyclerViewAdapter<String> {


    @Override
    protected int getItemViewType(int position, String data) {
        return R.layout.cell_commodity_specification_list;
    }

    @Override
    protected ViewHolder<String> getViewHolder(View view, int viewType) {
        return new CommoditySpecificationViewHolder(view);
    }


    private static class CommoditySpecificationViewHolder extends RecyclerViewAdapter.ViewHolder<String> {
        private TextView mSpecificationView;


        /**
         * 构造方法
         *
         * @param itemView
         */
        public CommoditySpecificationViewHolder(View itemView) {
            super(itemView);
            mSpecificationView = itemView.findViewById(R.id.specification);
        }



        @Override
        protected void onBind(String data) {

//            mSpecificationView.setText(data);

        }

        @Override
        public boolean isNeedClick() {
            return false;
        }
    }



}
