package cn.xmzt.www.mine.bean;

import android.graphics.Bitmap;

import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * @author tanlei
 * @date 2019/9/2
 * @describe
 */

public class ShapeBitmapBean {
    private Bitmap bitmap;
    private String url;
    private SHARE_MEDIA media;

    public ShapeBitmapBean() {
    }

    public ShapeBitmapBean(Bitmap bitmap, String url, SHARE_MEDIA media) {
        this.bitmap = bitmap;
        this.url = url;
        this.media = media;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public SHARE_MEDIA getMedia() {
        return media;
    }

    public void setMedia(SHARE_MEDIA media) {
        this.media = media;
    }
}
