package com.zgkj.fazhichun.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.gyf.barlibrary.ImmersionBar;
import com.zgkj.common.app.Activity;
import com.zgkj.common.app.Fragment;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.fragments.main.HomeFragment;
import com.zgkj.fazhichun.fragments.main.MineFragment;
import com.zgkj.fazhichun.fragments.main.OrderFragment;
import com.zgkj.fazhichun.helper.FragmentController;
import com.zgkj.fazhichun.observer.location.LocationStateObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/2/26.
 * Descr:   程序的主界面
 */

public class MainActivity extends Activity implements
        BottomNavigationView.OnNavigationItemSelectedListener,
        HomeFragment.OnLocationHandleListener {

    /**
     * UI
     */
    private BottomNavigationView mBottomNavigationView;     // 底部导航栏

    /**
     * DATA
     */
    private List<Fragment> mFragmentList;       // 碎片集合对象


    // 定义定位相关的对象
    private AMapLocationClient mAMapLocationClient;


    /**
     * 显示主界面
     *
     * @param context 上下文对象
     */
    public static void show(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initWidgets() {
        super.initWidgets();
//        // 初始化沉浸式状态栏对象
//        ImmersionBar.with(this).init();

        // init UI
        mBottomNavigationView = findViewById(R.id.bottom_navigation_view);
        // 注册菜单项的点击监听事件
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);

    }


    @Override
    protected void initDatas() {
        super.initDatas();

        mFragmentList = new ArrayList<>();
        mFragmentList.add(HomeFragment.newInstance(this));
        mFragmentList.add(OrderFragment.newInstance());
        mFragmentList.add(MineFragment.newInstance());

        // 初始化碎片控制的调度数据
        FragmentController.getInstance()
                .init(getSupportFragmentManager(), R.id.container_layout)
                .setFragments(mFragmentList);

        // 设置显示首页的碎片
        mBottomNavigationView.getMenu().performIdentifierAction(R.id.action_home, 0);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                FragmentController.getInstance().showFragment(0);
                return true;
            case R.id.action_order:
                FragmentController.getInstance().showFragment(1);
                return true;
            case R.id.action_mine:
                FragmentController.getInstance().showFragment(2);
                return true;
            default:
                break;

        }
        return false;
    }


    /**
     * 启动定位的方法
     */
    @Override
    public void onStartLocation() {

        if (mAMapLocationClient == null) {
            mAMapLocationClient = new AMapLocationClient(mContext);
        }
        // 加载配置属性
        mAMapLocationClient.setLocationOption(getLocationOption());
        // 设置定位的回调监听
        mAMapLocationClient.setLocationListener(mAMapLocationListener);
        // 启动定位
        mAMapLocationClient.startLocation();
        // 延迟两秒开启定位
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        }, 2000);
    }


    /**
     * 停止定位的方法
     */
    @Override
    public void onStopLocation() {
        if (mAMapLocationClient != null) {
            if (mAMapLocationClient.isStarted()) {
                mAMapLocationClient.stopLocation();
            }
            mAMapLocationClient.unRegisterLocationListener(mAMapLocationListener);
            mAMapLocationClient = null;
        }
    }


    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getLocationOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(3000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(false); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }


    /**
     * 定位操作的回调监听
     */
    AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                String address = aMapLocation.getAddress();

                String city = aMapLocation.getCity();
                if (!TextUtils.isEmpty(address)) {
                    Toast.makeText(mContext, "定位成功！当前地址为: " + address, Toast.LENGTH_SHORT).show();
                    // 通知定位成功
                    LocationStateObserver.getInstance().notifyObserverLocationSuccess();
                } else {
                    // 通知定位失败
                    LocationStateObserver.getInstance().notifyObserverLocationFailure();

                    Toast.makeText(mContext, "错误码：" + aMapLocation.getErrorCode() + "   " + aMapLocation.getLocationDetail(), Toast.LENGTH_LONG).show();
                }
            } else {
                // 通知定位失败
                LocationStateObserver.getInstance().notifyObserverLocationFailure();
            }


        }
    };
}
