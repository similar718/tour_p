
package cn.xmzt.www.route.bean;

import java.util.List;

/**
 * 线路订单
 */
public class LineOrderForm {

    private ContactForm bookPeople;//联系人
    private List<CarForm> carList;//车辆信息
    private List<CostForm> cost;//费用
    private String couponId;//优惠券id
    private String departDate;//出发日期
    private OrderInvoiceForm invoice;//发票
    private int openInvoice;//是否开发票(0:不开，1：开)
    private int isBuyInsurance;//是否购买保险 1 是 2 否
    private int lineId;//线路id

    public ContactForm getBookPeople() {
        return bookPeople;
    }

    public void setBookPeople(ContactForm bookPeople) {
        this.bookPeople = bookPeople;
    }

    public List<CarForm> getCarList() {
        return carList;
    }

    public void setCarList(List<CarForm> carList) {
        this.carList = carList;
    }

    public List<CostForm> getCost() {
        return cost;
    }

    public void setCost(List<CostForm> cost) {
        this.cost = cost;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getDepartDate() {
        return departDate;
    }

    public void setDepartDate(String departDate) {
        this.departDate = departDate;
    }

    public OrderInvoiceForm getInvoice() {
        return invoice;
    }

    public void setInvoice(OrderInvoiceForm invoice) {
        this.invoice = invoice;
    }

    public int getIsBuyInsurance() {
        return isBuyInsurance;
    }

    public void setIsBuyInsurance(int isBuyInsurance) {
        this.isBuyInsurance = isBuyInsurance;
    }

    public int getOpenInvoice() {
        return openInvoice;
    }

    public void setOpenInvoice(int openInvoice) {
        this.openInvoice = openInvoice;
    }
    public int getLineId() {
        return lineId;
    }

    public void setLineId(int lineId) {
        this.lineId = lineId;
    }
}