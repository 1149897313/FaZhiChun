package com.zgkj.fazhichun.adapter.member;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.entity.member.MemberPrivilege;
import com.zgkj.fazhichun.entity.shop.BarberInfo;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/18.
 * Descr:  会员特权
 */

public class MemberPrivilegeAdapter extends RecyclerViewAdapter<MemberPrivilege> {


    public MemberPrivilegeAdapter(AdapterListener<MemberPrivilege> adapterListener) {
        super(adapterListener);
    }

    @Override
    protected int getItemViewType(int position, MemberPrivilege data) {
        return R.layout.member_privilege_item;
    }


    @Override
    protected ViewHolder<MemberPrivilege> getViewHolder(View view, int viewType) {
        return new HairstylistViewHolder(view);
    }


    private static class HairstylistViewHolder extends ViewHolder<MemberPrivilege> implements View.OnClickListener {


        private ImageView member_image,be_coming_up;
        private TextView title;
        private TextView content;
        /**
         * 构造方法
         *
         * @param itemView
         */
        public HairstylistViewHolder(View itemView) {
            super(itemView);
            member_image=itemView.findViewById(R.id.member_image);
            be_coming_up=itemView.findViewById(R.id.be_coming_up);
            title=itemView.findViewById(R.id.title);
            content=itemView.findViewById(R.id.content);
        }

        @Override
        protected void onBind(MemberPrivilege data,int position) {
            Picasso.get().load(data.getImageUrl()).placeholder(R.drawable.none_img)
                    .into(member_image);
            title.setText(data.getName());
            if(data.isState()){
                be_coming_up.setVisibility(View.VISIBLE);
            }else{
                be_coming_up.setVisibility(View.GONE);
            }
            content.setText(data.getContent());
        }

        @Override
        public boolean isNeedClick() {
            return true;
        }

        @Override
        public void onClick(View v) {

        }
    }
}