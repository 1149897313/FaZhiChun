package com.zgkj.fazhichun.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;


/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/5/7.
 * Descr:
 */
public class LocalImageHolderView implements Holder<Integer> {

    private ImageView imageView;
    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，不一定是Image，任何控件都可以进行翻页
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, Integer data) {
        imageView.setImageResource(data);
    }
}
