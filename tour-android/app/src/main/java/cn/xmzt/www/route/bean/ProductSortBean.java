package cn.xmzt.www.route.bean;

/**
 * 产品类别排序
 */

public class ProductSortBean {
    private String sortText;//排序文本说明
    private int sortType;//排序方式()
    private boolean isChoose;//是否选择

    public String getSortText() {
        return sortText;
    }

    public void setSortText(String sortText) {
        this.sortText = sortText;
    }

    public int getSortType() {
        return sortType;
    }

    public void setSortType(int sortType) {
        this.sortType = sortType;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }
}
