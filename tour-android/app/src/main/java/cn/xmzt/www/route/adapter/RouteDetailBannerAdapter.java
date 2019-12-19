package cn.xmzt.www.route.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import cn.xmzt.www.R;
import cn.xmzt.www.glide.GlideApp;
import cn.xmzt.www.glide.GlideUtil;
import cn.xmzt.www.route.bean.RouteDetailPage;
import com.youth.banner.loader.ImageLoaderInterface;

/**
 * @describe 轮播图的图片加载器
 */
public class RouteDetailBannerAdapter implements ImageLoaderInterface {
    private int radius;
    public RouteDetailBannerAdapter() {
    }
    public RouteDetailBannerAdapter(int radius) {
        this.radius = radius;
    }
    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public void displayImage(Context context, Object obj, View view) {
        ImageView imageView=view.findViewById(R.id.iv_banner);
        ImageView ivPlay=view.findViewById(R.id.iv_play);
        String path="";
        ivPlay.setVisibility(View.GONE);
        if(obj instanceof RouteDetailPage.ResourceListBean){
            RouteDetailPage.ResourceListBean resource= (RouteDetailPage.ResourceListBean) obj;
            if("2".equals(resource.getType())){
                ivPlay.setVisibility(View.VISIBLE);
            }
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

    @Override
    public View createImageView(Context context) {
        View view =  LayoutInflater.from(context).inflate(R.layout.banner_img, null);
        return view;
    }
}
