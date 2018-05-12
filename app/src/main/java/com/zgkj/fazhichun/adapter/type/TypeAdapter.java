package com.zgkj.fazhichun.adapter.type;

import android.view.View;

import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.fazhichun.R;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/18.
 * Descr:   Type界面对应的适配器的定义
 */
public class TypeAdapter extends RecyclerViewAdapter<String> {



    public TypeAdapter(AdapterListener<String> adapterListener) {
        super(adapterListener);
    }

    @Override
    protected int getItemViewType(int position, String data) {
        return R.layout.cell_type_list;
    }

    @Override
    protected ViewHolder<String> getViewHolder(View view, int viewType) {
        return new TypeViewHolder(view);
    }




    private static class TypeViewHolder extends RecyclerViewAdapter.ViewHolder<String> {



        /**
         * 构造方法
         *
         * @param itemView
         */
        public TypeViewHolder(View itemView) {
            super(itemView);
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
