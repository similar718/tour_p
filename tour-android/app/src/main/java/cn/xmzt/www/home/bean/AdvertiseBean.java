package cn.xmzt.www.home.bean;

import android.text.TextUtils;

/**
 * 首页广告
 * @author Averysk
 */
public class AdvertiseBean {
    /**
     * 链接地址
     */
    private String advLink;
    /**
     * 图片地址
     */
    private String advPic;
    /**
     * 广告名称
     */
    private String homeName;
    /**
     * 链接类型：1图片 2视频 3网页
     */
    private int linkType;
    /**
     * 排序
     */
    private int sort;

    public String getAdvLink() {
        if(linkType==1&& TextUtils.isEmpty(advLink)){
            return advPic;
        }
        return advLink;
    }

    public void setAdvLink(String advLink) {
        this.advLink = advLink;
    }

    public String getAdvPic() {
        return advPic;
    }

    public void setAdvPic(String advPic) {
        this.advPic = advPic;
    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public int getLinkType() {
        return linkType;
    }

    public void setLinkType(int linkType) {
        this.linkType = linkType;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
