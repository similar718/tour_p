package cn.xmzt.www.route.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import cn.xmzt.www.R;
import cn.xmzt.www.glide.GlideApp;
import com.youth.banner.loader.ImageLoaderInterface;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * @describe 线路图片查看的图片加载器
 */
public class RoutePhotoViewAdapter implements ImageLoaderInterface{
    @Override
    public void displayImage(Context context, Object obj, View view) {
        PhotoView imageView=view.findViewById(R.id.image);
        imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                ActivityUtils.getTopActivity().onBackPressed();
            }

            @Override
            public void onOutsidePhotoTap() {
                ActivityUtils.getTopActivity().onBackPressed();
            }
        });
        GlideApp.with(context).load((String) obj).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }
    @Override
    public View createImageView(Context context) {
        View view =  LayoutInflater.from(context).inflate(R.layout.item_photo_view, null);
        return view;
    }
}
