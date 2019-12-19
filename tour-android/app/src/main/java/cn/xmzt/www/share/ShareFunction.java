package cn.xmzt.www.share;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.dialog.ShareDialog;
import cn.xmzt.www.glide.GlideApp;
import cn.xmzt.www.nim.uikit.common.util.media.BitmapUtil;
import cn.xmzt.www.utils.BitmapUtils;
import cn.xmzt.www.utils.NetWorkUtils;
import cn.xmzt.www.utils.ToastUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.media.UMusic;

/**
 * @author tanlei
 * @date 2019/8/20
 * @describe 友盟分享的封装类
 */

public class ShareFunction implements UMShareListener {
    /**
     * 分享到的平台
     */
    public static final SHARE_MEDIA SHARE_QQ = SHARE_MEDIA.QQ;//QQ
    public static final SHARE_MEDIA SHARE_QZONE = SHARE_MEDIA.QZONE;//QQ空间
    public static final SHARE_MEDIA SHARE_WEIXIN = SHARE_MEDIA.WEIXIN;//微信
    public static final SHARE_MEDIA SHARE_WEIXIN_CIRCLE = SHARE_MEDIA.WEIXIN_CIRCLE;//微信朋友圈
    public boolean shareing = false;

    private ShareFunction() {

    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {
        shareing=true;
        Log.d("ShareFunction","share onStart" );
    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {
        shareing=false;
        Log.d("ShareFunction","share onResult" );
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        shareing=false;
        Log.d("ShareFunction","share onError" );
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
        shareing=false;
        Log.d("ShareFunction","share onCancel" );
    }

    /**
     * 使用静态内部类不会再类加载的时候马上进行初始化操作
     */
    private static class Builder {
        private static ShareFunction shareFunction = new ShareFunction();
    }

    public static ShareFunction getInstance() {
        return Builder.shareFunction;
    }

    /**
     * 分享纯文本
     *
     * @param activity
     * @param text     文本内容
     * @param share    分享的平台
     */
    public void shareText(Activity activity, String text, SHARE_MEDIA share) {
        new ShareAction(activity).setPlatform(share).withText(text).setCallback(this).share();
    }

    /**
     * 分享图片
     *
     * @param activity
     * @param imageUrl 图片url
     * @param share
     */
    public void shareImage(Activity activity, String imageUrl, SHARE_MEDIA share) {
//        UMImage image = new UMImage(activity, "imageurl");//网络图片
//        UMImage image = new UMImage(activity, file);//本地文件
//        UMImage image = new UMImage(activity, R.drawable.xxx);//资源文件
//        UMImage image = new UMImage(activity, bitmap);//bitmap文件
//        UMImage image = new UMImage(activity, byte[]);//字节流
        UMImage image = new UMImage(activity, imageUrl);
        image.setThumb(image);
        //设置的图片大小最好不要超过250k，缩略图不要超过18k，如果超过太多（最好不要分享1M以上的图片，压缩效率会很低），图片会被压缩。可以设置压缩的方式：
//        image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
//        image.compressStyle = UMImage.CompressStyle.QUALITY;//质量压缩，适合长图的分享
        //压缩格式设置
//        image.compressFormat = Bitmap.CompressFormat.PNG;//用户分享透明背景的图片可以设置这种方式，但是qq好友，微信朋友圈，不支持透明背景图片，会变成黑色
        new ShareAction(activity).setPlatform(share).withMedia(image).setCallback(this).share();
    }

    /**
     * 分享图片
     *
     * @param activity
     * @param imageUrl 图片url
     * @param share
     */
    public void shareImage(Activity activity, Bitmap imageUrl, SHARE_MEDIA share) {
//        UMImage image = new UMImage(activity, "imageurl");//网络图片
//        UMImage image = new UMImage(activity, file);//本地文件
//        UMImage image = new UMImage(activity, R.drawable.xxx);//资源文件
//        UMImage image = new UMImage(activity, bitmap);//bitmap文件
//        UMImage image = new UMImage(activity, byte[]);//字节流
        UMImage image = new UMImage(activity, imageUrl);
        image.setThumb(image);
        //设置的图片大小最好不要超过250k，缩略图不要超过18k，如果超过太多（最好不要分享1M以上的图片，压缩效率会很低），图片会被压缩。可以设置压缩的方式：
//        image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
//        image.compressStyle = UMImage.CompressStyle.QUALITY;//质量压缩，适合长图的分享
        //压缩格式设置
//        image.compressFormat = Bitmap.CompressFormat.PNG;//用户分享透明背景的图片可以设置这种方式，但是qq好友，微信朋友圈，不支持透明背景图片，会变成黑色
        new ShareAction(activity).setPlatform(share).withMedia(image).setCallback(this).share();
    }

    /**
     * 分享web链接
     *
     * @param activity
     * @param url         链接
     * @param title       标题
     * @param imageUrl    缩略图
     * @param description 描述
     */
    public void shareWeb(Activity activity, String title, String imageUrl, String description, String url, SHARE_MEDIA share) {
        if(!NetWorkUtils.isNetConnected(activity)){
            ToastUtils.showText(activity,"请检测你的网络");
            return;
        }
        if(shareing){
            return;
        }
        UMImage image = new UMImage(activity, imageUrl);//网络图片
        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setThumb(image);  //缩略图
        web.setDescription(description);//描述
        new ShareAction(activity).setPlatform(share).withMedia(web).setCallback(this).share();
    }

    /**
     * 分享视频
     *
     * @param activity
     * @param videoUrl
     */
    public void shareVideo(Activity activity, String videoUrl, SHARE_MEDIA share) {
        UMVideo video = new UMVideo(videoUrl);
//        video.setTitle("This is music title");//视频的标题
//        video.setThumb("http://www.umeng.com/images/pic/social/chart_1.png");//视频的缩略图
//        video.setDescription("my description");//视频的描述
        new ShareAction(activity).setPlatform(share).withMedia(video).share();
    }


    /**
     * 分享音频
     *
     * @param activity
     * @param musicUrl
     */
    public void shareMusic(Activity activity, String musicUrl, SHARE_MEDIA share) {
        UMusic music = new UMusic(musicUrl);//音乐的播放链接
//        music.setTitle("This is music title");//音乐的标题
//        music.setThumb("http://www.umeng.com/images/pic/social/chart_1.png");//音乐的缩略图
//        music.setDescription("my description");//音乐的描述
//        music.setmTargetUrl(Defaultcontent.url);//音乐的跳转链接
        new ShareAction(activity).setPlatform(share).withMedia(music).share();
    }

    /**
     * 分享web链接
     *
     * @param activity
     * @param url         链接
     * @param title       标题
     * @param imageUrl    缩略图
     * @param description 描述
     */
    private ShareAction getWebShareAction(Activity activity, String title, String imageUrl, String description, String url) {
        UMImage image = new UMImage(activity, R.drawable.ic_travel_empty);//资源文件
        if (!TextUtils.isEmpty(imageUrl)) {
            image = new UMImage(activity, imageUrl);//网络图片
        }
        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setThumb(image);  //缩略图
        web.setDescription(description);//描述
        return new ShareAction(activity).withMedia(web);
    }

    private void share(int position, Activity activity, String title, String imageUrl, String description, String url) {
        ShareAction shareAction = new ShareAction(activity);
        switch (position) {
            case 0:
                shareAction.setPlatform(SHARE_MEDIA.WEIXIN);
                break;
            case 1:
                shareAction.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case 2:
                shareAction.setPlatform(SHARE_MEDIA.QQ);
                break;
            case 3:
                shareAction.setPlatform(SHARE_MEDIA.QZONE);
                break;
        }
        UMImage image = new UMImage(activity, R.drawable.ic_travel_empty);//资源文件
        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setThumb(image);  //缩略图
        web.setDescription(description);//描述
        if (!TextUtils.isEmpty(imageUrl)) {
            Glide.with(TourApplication.context).asBitmap().load(imageUrl).into(new SimpleTarget<Bitmap>(100, 100) {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    if(resource!=null){
                        UMImage image = new UMImage(TourApplication.context.getApplicationContext(),resource);
                        web.setThumb(image);  //缩略图
                    }
                    shareAction.withMedia(web).share();
                }
                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    super.onLoadFailed(errorDrawable);
                    shareAction.withMedia(web).share();
                }
            });
        }else {
            shareAction.withMedia(web).share();
        }
    }


    private void shareBitmap(int position, Activity activity, Bitmap bitmap) {
        ShareAction shareAction = new ShareAction(activity);
        switch (position) {
            case 0:
                shareAction.setPlatform(SHARE_MEDIA.WEIXIN);
                break;
            case 1:
                shareAction.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case 2:
                shareAction.setPlatform(SHARE_MEDIA.QQ);
                break;
            case 3:
                shareAction.setPlatform(SHARE_MEDIA.QZONE);
                break;
        }

        UMImage image = new UMImage(activity, bitmap);
        image.setThumb(image);
        shareAction.withMedia(image).share();
    }

    public void showWebShareAction(Activity activity, String title, String imageUrl, String description, String url, SHARE_MEDIA share) {
        ShareDialog dialog = new ShareDialog(activity);
        dialog.setListener(new ShareDialog.OnShareClickListener() {
            @Override
            public void onShareClick(int position) {
                share(position,activity,title,imageUrl,description,url);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void shareUrl(int position, Activity activity, String title, String imageUrl, String description, String url) {
        ShareAction shareAction = new ShareAction(activity);
        switch (position) {
            case 0:
                shareAction.setPlatform(SHARE_MEDIA.WEIXIN);
                break;
            case 1:
                shareAction.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case 2:
                shareAction.setPlatform(SHARE_MEDIA.QQ);
                break;
            case 3:
                shareAction.setPlatform(SHARE_MEDIA.QZONE);
                break;
        }

        UMImage image = new UMImage(activity, R.drawable.ic_travel_empty);//资源文件
        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setThumb(image);  //缩略图
        web.setDescription(description);//描述
        if (!TextUtils.isEmpty(imageUrl)) {
            Glide.with(TourApplication.context).asBitmap().load(imageUrl).into(new SimpleTarget<Bitmap>(100, 100) {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    if(resource!=null){
                        UMImage image = new UMImage(TourApplication.context.getApplicationContext(),resource);
                        web.setThumb(image);  //缩略图
                    }
                    shareAction.withMedia(web).share();
                }
                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    super.onLoadFailed(errorDrawable);
                    shareAction.withMedia(web).share();
                }
            });
        }else {
            shareAction.withMedia(web).share();
        }



    }

    public void showWebWeChatShareAction(Activity activity, String title, String imageUrl, String description, String url) {
        ShareDialog dialog = new ShareDialog(activity);
        dialog.setListener(new ShareDialog.OnShareClickListener() {
            @Override
            public void onShareClick(int position) {
                shareUrl(position,activity,title,imageUrl,description,url);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void showShareAction(Activity activity, Bitmap imageUrl) {
        ShareDialog dialog = new ShareDialog(activity);
        dialog.setListener(new ShareDialog.OnShareClickListener() {
            @Override
            public void onShareClick(int position) {
                shareBitmap(position,activity,imageUrl);
                dialog.dismiss();
            }
        });
        dialog.show();
//        UMImage image = new UMImage(activity, imageUrl);
//        image.setThumb(image);
//        ShareAction shareAction = new ShareAction(activity).withMedia(image);
//        shareAction.setDisplayList(SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)
//                .setCallback(new UMShareListener() {
//                    @Override
//                    public void onStart(SHARE_MEDIA share_media) {
//                    }
//
//                    @Override
//                    public void onResult(SHARE_MEDIA share_media) {
//                    }
//
//                    @Override
//                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
//                    }
//
//                    @Override
//                    public void onCancel(SHARE_MEDIA share_media) {
//                    }
//                }).open();
    }
}
