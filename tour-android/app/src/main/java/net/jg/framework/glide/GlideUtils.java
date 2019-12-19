package net.jg.framework.glide;

import android.content.Context;
import androidx.fragment.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ViewTarget;
import cn.xmzt.www.R;

/**
 * @项目名称： trunk
 * @类 名:  com.zdream.base.util
 * @类 描 述:
 * @创建时间: 2019/1/14 4:12 PM
 * @作 者:  Arthur
 * @修改内容：
 * @修改时间：
 * @修 改 人:
 * Copyright (C) 2014 Zoomingit Technology Co.,Ltd All Rights Reserved.
 * 本软件为深圳造梦信息技术有限公司开发研制。未经本公司正式书面同意，
 * 其他任何个人、团体不得使 用、复制、修改或发布本软件.
 */
public class GlideUtils {

    public static void load(Context context,
                            int placeHolder,
                            int errorImg,
                            String url,
                            int width,
                            int height,
                            int scaleType,
                            Transformation transformation,
                            ImageView imageView) {
        RequestOptions options = new RequestOptions();
        if (placeHolder > 0) {
            options.placeholder(placeHolder);
        }
        if (errorImg > 0) {
            options.error(errorImg);
        }
        if (width > 0 && height > 0) {
            options.override(width, height);
        } else if (width > 0 && height == 0) {
            options.override(width);
        }
        if (scaleType == 0) {
            options.centerCrop();
        } else if (scaleType == 1) {
            options.centerInside();
        } else if (scaleType == 2) {
            options.fitCenter();
        }
        if (transformation != null) {
            options.transform(transformation);
        }

        Glide.with(context).load(url).apply(options).into(imageView);
    }

    public static void load(Context context,
                            int placeHolder,
                            int errorImg,
                            String url,
                            int width,
                            int height,
                            int scaleType,
                            Transformation transformation,
                            ViewTarget viewTarget) {
        RequestOptions options = new RequestOptions();
        if (placeHolder > 0) {
            options.placeholder(placeHolder);
        }
        if (errorImg > 0) {
            options.error(errorImg);
        }
        if (width > 0 && height > 0) {
            options.override(width, height);
        } else if (width > 0 && height == 0) {
            options.override(width);
        }
        if (scaleType == 0) {
//            options.centerCrop();
            options.transform(new CenterCrop());
        } else if (scaleType == 1) {
//            options.centerInside();
            options.transform(new CenterInside());
        } else if (scaleType == 2) {
//            options.fitCenter();
            options.transform(new FitCenter());
        }

        if (transformation != null) {
            options.transform(transformation);
        }

        Glide.with(context).asBitmap().load(url).apply(options).into(viewTarget);
    }

    public static void load(Fragment fragment,
                            int placeHolder,
                            int errorImg,
                            String url,
                            int width,
                            int height,
                            int scaleType,
                            Transformation transformation,
                            ImageView imageView) {
        RequestOptions options = new RequestOptions();
        if (placeHolder > 0) {
            options.placeholder(placeHolder);
        }
        if (errorImg > 0) {
            options.error(errorImg);
        }
        if (width > 0 && height > 0) {
            options.override(width, height);
        } else if (width > 0 && height == 0) {
            options.override(width);
        }
        if (scaleType == 0) {
//            options.centerCrop();
            options.transform(new CenterCrop());
        } else if (scaleType == 1) {
//            options.centerInside();
            options.transform(new CenterInside());
        } else if (scaleType == 2) {
//            options.fitCenter();
            options.transform(new FitCenter());
        }

        if (transformation != null) {
            options.transform(transformation);
        }

        Glide.with(fragment).load(url).apply(options).into(imageView);
    }

    public static void load(Fragment fragment,
                            int placeHolder,
                            int errorImg,
                            String url,
                            int width,
                            int height,
                            int scaleType,
                            Transformation transformation,
                            ViewTarget viewTarget) {
        RequestOptions options = new RequestOptions();
        if (placeHolder > 0) {
            options.placeholder(placeHolder);
        }
        if (errorImg > 0) {
            options.error(errorImg);
        }
        if (width > 0 && height > 0) {
            options.override(width, height);
        } else if (width > 0 && height == 0) {
            options.override(width);
        }
        if (scaleType == 0) {
            options.transform(new CenterCrop());
        } else if (scaleType == 1) {
            options.transform(new CenterInside());
        } else if (scaleType == 2) {
            options.transform(new FitCenter());
        }
        if (transformation != null) {
            options.transform(transformation);
        }

        Glide.with(fragment).asBitmap().load(url).apply(options).into(viewTarget);
    }

    public static void loadHead(Context activity,
                                String url,
                                int width,
                                int scaleType,
                                Transformation transformation,
                                ImageView imageView) {
        load(activity, R.drawable.icon_head_default, R.drawable.icon_head_default,
                url, width, 0, scaleType, transformation, imageView);
    }

    public static void loadHead(Fragment fragment,
                                String url,
                                int width,
                                int scaleType,
                                Transformation transformation,
                                ImageView imageView) {
        load(fragment, R.drawable.icon_head_default, R.drawable.icon_head_default,
                url, width, 0, scaleType, transformation, imageView);
    }

    public static void loadImage(Context activity,
                                 String url,
                                 int width,
                                 int height,
                                 int scaleType,
                                 Transformation transformation,
                                 ImageView imageView) {
        load(activity, R.drawable.img_default, R.drawable.img_default,
                url, width, height, scaleType, transformation, imageView);
    }

    public static void loadImage(Context activity,
                                 String url,
                                 int width,
                                 int height,
                                 Transformation transformation,
                                 ViewTarget viewTarget) {
        load(activity, R.drawable.img_default, R.drawable.img_default,
                url, width, height, 0, transformation, viewTarget);
    }


    public static void loadImage(Fragment fragment,
                                 String url,
                                 int width,
                                 int height,
                                 int scaleType,
                                 Transformation transformation,
                                 ImageView imageView) {
        load(fragment, R.drawable.img_default, R.drawable.img_default,
                url, width, height, scaleType, transformation, imageView);
    }

    public static void loadImage(Context activity,
                                 String url,
                                 int width,
                                 int height,
                                 Transformation transformation,
                                 ImageView imageView) {
        load(activity, R.drawable.img_default, R.drawable.img_default,
                url, width, height, 0, transformation, imageView);
    }

    public static void loadImage(Fragment fragment,
                                 String url,
                                 int width,
                                 int height,
                                 Transformation transformation,
                                 ImageView imageView) {
        load(fragment, R.drawable.img_default, R.drawable.img_default,
                url, width, height, 0, transformation, imageView);
    }


    public static void loadImage(Context activity,
                                 String url,
                                 int width,
                                 int height,
                                 ImageView imageView) {
        load(activity, R.drawable.img_default, R.drawable.img_default,
                url, width, height, 0, null, imageView);
    }

    public static void loadImage(Context activity,
                                 String url,
                                 ImageView imageView) {
        load(activity, R.drawable.img_default, R.drawable.img_default,
                url, 0, 0, 0, null, imageView);
    }

    public static void loadImage(Context activity,
                                 String url,
                                 int width,
                                 int height,
                                 ViewTarget viewTarget) {
        load(activity, R.drawable.img_default, R.drawable.img_default,
                url, width, height, 0, null, viewTarget);
    }

    public static void loadImage(Context activity,
                                 String url,
                                 ViewTarget viewTarget) {
        load(activity, R.drawable.img_default, R.drawable.img_default,
                url, 0, 0, 0, null, viewTarget);
    }

    public static void loadHead(Context activity,
                                String url,
                                int width,
                                int height,
                                ImageView imageView) {
        load(activity, R.drawable.icon_head_default, R.drawable.icon_head_default,
                url, width, height, 0, null, imageView);
    }

    public static void loadHead(Context activity,
                                String url,
                                int width,
                                int height,
                                ViewTarget viewTarget) {
        load(activity, R.drawable.icon_head_default, R.drawable.icon_head_default,
                url, width, height, 0, null, viewTarget);
    }

    public static void loadHead(Fragment fragment,
                                String url,
                                int width,
                                int height,
                                ImageView imageView) {
        load(fragment, R.drawable.icon_head_default, R.drawable.icon_head_default,
                url, width, height, 0, null, imageView);
    }

    public static void loadHead(Context activity,
                                String url,
                                ViewTarget viewTarget) {
        load(activity, R.drawable.icon_head_default, R.drawable.icon_head_default,
                url, 0, 0, 0, null, viewTarget);
    }

    public static void loadHead(Fragment fragment,
                                String url,
                                ImageView imageView) {
        load(fragment, R.drawable.icon_head_default, R.drawable.icon_head_default,
                url, 0, 0, 0, null, imageView);
    }

    public static void loadHead(Fragment fragment,
                                String url,
                                int width,
                                int height,
                                ViewTarget viewTarget) {
        load(fragment, R.drawable.icon_head_default, R.drawable.icon_head_default,
                url, width, height, 0, null, viewTarget);
    }

    public static void loadImage(Fragment fragment,
                                 String url,
                                 int width,
                                 int height,
                                 ImageView imageView) {
        load(fragment, R.drawable.img_default, R.drawable.img_default,
                url, width, height, 0, null, imageView);
    }

    public static void loadImage(Fragment fragment,
                                 String url,
                                 int width,
                                 int height,
                                 ViewTarget viewTarget) {
        load(fragment, R.drawable.img_default, R.drawable.img_default,
                url, width, height, 0, null, viewTarget);
    }

}
