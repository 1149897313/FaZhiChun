package com.zgkj.fazhichun.fragments.main;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.zgkj.common.app.Fragment;
import com.zgkj.common.http.AsyncHttpPostFormData;
import com.zgkj.common.http.AsyncHttpResponse;
import com.zgkj.common.http.AsyncOkHttpClient;
import com.zgkj.common.http.AsyncResponseHandler;
import com.zgkj.common.widgets.CircleImageView;
import com.zgkj.common.widgets.text.DrawableCenterTextView;
import com.zgkj.factory.model.api.RspModel;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.activities.CollectActivity;
import com.zgkj.fazhichun.activities.CommentActivity;
import com.zgkj.fazhichun.activities.CouponsActivity;
import com.zgkj.fazhichun.activities.MessageActivity;
import com.zgkj.fazhichun.activities.OpenMemberActivity;
import com.zgkj.fazhichun.activities.PersonalActivity;
import com.zgkj.fazhichun.activities.SettingActivity;
import com.zgkj.fazhichun.activities.WalletActivity;
import com.zgkj.fazhichun.entity.user.PersonalCenter;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/2/27.
 * Descr:   我的碎片界面
 */

public class MineFragment extends Fragment implements View.OnClickListener {

    /**
     * UI
     */
    private ImageView mSettingView;
    private ImageView mMessageView;

    private CircleImageView portrait;
    private TextView name;
    private TextView becomeVip,info;
    private ImageView user_icon;
    private TextView user;
    private LinearLayout is_vip;

    private RelativeLayout mCollectLayout;      // 我的收藏
    private RelativeLayout mCommentLayout;      // 我的评论
    private RelativeLayout wallet_layout, coupons_layout;

    /**
     * 显示我的碎片
     *
     * @return
     */
    public static MineFragment newInstance() {
        MineFragment mineFragment = new MineFragment();

        return mineFragment;
    }

    @Override
    protected int getLayoutSourceId() {
        return R.layout.fragment_mine;
    }


    @Override
    protected void initWidgets(View rootView) {
        super.initWidgets(rootView);
        // init UI
        mSettingView = rootView.findViewById(R.id.setting);
        mMessageView = rootView.findViewById(R.id.message);
        portrait = rootView.findViewById(R.id.portrait);
        name = rootView.findViewById(R.id.name);
        becomeVip=rootView.findViewById(R.id.vip);
        info=rootView.findViewById(R.id.info);
        user = rootView.findViewById(R.id.user);
        is_vip=rootView.findViewById(R.id.is_vip);

        mCollectLayout = rootView.findViewById(R.id.collect_layout);
        mCommentLayout = rootView.findViewById(R.id.comment_layout);
        wallet_layout = rootView.findViewById(R.id.wallet_layout);
        coupons_layout = rootView.findViewById(R.id.coupons_layout);
        user_icon=rootView.findViewById(R.id.user_icon);

        coupons_layout.setOnClickListener(this);
        wallet_layout.setOnClickListener(this);
        becomeVip.setOnClickListener(this);
        info.setOnClickListener(this);
        mSettingView.setOnClickListener(this);
        mMessageView.setOnClickListener(this);
        mCollectLayout.setOnClickListener(this);
        mCommentLayout.setOnClickListener(this);


    }


    @Override
    protected void initDatas() {
        super.initDatas();

        onUserInfo();
    }


    /**
     * 获取用户信息
     */
    private void onUserInfo() {
        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        okHttpClient.post("/v1/user/user-center", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:onUserInfo " + response.toString());
                if (response.getCode() == 200) {
                    try {
                        Gson gson = new Gson();
                        Type type = new TypeToken<RspModel<PersonalCenter>>() {
                        }.getType();
                        RspModel<PersonalCenter> rspModel = gson.fromJson(response.getBody(), type);
                        showUserInfo(rspModel.getData());
                        Log.i(TAG, "onUserInfo: " + rspModel.toString());
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void showUserInfo(PersonalCenter personalCenter) {
        Picasso.get().load("".equals(personalCenter.getImage_path()) ? mContext.getResources().getString(R.string.none_image_url) : personalCenter.getImage_path()).placeholder(R.drawable.none_img)
                .into(portrait);
        name.setText(personalCenter.getNickname());
        if("2".equals(personalCenter.getMember_level())){
            user.setText("VIP用户");
            is_vip.setBackgroundResource(R.drawable.ic_mine_vip_user_bg);
            user_icon.setVisibility(View.VISIBLE);
            becomeVip.setVisibility(View.GONE);
        }else {
            user.setText("普通用户");
            is_vip.setBackgroundResource(R.drawable.ic_mine_ordinary_user_bg);
            user_icon.setVisibility(View.GONE);
            becomeVip.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting:      // 跳转到设置界面
                SettingActivity.show(mContext);
                break;
            case R.id.message:
                MessageActivity.show(mContext); // 显示用户的系统消息界面
                break;
            case R.id.vip://成为VIP
                OpenMemberActivity.show(mContext);
                break;
            case R.id.info://个人信息
                PersonalActivity.show(mContext);
                break;
            case R.id.collect_layout://我的收藏
                CollectActivity.show(mContext);
                break;
            case R.id.comment_layout:       // 我的评论
                CommentActivity.show(mContext, CommentActivity.TYPE_MINE_COMMENT);
                break;
            case R.id.wallet_layout://我的钱包
                WalletActivity.show(mContext, WalletActivity.TYPE_MINE_COMMENT);
                break;
            case R.id.coupons_layout://我的优惠券
                CouponsActivity.show(mContext);
                break;
            default:
                break;
        }
    }
}
