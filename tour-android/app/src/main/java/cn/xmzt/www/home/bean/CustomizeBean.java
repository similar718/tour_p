package cn.xmzt.www.home.bean;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 私人定制
 */
public class CustomizeBean {
    private int id;//私人订制id
    private int accommodationType;//住宿类型：1酒店 2名宿
    private int accommodationLevel;//住宿等级：1一星（100-300元） 2二星（300-500元） 3三星（500-800元）4四星（800-1000元）5五星（1000元以上）
    private int groundType;//落地类型：1落地自驾 2自己爱车
    private int tacticsType;//领队类型：1无需领队 2金牌领队

    private String departArea;//出发地，数据格式：[{’citycode’:’110000’,’cityname’:’北京市’}]
    private String arrivalArea; //目的地，数据格式：[{’citycode’:’110000’,’cityname’:’北京市’}]
    private double budgetNew; //人均预算
    private int crNumber; //出行成人数
    private int delFlag; //是否删除：0正常 1删除
    private String departDate;//出发时间
    private String endDate;//返程时间
    private int days;//游玩天数
    private String gmtCreate;//最后修改时间
    private String gmtModified;//最后修改时间

    private int lineId;//订制成功后的线路id
    private String remark;//备注
    private int state;//状态：0初始化 1定制中 2订制成功 3订制失败
    private String name;//联系人姓名
    private String tel;//联系人手机号
    private int userId;//用户id
    private String wechat;//微信号
    private int xhNumber;//出行儿童数
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccommodationType() {
        return accommodationType;
    }

    public void setAccommodationType(int accommodationType) {
        this.accommodationType = accommodationType;
    }

    public int getAccommodationLevel() {
        return accommodationLevel;
    }

    public void setAccommodationLevel(int accommodationLevel) {
        this.accommodationLevel = accommodationLevel;
    }

    public int getGroundType() {
        return groundType;
    }

    public void setGroundType(int groundType) {
        this.groundType = groundType;
    }

    public int getTacticsType() {
        return tacticsType;
    }

    public void setTacticsType(int tacticsType) {
        this.tacticsType = tacticsType;
    }

    public String getArrivalArea() {
        return arrivalArea;
    }
    public void setArrivalArea(String arrivalArea) {
        this.arrivalArea = arrivalArea;
    }

    public double getBudgetNew() {
        return budgetNew;
    }

    public void setBudgetNew(double budgetNew) {
        this.budgetNew = budgetNew;
    }

    public int getCrNumber() {
        return crNumber;
    }

    public void setCrNumber(int crNumber) {
        this.crNumber = crNumber;
    }

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }

    public String getDepartArea() {
        return departArea;
    }

    public void setDepartArea(String departArea) {
        this.departArea = departArea;
    }

    public String getDepartDate() {
        return departDate;
    }

    public void setDepartDate(String departDate) {
        this.departDate = departDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public String getGmtCreate() {
        if(TextUtils.isEmpty(gmtCreate)){
            return departDate;
        }
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

    public int getLineId() {
        return lineId;
    }

    public void setLineId(int lineId) {
        this.lineId = lineId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getState() {
        return state;
    }
    public String getStateName() {
        if(state<=1){
            return "确认中";
        }else if(state==2){
            return "订制成功";
        }else if(state==3){
            return "订制失败";
        }
        return "定制中";
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public int getXhNumber() {
        return xhNumber;
    }

    public void setXhNumber(int xhNumber) {
        this.xhNumber = xhNumber;
    }
}