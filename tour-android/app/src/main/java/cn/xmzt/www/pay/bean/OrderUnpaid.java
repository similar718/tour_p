package cn.xmzt.www.pay.bean;

import cn.xmzt.www.route.bean.ContactForm;
import cn.xmzt.www.route.bean.OrderInvoiceInfo;
import cn.xmzt.www.utils.MathUtil;
import cn.xmzt.www.utils.TimeUtil;

/**
 * 待支付订单
 */
public class OrderUnpaid {
    private int id;//订单id
    private String orderId;//订单号
    private String orderName;//订单名称
    private int orderState;//订单状态(0:初始化,10.待支付、20.订单超时、30.已取消、40.待确认、50.预定失败、60.待出行、100.已成交(已完成)、110.已关闭)
    private int refundState;//退款状态(1.退款中、2.退款失败、3.退款成功)(只有待出行才有退款状态)
    private double orderAmount;//订单金额
    private double discountAmount;//优惠金额
    private String departDate;//游玩/行程开始/入住时间
    private String endDate;//行程结束/离店时间
    private String orderCreateTime;//订单创建时间
    private ContactForm contact;
    private int singleRoomQuantity;//单房差
    private OrderInvoiceInfo invoice;
    private int openInvoice;//是否开发票(0:不需要发票,1:电子发票)
    private String ticketCode;//取票码
    private long currentTimestamp;//当前时间戳
    private long expireTimestamp;//到期时间戳
    public final String cny="¥";
    public final String onlinePay="(在线支付)";
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    public int getRefundState() {
        return refundState;
    }

    public void setRefundState(int refundState) {
        this.refundState = refundState;
    }

    public double getOrderAmount() {
        return orderAmount;
    }
    public String getOrderAmountFormat() {
        return MathUtil.formatDouble(orderAmount,2);
    }

    public void setOrderAmount(double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getDepartDate() {
        return departDate;
    }

    public void setDepartDate(String departDate) {
        this.departDate = departDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(String orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public ContactForm getContact() {
        return contact;
    }

    public void setContact(ContactForm contact) {
        this.contact = contact;
    }

    public int getSingleRoomQuantity() {
        return singleRoomQuantity;
    }

    public void setSingleRoomQuantity(int singleRoomQuantity) {
        this.singleRoomQuantity = singleRoomQuantity;
    }

    public OrderInvoiceInfo getInvoice() {
        return invoice;
    }

    public void setInvoice(OrderInvoiceInfo invoice) {
        this.invoice = invoice;
    }

    public int getOpenInvoice() {
        return openInvoice;
    }

    public void setOpenInvoice(int openInvoice) {
        this.openInvoice = openInvoice;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public long getCurrentTimestamp() {
        return currentTimestamp;
    }

    public void setCurrentTimestamp(long currentTimestamp) {
        this.currentTimestamp = currentTimestamp;
    }

    public long getExpireTimestamp() {
        return expireTimestamp;
    }

    public void setExpireTimestamp(long expireTimestamp) {
        this.expireTimestamp = expireTimestamp;
    }
    public String getRemainTime() {
        long second=(expireTimestamp-currentTimestamp)/1000;
        return  TimeUtil.change(second);
    }

}
