package com.zgkj.fazhichun.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.yalantis.ucrop.UCrop;
import com.zgkj.common.app.ToolbarActivity;
import com.zgkj.common.glide.GlideApp;
import com.zgkj.common.http.AsyncHttpPostFormData;
import com.zgkj.common.http.AsyncHttpResponse;
import com.zgkj.common.http.AsyncOkHttpClient;
import com.zgkj.common.http.AsyncResponseHandler;
import com.zgkj.common.utils.Base64Coder;
import com.zgkj.common.utils.FileUtil;
import com.zgkj.common.widgets.CircleImageView;
import com.zgkj.common.widgets.load.LoadFactory;
import com.zgkj.common.widgets.load.core.LoadManager;
import com.zgkj.common.widgets.load.view.AbsView;
import com.zgkj.factory.model.api.RspModel;
import com.zgkj.fazhichun.App;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.entity.user.HeardImgUp;
import com.zgkj.fazhichun.fragments.dialog.portrait.UpdatePortraitDialogFragment;
import com.zgkj.fazhichun.view.EmptyView;
import com.zgkj.fazhichun.view.NetErrorView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

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

    private void encode(String path) {
        //decode to bitmap
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        Log.d(TAG, "bitmap width: " + bitmap.getWidth() + " height: " + bitmap.getHeight());
        //convert to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();

        //base64 encode
        byte[] encode = Base64.encode(bytes, Base64.DEFAULT);
        String encodeString = new String(encode);
    }

    /**
     * 头像上传
     *
     * @param bitmap
     */
    private void upHeardImage(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] b = stream.toByteArray();
        // 将图片流以字符串形式存储下来
        String file = new String(Base64Coder.encodeLines(b));
//
//        long length = file.length();
//        long segmentSize=3000;
//        if (length <= segmentSize ) {// 长度小于等于限制直接打印
//            Log.e("xx", file);
//        }else {
//            String msg=null;
//            while (file.length() > segmentSize ) {// 循环分段打印日志
//                String logContent = file.substring(0, 3000 );
//                msg = file.replace(logContent, "");
//                Log.e("xx", logContent);
//            }
//            Log.e("xx", msg);// 打印剩余日志
//        }
//        String file = "iVBORw0KGgoAAAANSUhEUgAAABoAAAAaCAYAAACpSkzOAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyhpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTM4IDc5LjE1OTgyNCwgMjAxNi8wOS8xNC0wMTowOTowMSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTcgKE1hY2ludG9zaCkiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6OTM5OUVCQTEzMTA2MTFFOEFERURCOTAwRERDNkQxRTciIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6OTM5OUVCQTIzMTA2MTFFOEFERURCOTAwRERDNkQxRTciPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDo5Mzk5RUI5RjMxMDYxMUU4QURFREI5MDBEREM2RDFFNyIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDo5Mzk5RUJBMDMxMDYxMUU4QURFREI5MDBEREM2RDFFNyIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/Pu0tsl0AAAH4SURBVHjatJbLSwJRFMaPk4QJIeVGl0H0cNNSJSsKMqJF4TZaCeW2dbbKNu3aFRFBtC1qE/RAC0Oaf8DURUGrJIpW9hLsHOeMqc3D0enAJ8N9nJ937tzvXOv8bgl0og81hwqiPCgnt7+g0qhz1DEqp5XEogEaQ62hRqCxSKJWUddKnYJCmw21hUoYgACPTfBcmx6oGxVHLdFqwXhYeG6ccymC7KhTlB9aDz/nsiuBNlFeMC+8nLMGRBsfBvMjzLkroFiTe9LInsVk0BAq0Eq2QC+Ar0e9mxhW/Am1ClnkQ0AnUnxQHBYS9M5Ke5s+RMAX9FUEeCuoDh0l0IBabxANZx3Np8uuDfn4BthAI8rmVUH9QpV31cSgC2DBB+B2AKxM18KUILm85ht2Cmo9mSeAy7T07KqCNQEph5Vd2F3fQRu7fys9T3okWGwWoNNmHEIMWlFGrVeGXfDKHB0S5LNoCEKRFdjeQQ/2+PrbdiAagpRLCIGO9EYRLIql7f4ZYC+FtSBr+LgdyoUv2ao7aMQNnVX5q4vyHzc7Slx1K6ZK5XfnH0CU86q+Hi2TVZkIETnnn8JHTjVjEkzkXAW1OwMd3nHUdpN7VuK5E5xL8xb0joowMGUAkmJApHol1RakFvSBDLO7o/nAFHlt3QXyDnWGOtFyGIofAQYAT8iEQ1ZXA3gAAAAASUVORK5CYII=";

        System.out.println("----:file:"+file);
        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        AsyncHttpPostFormData.addFormData("image_path", file);
        okHttpClient.post("/v1/user/save-heard-img", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                mLoadManager.showStateView(NetErrorView.class);
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i(TAG, "onSuccess:upHeardImage" + response.toString());
                Type type = new TypeToken<RspModel<HeardImgUp>>() {
                }.getType();
                HeardImgUp order = getAnalysis(response, type, "upHeardImage");
                if (order != null) {

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
                            Log.i(TAG, log + ": " + rspModel.getData());
                            return rspModel.getData();
                        default:
                            App.showMessage("data:code" + rspModel.getCode());
                            mLoadManager.showStateView(EmptyView.class);
                            break;
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                break;
            default:
                mLoadManager.showStateView(EmptyView.class);
                break;
        }
        return null;
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
                    Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());

                    upHeardImage(bitmap);


                    break;
                default:
                    break;
            }

        } else if (resultCode == UCrop.RESULT_ERROR) {
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
