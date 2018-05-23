package com.zgkj.fazhichun.adapter.location;

import android.view.View;
import android.widget.TextView;

import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.fazhichun.R;


/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/26.
 * Descr:   首页城市定位切换城市显示界面的适配器类
 */

public class CityPickerAdapter extends RecyclerViewAdapter<String> {


    public CityPickerAdapter(AdapterListener<String> adapterListener) {
        super(adapterListener);
    }

    @Override
    protected int getItemViewType(int position, String data) {
        return R.layout.cell_location_city_list;
    }


    @Override
    protected ViewHolder<String> getViewHolder(View view, int viewType) {
        return new CityPickerViewHolder(view);
    }


    private static class CityPickerViewHolder extends RecyclerViewAdapter.ViewHolder<String> {

        private TextView mCityView;
        /**
         * 构造方法
         *
         * @param itemView
         */
        public CityPickerViewHolder(View itemView) {
            super(itemView);
            mCityView = itemView.findViewById(R.id.city);
        }

        @Override
        protected void onBind(String data,int position) {
            mCityView.setText(data);
        }

        @Override
        public boolean isNeedClick() {
            return true;
        }
    }


}
