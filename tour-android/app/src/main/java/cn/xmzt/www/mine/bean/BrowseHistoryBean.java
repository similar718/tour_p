package cn.xmzt.www.mine.bean;

/**
 * @author tanlei
 * @date 2019/8/14
 * @describe
 */

public class BrowseHistoryBean {

    /**
     * image : string
     * dataid : 0
     * price : 0
     * collectionCount : 0
     * name : string
     * id : 0
     * gmtCreate : string
     * type : 0
     * collectionId : 0
     */
    private String image;
    private int dataid;
    private int price;
    private int collectionCount;
    private String name;
    private int id;
    private String gmtCreate;
    private int type;
    private long collectionId;
    private boolean isCheck;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setDataid(int dataid) {
        this.dataid = dataid;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setCollectionCount(int collectionCount) {
        this.collectionCount = collectionCount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setCollectionId(long collectionId) {
        this.collectionId = collectionId;
    }

    public String getImage() {
        return image;
    }

    public int getDataid() {
        return dataid;
    }

    public int getPrice() {
        return price;
    }

    public int getCollectionCount() {
        return collectionCount;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public int getType() {
        return type;
    }

    public long getCollectionId() {
        return collectionId;
    }
}
