package cn.xmzt.www.ticket.bean;


import java.util.List;

public class SpecialTicketMustPlayBean {

    /**
     * total : 2
     * totalPage : 1
     * items : [{"id":1,"scenicName":"东部华侨城大侠谷","photoUrl":"http://s1.lvjs.com.cn/uploads/pc/place2/2015-02-16/04943ae2-3112-4fbe-9b37-adef49a31401_480_320.jpg","scenicAddress":"大梅沙东部华侨城","coordinate":"114.287666,22.626673","province":"广东省","city":"深圳市","area":"盐田区","minOnlineOrderPrice":0,"ticketClass":3,"ticketLabel":2,"grade":5,"isHot":1,"isPopularity":1,"isTodayScheduled":1,"theme":"1,2,3"},{"id":3,"scenicName":"深圳欢乐谷","photoUrl":"http://s1.lvjs.com.cn/uploads/pc/place2/2015-02-16/04943ae2-3112-4fbe-9b37-adef49a31401_480_320.jpg","scenicAddress":"华侨城欢乐谷","coordinate":"113.979573,22.541737","province":"广东省","city":"深圳市","area":"盐田区","minOnlineOrderPrice":185,"ticketClass":6,"ticketLabel":2,"grade":5,"isHot":1,"isPopularity":1,"isTodayScheduled":1,"theme":"1,2,3"}]
     */

    private int total;
    private int totalPage;
    private List<ItemsBean> items;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {
        /**
         * id : 1
         * scenicName : 东部华侨城大侠谷
         * photoUrl : http://s1.lvjs.com.cn/uploads/pc/place2/2015-02-16/04943ae2-3112-4fbe-9b37-adef49a31401_480_320.jpg
         * scenicAddress : 大梅沙东部华侨城
         * coordinate : 114.287666,22.626673
         * province : 广东省
         * city : 深圳市
         * area : 盐田区
         * minOnlineOrderPrice : 0
         * ticketClass : 3
         * ticketLabel : 2
         * grade : 5
         * isHot : 1
         * isPopularity : 1
         * isTodayScheduled : 1
         * theme : 1,2,3
         */

        private int id;
        private String scenicName;
        private String photoUrl;
        private String scenicAddress;
        private String coordinate;
        private String province;
        private String city;
        private String area;
        private int minOnlineOrderPrice;
        private int ticketClass;
        private int ticketLabel;
        private int grade;
        private int isHot;
        private int isPopularity;
        private int isTodayScheduled;
        private String theme;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public String getScenicAddress() {
            return scenicAddress;
        }

        public void setScenicAddress(String scenicAddress) {
            this.scenicAddress = scenicAddress;
        }

        public String getCoordinate() {
            return coordinate;
        }

        public void setCoordinate(String coordinate) {
            this.coordinate = coordinate;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public int getMinOnlineOrderPrice() {
            return minOnlineOrderPrice;
        }

        public void setMinOnlineOrderPrice(int minOnlineOrderPrice) {
            this.minOnlineOrderPrice = minOnlineOrderPrice;
        }

        public int getTicketClass() {
            return ticketClass;
        }

        public void setTicketClass(int ticketClass) {
            this.ticketClass = ticketClass;
        }

        public int getTicketLabel() {
            return ticketLabel;
        }

        public void setTicketLabel(int ticketLabel) {
            this.ticketLabel = ticketLabel;
        }

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }

        public int getIsHot() {
            return isHot;
        }

        public void setIsHot(int isHot) {
            this.isHot = isHot;
        }

        public int getIsPopularity() {
            return isPopularity;
        }

        public void setIsPopularity(int isPopularity) {
            this.isPopularity = isPopularity;
        }

        public int getIsTodayScheduled() {
            return isTodayScheduled;
        }

        public void setIsTodayScheduled(int isTodayScheduled) {
            this.isTodayScheduled = isTodayScheduled;
        }

        public String getTheme() {
            return theme;
        }

        public void setTheme(String theme) {
            this.theme = theme;
        }
    }
}
