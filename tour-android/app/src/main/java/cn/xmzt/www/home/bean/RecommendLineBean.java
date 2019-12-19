package cn.xmzt.www.home.bean;

import android.text.TextUtils;

import cn.xmzt.www.utils.MathUtil;

/**
 * 超值抢购
 * @author Averysk
 */
public class RecommendLineBean {
    /**
     * 出发地名字
     */
    private String departAreaName;
    /**
     * 出发日期
     */
    private String departTime;
    /**
     * 线路简介
     */
    private String intro;
    /**
     * 线路宣传语
     */
    private String language;
    /**
     * 线路id
     */
    private int lineId;
    /**
     * 线路名字
     */
    private String lineName;
    /**
     * 最小价格
     */
    private double minPrice;
    /**
     * 线路封面图片
     */
    private String photoUrl;
    /**
     * 满意度
     */
    private int satisfied;
    private int lineType;//线路类型：1自驾游（自由出行） 2跟团游（跟团自驾）
    private String officeId; //服务机构id
    private String officeName;// 服务机构名称
    public String getDepartAreaName() {
        return departAreaName;
    }

    public void setDepartAreaName(String departAreaName) {
        this.departAreaName = departAreaName;
    }
    public String getDepartTimes() {
        if(!TextUtils.isEmpty(departTime)){
            return departTime.substring(0,10)+"出发";
        }
        return "";
    }
    public String getDepartTime() {
        return departTime;
    }

    public void setDepartTime(String departTime) {
        this.departTime = departTime;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getLineId() {
        return lineId;
    }

    public void setLineId(int lineId) {
        this.lineId = lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }
    public String getMinPriceStr() {
        return MathUtil.formatDouble(minPrice,2);
    }
    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getSatisfied() {
        return satisfied;
    }

    public void setSatisfied(int satisfied) {
        this.satisfied = satisfied;
    }
    public int getLineType() {
        return lineType;
    }

    public void setLineType(int lineType) {
        this.lineType = lineType;
    }
    public String getLineTypeName() {
        if(lineType==1){
            return "自由出行";
        }else if(lineType==2){
            return "跟团自驾";
        }
        return "";
    }
    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getOfficeName() {
        return officeName;
    }
    public String getOfficeNames() {
        if(!TextUtils.isEmpty(officeName)){
            return "由"+officeName+"提供服务";
        }
        return "";
    }
    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }
}
