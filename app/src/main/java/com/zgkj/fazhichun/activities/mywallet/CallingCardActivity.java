package com.zgkj.fazhichun.activities.mywallet;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zgkj.common.app.ToolbarActivity;
import com.zgkj.common.http.AsyncHttpPostFormData;
import com.zgkj.common.http.AsyncHttpResponse;
import com.zgkj.common.http.AsyncOkHttpClient;
import com.zgkj.common.http.AsyncResponseHandler;
import com.zgkj.common.widgets.load.LoadFactory;
import com.zgkj.common.widgets.load.core.LoadManager;
import com.zgkj.common.widgets.load.view.AbsView;
import com.zgkj.factory.model.api.RspModel;
import com.zgkj.fazhichun.App;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.entity.Verification;
import com.zgkj.fazhichun.entity.wallet.BrokerageTotal;
import com.zgkj.fazhichun.view.LoadingView;
import com.zgkj.fazhichun.view.NetErrorView;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 我的名片
 */

public class CallingCardActivity extends ToolbarActivity {

    /**
     * UI
     */
    private ImageView qr_code;
    private LinearLayout content;
    /**
     * DATA
     */
    private LoadManager mLoadManager;

    public static void show(Context context) {
        // 跳转并传递值
        Intent intent = new Intent(context, CallingCardActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_wellet_calling_card;
    }

    @Override
    protected void initWidgets() {
        super.initWidgets();
        // init UI
        content=findViewById(R.id.content);
        qr_code=findViewById(R.id.qr_code);
        mLoadManager = LoadFactory.getInstance().register(content, new AbsView.OnReloadListener() {
            @Override
            public void onReload(View view) {
                mLoadManager.showStateView(LoadingView.class);
            }
        });
        mLoadManager.showSuccessView();
    }

    @Override
    protected void initDatas() {
        super.initDatas();
//        getExtensionCard();

        Bitmap mBitmap = CodeUtils.createImage("value.getUrl()", 180, 180, null);
        qr_code.setImageBitmap(mBitmap);

    }

    private void getExtensionCard() {
        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        okHttpClient.post("/v1/extension/extension-card", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                mLoadManager.showStateView(NetErrorView.class);
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:佣金总额" + response.toString());
                Type type = new TypeToken<RspModel<Verification>>() {
                }.getType();
                Verification value = getAnalysis(response, type, "佣金总额");
                mLoadManager.showSuccessView();
                if (value != null && !"[]".equals(value.toString())) {
                    Bitmap mBitmap = CodeUtils.createImage(value.getUrl(), 400, 400, null);
                    qr_code.setImageBitmap(mBitmap);
                } else {
                    App.showMessage();
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
                            break;
                        case 200:
                            return rspModel.getData();
                        default:
                            App.showMessage("错误码：" + rspModel.getCode());
                            break;
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                break;
        }
        return null;
    }
}
