package cn.xmzt.www.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import cn.xmzt.www.R;
import cn.xmzt.www.utils.DensityUtil;
import jp.wasabeef.glide.transformations.CropTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * 图片工具类
 */
public class GlideUtil {
    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView imageView, String url) {
        GlideApp.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.icon_default)
                .error(R.drawable.icon_default)
                .centerCrop()
                .into(imageView);
    }

    /**
     * 加载顶部圆角图片
     * @param imageView
     * @param url
     */
    @BindingAdapter({"imgTopCUrl"})
    public static void loadImgTopC(ImageView imageView,String url) {
        int radius= DensityUtil.dip2px(imageView.getContext(),4);
        RoundedCornersTransform transform = new RoundedCornersTransform(imageView.getContext(), radius);
        transform.setNeedCorner(true, true, false, false);
        GlideApp.with(imageView.getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(R.drawable.icon_default)
                .error(R.drawable.icon_default)
                .transform(transform)
                .into(imageView);
    }
    /**
     * 加载顶部圆角图片
     * @param imageView
     * @param url
     */
    @BindingAdapter({"imgLeftCUrl"})
    public static void loadImgLeftC(ImageView imageView,String url) {
        int radius= DensityUtil.dip2px(imageView.getContext(),4);
        RoundedCornersTransform transform = new RoundedCornersTransform(imageView.getContext(), radius);
        transform.setNeedCorner(true, false, true, false);
        GlideApp.with(imageView.getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(R.drawable.icon_default)
                .error(R.drawable.icon_default)
                .transform(transform)
                .into(imageView);
    }
    /**
     * 加载圆角图片
     * @param imageView
     * @param url
     */
    @BindingAdapter({"imgCUrl"})
    public static void loadImgC(ImageView imageView,String url) {
        int radius=DensityUtil.dip2px(imageView.getContext(),4);
        RoundedCornersTransform transform = new RoundedCornersTransform(imageView.getContext(), radius);
        transform.setNeedCorner(true, true, true, true);
        GlideApp.with(imageView.getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(R.drawable.icon_default)
                .error(R.drawable.icon_default)
                .transform(transform)
                .into(imageView);
    }
    /**
     * 加载圆角图片
     * @param imageView
     * @param url
     * @param radius 半径单位dp
     */
    public static void loadImgRoundRadius(ImageView imageView,String url,int radius) {
        int dpRadius=DensityUtil.dip2px(imageView.getContext(),radius);
        RoundedCornersTransformation transform=new RoundedCornersTransformation(dpRadius,0, RoundedCornersTransformation.CornerType.ALL);
        RequestOptions options = new RequestOptions();
        options.diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.drawable.icon_default)
                .error(R.drawable.icon_default)
                .transforms(new CenterCrop(),transform);
        Glide.with(imageView.getContext())
                .load(url)
                .apply(options)
                .into(imageView);
    }
    /**
     * 加载半径为8dp的圆角图片
     * @param imageView
     * @param url
     */
    @BindingAdapter({"imgCRadius8Url"})
    public static void loadImgCRadius8(ImageView imageView,String url) {
        int radius=DensityUtil.dip2px(imageView.getContext(),12);
        RoundedCornersTransform transform = new RoundedCornersTransform(imageView.getContext(), radius);
        transform.setNeedCorner(true, true, true, true);
        GlideApp.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.icon_default)
                .error(R.drawable.icon_default)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .transform(transform)
                .into(imageView);
    }
    /**
     * 加载圆图片
     * @param imageView
     * @param url
     */
    @BindingAdapter({"imgCircleUrl"})
    public static void loadImgCircle(ImageView imageView,String url) {
        GlideApp.with(imageView.getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.icon_default)
                .error(R.drawable.icon_default)
                .centerCrop()
                .circleCrop()
                .into(imageView);
    }
    /**
     * 加载圆角图片
     * @param imageView
     * @param radiusDp 圆角半径 单位dp
     * @param url 图片url
     */
    public static void loadImgRadius(ImageView imageView,int radiusDp,String url) {
        loadImgRoundRadius(imageView,url,radiusDp);
    }

    public static void loadImgRadius(ImageView imageView,int radiusDp,int url) {
        int radius=DensityUtil.dip2px(imageView.getContext(),radiusDp);
        RoundedCornersTransform transform = new RoundedCornersTransform(imageView.getContext(), radius);
        transform.setNeedCorner(true, true, true, true);
        GlideApp.with(imageView.getContext())
                .load(url).dontAnimate()
                .placeholder(R.drawable.icon_default)
                .error(R.drawable.icon_default)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .transform(transform)
                .into(imageView);
    }
    /**
     * 加载圆角图片
     * @param imageView
     * @param radiusDp 圆角半径 单位dp
     * @param url 图片url
     */
    public static void loadImgRadius(ImageView imageView,String url,int radiusDp,int w,int h) {
        int radius=DensityUtil.dip2px(imageView.getContext(),radiusDp);
        RoundedCornersTransform transform = new RoundedCornersTransform(imageView.getContext(), radius);
        transform.setNeedCorner(true, true, true, true);
        GlideApp.with(imageView.getContext())
                .load(url).dontAnimate()
                .placeholder(R.drawable.icon_default)
                .error(R.drawable.icon_default)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .transform(transform)
                .override(w,h)
                .into(imageView);
    }
    /**
     * 带圆角的图片
     *
     * @param avator
     * @param main_user_iv
     */
    public static void setImageCircle(String avator, ImageView main_user_iv, int errorPic) {
        if (avator != null) {
            if (avator.length() > 0) {
                //圆角处理
                GlideApp.with(main_user_iv.getContext())
                        .load(avator)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(errorPic)
                        .error(errorPic)
                        .centerCrop()
                        .circleCrop()
                        .into(main_user_iv);
            } else {
                main_user_iv.setImageResource(errorPic);
            }
        } else {
            main_user_iv.setImageResource(errorPic);
        }
    }
    /**
     * 带圆角的图片
     *
     * @param avator
     * @param main_user_iv
     */
    public static void setImageCircle(Context context,String avator, ImageView main_user_iv, int errorPic) {
        if (avator != null) {
            if (avator.length() > 0) {
                //圆角处理
                GlideApp.with(context)
                        .load(avator)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(errorPic)
                        .error(errorPic)
                        .centerCrop()
                        .circleCrop()
                        .into(main_user_iv);
            } else {
                main_user_iv.setImageResource(errorPic);
            }
        } else {
            main_user_iv.setImageResource(errorPic);
        }
    }

    /**
     * 不带圆角图片
     */
    public static void setImage(Context context,String avator, ImageView main_user_iv, int errorPic) {
        if (avator != null) {
            if (avator.length() > 0) {
                //圆角处理
                GlideApp.with(context)
                        .load(avator)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(errorPic)
                        .error(errorPic)
                        .centerCrop()
                        .into(main_user_iv);
            } else {
                main_user_iv.setImageResource(errorPic);
            }
        } else {
            main_user_iv.setImageResource(errorPic);
        }
    }

    /**
     * 不带圆角图片
     */
    public static void setImage(Context context,String avator, ImageView main_user_iv, int errorPic, int w, int h) {
        if (avator != null) {
            if (avator.length() > 0) {
                //圆角处理
                GlideApp.with(context)
                        .load(avator)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(errorPic)
                        .error(errorPic)
                        .override(w, h)
                        .centerCrop()
                        .into(main_user_iv);
            } else {
                main_user_iv.setImageResource(errorPic);
            }
        } else {
            main_user_iv.setImageResource(errorPic);
        }
    }
    /**
     * 不带圆角图片
     */
    public static void setImage(Context context, Bitmap mBitmap, ImageView main_user_iv, int errorPic) {
        //圆角处理
        GlideApp.with(context)
                .load(mBitmap)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(errorPic)
                .error(errorPic)
                .centerCrop()
                .into(main_user_iv);
    }
    public static void setImage(Context context,String avator, ImageView main_user_iv) {
        if (avator.length() > 0) {
            //圆角处理
            GlideApp.with(context)
                    .load(avator)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transition(new DrawableTransitionOptions().crossFade(1000))
                    .centerCrop()
                    .into(main_user_iv);
        }
    }
    public static void setImageTopC(Context context,int resourceId,int radius,String url, ImageView imgView) {
        if (url.length() > 0) {
           GlideApp.with(context)
                    .load(url).dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(resourceId)
                    .transform(new CropTransformation(radius,radius, CropTransformation.CropType.TOP))
                    .placeholder(resourceId)
                    .into(imgView);
        }
    }

    /**
     * 停止下载
     */
    public static void pauseRequests() {
//        GlideApp.with(MyApplication.getInstance()).pauseRequests();
    }

    /**
     * 恢复下载
     */
    public static void resumeRequests() {
        //GlideApp.with(MyApplication.getInstance()).resumeRequests();
    }
}
