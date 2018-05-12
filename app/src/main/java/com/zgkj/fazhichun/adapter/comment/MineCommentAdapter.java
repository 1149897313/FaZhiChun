package com.zgkj.fazhichun.adapter.comment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zgkj.common.widgets.CircleImageView;
import com.zgkj.common.widgets.StarBarView;
import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.common.widgets.recycler.decoration.SpaceItemDecoration;
import com.zgkj.common.widgets.text.ExpandableTextView;
import com.zgkj.fazhichun.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/28.
 * Descr:   我的评论适配器对象
 */
public class MineCommentAdapter extends RecyclerViewAdapter<String> {




    @Override
    protected int getItemViewType(int position, String data) {
        return R.layout.cell_mine_comment_list;
    }

    @Override
    protected ViewHolder<String> getViewHolder(View view, int viewType) {

        return new MineCommentViewHolder(view);
    }


    private static class MineCommentViewHolder extends RecyclerViewAdapter.ViewHolder<String> {

        /**
         * UI
         */
        private CircleImageView mPortraitView;
        private TextView mNameView;
        private StarBarView mStarBarView;
        private TextView mTimeView;
        private ExpandableTextView mExpandableTextView;
        private RecyclerView mRecyclerView;

        private CommentImageAdapter mCommentImageAdapter;



        /**
         * 构造方法
         *
         * @param itemView
         */
        public MineCommentViewHolder(View itemView) {
            super(itemView);

            mPortraitView = itemView.findViewById(R.id.portrait);
            mNameView = itemView.findViewById(R.id.name);
            mStarBarView = itemView.findViewById(R.id.star_bar_view);
            mTimeView = itemView.findViewById(R.id.time);
            mExpandableTextView = itemView.findViewById(R.id.expand_text_view);
            mRecyclerView = itemView.findViewById(R.id.recycler_view);

            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            mRecyclerView.addItemDecoration(new SpaceItemDecoration(mContext, 10));
            mCommentImageAdapter = new CommentImageAdapter();
            mRecyclerView.setAdapter(mCommentImageAdapter);
        }

        @Override
        protected void onBind(String data) {

            mExpandableTextView.setText("人工智能是计算机科学的一个分支，它企图了解智能的实质，并生产出一种新的能以人类智能相似的方式做出反应的智能机器，该领域的研究包括机器人、语言识别、图像识别、自然语言处理和专家系统等。人工智能从诞生以来，理论和技术日益成熟，应用领域也不断扩大，可以设想，未来人工智能带来的科技产品，将会是人类智慧的“容器”。人工智能可以对人的意识、思维的信息过程的模拟。人工智能不是人的智能，但能像人那样思考、也可能超过人的智能。", mPosition);

            List<String> list = new ArrayList<>();
            for (int i = 0; i < 8; i++){
                list.add("a");
            }

            mCommentImageAdapter.replace(list);

        }

        @Override
        public boolean isNeedClick() {
            return false;
        }
    }
}
