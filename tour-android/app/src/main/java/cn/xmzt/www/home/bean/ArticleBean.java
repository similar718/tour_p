package cn.xmzt.www.home.bean;

import android.text.TextUtils;

/**
 * 首页广告
 * @author Averysk
 */
public class ArticleBean {
    /**
     * 数据id
     */
    private int id;
    /**
     * 位置id
     */
    private String areaId;
    /**
     * 位置
     */
    private String area;
    /**
     * 点击量
     */
    private int clickCount;
    /**
     * 内容
     */
    private String content;
    /**
     * 简述
     */
    private String des;
    /**
     * 点赞量
     */
    private int giveCount;
    /**
     * 最后修改时间
     */
    private String gmtModified;
    /**
     * 主题图片（大图）
     */
    private String image;
    /**
     * 主题图片（小图）
     */
    private String minImage;
    /**
     * 标题
     */
    private String title;
    /**
     * 子标题
     */
    private String subTitle;
    /**
     * 链接
     */
    private String url;
    /**
     * 线路类型
     */
    private int lineType;

    public int getLineType() {
        return lineType;
    }

    public void setLineType(int lineType) {
        this.lineType = lineType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getGiveCount() {
        return giveCount;
    }
    public String getGiveCountStr() {
        return giveCount+"";
    }

    public void setGiveCount(int giveCount) {
        this.giveCount = giveCount;
    }

    public String getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(String gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getImage() {
        if(image==null){
            return "";
        }
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMinImage() {
        if(TextUtils.isEmpty(minImage)){
            minImage=image;
        }
        return minImage;
    }

    public void setMinImage(String minImage) {
        this.minImage = minImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
