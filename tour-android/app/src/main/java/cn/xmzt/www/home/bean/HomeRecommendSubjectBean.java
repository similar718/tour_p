package cn.xmzt.www.home.bean;

/**
 * 主题推荐实体
 * @author Averysk
 */
public class HomeRecommendSubjectBean {

    /**
     * id
     */
    private int id;
    /**
     * 推荐产品ID
     */
    private int subjectId;
    /**
     * 推荐主题名称
     */
    private String subjectName;
    /**
     * 推荐类型(1:线路,2:酒店,3:门票)
     */
    private int subjectType;
    /**
     * 推荐主题链接
     */
    private String subjectLink;
    /**
     * 图标url
     */
    private String iconUrl;
    /**
     * 展示位置(0:App首页，1:线路首页, 2:酒店首页, 3:门票首页)
     */
    private int showLocation;
    /**
     * 排序
     */
    private int sort;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(int subjectType) {
        this.subjectType = subjectType;
    }

    public String getSubjectLink() {
        return subjectLink;
    }

    public void setSubjectLink(String subjectLink) {
        this.subjectLink = subjectLink;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getShowLocation() {
        return showLocation;
    }

    public void setShowLocation(int showLocation) {
        this.showLocation = showLocation;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
