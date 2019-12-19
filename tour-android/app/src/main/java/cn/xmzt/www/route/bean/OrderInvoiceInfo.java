package cn.xmzt.www.route.bean;

import java.io.Serializable;

public class OrderInvoiceInfo implements Serializable {
    private String drawer;//开票方
    private String dutyParagraph;//税号
    private String item;//开票项目
    private String receiveEmail;//接收邮箱
    private int sendTimes;//发送次数
    private String titleName;//抬头名称
    private int titleType;//抬头类型(1:公司,2:个人)

    public String getSendStatusName() {
        if(sendTimes>0){
            return "已发送 | 电子发票";
        }else {
            return "未发送 | 电子发票";
        }
    }
    public String getDrawer() {
        return drawer;
    }

    public void setDrawer(String drawer) {
        this.drawer = drawer;
    }

    public String getDutyParagraph() {
        return dutyParagraph;
    }
    public String getDutyParagraphStr() {
        return "税号："+dutyParagraph;
    }
    public void setDutyParagraph(String dutyParagraph) {
        this.dutyParagraph = dutyParagraph;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getReceiveEmail() {
        return receiveEmail;
    }

    public void setReceiveEmail(String receiveEmail) {
        this.receiveEmail = receiveEmail;
    }

    public int getSendTimes() {
        return sendTimes;
    }

    public void setSendTimes(int sendTimes) {
        this.sendTimes = sendTimes;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public int getTitleType() {
        return titleType;
    }
    public String getTitleTypeName() {
        if(titleType==1){
            return "公司";
        }
        return "个人";
    }

    public void setTitleType(int titleType) {
        this.titleType = titleType;
    }
}