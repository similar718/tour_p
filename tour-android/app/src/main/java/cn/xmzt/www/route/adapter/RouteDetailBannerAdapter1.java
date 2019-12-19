package cn.xmzt.www.route.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import cn.xmzt.www.glide.GlideApp;
import cn.xmzt.www.glide.GlideUtil;
import cn.xmzt.www.route.bean.RouteDetailPage;
import com.youth.banner.loader.ImageLoader;

/**
 * @describe 轮播图的图片加载器
 */
public class RouteDetailBannerAdapter1 extends ImageLoader {
    private int radius;
    public RouteDetailBannerAdapter1() {
    }
    public RouteDetailBannerAdapter1(int radius) {
        this.radius = radius;
    }
    public void setRadius(int radius) {
        this.radius = radius;
    }
    
    @Override
    public void displayImage(Context context, Object obj, ImageView imageView) {
        String path="";
        if(obj instanceof RouteDetailPage.ResourceListBean){
            RouteDetailPage.ResourceListBean resource= (RouteDetailPage.ResourceListBean) obj;
            path=resource.getThumb();
        }else if(obj instanceof String){
            //Glide 加载图片简单用法
            path= (String) obj;
        }
        if(radius>0){
            GlideUtil.loadImgRadius(imageView,radius,path);
        }else {
            GlideApp.with(context).load(path).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        }

    }
}
