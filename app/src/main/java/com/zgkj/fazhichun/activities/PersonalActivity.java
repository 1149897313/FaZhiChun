package com.zgkj.fazhichun.activities;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yalantis.ucrop.UCrop;
import com.zgkj.common.app.Application;
import com.zgkj.common.app.ToolbarActivity;
import com.zgkj.common.glide.GlideApp;
import com.zgkj.common.utils.FileUtil;
import com.zgkj.common.utils.PhotoUtil;
import com.zgkj.common.widgets.CircleImageView;
import com.zgkj.common.widgets.load.LoadFactory;
import com.zgkj.common.widgets.load.core.LoadManager;
import com.zgkj.common.widgets.load.view.AbsView;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.fragments.dialog.portrait.UpdatePortraitDialogFragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/8.
 * Descr:   个人信息显示界面
 */

public class PersonalActivity extends ToolbarActivity implements View.OnClickListener,
        UpdatePortraitDialogFragment.OnUpdatePortraitListener,
        EasyPermissions.PermissionCallbacks {

    /**
     * UI
     */
    private LinearLayout mContentLayout;
    private RelativeLayout mPortraitLayout;
    private CircleImageView mPortraitView;
    private RelativeLayout mNicknameLayout;
    private TextView mNicknameView;
    private RelativeLayout mPhoneLayout;
    private TextView mPhoneView;



    /**
     * DATA
     */
    private LoadManager mLoadManager;


    public static final int PERMISSION_REQUEST_CODE = 1;

    // 定义相关权限
    private String[] mCameraPerms = new String[]{
            Manifest.permission.CAMERA,
    };


    // 定义拍照和相册裁剪操作的请求码
    private static final int CAMERA_REQUEST_CODE = 5000;
    private static final int GALLERY_REQUEST_CODE = 5001;
    private static final int CROP_REQUEST_CODE = 5002;

    // Android7.0 以上系统图片的Uri地址
    private Uri mProviderUri;

    // Android7.0 以下系统图片的Uri地址
    private Uri mUri;

    /**
     * 显示个人信息界面
     *
     * @param context
     */
    public static void show(Context context) {
        context.startActivity(new Intent(context, PersonalActivity.class));
    }


    @Override
    protected int getLayoutSourceId() {
        return R.layout.activity_personal;
    }


    @Override
    protected void initWidgets() {
        super.initWidgets();
        // init UI
        mContentLayout = findViewById(R.id.content_layout);
        mPortraitLayout = findViewById(R.id.portrait_layout);
        mPortraitView = findViewById(R.id.portrait);
        mNicknameLayout = findViewById(R.id.nickname_layout);
        mNicknameView = findViewById(R.id.nickname);
        mPhoneLayout = findViewById(R.id.phone_layout);
        mPhoneView = findViewById(R.id.phone);

        mPortraitLayout.setOnClickListener(this);
        mNicknameLayout.setOnClickListener(this);
        mPhoneLayout.setOnClickListener(this);

        mLoadManager = LoadFactory.getInstance().register(mContentLayout, new AbsView.OnReloadListener() {
            @Override
            public void onReload(View view) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoadManager.showSuccessView();
            }
        }, 500);
    }


    @Override
    protected void initDatas() {
        super.initDatas();



    }

    /**
     * 点击事件的回调方法
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.portrait_layout:      // 点击弹出提示更新头像的提示框
                // 检查并申请权限
                checkAndRequestPermission();
                break;
            case R.id.nickname_layout:      // 修改昵称

                break;
            case R.id.phone_layout:

                break;
            default:
                break;
        }

    }


    /**
     * 更新头像
     */
    private void showUpdatePortraitDialogFragment() {
        // 显示一个提示的dialog
        UpdatePortraitDialogFragment updatePortraitDialogFragment
                = UpdatePortraitDialogFragment.newInstance(this);
        if (!updatePortraitDialogFragment.isAdded()) {
            updatePortraitDialogFragment.show(getSupportFragmentManager(),
                    updatePortraitDialogFragment.getClass().getName());
        }
    }


    /**
     * 拍一张的回调
     */
    @Override
    public void onCamera() {
        // 跳转到相机
        camera();
    }

    /**
     * 相册选择的回调
     */
    @Override
    public void onGallery() {
        // 跳转到相册
        gallery();
    }

    /**
     * 拍照片
     */
    private void camera() {
        //使用当前系统的日期加以调整作为照片的名称
        Date date = new Date(System.currentTimeMillis());
        //创建日期格式转换对象
        SimpleDateFormat dataFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        // 定义Image图片的文件名
        String imageFileName = dataFormat.format(date) + ".jpg";
        // 获取储存头像图片文件夹的路径
        String portraitDirName = FileUtil.createAppFoldersRootPath() + File.separator + "Pictures";

        File imageFile = new File(portraitDirName, imageFileName);
        // 如果储存图片文件的目录没有创建则进行创建的操作
        if (!imageFile.getParentFile().exists()) {
            imageFile.getParentFile().mkdirs();
        }
        // 创建一个打开相机的意图
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Android7.0 以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //通过FileProvider创建一个content类型的Uri
            mProviderUri = FileProvider.getUriForFile(mContext, "com.zgkj.fazhichun.fileprovider", imageFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mProviderUri);
            // 添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            mUri = Uri.fromFile(imageFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        }
        // 启动相机
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }



    /**
     * 跳转到相册
     */
    private void gallery() {
        // 创建意图对象
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");

        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }



    /**
     * 使用uCrop进行原图片的裁剪
     *
     * @param uri
     */
    private void cropRawPhoto(Uri uri) {
        // 配置裁剪的相关属性
        UCrop.Options options = new UCrop.Options();
        // 修改标题栏颜色
//        options.setToolbarColor(getResources().getColor(R.color.orange_400));
        // 设置状态栏颜色
//        options.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        // 设置标题文字
        options.setToolbarTitle("裁剪头像");
        // 隐藏底部工具
        options.setHideBottomControls(false);
        // 设置图片格式
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        // 设置图片压缩质量
        options.setCompressionQuality(100);
        // 是否让用户调整范围(默认false)，如果开启，可能会造成剪切的图片的长宽比不是设定的
        // 如果不开启，用户不能拖动选框，只能缩放图片
        options.setFreeStyleCropEnabled(true);
        // 设置裁剪窗口是否为椭圆
        options.setCircleDimmedLayer(false);
        // 显示网格线
        options.setShowCropGrid(true);
        //设置是否展示矩形裁剪框
        options.setShowCropFrame(true);
        //设置裁剪框横竖线的宽度
//        options.setCropGridStrokeWidth(4);
        //设置裁剪框横竖线的颜色
//        options.setCropGridColor(Color.YELLOW);
        //设置竖线的数量
        options.setCropGridColumnCount(2);
        //设置横线的数量
        options.setCropGridRowCount(2);

        //使用当前系统的日期加以调整作为照片的名称
        Date date = new Date(System.currentTimeMillis());
        //创建日期格式转换对象
        SimpleDateFormat dataFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        // 定义Image图片的文件名
        String imageFileName = dataFormat.format(date) + ".jpg";
        // 获取储存头像图片文件夹的路径
        String portraitDirName = FileUtil.createAppFoldersRootPath() + File.separator + "Pictures";

        // 设置源Uri以及目标Uri
        UCrop.of(uri, Uri.fromFile(new File(portraitDirName, imageFileName)))
                // 设置长宽比
                .withAspectRatio(1, 1)
                // 设置图片的长度和宽度
                .withMaxResultSize(200, 200)
                // 加载配置属性
                .withOptions(options)
                .start(this, CROP_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 如果裁剪错误则直接返回不做任何操作
        if (resultCode == UCrop.RESULT_ERROR) {
            return;
        }
        // 如果有返回值
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST_CODE:       // 拍照
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        cropRawPhoto(mProviderUri);
                    } else {
                        cropRawPhoto(mUri);
                    }
                    break;
                case GALLERY_REQUEST_CODE:      // 相册选择
                    cropRawPhoto(data.getData());
                    break;
                case CROP_REQUEST_CODE:
                    Uri uri = UCrop.getOutput(data);
                    GlideApp.with(this)
                            .load(uri)
                            .into(mPortraitView);
                    break;
                default:
                    break;
            }

        }else if (resultCode == UCrop.RESULT_ERROR){
            // 裁剪图片时出现位置的错误
            // TODO .....
        }

    }


    private void checkAndRequestPermission() {
        // 如果具有相关权限
        if (EasyPermissions.hasPermissions(mContext, mCameraPerms)) {
            // 更新头像
            showUpdatePortraitDialogFragment();
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this,
                    "更新头像需要你授权手机的相机权限和文件的读写权限",
                    PERMISSION_REQUEST_CODE,
                    mCameraPerms);
        }
    }

    /**
     * 重写权限请求的方法
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 把权限申请的执行结果的操作给EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    /**
     * 申请权限成功
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        // 如果权限申请成功
        if (EasyPermissions.hasPermissions(mContext, mCameraPerms)) {
            showUpdatePortraitDialogFragment();
        }
    }

    /**
     * 申请权限失败
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        //处理权限名字字符串
        StringBuilder sb = new StringBuilder();
        for (String str : perms) {
            sb.append("(" + str + ")");
            sb.append("\n");
        }
        String message = sb.append("\n").toString();

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle("提示")
                    .setRationale("更新头像需要以下权限：" + message + "是否打开设置界面进行授权？")
                    .setPositiveButton("是")
                    .setNegativeButton("否")
                    .build()
                    .show();
        }

    }
}
