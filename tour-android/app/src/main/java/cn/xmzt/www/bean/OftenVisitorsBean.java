package cn.xmzt.www.bean;

import java.io.Serializable;

/**
 * 常用出游人
 */
public class OftenVisitorsBean implements Serializable {
    private int id;//数据id
    private String name;//姓名
    private String tel;//手机号
    private String identityCard;//身份证号码
    private int certificateType;//证件类型：1身份证
    private int isDefault;//是否默认：0否 1是
    private int gender;//性别：0否 1是
    private boolean isChildren;//是否小孩

    private String gmtCreate;//创建时间
    private String gmtModified;//最后修改时间
    private int state;//状态(0:正常,1:删除)
    private int type;//类型：1常用出游人 2酒店入住人 3取票人
    private boolean isSelect;//是选择

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadTel() {
        return "手机号："+tel;
    }
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getIdentityCard() {
        return identityCard;
    }
    public String getHeadIdentityCard() {
        return "身份证："+identityCard;
    }
    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public int getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(int certificateType) {
        this.certificateType = certificateType;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public boolean isChildren() {
        return isChildren;
    }

    public String getAgeStageName() {
        if(isChildren){
            return "儿童";
        }
        return "成人";
    }

    public void setChildren(boolean children) {
        isChildren = children;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(String gmtModified) {
        this.gmtModified = gmtModified;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}