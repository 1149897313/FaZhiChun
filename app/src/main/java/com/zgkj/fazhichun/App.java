package com.zgkj.fazhichun;

import android.content.Context;
import android.widget.Toast;

import com.zgkj.common.app.Application;
import com.zgkj.common.widgets.load.LoadFactory;
import com.zgkj.factory.Factory;
import com.zgkj.fazhichun.view.EmptyView;
import com.zgkj.fazhichun.view.ErrorView;
import com.zgkj.fazhichun.view.LoadingView;
import com.zgkj.fazhichun.view.LocationView;
import com.zgkj.fazhichun.view.NetErrorView;


/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/2/27.
 * Descr:
 */

public class App extends Application {

    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LoadFactory.getInstance()
                .addView(new LoadingView())
                .addView(new EmptyView())
                .addView(new LocationView())
                .addView(new ErrorView())
                .addView(new NetErrorView())
                .setDefaultViewClass(LoadingView.class);


        // 加载配置数据
        Factory.setup();
    }

    public static void showMessage() {
        Toast.makeText(context, context.getResources().getString(R.string.label_launch_location_exit_app), Toast.LENGTH_SHORT).show();
    }

    public static void showMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


}
