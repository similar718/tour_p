package cn.xmzt.www.mine.bean;

/**
 * @author tanlei
 * @date 2019/8/16
 * @describe
 */

public class ShareBean {

    /**
     * gmtCreator : 0
     * image : string
     * gmtModified : 2019-08-16T09:40:36.943Z
     * gmtModifiedId : 0
     * id : 0
     * gmtCreate : 2019-08-16T09:40:36.943Z
     * title : string
     * type : 0
     * content : string
     * url : string
     */
    private int gmtCreator;
    private String image;
    private String gmtModified;
    private int gmtModifiedId;
    private int id;
    private String gmtCreate;
    private String title;
    private int type;
    private String content;
    private String url;
    private boolean isCheck;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public void setGmtCreator(int gmtCreator) {
        this.gmtCreator = gmtCreator;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setGmtModified(String gmtModified) {
        this.gmtModified = gmtModified;
    }

    public void setGmtModifiedId(int gmtModifiedId) {
        this.gmtModifiedId = gmtModifiedId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getGmtCreator() {
        return gmtCreator;
    }

    public String getImage() {
        return image;
    }

    public String getGmtModified() {
        return gmtModified;
    }

    public int getGmtModifiedId() {
        return gmtModifiedId;
    }

    public int getId() {
        return id;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public String getTitle() {
        return title;
    }

    public int getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "ShareBean{" +
                "gmtCreator=" + gmtCreator +
                ", image='" + image + '\'' +
                ", gmtModified='" + gmtModified + '\'' +
                ", gmtModifiedId=" + gmtModifiedId +
                ", id=" + id +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
