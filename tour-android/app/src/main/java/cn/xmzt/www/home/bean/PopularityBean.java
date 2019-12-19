package cn.xmzt.www.home.bean;

/**
 * 人气必玩
 * @author Averysk
 */
public class PopularityBean {
    /**
     * 城市Code
     */
    private String citycode;
    /**
     * 城市名称
     */
    private String cityname;
    /**
     * 城市城市美图
     */
    private String pictures;
    /**
     * 类型(1：出发，2：落地，3：私人订制)
     */
    private int type;
    /**
     * 排序
     */
    private int sort;
    /**
     * 本地类型（0： 网络获取数据， 1： 本地自建数据）
     */
    private int typeLoad;
    /**
     * 本地图片资源id
     */
    private int picResourceId;

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getTypeLoad() {
        return typeLoad;
    }

    public void setTypeLoad(int typeLoad) {
        this.typeLoad = typeLoad;
    }

    public int getPicResourceId() {
        return picResourceId;
    }

    public void setPicResourceId(int picResourceId) {
        this.picResourceId = picResourceId;
    }
}
