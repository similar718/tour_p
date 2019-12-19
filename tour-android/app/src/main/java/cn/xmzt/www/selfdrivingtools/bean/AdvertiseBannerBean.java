package cn.xmzt.www.selfdrivingtools.bean;

import android.text.TextUtils;

public class AdvertiseBannerBean {

    /**
     * homeName : 首页图片二
     * advPic : http://pf.xmzt.cn/scenic/th1.jpg
     * advLink : http://127.0.0.1:9005/home/homeAdvertise
     * sort : 1
     * linkType : 1
     */

    private String homeName;
    private String advPic;
    private String advLink;
    private int sort;
    private int linkType;

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public String getAdvPic() {
        return advPic;
    }

    public void setAdvPic(String advPic) {
        this.advPic = advPic;
    }

    public String getAdvLink() {
        if(linkType==1&& TextUtils.isEmpty(advLink)){
            return advPic;
        }
        return advLink;
    }

    public void setAdvLink(String advLink) {
        this.advLink = advLink;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getLinkType() {
        return linkType;
    }

    public void setLinkType(int linkType) {
        this.linkType = linkType;
    }
}
