package cn.xmzt.www.selfdrivingtools.bean;

public class ScenicVoicePackageBean {

    /**
     * id : 308
     * scenicId : 14908
     * resType : 1
     * resUrl : http://pf.tour.com/scenic/officeline/package/14908.zip
     * resSize : 1579202
     * version : 100
     */

    private int id;
    private int scenicId;
    private int resType;
    private String resUrl;
    private int resSize;
    private String version;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScenicId() {
        return scenicId;
    }

    public void setScenicId(int scenicId) {
        this.scenicId = scenicId;
    }

    public int getResType() {
        return resType;
    }

    public void setResType(int resType) {
        this.resType = resType;
    }

    public String getResUrl() {
        return resUrl;
    }

    public void setResUrl(String resUrl) {
        this.resUrl = resUrl;
    }

    public int getResSize() {
        return resSize;
    }

    public void setResSize(int resSize) {
        this.resSize = resSize;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
