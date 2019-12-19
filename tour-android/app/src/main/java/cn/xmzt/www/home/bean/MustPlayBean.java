package cn.xmzt.www.home.bean;

/**
 * @author tanlei
 * @date 2019/8/22
 * @describe
 */

public class MustPlayBean {

    /**
     * photoUrl : http://s1.lvjs.com.cn/uploads/pc/place2/2015-02-16/04943ae2-3112-4fbe-9b37-adef49a31401_480_320.jpg
     * minPrice : 100
     * lineName : 成都集合3日稻城-西藏自驾游-中秋佳节
     * language : 线路宣传语
     * id : 1
     */
    private String photoUrl;
    private int minPrice;
    private String lineName;
    private String language;
    private int id;
    private int lineType; //1、增加线路标签【跟团出行】/【自由出行】；2、增加线路服务提供商名称，如天天自驾旅行社，后台接口已完成，接口文档也已经更新了

    public int getLineType() {
        return lineType;
    }

    public void setLineType(int lineType) {
        this.lineType = lineType;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public String getLineName() {
        return lineName;
    }

    public String getLanguage() {
        return language;
    }

    public int getId() {
        return id;
    }
}
