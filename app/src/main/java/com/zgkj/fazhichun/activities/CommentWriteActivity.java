package com.zgkj.fazhichun.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.zgkj.common.app.ToolbarActivity;
import com.zgkj.common.http.AsyncHttpPostFormData;
import com.zgkj.common.http.AsyncHttpResponse;
import com.zgkj.common.http.AsyncOkHttpClient;
import com.zgkj.common.http.AsyncResponseHandler;
import com.zgkj.common.widgets.CommentTag;
import com.zgkj.common.widgets.MultiLineRadioGroup;
import com.zgkj.common.widgets.StarBarView;
import com.zgkj.common.widgets.load.LoadFactory;
import com.zgkj.common.widgets.load.core.LoadManager;
import com.zgkj.common.widgets.load.view.AbsView;
import com.zgkj.factory.model.api.RspModel;
import com.zgkj.fazhichun.App;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.view.EmptyView;
import com.zgkj.fazhichun.view.LoadingView;
import com.zgkj.fazhichun.view.NetErrorView;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/16.
 * Descr: 写评论
 */
public class CommentWriteActivity extends ToolbarActivity implements View.OnClickListener {
    /**
     * DATA
     */
    private LoadManager mLoadManager;
    private String id;

    //评价分数
    private String service_score = "5";
    //评价标签id
    private String label_detail = "";

    /**
     * UI
     */
    private NestedScrollView mNestedScrollView;
    private MultiLineRadioGroup tagCheck;
    private RadioGroup radio_group;
    private StarBarView jcSb, bmySb, ybSb, mySb, fcmySb;
    private EditText text_content;
    private TextView submit;

    public static void show(Context context, String id) {
        Intent intent = new Intent(context, CommentWriteActivity.class);
        intent.putExtra("ID", id);
        context.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        id = bundle.getString("ID");
        return !TextUtils.isEmpty(id);
    }

    @Override
    protected void initWidgets() {
        super.initWidgets();
        mNestedScrollView = findViewById(R.id.nested_scroll_view);
        radio_group = findViewById(R.id.radio_group);
        tagCheck = findViewById(R.id.content);
        jcSb = findViewById(R.id.jc_sb);
        bmySb = findViewById(R.id.bmy_sb);
        ybSb = findViewById(R.id.yb_sb);
        mySb = findViewById(R.id.my_sb);
        fcmySb = findViewById(R.id.fcmy_sb);
        text_content = findViewById(R.id.text_content);
        submit = findViewById(R.id.submit);

        tagCheck.getCheckedItems();

        submit.setOnClickListener(this);
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.jc:
                        onGetTag("1");
                        jcSb.setStarMark(1f);
                        bmySb.setStarMark(0f);
                        ybSb.setStarMark(0f);
                        mySb.setStarMark(0f);
                        fcmySb.setStarMark(0f);
                        break;
                    case R.id.bmy:
                        onGetTag("2");
                        jcSb.setStarMark(0f);
                        bmySb.setStarMark(2f);
                        ybSb.setStarMark(0f);
                        mySb.setStarMark(0f);
                        fcmySb.setStarMark(0f);
                        break;
                    case R.id.yb:
                        onGetTag("3");
                        jcSb.setStarMark(0f);
                        bmySb.setStarMark(0f);
                        ybSb.setStarMark(3f);
                        mySb.setStarMark(0f);
                        fcmySb.setStarMark(0f);
                        break;
                    case R.id.my:
                        System.out.println("---x:" + jcSb);
                        onGetTag("4");
                        jcSb.setStarMark(0f);
                        bmySb.setStarMark(0f);
                        ybSb.setStarMark(0f);
                        mySb.setStarMark(4f);
                        fcmySb.setStarMark(0f);
                        break;
                    case R.id.fcmy:
                        onGetTag("5");
                        jcSb.setStarMark(0f);
                        bmySb.setStarMark(0f);
                        ybSb.setStarMark(0f);
                        mySb.setStarMark(0f);
                        fcmySb.setStarMark(5f);
                        break;
                }
            }
        });
    }


    @Override
    protected void initDatas() {
        super.initDatas();
        mLoadManager = LoadFactory.getInstance().register(mNestedScrollView, new AbsView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                mLoadManager.showStateView(LoadingView.class);
            }
        });
        fcmySb.setStarMark(5f);
        onGetTag("5");
    }

    @Override
    protected int getLayoutSourceId() {
        return R.layout.comment_write;
    }

    /**
     * 获取评价标签
     *
     * @param commentType 评价等级
     */
    private void onGetTag(final String commentType) {
        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        AsyncHttpPostFormData.addFormData("evaluate_id", commentType);
        okHttpClient.post("/v1/order-evaluate/evaluate-labels", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                mLoadManager.showStateView(NetErrorView.class);
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:onGetTag" + response.toString());
                Type type = new TypeToken<RspModel<List<CommentTag>>>() {
                }.getType();
                List<CommentTag> list = getAnalysis(response, type, "onGetTag");
                if (list != null && !"[]".equals(list.toString())) {
                    tagCheck.removeAll();
                    tagCheck.addAll(list);
                    service_score = commentType;
                } else {
                    mLoadManager.showStateView(EmptyView.class);
                }
            }
        });
    }

    /**
     * 评论提交
     *
     * @param order_id        int	是		无	评论订单id
     * @param evaluate_detail string	否		无	评价内容
     * @param label_detail    json	否		无	评价标签id
     * @param image_url       json-base64	是			上传的图片
     * @param service_score   int	是			评价分数
     */
    private void onCommentSubmit(String order_id, String evaluate_detail, String label_detail, String image_url, String service_score) {
        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        AsyncHttpPostFormData.addFormData("order_id", order_id);
        AsyncHttpPostFormData.addFormData("evaluate_detail", evaluate_detail);
        AsyncHttpPostFormData.addFormData("label_detail", label_detail);
        AsyncHttpPostFormData.addFormData("image_url", image_url);
        AsyncHttpPostFormData.addFormData("service_score", service_score);
        okHttpClient.post("/v1/order-evaluate/evaluate", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                mLoadManager.showStateView(NetErrorView.class);
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:onCommentSubmit" + response.toString());
                Type type = new TypeToken<RspModel<RspModel>>() {
                }.getType();
                RspModel value = getAnalysis(response, type, "onCommentSubmit");
                if (value != null && !"[]".equals(value.toString())) {
                    if(value.getCode()==1){

                    }
                    App.showMessage(value.getData().toString());
                } else {
                    mLoadManager.showStateView(EmptyView.class);
                }
            }
        });
    }

    /**
     * 数据解析
     *
     * @param response
     * @param type
     * @param log
     * @param <T>
     * @return
     */
    private <T> T getAnalysis(AsyncHttpResponse response, Type type, String log) {
        switch (response.getCode()) {
            case 200:
                try {
                    Gson gson = new Gson();
                    RspModel<T> rspModel = gson.fromJson(response.getBody(), type);
                    Log.i(TAG, "onSuccess: " + log + rspModel.toString());
                    switch (rspModel.getCode()) {
                        case 1:
                            mLoadManager.showStateView(EmptyView.class);
                            break;
                        case 200:
                            mLoadManager.showSuccessView();
                            return rspModel.getData();
                        default:
                            App.showMessage("错误码：" + rspModel.getCode());
                            mLoadManager.showStateView(EmptyView.class);
                            break;
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                break;
        }
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit://提交评论
                //获取评论标签ID
                List<CommentTag> tagList = tagCheck.getCheckedValues();
                if (tagList != null && !"[]".equals(tagList.toString())) {
                    List<String> list = new ArrayList<>();
                    for (int i = 0; i < tagList.size(); i++) {
                        list.add(tagList.get(i).getLabel_id());
                    }
                    label_detail = new Gson().toJson(list);
                }
                onCommentSubmit(id, text_content.getText().toString(),label_detail, "" , service_score);
                break;
            default:
                break;
        }
    }
}
