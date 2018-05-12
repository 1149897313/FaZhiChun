package com.zgkj.common.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.zgkj.common.R;
import com.zgkj.common.glide.GlideApp;

import java.util.Random;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/14.
 * Descr:   glide加载图片的辅助工具类
 */

public class GlideUtil {

    // 定义颜色的数组
    private static int[] mDrawableResId = new int[]{
            R.drawable.ic_drawable_purple_100,
            R.drawable.ic_drawable_teal_100,
            R.drawable.ic_drawable_brown_100,
            R.drawable.ic_drawable_orange_100,
            R.drawable.ic_drawable_light_green_100,
            R.drawable.ic_drawable_blue_100,
            R.drawable.ic_drawable_light_blue_100,
            R.drawable.ic_drawable_amber_100};

    /**
     * 私有化的构造方法，禁止通过new的方式创建对象
     */
    private GlideUtil(){

        // 抛出不支持的操作异常
        throw new UnsupportedOperationException("cannot be instantiated!");
    }


    /**
     * 加载图片
     *
     * @param context
     * @param path
     * @param imageView
     */
    public static void loadImage(Context context, String path, ImageView imageView){
        GlideApp.with(context)
                .load(path)
                .centerCrop()
                .placeholder(mDrawableResId[getIntRandom(mDrawableResId.length)])
                .error(mDrawableResId[getIntRandom(mDrawableResId.length)])
                .transition(new DrawableTransitionOptions().crossFade(200))
                .into(imageView);
    }




    /**
     * 返回一个int类型的随机数
     *
     * @param bound     随机数的产生返回
     * @return
     */
    private static int getIntRandom(int bound){
        Random random = new Random();
        return random.nextInt(bound);
    }











}
