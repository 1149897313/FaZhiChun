package com.zgkj.fazhichun.fragments.main;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zgkj.common.app.Fragment;
import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.common.widgets.recycler.decoration.DividerItemDecoration;
import com.zgkj.common.widgets.text.DrawableCenterTextView;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.activities.BarberShopActivity;
import com.zgkj.fazhichun.activities.ExistActivity;
import com.zgkj.fazhichun.adapter.order.OrderAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/2/27.
 * Descr:   主页订单界面显示的碎片
 */

public class OrderFragment extends Fragment implements View.OnClickListener {

    /**
     * UI
     */
    private TextView mCityView;
    private TextView mWholeView;
    private DrawableCenterTextView mObligationView;
    private DrawableCenterTextView mUsedView;
    private DrawableCenterTextView mEvaluateView;
    private DrawableCenterTextView mRefundView;
    private RecyclerView mLatelyOrderRecyclerView;



    private OrderAdapter mLatelyOrderAdapter;

    /**
     * 显示订单界面
     *
     * @return
     */
    public static OrderFragment newInstance(){
        OrderFragment orderFragment = new OrderFragment();

        return orderFragment;
    }


    @Override
    protected int getLayoutSourceId() {
        return R.layout.fragment_order;
    }


    @Override
    protected void initWidgets(View rootView) {
        super.initWidgets(rootView);
        // init UI
        mWholeView = rootView.findViewById(R.id.whole);
        mObligationView = rootView.findViewById(R.id.obligation);
        mUsedView = rootView.findViewById(R.id.used);
        mEvaluateView = rootView.findViewById(R.id.evaluate);
        mRefundView = rootView.findViewById(R.id.refund);
        mLatelyOrderRecyclerView = rootView.findViewById(R.id.lately_order_recycler_view);

        mWholeView.setOnClickListener(this);
        mUsedView.setOnClickListener(this);
        mObligationView.setOnClickListener(this);
        mEvaluateView.setOnClickListener(this);
        mRefundView.setOnClickListener(this);
    }


    @Override
    protected void initDatas() {

        mLatelyOrderRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mLatelyOrderRecyclerView.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL,
                R.drawable.shape_divider_line,
                16,
                true));

        mLatelyOrderAdapter = new OrderAdapter(new RecyclerViewAdapter.AdapterListenerImpl<String>() {
            @Override
            public void onItemClick(RecyclerViewAdapter.ViewHolder<String> holder, String data) {
                super.onItemClick(holder, data);
                // 跳转到理发店详情界面
                BarberShopActivity.show(mContext,null);

            }
        });
        mLatelyOrderRecyclerView.setAdapter(mLatelyOrderAdapter);


        loadData();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.whole:        // 全部
                skip(0);
                break;
            case R.id.obligation:   // 待付款
                skip(1);
                break;
            case R.id.used:         // 待使用
                skip(2);
                break;
            case R.id.evaluate:     // 待评价
                skip(3);
                break;
            case R.id.refund:       // 退款
                skip(4);
                break;
            default:
                break;
        }

    }

    /**
     * 实现跳转到具体的子页面
     *
     * @param typeValue
     */
    private void skip(int typeValue){
        ExistActivity.show(mContext, typeValue);
    }


    private void loadData(){
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");

        mLatelyOrderAdapter.replace(list);
    }

}
