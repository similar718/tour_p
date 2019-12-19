package cn.xmzt.www.bean;

import java.io.Serializable;

public class InvoiceTitleBean implements Serializable {
    private String bankCount;//银行账号
    private String depositBank;//开户银行
    private String dutyParagraph;//税号
    private String gmtCreate;//创建时间
    private int gmtCreator;//创建人
    private String gmtModified;//最后修改时间
    private int gmtModifiedId;//最后修改人ID
    private int id;//发票抬头id
    private String registrationAddress;//注册地址
    private String registrationPhone;//注册电话
    private int state;//状态(0:正常,1:删除)
    private String titleName;//抬头名称
    private int titleType;//抬头类型(1:公司,2:个人)
    private int userId;//用户ID
    private boolean isSelect;//是否选中
    public String getBankCount() {
        if(bankCount==null){
            return "";
        }
        return bankCount;
    }

    public void setBankCount(String bankCount) {
        this.bankCount = bankCount;
    }

    public String getDepositBank() {
        if(depositBank==null){
            return "";
        }
        return depositBank;
    }

    public void setDepositBank(String depositBank) {
        this.depositBank = depositBank;
    }

    public String getDutyParagraph() {
        if(dutyParagraph==null){
            return "";
        }
        return dutyParagraph;
    }
    public String getDutyParagraphStr() {
        return "税号："+dutyParagraph;
    }

    public void setDutyParagraph(String dutyParagraph) {
        this.dutyParagraph = dutyParagraph;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public int getGmtCreator() {
        return gmtCreator;
    }

    public void setGmtCreator(int gmtCreator) {
        this.gmtCreator = gmtCreator;
    }

    public String getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(String gmtModified) {
        this.gmtModified = gmtModified;
    }

    public int getGmtModifiedId() {
        return gmtModifiedId;
    }

    public void setGmtModifiedId(int gmtModifiedId) {
        this.gmtModifiedId = gmtModifiedId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegistrationAddress() {
        if(registrationAddress==null){
            return "";
        }
        return registrationAddress;
    }

    public void setRegistrationAddress(String registrationAddress) {
        this.registrationAddress = registrationAddress;
    }

    public String getRegistrationPhone() {
        if(registrationPhone==null){
            return "";
        }
        return registrationPhone;
    }

    public void setRegistrationPhone(String registrationPhone) {
        this.registrationPhone = registrationPhone;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTitleName() {
        if(titleName==null){
            return "";
        }
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public int getTitleType() {
        return titleType;
    }

    public void setTitleType(int titleType) {
        this.titleType = titleType;
    }
    public String getTitleTypeName() {
        if(titleType==1){//抬头类型(1:公司,2:个人)
            return "公司";
        }else {
            return "个人";
        }
    }
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}