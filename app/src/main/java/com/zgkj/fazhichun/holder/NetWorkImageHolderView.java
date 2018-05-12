package com.zgkj.fazhichun.holder;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.squareup.picasso.Picasso;
import com.zgkj.factory.model.api.home.Banner;
import com.zgkj.fazhichun.R;

/**
 * 网络图片加载
 */
public class NetWorkImageHolderView implements Holder<Banner> {

    private ImageView imageView;
    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，不一定是Image，任何控件都可以进行翻页
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, Banner data) {
        Picasso.get().load(data.getCarousel_path()).placeholder(R.drawable.ic_shop_bg)
                .into(imageView);

    }
}
