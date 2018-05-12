package com.zgkj.common.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.zgkj.common.app.Activity;
import com.zgkj.common.app.Application;

import java.io.File;
import java.io.IOException;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/15.
 * Descr:   拍照和相册选取照片的处理
 */

public class PhotoUtil {


    /**
     * 私有化的构造方法，禁止通过new的方式创建对象
     */
    private PhotoUtil(){
        // 抛出不支持的操作异常
        throw new UnsupportedOperationException("cannot be instantiated!");
    }


    /**
     * 拍照片，跳转到相机界面
     *
     * @param code
     */
    public static File toCamera(Activity activity, int code){
        // 创建一个图片文件
        File file = Application.getPortraitTmpFile();
        try {
            // 如果文件存在则先删除
            if (file.exists()){
                file.delete();
            }
            // 重新创建新的文件
            file.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Uri uri = getImageContentUri(activity, file);

        // 创建一个跳转相机的指令
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        // 跳转
        activity.startActivityForResult(intent, code);

        return file;

    }

    /**
     * 相册，跳转到相册
     */
    private void toGallery(Activity activity, int code) {
        // 创建意图对象
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activity.startActivityForResult(intent, code);
    }






    /**
     * 实现裁剪图片的方法
     * @param activity
     * @param uri
     * @param width
     * @param height
     * @param code
     */
    public static void photoZoom(Activity activity, Uri uri, int width, int height, int code) {
        // 创建意图对象
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", true);
        // 设置宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 设置裁剪图片的质量
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        //禁用人脸识别功能
        intent.putExtra("noFaceDetection", true);
        // 发送数据
        intent.putExtra("return-data", true);

        activity.startActivityForResult(intent, code);
    }




    /**
     * 转换Uri类型的方法
     *
     * @param context
     * @param imageFile
     * @return
     */
    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }













}
