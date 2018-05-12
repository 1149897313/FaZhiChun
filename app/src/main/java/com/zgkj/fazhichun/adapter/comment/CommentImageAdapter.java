package com.zgkj.fazhichun.adapter.comment;

import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.fazhichun.R;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/17.
 * Descr:   理发店评论列表评论图片的适配器对象的定义
 */
public class CommentImageAdapter extends RecyclerViewAdapter<String> {


    @Override
    protected int getItemViewType(int position, String data) {
        return R.layout.cell_comment_image_list;
    }

    @Override
    protected ViewHolder<String> getViewHolder(View view, int viewType) {
        return new CommentImageViewHolder(view);
    }


    private static class CommentImageViewHolder extends RecyclerViewAdapter.ViewHolder<String> {

        private ImageView mCommentImageView;


        /**
         * 构造方法
         *
         * @param itemView
         */
        public CommentImageViewHolder(View itemView) {
            super(itemView);
            mCommentImageView = itemView.findViewById(R.id.comment_image);
        }

        @Override
        protected void onBind(String data) {
            Picasso.get().load(data).placeholder(R.drawable.none_img)
                    .into(mCommentImageView);
        }

        @Override
        public boolean isNeedClick() {
            return false;
        }
    }
}
