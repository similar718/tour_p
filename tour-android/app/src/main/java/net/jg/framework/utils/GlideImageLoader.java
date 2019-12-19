package net.jg.framework.utils;

import android.content.Context;
import android.widget.ImageView;

import com.youth.banner.loader.ImageLoader;

import net.jg.framework.glide.GlideUtils;


/**
 * Created by Averysk on 2019/05/17.
 * Describe:提供Banner图片加载器
 */
public class GlideImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        GlideUtils.loadImage(context, (String) path, imageView);
    }
}
