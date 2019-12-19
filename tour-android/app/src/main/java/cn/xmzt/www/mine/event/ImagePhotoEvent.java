package cn.xmzt.www.mine.event;

/**
 * @author tanlei
 * @date 2019/8/17
 * @describe
 */

public class ImagePhotoEvent {
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ImagePhotoEvent() {
    }

    public ImagePhotoEvent(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
