package com.zgkj.fazhichun.adapter.history;

import android.view.View;
import android.widget.TextView;

import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.fazhichun.R;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/9.
 * Descr:   代金券历史界面适配器的定义
 */

public class HistoryAdapter extends RecyclerViewAdapter<String> {

    // 创建一个监听查看历史的监听接口对象
    private OnSeeHistoryListener mOnSeeHistoryListener;

    /**
     * 构造方法，初始化数据
     *
     * @param onSeeHistoryListener
     */
    public HistoryAdapter(OnSeeHistoryListener onSeeHistoryListener) {
        mOnSeeHistoryListener = onSeeHistoryListener;
    }


    @Override
    protected int getItemViewType(int position, String data) {
        return R.layout.cell_history_list;
    }

    @Override
    protected ViewHolder<String> getViewHolder(View view, int viewType) {
        return new HistoryViewHolder(view);
    }

    private class HistoryViewHolder extends RecyclerViewAdapter.ViewHolder<String> implements View.OnClickListener {
        private TextView mMoneyView;        // 金额
        private TextView mStateView;        // 状态
        private TextView mTimeView;         // 时间
        private TextView mSeeView;          // 查看

        /**
         * 构造方法
         *
         * @param itemView
         */
        public HistoryViewHolder(View itemView) {
            super(itemView);
            mMoneyView = itemView.findViewById(R.id.money);
            mStateView = itemView.findViewById(R.id.state);
            mTimeView = itemView.findViewById(R.id.time);
            mSeeView = itemView.findViewById(R.id.see);

            // 为查看显示控件注册点击响应事件
            mSeeView.setOnClickListener(this);
        }

        @Override
        protected void onBind(String data) {

            mMoneyView.setText("2000.00");
            mStateView.setText("消费");
            mTimeView.setText("2018-03-09 10:30");
        }

        @Override
        public boolean isNeedClick() {
            return true;
        }


        /**
         * 点击事件的回调方法
         *
         * @param v
         */
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.see) {
                // 回调给显示历史界面的载体去处理
                if (mOnSeeHistoryListener != null) {
                    mOnSeeHistoryListener.onSeeHistory();
                }
            }
        }
    }


    /**
     * 定义一个查看历史的监听接口
     */
    public interface OnSeeHistoryListener {

        /**
         * 查看历史的回调方法
         */
        void onSeeHistory();

    }
}
