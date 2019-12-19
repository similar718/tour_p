package cn.xmzt.www.ticket.bean;


import java.util.List;

public class SpecialTicketBean {

    private List<AdvertiseBean> advertise;
    private List<SubjectListBean> subjectList;
    private List<HotBean> hot;

    public List<AdvertiseBean> getAdvertise() {
        return advertise;
    }

    public void setAdvertise(List<AdvertiseBean> advertise) {
        this.advertise = advertise;
    }

    public List<SubjectListBean> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<SubjectListBean> subjectList) {
        this.subjectList = subjectList;
    }

    public List<HotBean> getHot() {
        return hot;
    }

    public void setHot(List<HotBean> hot) {
        this.hot = hot;
    }

    public static class AdvertiseBean {
        /**
         * homeName : 首页图片一
         * advPic : http://pic37.nipic.com/20140113/8800276_184927469000_2.png
         * advLink : http://127.0.0.1:9005/home/homeAdvertise
         * sort : 1
         * linkType : 1
         */

        private String homeName;
        private String advPic;
        private String advLink;
        private int sort;
        private int linkType;

        public String getHomeName() {
            return homeName;
        }

        public void setHomeName(String homeName) {
            this.homeName = homeName;
        }

        public String getAdvPic() {
            return advPic;
        }

        public void setAdvPic(String advPic) {
            this.advPic = advPic;
        }

        public String getAdvLink() {
            return advLink;
        }

        public void setAdvLink(String advLink) {
            this.advLink = advLink;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getLinkType() {
            return linkType;
        }

        public void setLinkType(int linkType) {
            this.linkType = linkType;
        }
    }

    public static class SubjectListBean {
        /**
         * id : 9
         * subjectId : 1
         * subjectName : 周末价到
         * subjectType : 3
         * iconUrl : http://pf.tour.com/icon/subject/recommend/ticket_selection_subject_1.png
         * showLocation : 3
         * sort : 100
         */

        private int id;
        private int subjectId;
        private String subjectName;
        private int subjectType;
        private String iconUrl;
        private int showLocation;
        private int sort;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getSubjectId() {
            return subjectId;
        }

        public void setSubjectId(int subjectId) {
            this.subjectId = subjectId;
        }

        public String getSubjectName() {
            return subjectName;
        }

        public void setSubjectName(String subjectName) {
            this.subjectName = subjectName;
        }

        public int getSubjectType() {
            return subjectType;
        }

        public void setSubjectType(int subjectType) {
            this.subjectType = subjectType;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public int getShowLocation() {
            return showLocation;
        }

        public void setShowLocation(int showLocation) {
            this.showLocation = showLocation;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }
    }

    public static class HotBean {
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
