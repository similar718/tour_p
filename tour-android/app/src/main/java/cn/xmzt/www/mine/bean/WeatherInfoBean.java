package cn.xmzt.www.mine.bean;

import android.text.TextUtils;

public class WeatherInfoBean {

    private String maxTemperature; // 最大温度
    private String minTemperature; // 最小温度（当前温度）
    private String icon; // 图标
    private String remark; // 备注
    private String situation; // 天气情况（文字）
    private String situationCode; // 天气情况（编码）
    private SysArea sysArea;
    private String areaName;//城市名称
    private int areaType;//城市类型：1当前城市 2目的地


    public String getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(String maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public String getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(String minTemperature) {
        this.minTemperature = minTemperature;
    }

    /**
     * 获取温度范围
     * @return
     */
    public String getTemperatureRange() {
        if(!TextUtils.isEmpty(maxTemperature)&&!TextUtils.isEmpty(minTemperature)){
            if(maxTemperature.equals(minTemperature)){
                return maxTemperature+"°";
            }else {
                return maxTemperature+"°/"+minTemperature+"°";
            }
        }else if(!TextUtils.isEmpty(maxTemperature)&&TextUtils.isEmpty(minTemperature)){
            return maxTemperature+"°";
        }else if(TextUtils.isEmpty(maxTemperature)&&!TextUtils.isEmpty(minTemperature)){
            return minTemperature+"°";
        }else {
            return maxTemperature+"--/"+minTemperature+"--";
        }
    }

    public String getIcon() {
        if(icon==null){
            return "";
        }
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public String getSituation() {
        if(situation==null){
            return "";
        }
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public String getSituationCode() {
        return situationCode;
    }

    public void setSituationCode(String situationCode) {
        this.situationCode = situationCode;
    }

    public SysArea getSysArea() {
        return sysArea;
    }

    public void setSysArea(SysArea sysArea) {
        this.sysArea = sysArea;
    }

    public String getAreaName() {
        if(areaName==null){
            return "";
        }
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public int getAreaType() {
        return areaType;
    }
    public String getAreaTypeName() {
        //城市类型：1当前城市 2目的地
        if(areaType==1){
            return "(当前城市)";
        }else if(areaType==2){
            return "(目的地)";
        }
        return "";
    }
    public void setAreaType(int areaType) {
        this.areaType = areaType;
    }

    public class SysArea{
        private String areaCode;
        private String areaName;
        private String areaStationCode;
        private int areaType;
        private String createBy;
        private String createDate;
        private String parentCode;
        private String parentCodes;
        private String remarks;
        private int status;
        private String treeLeaf;
        private double treeLevel;
        private String treeNames;
        private double treeSort;
        private String treeSorts;
        private String updateBy;
        private String updateDate;

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public String getAreaStationCode() {
            return areaStationCode;
        }

        public void setAreaStationCode(String areaStationCode) {
            this.areaStationCode = areaStationCode;
        }

        public int getAreaType() {
            return areaType;
        }

        public void setAreaType(int areaType) {
            this.areaType = areaType;
        }

        public String getCreateBy() {
            return createBy;
        }

        public void setCreateBy(String createBy) {
            this.createBy = createBy;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getParentCode() {
            return parentCode;
        }

        public void setParentCode(String parentCode) {
            this.parentCode = parentCode;
        }

        public String getParentCodes() {
            return parentCodes;
        }

        public void setParentCodes(String parentCodes) {
            this.parentCodes = parentCodes;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTreeLeaf() {
            return treeLeaf;
        }

        public void setTreeLeaf(String treeLeaf) {
            this.treeLeaf = treeLeaf;
        }

        public double getTreeLevel() {
            return treeLevel;
        }

        public void setTreeLevel(double treeLevel) {
            this.treeLevel = treeLevel;
        }

        public String getTreeNames() {
            return treeNames;
        }

        public void setTreeNames(String treeNames) {
            this.treeNames = treeNames;
        }

        public double getTreeSort() {
            return treeSort;
        }

        public void setTreeSort(double treeSort) {
            this.treeSort = treeSort;
        }

        public String getTreeSorts() {
            return treeSorts;
        }

        public void setTreeSorts(String treeSorts) {
            this.treeSorts = treeSorts;
        }

        public String getUpdateBy() {
            return updateBy;
        }

        public void setUpdateBy(String updateBy) {
            this.updateBy = updateBy;
        }

        public String getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(String updateDate) {
            this.updateDate = updateDate;
        }
    }
}
