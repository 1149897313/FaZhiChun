package com.zgkj.fazhichun;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import com.zgkj.common.app.Activity;
import com.zgkj.fazhichun.activities.MainActivity;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;


/**
 * 程序显示界面的入口
 */
public class LaunchActivity extends Activity implements EasyPermissions.PermissionCallbacks {

    // 声明延迟启动运行的标识常量
    private static final int DELAY_LAUNCH_FLAG = 1000;


    // 权限申请的回调
    private static final int PERMISSION_REQUEST_CODE = 1;
    private String[] mLocationPerms = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };

    // 视图是否为第一次可见
    private boolean mIsFirstResume = false;

    // 创建一个权限提示的dialog
    private AlertDialog mPermissionDialog;

    private String mPermissionListText;




    // 创建一个Handler对象，用于延迟启动
    private Handler mHandler;


    /**
     * 定义Handler的内部类
     */
    private  class MyHandler extends Handler {

        private Context mContext;

        /**
         * 构造方法初始化对象
         * @param context
         */
        public MyHandler(Context context){
            mContext = context;
        }


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == DELAY_LAUNCH_FLAG) {
                MainActivity.show(mContext);
                finish();
            }

        }
    }


    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_launch;
    }


    @Override
    protected void initDatas() {
        super.initDatas();

        mHandler = new MyHandler(mContext);

        // 检查并请求定位权限
        checkAndRequestPermissions();
    }

    /**
     * 实现检查权限的方法
     */
    private void checkAndRequestPermissions() {
        if (EasyPermissions.hasPermissions(mContext, mLocationPerms)) {
            // 发送延迟2秒的启动指令
            sendDelayLaunchMessage();
        } else {
            // 请求权限
            EasyPermissions.requestPermissions(this,
                    getResources().getString(R.string.label_launch_location_permission_prompt),
                    PERMISSION_REQUEST_CODE,
                    mLocationPerms);
        }
    }


    /**
     * 发送延迟启动消息的方法
     */
    private void sendDelayLaunchMessage(){

        mHandler.sendEmptyMessageDelayed(DELAY_LAUNCH_FLAG, 2000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIsFirstResume) {
            if (EasyPermissions.hasPermissions(mContext, mLocationPerms)) {
                // 发送延迟2秒的启动指令
                sendDelayLaunchMessage();
            } else {
                // 弹出提示的dialog
                startSettingDialog(mPermissionListText);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 转交给权限申请框架处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 权限申请成功
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        // 如果权限申请成功
        if (EasyPermissions.hasPermissions(mContext, mLocationPerms)) {
            // 发送延迟2秒的启动指令
            sendDelayLaunchMessage();
        }
    }


    /**
     * 权限申请失败
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

        //处理权限名字字符串
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for (String str : mLocationPerms) {
            sb.append("(" + str + ")");
            sb.append("\n");
        }
        mPermissionListText = sb.append("\n").toString();

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {

            startSettingDialog(mPermissionListText);
        }

    }


    /**
     * 启动一个跳转设置的dialog
     *
     * @param message
     */
    private void startSettingDialog(String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(getResources().getString(R.string.label_launch_location_failure_prompt_title));
        builder.setMessage(String.format(getResources().getString(R.string.label_launch_location_failure_prompt_content), message));
        builder.setCancelable(false);

        // 拒绝退出应用
        builder.setNegativeButton(getResources().getString(R.string.label_launch_location_exit_app),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               closeSettingDialog();
                // 退出应用程序
                finish();
            }
        });

        // 进入设置界面
        builder.setPositiveButton(getResources().getString(R.string.label_launch_location_open_setting),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 跳转到设置界面
                startAppSettings();
                // 改变状态标识
                mIsFirstResume = true;
                closeSettingDialog();

            }
        });
        // 注册按键的监听
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                // 如果用户按下了返回键且同时按下的次数为第一次
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
                    closeSettingDialog();
                    // 退出应用程序
                    finish();
                    return true;
                }
                return false;
            }
        });

        mPermissionDialog = builder.create();

        if (mPermissionDialog != null && !mPermissionDialog.isShowing()){
            mPermissionDialog.show();
        }

    }

    /**
     * 关闭一个设置提示的dialog
     */
    private void closeSettingDialog(){
        if (mPermissionDialog != null && mPermissionDialog.isShowing()){
            mPermissionDialog.dismiss();
        }
    }

    /**
     *  启动应用的设置
     *
     * @since 2.5.0
     *
     */
    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 销毁handler对象
        if (mHandler != null) {
            // 移除所有的回调消息
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }
}
