package com.zgkj.fazhichun;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import com.squareup.picasso.Transformation;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/16.
 * Descr:
 */
public class CropSquareTransformation implements Transformation {


    @Override
    public Bitmap transform(Bitmap source) {
        int widthLight = source.getWidth();
        int heightLight = source.getHeight();

        Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);
        Paint paintColor = new Paint();
        paintColor.setFlags(Paint.ANTI_ALIAS_FLAG);

        RectF rectF = new RectF(new Rect(0, 0, widthLight, heightLight));

        canvas.drawRoundRect(rectF, widthLight / 16, heightLight / 16, paintColor);

        Paint paintImage = new Paint();
        paintImage.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(source, 0, 0, paintImage);
        source.recycle();
        return output;
    }

//    @Override
//    public Bitmap transform(Bitmap source) {
//        /**
//         * 求出宽和高的哪个小
//         */
//        int size = Math.min(source.getWidth(), source.getHeight());
//
//        /**
//         * 求中心点
//         */
//        int x = (source.getWidth() - size) / 2;
//        int y = (source.getHeight() - size) / 2;
//
//        /**
//         * 生成BitMap
//         */
//        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
//        if (squaredBitmap != source) {
//            //释放
//            source.recycle();
//        }
//
//        /**
//         * 建立新的Bitmap
//         */
//        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());
//
//        /**
//         * 画布画笔
//         */
//        Canvas canvas = new Canvas(bitmap);
//        Paint paint = new Paint();
//
//        BitmapShader shader = new BitmapShader(squaredBitmap,
//                BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
//        paint.setShader(shader);
//        paint.setAntiAlias(true);
//
//        float r = size / 2f;
//        /**
//         * 画圆
//         */
//        canvas.drawCircle(r, r, r, paint);
//
//        squaredBitmap.recycle();
//        return bitmap;
//    }

    @Override
    public String key() {
        return "circle";
    }
}
