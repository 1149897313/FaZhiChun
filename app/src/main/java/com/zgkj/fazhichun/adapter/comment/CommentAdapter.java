package com.zgkj.fazhichun.adapter.comment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zgkj.common.widgets.CircleImageView;
import com.zgkj.common.widgets.StarBarView;
import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.common.widgets.recycler.decoration.SpaceItemDecoration;
import com.zgkj.common.widgets.text.ExpandableTextView;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.entity.shop.Talk;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/17.
 * Descr:   用户评论列表的适配器的定义
 */
public class CommentAdapter extends RecyclerViewAdapter<Talk> {


    @Override
    protected int getItemViewType(int position, Talk data) {
        return R.layout.cell_comment_list;
    }

    @Override
    protected ViewHolder<Talk> getViewHolder(View view, int viewType) {
        return new CommentViewHolder(view);
    }


    private static class CommentViewHolder extends RecyclerViewAdapter.ViewHolder<Talk> {
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
        public CommentViewHolder(View itemView) {
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
        protected void onBind(Talk data,int position) {
            Picasso.get().load(data.getImage_path()).placeholder(R.drawable.none_img)
                    .into(mPortraitView);
            mNameView.setText(data.getNickname());
            mTimeView.setText(data.getCreate_time());
            mStarBarView.setStarMark(data.getService_score());
            mExpandableTextView.setText(data.getEvaluate_detail(), mPosition);
            mCommentImageAdapter.replace(data.getImage_url());

        }

        @Override
        public boolean isNeedClick() {
            return false;
        }
    }


}
