package com.cuihai.base.widget;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.cuihai.base.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;

/**
 * @author: cuihai
 * @description: 类描述
 * @date: 2019/12/23
 * @email: nicech6@163.com
 */
public class GlideUtil {
    private static GlideUtil instance;

    private GlideUtil() {
    }

    public static GlideUtil getInstance() {
        if (instance == null) {
            instance = new GlideUtil();
        }
        return instance;
    }

    @BindingAdapter({"android:displayRound", "android:radius"})
    public static void displayRound(ImageView imageView, String imageUrl, int radius) {
        RequestOptions myOptions = new RequestOptions()
                .transforms(new CenterCrop(), new RoundBitmapTranformation(radius))
                .placeholder(R.color.config_color_bg_f7)
                .error(R.color.config_color_bg_f7);
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .apply(myOptions)
                .into(imageView);
    }
}
