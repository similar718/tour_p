package cn.xmzt.www.selfdrivingtools.bean;

/**
 * 一键救援
 */
public class AKeySOSBean {
    private int type;//紧急联系 1行程领队,2官方客服, 0免费救援
    private String title;
    private String  subTitle;
    private String phone;
    private int src;

    public AKeySOSBean(int type, String title, String phone) {
        this.type = type;
        this.title = title;
        this.phone = phone;
    }
    public AKeySOSBean(int type, String title,String subTitle, String phone,int src) {
        this.type = type;
        this.title = title;
        this.subTitle = subTitle;
        this.phone = phone;
        this.src = src;
    }

    public int getSrc() {
        return src;
    }

    public void setSrc(int src) {
        this.src = src;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
