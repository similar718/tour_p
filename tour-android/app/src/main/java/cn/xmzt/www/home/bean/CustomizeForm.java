package cn.xmzt.www.home.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 私人定制表单
 */
public class CustomizeForm implements Serializable {

    private CityArea departArea;//出发地
    private List<CityArea> arrivalArea;//目的地
    private String departDate;//出发时间
    private String endDate;//出发时间
    /**
     * 住宿类型：
     * 1酒店 2名宿
     */
    private int accommodationType=0;
    /**
     * 住宿等级：
     * 1一星（100-300元）
     * 2二星（300-500元）
     * 3三星（500-800元）
     * 4四星（800-1000元）
     * 5五星（1000元以上）
     */
    private int accommodationLevel;
    /**
     * 人均预算：
     * 1：无明确预算
     * 2：1000元以下
     * 3：1000-3000元
     * 4：3000-5000元
     * 5：5000-8000元
     * 6:8000-10000元
     * 7：10000-15000元
     * 8：15000元-20000元
     * 9：20000元以上
     */
    private int budgetNew=1;
    /**
     * 出行成人数
     */
    private int crNumber=2;//默认为2
    private int xhNumber;//出行儿童数
    private int groundType=1;//落地类型：1落地自驾 2自己爱车
    private Long id;
    private int state;//状态：0初始化 1定制中 2订制成功 3订制失败
    private String name;//联系人姓名
    private String remark;//备注
    private int tacticsType=1;//领队类型：1无需领队 2金牌领队
    private String tel;//联系人手机号

    public int getAccommodationType() {
        return accommodationType;
    }

    public void setAccommodationType(int accommodationType) {
        this.accommodationType = accommodationType;
    }

    public CityArea getDepartArea() {
        return departArea;
    }

    public void setDepartArea(CityArea departArea) {
        this.departArea = departArea;
    }

    public List<CityArea> getArrivalArea() {
        if(arrivalArea==null){
            arrivalArea=new ArrayList<>();
        }
        return arrivalArea;
    }

    public void setArrivalArea(List<CityArea> arrivalArea) {
        this.arrivalArea = arrivalArea;
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

    public int getAccommodationLevel() {
        return accommodationLevel;
    }

    public void setAccommodationLevel(int accommodationLevel) {
        this.accommodationLevel = accommodationLevel;
    }

    public int getBudgetNew() {
        return budgetNew;
    }

    public void setBudgetNew(int budgetNew) {
        this.budgetNew = budgetNew;
    }

    public int getCrNumber() {
        return crNumber;
    }

    public void setCrNumber(int crNumber) {
        this.crNumber = crNumber;
    }

    public int getXhNumber() {
        return xhNumber;
    }

    public void setXhNumber(int xhNumber) {
        this.xhNumber = xhNumber;
    }

    public int getGroundType() {
        return groundType;
    }

    public void setGroundType(int groundType) {
        this.groundType = groundType;
    }

    public Long getId() {
        return id;
    }

    public void setId(int Long) {
        this.id = id;
    }

    public String getName() {
        if(name==null){
            return "";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        if(remark==null){
            return "";
        }
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getTacticsType() {
        return tacticsType;
    }

    public void setTacticsType(int tacticsType) {
        this.tacticsType = tacticsType;
    }

    public String getTel() {
        if(tel==null){
            return "";
        }
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    /**
     * 城市区域
     */
    public static class CityArea {
        private String cityCode;
        private String cityName;
        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }
        public String getCityCode() {
            return cityCode;
        }
        public void setCityName(String cityName) {
            this.cityName = cityName;
        }
        public String getCityName() {
            return cityName;
        }
    }
}