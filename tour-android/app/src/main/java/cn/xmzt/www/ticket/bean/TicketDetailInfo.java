package cn.xmzt.www.ticket.bean;


import java.util.List;

public class TicketDetailInfo {

    /**
     * scenicId : 1
     * scenicName : 东部华侨城大侠谷
     * area : {"areaCode":"440308","parentCode":"440300","treeNames":"广东省/深圳市/盐田区","areaName":"盐田区","areaType":3}
     * scenicAddress : 大梅沙东部华侨城
     * lonLat : {"longitude":114.287666,"latitude":22.626673}
     * grade : {"code":5,"label":"5A"}
     * openTime : 09:30～18:00开放
     * labels : 特惠推荐,特价门票
     * lightspot : 国内首个集休闲度假、观光旅游、户外运动、科普教育等主题于一体的大型综合性国家生态旅游示范区。
     ·大侠谷生态乐园和茶溪谷度假公园，集山地郊野和都市主题公园于一体，呈现了一个中西文化交融的世界。
     ·茵特拉根小镇、海菲德小镇和茶翁古镇，打造出童话一般的茶文化、葡萄酒文化梦幻小镇。
     ·每天还会有融合了多种艺术手段，以禅茶文化为主题的大型多媒体交响音画晚会《天禅》在大剧院上演
     * describeShort : 东部华侨城是一处大型生态度假区，包括大侠谷生态公园、茶溪谷、云海谷、大华兴寺等游玩项目。
     在大侠谷可以体验全世界最长的激流勇进，也可以走上云顶观光区的全玻璃观景台，纵览整个东部华侨城的景色。
     茶溪谷则是一片繁花似锦的田园风光，看湿地花园的百花齐放，徜徉在三洲茶园的茶田里，非常惬意。
     这里还有茵特拉根小镇、茶翁古镇等特色小镇，不仅有各具特色的风情建筑，还可以看到各种特色表演。
     此外，如果时间和预算比较充裕，华侨城还有云海谷高尔夫、茵特拉根养生温泉，可以享受假日时光。
     每天下午3点，在东部华侨城大剧院有以禅茶文化为主题的大型表演《天禅》
     * photoUrl : http://s1.lvjs.com.cn/uploads/pc/place2/2015-02-16/04943ae2-3112-4fbe-9b37-adef49a31401_480_320.jpg
     * themeList : [{"code":1,"label":"主题乐园"},{"code":2,"label":"田园度假"},{"code":3,"label":"都市观光"}]
     * isHot : 1
     * freePolicy : 持有国家残联颁发的《一、二等残疾证》（新版1-6级伤残证明）伤障人士；国家特等、一等及二等残疾军人；“建军节”当日，现役军人凭有效证件（军官证、文职干部证、士官证、士兵证、武警警官证，（以上人群持相关证件免票）。
     * discounts : （所有优惠票（老人、儿童票）的优惠额度以景区门市价为基准，优惠票（老人、儿童票）仅限景区售票窗口购买，具体信息请以景区当天披露为准）
     * busInfo : 2. 乘103、308、380B、103B、364、387路至大梅沙海滨浴场站或者梅沙街道办站，转乘B620至东部华侨城大侠谷站。
     * collection : 0
     * pageView : 8
     * ticketMap : {}
     */

    private int scenicId;
    private String scenicName;
    private AreaBean area;
    private String scenicAddress;
    private LonLatBean lonLat;
    private GradeBean grade;
    private String openTime;
    private String labels;
    private String lightspot;
    private String describeShort;
    private String photoUrl;
    private int isHot;
    private String freePolicy;
    private String discounts;
    private String busInfo;
    private int collection;
    private int pageView;
    private TicketMapBean ticketMap;
    private List<ThemeListBean> themeList;

    public int getScenicId() {
        return scenicId;
    }

    public void setScenicId(int scenicId) {
        this.scenicId = scenicId;
    }

    public String getScenicName() {
        return scenicName;
    }

    public void setScenicName(String scenicName) {
        this.scenicName = scenicName;
    }

    public AreaBean getArea() {
        return area;
    }

    public void setArea(AreaBean area) {
        this.area = area;
    }

    public String getScenicAddress() {
        return scenicAddress;
    }

    public void setScenicAddress(String scenicAddress) {
        this.scenicAddress = scenicAddress;
    }

    public LonLatBean getLonLat() {
        return lonLat;
    }

    public void setLonLat(LonLatBean lonLat) {
        this.lonLat = lonLat;
    }

    public GradeBean getGrade() {
        return grade;
    }

    public void setGrade(GradeBean grade) {
        this.grade = grade;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getLightspot() {
        return lightspot;
    }

    public void setLightspot(String lightspot) {
        this.lightspot = lightspot;
    }

    public String getDescribeShort() {
        return describeShort;
    }

    public void setDescribeShort(String describeShort) {
        this.describeShort = describeShort;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getIsHot() {
        return isHot;
    }

    public void setIsHot(int isHot) {
        this.isHot = isHot;
    }

    public String getFreePolicy() {
        return freePolicy;
    }

    public void setFreePolicy(String freePolicy) {
        this.freePolicy = freePolicy;
    }

    public String getDiscounts() {
        return discounts;
    }

    public void setDiscounts(String discounts) {
        this.discounts = discounts;
    }

    public String getBusInfo() {
        return busInfo;
    }

    public void setBusInfo(String busInfo) {
        this.busInfo = busInfo;
    }

    public int getCollection() {
        return collection;
    }

    public void setCollection(int collection) {
        this.collection = collection;
    }

    public int getPageView() {
        return pageView;
    }

    public void setPageView(int pageView) {
        this.pageView = pageView;
    }

    public TicketMapBean getTicketMap() {
        return ticketMap;
    }

    public void setTicketMap(TicketMapBean ticketMap) {
        this.ticketMap = ticketMap;
    }

    public List<ThemeListBean> getThemeList() {
        return themeList;
    }

    public void setThemeList(List<ThemeListBean> themeList) {
        this.themeList = themeList;
    }

    public static class AreaBean {
        /**
         * areaCode : 440308
         * parentCode : 440300
         * treeNames : 广东省/深圳市/盐田区
         * areaName : 盐田区
         * areaType : 3
         */

        private String areaCode;
        private String parentCode;
        private String treeNames;
        private String areaName;
        private int areaType;

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public String getParentCode() {
            return parentCode;
        }

        public void setParentCode(String parentCode) {
            this.parentCode = parentCode;
        }

        public String getTreeNames() {
            return treeNames;
        }

        public void setTreeNames(String treeNames) {
            this.treeNames = treeNames;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public int getAreaType() {
            return areaType;
        }

        public void setAreaType(int areaType) {
            this.areaType = areaType;
        }
    }

    public static class LonLatBean {
        /**
         * longitude : 114.287666
         * latitude : 22.626673
         */

        private double longitude;
        private double latitude;

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }
    }

    public static class GradeBean {
        /**
         * code : 5
         * label : 5A
         */

        private int code;
        private String label;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }

    public static class TicketMapBean {
    }

    public static class ThemeListBean {
        /**
         * code : 1
         * label : 主题乐园
         */

        private int code;
        private String label;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }
}
