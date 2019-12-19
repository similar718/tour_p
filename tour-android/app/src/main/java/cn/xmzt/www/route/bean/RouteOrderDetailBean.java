package cn.xmzt.www.route.bean;

import cn.xmzt.www.bean.BaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 线路订单详情
 */
public class RouteOrderDetailBean extends BaseBean {
    private OrderBean head;
    private ItemBean item;

    public OrderBean getHead() {
        if(head==null){
            head=new OrderBean();
        }
        return head;
    }

    public void setHead(OrderBean head) {
        this.head = head;
    }

    public ItemBean getItem() {
        if(item==null){
            item=new ItemBean();
        }
        return item;
    }

    public void setItem(ItemBean item) {
        this.item = item;
    }

    public static class OrderBean {
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
        private List<InsuranceCost> orderGuestInsurances;//保险费用列表
        private ContactForm contact;
        private int singleRoomQuantity;//单房差
        private OrderInvoiceInfo invoice;
        private int openInvoice;//是否开发票(0:不需要发票,1:电子发票)
        private String ticketCode;//取票码
        private long currentTimestamp;//当前时间戳
        private long expireTimestamp;//到期时间戳
        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }
        public String getOrderId() {
            return orderId;
        }

        public void setOrderName(String orderName) {
            this.orderName = orderName;
        }
        public String getOrderName() {
            return orderName;
        }

        public void setOrderState(int orderState) {
            this.orderState = orderState;
        }
        public int getOrderState() {
            return orderState;
        }
        public String getOrderStateName() {
            if(orderState==0){
                return "初始化";
            }else if(orderState==10){
                return "待支付";
            }else if(orderState==20){
                return "订单超时";
            }else if(orderState==30){
                return "已取消";
            }else if(orderState==40){
                return "待确认";
            }else if(orderState==50){
                return "预定失败";
            }else if(orderState==60){
                return "待出行";
            }else if(orderState==100){
                return "已完成";
            }else if(orderState==110){
                return "已关闭";
            }
            return "";
        }

        public int getRefundState() {
            return refundState;
        }

        public void setRefundState(int refundState) {
            this.refundState = refundState;
        }

        public void setOrderAmount(double orderAmount) {
            this.orderAmount = orderAmount;
        }
        public double getOrderAmount() {
            return orderAmount;
        }

        public void setDiscountAmount(double discountAmount) {
            this.discountAmount = discountAmount;
        }
        public double getDiscountAmount() {
            return discountAmount;
        }

        public void setDepartDate(String departDate) {
            this.departDate = departDate;
        }
        public String getDepartDate() {
            return departDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }
        public String getEndDate() {
            return endDate;
        }

        public void setOrderCreateTime(String orderCreateTime) {
            this.orderCreateTime = orderCreateTime;
        }
        public String getOrderCreateTime() {
            return orderCreateTime;
        }

        public List<InsuranceCost> getOrderGuestInsurances() {
            if(orderGuestInsurances==null){
                orderGuestInsurances=new ArrayList<>();
            }
            return orderGuestInsurances;
        }

        public void setOrderGuestInsurances(List<InsuranceCost> orderGuestInsurances) {
            this.orderGuestInsurances = orderGuestInsurances;
        }

        public void setContact(ContactForm contact) {
            this.contact = contact;
        }
        public ContactForm getContact() {
            return contact;
        }

        public void setSingleRoomQuantity(int singleRoomQuantity) {
            this.singleRoomQuantity = singleRoomQuantity;
        }
        public int getSingleRoomQuantity() {
            return singleRoomQuantity;
        }

        public OrderInvoiceInfo getInvoice() {
            if(invoice==null){
                invoice=new OrderInvoiceInfo();
            }
            return invoice;
        }

        public void setInvoice(OrderInvoiceInfo invoice) {
            this.invoice = invoice;
        }

        public void setOpenInvoice(int openInvoice) {
            this.openInvoice = openInvoice;
        }
        public int getOpenInvoice() {
            return openInvoice;
        }

        public String getTicketCode() {
            return ticketCode;
        }

        public void setTicketCode(String ticketCode) {
            this.ticketCode = ticketCode;
        }

        public void setCurrentTimestamp(long currentTimestamp) {
            this.currentTimestamp = currentTimestamp;
        }
        public long getCurrentTimestamp() {
            return currentTimestamp;
        }

        public void setExpireTimestamp(long expireTimestamp) {
            this.expireTimestamp = expireTimestamp;
        }
        public long getExpireTimestamp() {
            return expireTimestamp;
        }
    }
    public static class ItemBean {
        private Common common;
        private Product product;

        public Common getCommon() {
            if(common==null){
                common=new Common();
            }
            return common;
        }

        public void setCommon(Common common) {
            this.common = common;
        }

        public Product getProduct() {
            if(product==null){
                product=new Product();
            }
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }
    }
    public static class Common {
        private int productId;
        private int productType;
        private String productName;
        private String photoUrl;
        private String endDate;//行程结束/离店时间
        private String departDate;//游玩/行程开始/入住时间
        private String orderCreateTime;//订单创建时间
        private int productNum;
        public void setProductId(int productId) {
            this.productId = productId;
        }
        public int getProductId() {
            return productId;
        }

        public void setProductType(int productType) {
            this.productType = productType;
        }
        public int getProductType() {
            return productType;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }
        public String getProductName() {
            return productName;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }
        public String getPhotoUrl() {
            return photoUrl;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }
        public String getEndDate() {
            return endDate;
        }

        public String getDepartDate() {
            return departDate;
        }
        public String getDepartDateStr() {
            return "出发日期："+departDate;
        }
        public void setDepartDate(String departDate) {
            this.departDate = departDate;
        }

        public String getOrderCreateTime() {
            return orderCreateTime;
        }

        public String getOrderCreateTimeStr() {
            return "预定日期："+orderCreateTime;
        }
        public void setOrderCreateTime(String orderCreateTime) {
            this.orderCreateTime = orderCreateTime;
        }

        public void setProductNum(int productNum) {
            this.productNum = productNum;
        }
        public int getProductNum() {
            return productNum;
        }

    }
    public static class Product {
        private CostBean adult;//成人费用
        private CostBean children;//小孩费用
        private CostBean singleRoom;//单房差
        private List<CarForm> carList;//车辆信息

        public CostBean getAdult() {
            return adult;
        }

        public void setAdult(CostBean adult) {
            this.adult = adult;
        }

        public CostBean getChildren() {
            return children;
        }

        public void setChildren(CostBean children) {
            this.children = children;
        }

        public CostBean getSingleRoom() {
            return singleRoom;
        }

        public void setSingleRoom(CostBean singleRoom) {
            this.singleRoom = singleRoom;
        }

        public List<CarForm> getCarList() {
            if(carList==null){
                carList=new ArrayList<>();
            }
            return carList;
        }

        public void setCarList(List<CarForm> carList) {
            this.carList = carList;
        }
    }

    public static class InsuranceCost {
        private int insuranceType=2;//1、表示75以上、2、表示75以下
        private String insuranceName;
        private int insuranceNum;
        private double insurancePrice;

        public int getInsuranceType() {
            return insuranceType;
        }

        public void setInsuranceType(int insuranceType) {
            this.insuranceType = insuranceType;
        }

        public String getInsuranceName() {
            if(insuranceName==null){
                return "";
            }
            return insuranceName;
        }

        public void setInsuranceName(String insuranceName) {
            this.insuranceName = insuranceName;
        }

        public int getInsuranceNum() {
            return insuranceNum;
        }

        public void setInsuranceNum(int insuranceNum) {
            this.insuranceNum = insuranceNum;
        }

        public double getInsurancePrice() {
            return insurancePrice;
        }

        public void setInsurancePrice(double insurancePrice) {
            this.insurancePrice = insurancePrice;
        }
    }
}
