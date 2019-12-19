package cn.xmzt.www.home.bean;

/**
 * @author tanlei
 * @date 2019/8/23
 * @describe
 */

public class ArticleDetailsBean {

    /**
     * area : string
     * image : string
     * gmtModified : 2019-08-23T03:15:36.638Z
     * clickCount : 0
     * title : string
     * content : string
     * url : string
     * minImage : string
     * areaId : string
     * des : string
     * subTitle : string
     * giveCountByUser : 0
     * id : 0
     * giveCount : 0
     */
    private String area;
    private String image;
    private String gmtModified;
    private int clickCount;
    private String title;
    private String content;
    private String url;
    private String minImage;
    private String areaId;
    private String des;
    private String subTitle;
    private int giveCountByUser;
    private int id;
    private int giveCount;

    public void setArea(String area) {
        this.area = area;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setGmtModified(String gmtModified) {
        this.gmtModified = gmtModified;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setMinImage(String minImage) {
        this.minImage = minImage;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public void setGiveCountByUser(int giveCountByUser) {
        this.giveCountByUser = giveCountByUser;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGiveCount(int giveCount) {
        this.giveCount = giveCount;
    }

    public String getArea() {
        return area;
    }

    public String getImage() {
        return image;
    }

    public String getGmtModified() {
        return gmtModified;
    }

    public int getClickCount() {
        return clickCount;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public String getMinImage() {
        return minImage;
    }

    public String getAreaId() {
        return areaId;
    }

    public String getDes() {
        return des;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public int getGiveCountByUser() {
        return giveCountByUser;
    }

    public int getId() {
        return id;
    }

    public int getGiveCount() {
        return giveCount;
    }
}
