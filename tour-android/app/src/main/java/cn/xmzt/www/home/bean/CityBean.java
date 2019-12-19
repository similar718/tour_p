package cn.xmzt.www.home.bean;

/**
 * 城市
 */
public class CityBean {
    private String areaCode;
    private String parentCode;
    private String treeNames;
    private String areaName;
    private String areaNamePinYin;
    private int areaType;//1表示省 2市 3区
    private boolean isSelect;

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getTreeNames() {
        return treeNames;
    }

    public void setTreeNames(String treeNames) {
        this.treeNames = treeNames;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaNamePinYin() {
        if(areaNamePinYin==null){
           return "";
        }
        return areaNamePinYin;
    }

    public void setAreaNamePinYin(String areaNamePinYin) {
        this.areaNamePinYin = areaNamePinYin;
    }

    public int getAreaType() {
        return areaType;
    }

    public void setAreaType(int areaType) {
        this.areaType = areaType;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
