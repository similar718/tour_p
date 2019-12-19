package cn.xmzt.www.selfdrivingtools.bean;

public class OfflineFileManagerBean {
    /**
     * id : 12056
     * scenicId : 7345
     * resUrl : https://xmzt-scenic.oss-cn-shanghai.aliyuncs.com/zip/7345.zip
     * resSize : 1886
     * scenicName : 西湾红树林公园
     * photoUrl : https://pf.xmzt.cn/img/20191009/c4a0f4c0c1ac4e8d8528f98621d9bc75.jpg
     */
    private int id;
    private long scenicId;
    private String resUrl;
    private int resSize;
    private String scenicName;
    private String photoUrl;
    private boolean isSelect = false;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getScenicId() {
        return scenicId;
    }

    public void setScenicId(long scenicId) {
        this.scenicId = scenicId;
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

    public String getScenicName() {
        return scenicName;
    }

    public void setScenicName(String scenicName) {
        this.scenicName = scenicName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
