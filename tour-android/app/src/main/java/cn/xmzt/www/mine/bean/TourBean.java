package cn.xmzt.www.mine.bean;

/**
 * @author tanlei
 * @date 2019/8/14
 * @describe
 */

public class TourBean {

    /**
     * isDefault : 0
     * gender : 0
     * name : string
     * tel : string
     * id : 0
     * identityCard : string
     * isChildren : true
     * type : 0
     * certificateType : 0
     */
    private int isDefault;
    private int gender;
    private String name;
    private String tel;
    private int id;
    private String identityCard;
    private boolean isChildren;
    private int type;
    private int certificateType;

    public TourBean() {
    }
    public TourBean(int id,String name, String tel, String identityCard) {
        this.id = id;
        this.name = name;
        this.tel = tel;
        this.identityCard = identityCard;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public void setIsChildren(boolean isChildren) {
        this.isChildren = isChildren;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setCertificateType(int certificateType) {
        this.certificateType = certificateType;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public int getGender() {
        return gender;
    }

    public String getName() {
        return name;
    }

    public String getTel() {
        return tel;
    }

    public int getId() {
        return id;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public boolean isIsChildren() {
        return isChildren;
    }

    public int getType() {
        return type;
    }

    public int getCertificateType() {
        return certificateType;
    }
}
