package cn.xmzt.www.route.bean;

/**
 * 产品列表中的地址筛选条件的地址
 */

public class SelectAddressBean {
    private String address;
    private boolean isSelect;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
