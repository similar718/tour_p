package cn.xmzt.www.mine.bean;

/**
 * @author tanlei
 * @date 2019/8/13
 * @describe
 */

public class CollectionBean {

    /**
     * image : string
     * price : 0
     * name : string
     * id : 0
     * gmtCreate : string
     * type : 0
     */
    private String image;
    private double price;
    private String name;
    private int id;
    private String gmtCreate;
    private int type;
    private boolean isCheck;//是否被选中
    private int dataid;

    public int getDataid() {
        return dataid;
    }

    public void setDataid(int dataid) {
        this.dataid = dataid;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPrice(double price) {
        this.price = price;
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

    public String getImage() {
        return image;
    }

    public double getPrice() {
        return price;
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
}
