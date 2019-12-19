package cn.xmzt.www.mine.bean;

/**
 * @author tanlei
 * @date 2019/8/14
 * @describe
 */

public class MyOrderBean {

    /**
     * beginDate : string
     * number : 0
     * orderType : 0
     * endDate : string
     * ordState : 0
     * orderId : string
     * detailAddress : string
     * id : 0
     * pirceCount : 0
     * productCount : 0
     * orderName : string
     */
    private String beginDate;
    private int number;
    private int orderType;
    private String endDate;
    private int ordState;//订单状态(0:初始化,1.待支付、2.已取消、3.待确认、4.预定失败、6.待出行、7.退款中、8.退款失败、9.退款成功、10.已成交、11已关闭)
    private String orderId;
    private String detailAddress;
    private int id;
    private double pirceCount;
    private double productCount;
    private String orderName;
    private int refundState;//原始退款状态(1:退款中，2：退款失败，3：退款成功)
    private int state;//原始订单状态(0:初始化,10.待支付、30.已取消、40.待确认、50.预定失败、60.待出行、100.已成交, 110.已关闭)
    private int productId;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getRefundState() {
        return refundState;
    }

    public void setRefundState(int refundState) {
        this.refundState = refundState;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setOrdState(int ordState) {
        this.ordState = ordState;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPirceCount(double pirceCount) {
        this.pirceCount = pirceCount;
    }

    public void setProductCount(double productCount) {
        this.productCount = productCount;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public int getNumber() {
        return number;
    }

    public int getOrderType() {
        return orderType;
    }

    public String getEndDate() {
        return endDate;
    }

    public int getOrdState() {
        return ordState;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public int getId() {
        return id;
    }

    public double getPirceCount() {
        return pirceCount;
    }

    public double getProductCount() {
        return productCount;
    }

    public String getOrderName() {
        return orderName;
    }
}
