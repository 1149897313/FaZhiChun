package com.zgkj.fazhichun.fragments.exist;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zgkj.common.app.Fragment;
import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.common.widgets.recycler.decoration.DividerItemDecoration;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.adapter.order.OrderAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/12.
 * Descr:   待评价订单碎片的定义
 */
public class EvaluateOrderFragment extends Fragment {

    /**
     * UI
     */
    private RecyclerView mRecyclerView;

    /**
     * DATA
     */
    private OrderAdapter mOrderAdapter;

    /**
     * 显示待评价的订单碎片
     *
     * @return
     */
    public static EvaluateOrderFragment newInstance(){
        EvaluateOrderFragment fragment = new EvaluateOrderFragment();

        return fragment;
    }


    @Override
    protected int getLayoutSourceId() {
        return R.layout.fragment_order_evaluate;
    }

    @Override
    protected void initWidgets(View rootView) {
        super.initWidgets(rootView);
        // init UI
        mRecyclerView = rootView.findViewById(R.id.recycler_view);

    }

    @Override
    protected void initDatas() {
        super.initDatas();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL,
                R.drawable.shape_divider_line,
                16,
                false));

        mOrderAdapter = new OrderAdapter(new RecyclerViewAdapter.AdapterListenerImpl<String>() {
            @Override
            public void onItemClick(RecyclerViewAdapter.ViewHolder<String> holder, String data) {
                super.onItemClick(holder, data);
            }
        });

        mRecyclerView.setAdapter(mOrderAdapter);
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("a");
        list.add("a");

        mOrderAdapter.replace(list);

    }
}
