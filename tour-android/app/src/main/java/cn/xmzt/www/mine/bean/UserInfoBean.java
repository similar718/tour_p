package cn.xmzt.www.mine.bean;

/**
 * @author tanlei
 * @date 2019/8/7
 * @describe 个人中心用户信息实体类
 */

public class UserInfoBean {

    /**
     * head : http://pf.tour.com/img/20190806/91557836583747c5a219a0ec33134979.jpeg
     * balance : 604
     * grade : {"code":1,"label":"普通会员"}
     * couponQuantity : 8
     * sign : false
     * userId : 186139
     * username : 大师兄
     */
    private String head;
    private double balance;
    private GradeEntity grade;
    private int couponQuantity;
    private boolean sign;
    private int userId;
    private String username;
    private int continuouNum;

    public int getContinuouNum() {
        return continuouNum;
    }

    public void setContinuouNum(int continuouNum) {
        this.continuouNum = continuouNum;
    }

    /**
     * orderHintCount : {"refundState":0,"maxId":0,"unpaidCount":0,"toTravelCount":0,"allCount":0}
     */
    private OrderHintCountEntity orderHintCount;

    public void setHead(String head) {
        this.head = head;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setGrade(GradeEntity grade) {
        this.grade = grade;
    }

    public void setCouponQuantity(int couponQuantity) {
        this.couponQuantity = couponQuantity;
    }

    public void setSign(boolean sign) {
        this.sign = sign;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHead() {
        return head;
    }

    public double getBalance() {
        return balance;
    }

    public GradeEntity getGrade() {
        return grade;
    }

    public int getCouponQuantity() {
        return couponQuantity;
    }

    public boolean isSign() {
        return sign;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public void setOrderHintCount(OrderHintCountEntity orderHintCount) {
        this.orderHintCount = orderHintCount;
    }

    public OrderHintCountEntity getOrderHintCount() {
        if(orderHintCount==null){
            orderHintCount=new OrderHintCountEntity();
        }
        return orderHintCount;
    }

    public class GradeEntity {
        /**
         * code : 1
         * label : 普通会员
         */
        private int code;
        private String label;

        public void setCode(int code) {
            this.code = code;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public int getCode() {
            return code;
        }

        public String getLabel() {
            return label;
        }
    }

    public class OrderHintCountEntity {
        /**
         * refundState : 0
         * maxId : 0
         * unpaidCount : 0
         * toTravelCount : 0
         * allCount : 0
         */
        private int refundState;
        private int maxId;
        private int unpaidCount;
        private int toTravelCount;
        private int allCount;

        public void setRefundState(int refundState) {
            this.refundState = refundState;
        }

        public void setMaxId(int maxId) {
            this.maxId = maxId;
        }

        public void setUnpaidCount(int unpaidCount) {
            this.unpaidCount = unpaidCount;
        }

        public void setToTravelCount(int toTravelCount) {
            this.toTravelCount = toTravelCount;
        }

        public void setAllCount(int allCount) {
            this.allCount = allCount;
        }

        public int getRefundState() {
            return refundState;
        }

        public int getMaxId() {
            return maxId;
        }

        public int getUnpaidCount() {
            return unpaidCount;
        }

        public int getToTravelCount() {
            return toTravelCount;
        }

        public int getAllCount() {
            return allCount;
        }
    }
}
