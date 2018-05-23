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
import com.zgkj.fazhichun.entity.comment.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/28.
 * Descr:   我的评论适配器对象
 */
public class MineCommentAdapter extends RecyclerViewAdapter<Comment> {


    @Override
    protected int getItemViewType(int position, Comment data) {
        return R.layout.cell_mine_comment_list;
    }

    @Override
    protected ViewHolder<Comment> getViewHolder(View view, int viewType) {

        return new MineCommentViewHolder(view);
    }


    private static class MineCommentViewHolder extends RecyclerViewAdapter.ViewHolder<Comment> {

        /**
         * UI
         */
        private CircleImageView mPortraitView;
        private TextView mNameView;
        private StarBarView mStarBarView;
        private TextView mTimeView,graded_text;
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
            graded_text=itemView.findViewById(R.id.graded_text);
            mExpandableTextView = itemView.findViewById(R.id.expand_text_view);
            mRecyclerView = itemView.findViewById(R.id.recycler_view);

            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            mRecyclerView.addItemDecoration(new SpaceItemDecoration(mContext, 10));
            mCommentImageAdapter = new CommentImageAdapter();
            mRecyclerView.setAdapter(mCommentImageAdapter);
        }

        @Override
        protected void onBind(Comment data,int position) {
            Picasso.get().load("".equals(data.getImage_path()) ? mContext.getResources().getString(R.string.none_image_url) : data.getImage_path()).placeholder(R.drawable.none_img).noFade().error(R.drawable.image_error)
                    .into(mPortraitView);
            mNameView.setText(data.getNickname());
            mTimeView.setText(data.getCreate_time());
            mStarBarView.setStarMark(data.getService_score());
            graded_text.setText(data.getService_score()+"分");
            mExpandableTextView.setText(data.getEvaluate_detail(), mPosition);
            if(data.getImage_url()!=null){
                mRecyclerView.setVisibility(View.VISIBLE);
//                mCommentImageAdapter.replace(data.getNickname());
            }else{
                mRecyclerView.setVisibility(View.GONE);
            }
        }

        @Override
        public boolean isNeedClick() {
            return false;
        }
    }
}
