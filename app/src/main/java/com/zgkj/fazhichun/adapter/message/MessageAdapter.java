package com.zgkj.fazhichun.adapter.message;

import android.view.View;
import android.widget.TextView;

import com.zgkj.common.widgets.CircleImageView;
import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.common.widgets.text.ExpandableTextView;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.entity.massage.Massage;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/18.
 * Descr:   系统消息
 */
public class MessageAdapter extends RecyclerViewAdapter<Massage> {


    public MessageAdapter(AdapterListener<Massage> adapterListener) {
        super(adapterListener);
    }

    @Override
    protected int getItemViewType(int position, Massage data) {
        return R.layout.item_message;
    }

    @Override
    protected ViewHolder<Massage> getViewHolder(View view, int viewType) {
        return new MessageeViewHolder(view);
    }


    private static class MessageeViewHolder extends ViewHolder<Massage> {

        /**
         * UI
         */
        private CircleImageView mPortraitView;
        private TextView mNameView;
        private TextView mTimeView;
        private ExpandableTextView mExpandableTextView;


        /**
         * 构造方法
         *
         * @param itemView
         */
        public MessageeViewHolder(View itemView) {
            super(itemView);
            mPortraitView = itemView.findViewById(R.id.portrait);
            mNameView = itemView.findViewById(R.id.name);
            mTimeView = itemView.findViewById(R.id.time);
            mExpandableTextView = itemView.findViewById(R.id.expand_text_view);
        }

        @Override
        protected void onBind(Massage data,int position) {
            mNameView.setText(data.getMessage_title());
            mTimeView.setText(data.getCreate_time());
            //String content="富士达发时代峰峻阿萨德路附近的搜房had搜房哈斯豆腐哈手动阀是东方红松岛枫哈山东发松岛枫萨拉丁大佛山将碍事的立法会的萨芬蓝色大海你发了盛大好烦啦时代峰峻发生的浪费绝对是佛教爱上大连风华绝代说风凉话你说的两个黄蜡石更合适的按时发哪里手动返回了斯蒂芬哈市的理发师代理费哈拉水电费十多年来哈工大是那个浪费你撒谎的浪费哈三联代发货送大礼菲尼萨理发的两会上的立法收到货发两双得喝风蓝色大海范德萨发了很大水力发电";
            mExpandableTextView.setText(data.getMessage_content(), mPosition);
        }

        @Override
        public boolean isNeedClick() {
            return true;
        }
    }
}
