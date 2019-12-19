package cn.xmzt.www.route.bean;

import android.text.TextUtils;

import com.airbnb.lottie.animation.content.Content;

import cn.xmzt.www.utils.MathUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 线路详情
 */
public class RouteDetailPage{
    public final String cfd="出发地：";
    private ArrivalAreaBean arrivalArea;//
    private String backRule;//退改规则
    private int collection;//收藏量
    private CostBean cost;//费用
    private int days;//天数
    private DepartAreaBean departArea;//
    private String departTime;//出发日期
    private int departType;//出发地类型(0:出发,1:落地)
    private int discounts;//是否特惠推荐(0:否,1:是)
    private String driveInformation;//自驾须知
    private int guessLike;//是否猜你喜欢(0:否,1:错峰出行,2:亲子时光,3:休闲度假)
    private boolean hasCollection;//是否已收藏 true:已收藏 false:未收藏
    private int id;//
    private String intro;//线路简介
    private int isTactics;//是否有领队(0:没有,1有:
    private int isVouchers;//是否可用抵用券
    private String language;//线路宣传语
    private String lineName;//线路名称
    private int lineType;//线路类型：1自驾游（自由出行） 2跟团游（跟团自驾）
    private String officeId; //服务机构id
    private String officeName;// 服务机构名称
    private String notice;//须知
    private String photoUrl;//线路封面图片
    private int popPlay;//是否人气必玩(0:否,1:是)
    private int purchase;//是否超值抢购(0:否,1:是)
    private String recommend;//达人推荐信息
    private String safetyInstruction;//安全须知
    private String serviceAssurance;//服务保障
    private String tripDistance;//行程距离
    private int views;//浏览量
    private List<CouponListBean> couponList;//优惠券列表
    private List<HotSpotListBean> hotSpotList;//热门景点列表
    private List<LightspotListBean> lightspotList;//亮点列表
//    private List<LineRouteListBean> lineRouteList;//行程列表
    private List<DayLineTrip> lineRouteListVO;//行程列表
    private List<RecommendsBean> recommends;//推荐列表
    private List<ResourceListBean> resourceList;//路宣传图或视频
    private List<String> themes;//主题

    public ArrivalAreaBean getArrivalArea() {
        return arrivalArea;
    }

    public void setArrivalArea(ArrivalAreaBean arrivalArea) {
        this.arrivalArea = arrivalArea;
    }

    public String getBackRule() {
        return backRule;
    }

    public void setBackRule(String backRule) {
        this.backRule = backRule;
    }

    public int getCollection() {
        return collection;
    }
    public String getCollectionStr() {
        if(collection>10000){
            return String.valueOf(collection/10000)+"w";
        }else {
            return String.valueOf(collection);
        }
    }

    public void setCollection(int collection) {
        this.collection = collection;
    }

    public CostBean getCost() {
        return cost;
    }

    public void setCost(CostBean cost) {
        this.cost = cost;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public DepartAreaBean getDepartArea() {
        return departArea;
    }

    public void setDepartArea(DepartAreaBean departArea) {
        this.departArea = departArea;
    }

    public String getDepartTime() {
        return departTime;
    }

    public void setDepartTime(String departTime) {
        this.departTime = departTime;
    }

    public int getDepartType() {
        return departType;
    }

    public void setDepartType(int departType) {
        this.departType = departType;
    }

    public int getDiscounts() {
        return discounts;
    }

    public void setDiscounts(int discounts) {
        this.discounts = discounts;
    }

    public String getDriveInformation() {
        return driveInformation;
    }

    public void setDriveInformation(String driveInformation) {
        this.driveInformation = driveInformation;
    }

    public int getGuessLike() {
        return guessLike;
    }

    public void setGuessLike(int guessLike) {
        this.guessLike = guessLike;
    }

    public boolean isHasCollection() {
        return hasCollection;
    }

    public void setHasCollection(boolean hasCollection) {
        this.hasCollection = hasCollection;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getIsTactics() {
        return isTactics;
    }
    public String getIsTacticsStr() {
        if(isTactics==1){
            return "金牌领队";
        }else {
            return "无领队";
        }
    }

    public void setIsTactics(int isTactics) {
        this.isTactics = isTactics;
    }

    public int getIsVouchers() {
        return isVouchers;
    }

    public void setIsVouchers(int isVouchers) {
        this.isVouchers = isVouchers;
    }

    public String getLanguage() {
        if(language==null){
            return "";
        }
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }
    public int getLineType() {
        return lineType;
    }

    public void setLineType(int lineType) {
        this.lineType = lineType;
    }
    public String getLineTypeName() {
        if(lineType==1){
            return "自由出行";
        }else if(lineType==2){
            return "跟团自驾";
        }
        return "";
    }
    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getOfficeName() {
        return officeName;
    }
    public String getOfficeNames() {
        if(!TextUtils.isEmpty(officeName)){
            return "由"+officeName+"提供服务";
        }
        return "";
    }
    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }
    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getPopPlay() {
        return popPlay;
    }

    public void setPopPlay(int popPlay) {
        this.popPlay = popPlay;
    }

    public int getPurchase() {
        return purchase;
    }

    public void setPurchase(int purchase) {
        this.purchase = purchase;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public String getSafetyInstruction() {
        return safetyInstruction;
    }

    public void setSafetyInstruction(String safetyInstruction) {
        this.safetyInstruction = safetyInstruction;
    }

    public String getServiceAssurance() {
        return serviceAssurance;
    }

    public void setServiceAssurance(String serviceAssurance) {
        this.serviceAssurance = serviceAssurance;
    }

    public String getTripDistance() {
        return tripDistance;
    }

    public void setTripDistance(String tripDistance) {
        this.tripDistance = tripDistance;
    }

    public int getViews() {
        return views;
    }
    public String getViewsStr() {
        if(views>10000){
            return String.valueOf(views/10000)+"w";
        }else {
            return String.valueOf(views);
        }
    }
    public void setViews(int views) {
        this.views = views;
    }

    public List<CouponListBean> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<CouponListBean> couponList) {
        this.couponList = couponList;
    }

    public List<HotSpotListBean> getHotSpotList() {
        return hotSpotList;
    }

    public void setHotSpotList(List<HotSpotListBean> hotSpotList) {
        this.hotSpotList = hotSpotList;
    }

    public List<LightspotListBean> getLightspotList() {
        return lightspotList;
    }

    public void setLightspotList(List<LightspotListBean> lightspotList) {
        this.lightspotList = lightspotList;
    }

    /*public List<LineRouteListBean> getLineRouteList() {
        return lineRouteList;
    }

    public void setLineRouteList(List<LineRouteListBean> lineRouteList) {
        this.lineRouteList = lineRouteList;
    }*/

    public List<DayLineTrip> getLineRouteListVO() {
        return lineRouteListVO;
    }

    public void setLineRouteListVO(List<DayLineTrip> lineRouteListVO) {
        this.lineRouteListVO = lineRouteListVO;
    }

    public List<RecommendsBean> getRecommends() {
        return recommends;
    }

    public void setRecommends(List<RecommendsBean> recommends) {
        this.recommends = recommends;
    }

    public List<ResourceListBean> getResourceList() {
        if(resourceList==null){
            resourceList=new ArrayList<ResourceListBean>();
        }
        return resourceList;
    }

    public void setResourceList(List<ResourceListBean> resourceList) {
        this.resourceList = resourceList;
    }

    public static class ArrivalAreaBean {
        private String areaCode;//区域编码
        private String areaName;//区域名称
        private String areaType;//区域类型
        private String parentCode;//父级编号
        private String treeNames;//全节点名

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public String getAreaType() {
            return areaType;
        }

        public void setAreaType(String areaType) {
            this.areaType = areaType;
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
    }

    public static class CostBean {

        private double carPrice;//拼车价格
        private String costContain;//费用包含信息
        private String costNocontain;//费用不包含信息
        private double crPrice;//成人门市价
        private double fangPrice;//单房差
        private double jcrPrice;//成人结算价
        private double jxhPrice;//儿童结算价
        private double xhPrice;//儿童门市价

        public double getCarPrice() {
            return carPrice;
        }
        public String getCarPriceStr() {
            return MathUtil.formatDouble(carPrice,2);
        }

        public void setCarPrice(double carPrice) {
            this.carPrice = carPrice;
        }

        public String getCostContain() {
            return costContain;
        }

        public void setCostContain(String costContain) {
            this.costContain = costContain;
        }

        public String getCostNocontain() {
            return costNocontain;
        }

        public void setCostNocontain(String costNocontain) {
            this.costNocontain = costNocontain;
        }

        public double getCrPrice() {
            return crPrice;
        }
        public String getCrPriceStr() {
            return MathUtil.formatDouble(crPrice,2);
        }

        public void setCrPrice(double crPrice) {
            this.crPrice = crPrice;
        }

        public double getFangPrice() {
            return fangPrice;
        }
        public String getFangPriceStr() {
            return MathUtil.formatDouble(fangPrice,2);
        }

        public void setFangPrice(double fangPrice) {
            this.fangPrice = fangPrice;
        }

        public double getJcrPrice() {
            return jcrPrice;
        }
        public String getJcrPriceStr() {
            return MathUtil.formatDouble(jcrPrice,2);
        }

        public void setJcrPrice(double jcrPrice) {
            this.jcrPrice = jcrPrice;
        }

        public double getJxhPrice() {
            return jxhPrice;
        }
        public String getJxhPriceStr() {
            return MathUtil.formatDouble(jxhPrice,2);
        }

        public void setJxhPrice(double jxhPrice) {
            this.jxhPrice = jxhPrice;
        }

        public double getXhPrice() {
            return xhPrice;
        }
        public String getXhPriceStr() {
            return MathUtil.formatDouble(xhPrice,2);
        }
        public void setXhPrice(double xhPrice) {
            this.xhPrice = xhPrice;
        }
    }

    public static class DepartAreaBean {

        private String areaCode;//区域编码
        private String areaName;//区域名称
        private String areaType;//区域类型
        private String parentCode;//父级编号
        private String treeNames;//全节点名

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public String getAreaType() {
            return areaType;
        }

        public void setAreaType(String areaType) {
            this.areaType = areaType;
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
    }

    public static class CouponListBean {

        private int amount;//数量
        private int astrictCondition;//使用上限条件(1:有效期内只能使用一张,2:每天使用限制数量,3:不限量)
        private String beginDate;//开始时间
        private String couponName;//优惠劵名称
        private int couponType;//优惠劵类型(0:通用,1:线路,2:酒店,3:门票)
        private String endDate;//结束时间
        private int id;//
        private double maxSubtract;//最高减额度
        private int minConsume;//最低消费可使用优惠劵
        private String openTime;//开放时间
        private int priority;//优惠劵优先级(123)
        private boolean received;//领取状态 true:已领取 false:未领取*
        private int sendMode;//发放方式(1:新用户注册,2:30天内未登录用户)
        private int useAstrict;//使用上限

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getAstrictCondition() {
            return astrictCondition;
        }

        public void setAstrictCondition(int astrictCondition) {
            this.astrictCondition = astrictCondition;
        }

        public String getBeginDate() {
            return beginDate;
        }

        public void setBeginDate(String beginDate) {
            this.beginDate = beginDate;
        }

        public String getCouponName() {
            return couponName;
        }

        public void setCouponName(String couponName) {
            this.couponName = couponName;
        }

        public int getCouponType() {
            return couponType;
        }

        public void setCouponType(int couponType) {
            this.couponType = couponType;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getMaxSubtract() {
            return maxSubtract;
        }

        public void setMaxSubtract(double maxSubtract) {
            this.maxSubtract = maxSubtract;
        }

        public int getMinConsume() {
            return minConsume;
        }

        public void setMinConsume(int minConsume) {
            this.minConsume = minConsume;
        }

        public String getOpenTime() {
            return openTime;
        }

        public void setOpenTime(String openTime) {
            this.openTime = openTime;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public boolean isReceived() {
            return received;
        }

        public void setReceived(boolean received) {
            this.received = received;
        }

        public int getSendMode() {
            return sendMode;
        }

        public void setSendMode(int sendMode) {
            this.sendMode = sendMode;
        }

        public int getUseAstrict() {
            return useAstrict;
        }

        public void setUseAstrict(int useAstrict) {
            this.useAstrict = useAstrict;
        }
    }

    public static class HotSpotListBean {

        private String content;//内容
        private String title;//标题
        private List<String> images;//图集

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getImages() {
            if(images==null){
                images=new ArrayList<>();
            }
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }

    public static class LightspotListBean {

        private String content;//内容
        private String title;//标题
        private List<String> images;//图集

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }

   /* public static class LineRouteListBean {

        private String accommodation;//住宿
        private AccommodationCoorBean accommodationCoor;//
        private String breakfast;//早餐
        private String car;//交通计划
        private String carXy;//交通计划坐标逗号分隔
        private String content;//内容
        private int dayNum;//第几天
        private String dinner;//晚餐
        private AreaBean end;//
        private EndCoorBean endCoor;//
        private int hotelId;//酒店Id
        private int id;//
        private int lineId;//路线ID
        private String lunch;//午餐
        private StaginCoorBean staginCoor;//
        private AreaBean staging;//
        private AreaBean start;//
        private String title;//行程标题
        private List<SpotsListBean> spotsList;//景点

        public String getAccommodation() {
            return accommodation;
        }

        public void setAccommodation(String accommodation) {
            this.accommodation = accommodation;
        }

        public AccommodationCoorBean getAccommodationCoor() {
            return accommodationCoor;
        }

        public void setAccommodationCoor(AccommodationCoorBean accommodationCoor) {
            this.accommodationCoor = accommodationCoor;
        }

        public String getBreakfast() {
            return breakfast;
        }

        public void setBreakfast(String breakfast) {
            this.breakfast = breakfast;
        }

        public String getCar() {
            return car;
        }

        public void setCar(String car) {
            this.car = car;
        }

        public String getCarXy() {
            return carXy;
        }

        public void setCarXy(String carXy) {
            this.carXy = carXy;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getDayNum() {
            return dayNum;
        }
        public String getDayNumStr() {
            if(dayNum<10){
                return "0"+dayNum;
            }
            return dayNum+"";
        }

        public void setDayNum(int dayNum) {
            this.dayNum = dayNum;
        }

        public String getDinner() {
            return dinner;
        }

        public void setDinner(String dinner) {
            this.dinner = dinner;
        }

        public AreaBean getEnd() {
            return end;
        }

        public void setEnd(AreaBean end) {
            this.end = end;
        }

        public EndCoorBean getEndCoor() {
            return endCoor;
        }

        public void setEndCoor(EndCoorBean endCoor) {
            this.endCoor = endCoor;
        }

        public int getHotelId() {
            return hotelId;
        }

        public void setHotelId(int hotelId) {
            this.hotelId = hotelId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getLineId() {
            return lineId;
        }

        public void setLineId(int lineId) {
            this.lineId = lineId;
        }

        public String getLunch() {
            return lunch;
        }

        public void setLunch(String lunch) {
            this.lunch = lunch;
        }

        public StaginCoorBean getStaginCoor() {
            return staginCoor;
        }

        public void setStaginCoor(StaginCoorBean staginCoor) {
            this.staginCoor = staginCoor;
        }

        public AreaBean getStaging() {
            return staging;
        }

        public void setStaging(AreaBean staging) {
            this.staging = staging;
        }

        public AreaBean getStart() {
            return start;
        }

        public void setStart(AreaBean start) {
            this.start = start;
        }
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<SpotsListBean> getSpotsList() {
            if(spotsList==null){
                spotsList=new ArrayList<>();
            }
            return spotsList;
        }

        *//**
         * 是否有景点
         * @return
         *//*
        public boolean isSpotsList() {
            if(spotsList!=null&&spotsList.size()>0){
                return true;
            }
            return false;
        }
        public void setSpotsList(List<SpotsListBean> spotsList) {
            this.spotsList = spotsList;
        }

        public static class AccommodationCoorBean {

//                private int latitude;//纬度
//                private int longitude;//经度
//
//                public int getLatitude() {
//                    return latitude;
//                }
//
//                public void setLatitude(int latitude) {
//                    this.latitude = latitude;
//                }
//
//                public int getLongitude() {
//                    return longitude;
//                }
//
//                public void setLongitude(int longitude) {
//                    this.longitude = longitude;
//                }
        }


        public static class EndCoorBean {

//                private int latitude;//纬度
//                private int longitude;//经度
//
//                public int getLatitude() {
//                    return latitude;
//                }
//
//                public void setLatitude(int latitude) {
//                    this.latitude = latitude;
//                }
//
//                public int getLongitude() {
//                    return longitude;
//                }
//
//                public void setLongitude(int longitude) {
//                    this.longitude = longitude;
//                }
        }

        public static class StaginCoorBean {

//                private int latitude;//纬度
//                private int longitude;//经度
//
//                public int getLatitude() {
//                    return latitude;
//                }
//
//                public void setLatitude(int latitude) {
//                    this.latitude = latitude;
//                }
//
//                public int getLongitude() {
//                    return longitude;
//                }
//
//                public void setLongitude(int longitude) {
//                    this.longitude = longitude;
//                }
        }


        public static class SpotsListBean {

            private String content;//内容
            private String title;//标题
            private List<String> images;//图集

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public List<String> getImages() {
                return images;
            }

            public void setImages(List<String> images) {
                this.images = images;
            }
        }
    }*/

    public static class RecommendsBean {

        private String area;
        private String city;
        private String departTime;
        private int id;
        private int lineType;//线路类型：1自驾游（自由行程） 2跟团游（跟团出行）
        private String officeId; //服务机构id
        private String officeName;// 服务机构名称
        private String language;
        private String lineName;
        private String photoUrl;
        private double price;
        private String province;
        private String resourceUrl;

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDepartTime() {
            return departTime;
        }

        public void setDepartTime(String departTime) {
            this.departTime = departTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
        public int getLineType() {
            return lineType;
        }

        public void setLineType(int lineType) {
            this.lineType = lineType;
        }
        public String getLineTypeName() {
            if(lineType==1){
                return "自由出行";
            }else if(lineType==2){
                return "跟团自驾";
            }
            return "";
        }

        public String getOfficeId() {
            return officeId;
        }

        public void setOfficeId(String officeId) {
            this.officeId = officeId;
        }

        public String getOfficeName() {
            return officeName;
        }
        public String getOfficeNames() {
            if(!TextUtils.isEmpty(officeName)){
                return "由"+officeName+"提供服务";
            }
            return "";
        }
        public void setOfficeName(String officeName) {
            this.officeName = officeName;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getLineName() {
            return lineName;
        }

        public void setLineName(String lineName) {
            this.lineName = lineName;
        }

        public String getPhotoUrl() {
            return photoUrl;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }

        public double getPrice() {
            return price;
        }
        public String getPriceStr() {
            return MathUtil.formatDouble(price,2);
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getResourceUrl() {
            return resourceUrl;
        }

        public void setResourceUrl(String resourceUrl) {
            this.resourceUrl = resourceUrl;
        }
    }

    public static class ResourceListBean {
        private String href;// 链接地址 web页面的链接
        private String thumb;//缩略图
        private String type;//1图片 2视频
        private String url;//资源url

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }


    public List<String> getThemes() {
        return themes;
    }

    public void setThemes(List<String> themes) {
        this.themes = themes;
    }
    /**
     * 某天的行程
     */
    public static class DayLineTrip {
        private String date;
        private int dayNum;
        private List<DayLineTripInfo> detailVOList;
        private int id;
        private int lineId;
        private String title;
        public void setDate(String date) {
            this.date = date;
        }
        public String getDate() {
            return date;
        }

        public void setDayNum(int dayNum) {
            this.dayNum = dayNum;
        }
        public int getDayNum() {
            return dayNum;
        }
        public String getDayNumStr() {
            if(dayNum<10){
                return "0"+dayNum;
            }
            return dayNum+"";
        }
        public void setDetailVOList(List<DayLineTripInfo> detailVOList) {
            this.detailVOList = detailVOList;
        }
        public List<DayLineTripInfo> getDetailVOList() {
            return detailVOList;
        }

        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public void setLineId(int lineId) {
            this.lineId = lineId;
        }
        public int getLineId() {
            return lineId;
        }

        public void setTitle(String title) {
            this.title = title;
        }
        public String getTitle() {
            return title;
        }

    }

    /**
     * 某天的行程详情
     */
    public static class DayLineTripInfo {
        private LineTripAddress staging;//集结点
        private LineTripAddress start;//出发地
        private LineTripAddress end;//目的地
        private LineTripMeal breakfast;//早餐
        private LineTripMeal lunch;//午餐
        private LineTripMeal dinner;//晚餐
        private LineTripCar car;
        private LineTripContent content;
        /**
         * 数据类型：1行程信息 2景点信息 3交通信息 4住宿信息 5集结地信息
         * 6开始地信息 7目的地信息 8早餐信息 9午餐信息 10晚餐信息
         */
        private int dataType;
        private LineTripHotel hotel;
        private LineTripSpot spot;

        public LineTripAddress getStaging() {
            return staging;
        }

        public void setStaging(LineTripAddress staging) {
            this.staging = staging;
        }

        public LineTripAddress getStart() {
            return start;
        }

        public void setStart(LineTripAddress start) {
            this.start = start;
        }

        public LineTripAddress getEnd() {
            return end;
        }

        public void setEnd(LineTripAddress end) {
            this.end = end;
        }

        public LineTripMeal getBreakfast() {
            return breakfast;
        }

        public void setBreakfast(LineTripMeal breakfast) {
            this.breakfast = breakfast;
        }

        public LineTripMeal getLunch() {
            return lunch;
        }

        public void setLunch(LineTripMeal lunch) {
            this.lunch = lunch;
        }

        public LineTripMeal getDinner() {
            return dinner;
        }

        public void setDinner(LineTripMeal dinner) {
            this.dinner = dinner;
        }

        public LineTripCar getCar() {
            return car;
        }

        public void setCar(LineTripCar car) {
            this.car = car;
        }

        public LineTripContent getContent() {
            return content;
        }

        public void setContent(LineTripContent content) {
            this.content = content;
        }

        public int getDataType() {
            return dataType;
        }

        public void setDataType(int dataType) {
            this.dataType = dataType;
        }

        public LineTripHotel getHotel() {
            return hotel;
        }

        public void setHotel(LineTripHotel hotel) {
            this.hotel = hotel;
        }

        public LineTripSpot getSpot() {
            return spot;
        }

        public void setSpot(LineTripSpot spot) {
            this.spot = spot;
        }
    }

    /**
     * 线路行程餐点
     */
    public static class LineTripMeal{
        private String meal;//餐点
        private String address;//详细地址
        private String areaId;//区域id
        private AreaBean areaVO;//区域
        private String coor;//坐标

        public String getMeal() {
            if(meal==null){
                return "";
            }
            return meal;
        }

        public void setMeal(String meal) {
            this.meal = meal;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAreaId() {
            return areaId;
        }

        public void setAreaId(String areaId) {
            this.areaId = areaId;
        }

        public AreaBean getAreaVO() {
            return areaVO;
        }

        public void setAreaVO(AreaBean areaVO) {
            this.areaVO = areaVO;
        }

        public String getCoor() {
            return coor;
        }

        public void setCoor(String coor) {
            this.coor = coor;
        }
    }
    /**
     * 线路行程交通
     */
    public static class LineTripCar{
        private String car;//描述信息
        private String address;//详细地址
        private String areaId;//区域id
        private AreaBean areaVO;//区域
        private String coor;//坐标

        public String getCar() {
            if(car==null){
                return "";
            }
            return car;
        }

        public void setCar(String car) {
            this.car = car;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAreaId() {
            return areaId;
        }

        public void setAreaId(String areaId) {
            this.areaId = areaId;
        }

        public AreaBean getAreaVO() {
            return areaVO;
        }

        public void setAreaVO(AreaBean areaVO) {
            this.areaVO = areaVO;
        }

        public String getCoor() {
            return coor;
        }

        public void setCoor(String coor) {
            this.coor = coor;
        }
    }
    /**
     * 线路行程内容
     */
    public static class LineTripContent{
        private String content;//内容描述信息
        private String address;//详细地址
        private String areaId;//区域id
        private AreaBean areaVO;//区域
        private String coor;//坐标

        public String getContent() {
            if(content==null){
                return "";
            }
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAreaId() {
            return areaId;
        }

        public void setAreaId(String areaId) {
            this.areaId = areaId;
        }

        public AreaBean getAreaVO() {
            return areaVO;
        }

        public void setAreaVO(AreaBean areaVO) {
            this.areaVO = areaVO;
        }

        public String getCoor() {
            return coor;
        }

        public void setCoor(String coor) {
            this.coor = coor;
        }
    }
    /**
     * 线路行程酒店
     */
    public static class LineTripHotel{
        private int hotelId;//系统酒店id
        private String accommodation;//酒店名字
        private String address;//详细地址
        private String areaId;//区域id
        private AreaBean areaVO;//区域
        private String coor;//坐标

        public int getHotelId() {
            return hotelId;
        }

        public void setHotelId(int hotelId) {
            this.hotelId = hotelId;
        }

        public String getAccommodation() {
            if(accommodation==null){
                return "";
            }
            return accommodation;
        }

        public void setAccommodation(String accommodation) {
            this.accommodation = accommodation;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAreaId() {
            return areaId;
        }

        public void setAreaId(String areaId) {
            this.areaId = areaId;
        }

        public AreaBean getAreaVO() {
            return areaVO;
        }

        public void setAreaVO(AreaBean areaVO) {
            this.areaVO = areaVO;
        }

        public String getCoor() {
            return coor;
        }

        public void setCoor(String coor) {
            this.coor = coor;
        }
    }
    /**
     * 线路行程景点
     */
    public static class LineTripSpot{
        private String title;//标题
        private String content;//景点描述内容
        private String address;//详细地址
        private String areaId;//区域id
        private AreaBean areaVO;//区域
        private String coor;//坐标
        private List<String> imageArray;//图集
        private String images;//图片
        private int scenicId;//系统景区id
        private int spotId;//系统景点id

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAreaId() {
            return areaId;
        }

        public void setAreaId(String areaId) {
            this.areaId = areaId;
        }

        public AreaBean getAreaVO() {
            return areaVO;
        }

        public void setAreaVO(AreaBean areaVO) {
            this.areaVO = areaVO;
        }

        public String getCoor() {
            return coor;
        }

        public void setCoor(String coor) {
            this.coor = coor;
        }

        public List<String> getImageArray() {
            return imageArray;
        }

        public void setImageArray(List<String> imageArray) {
            this.imageArray = imageArray;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public int getScenicId() {
            return scenicId;
        }

        public void setScenicId(int scenicId) {
            this.scenicId = scenicId;
        }

        public int getSpotId() {
            return spotId;
        }

        public void setSpotId(int spotId) {
            this.spotId = spotId;
        }
    }
    /**
     * 线路行程地点
     */
    public static class LineTripAddress {
        private String areaId;//区域id
        private String address;//详细地址
        private String coor;//坐标
        private AreaBean areaVO;//区域

        public String getAreaId() {
            return areaId;
        }

        public void setAreaId(String areaId) {
            this.areaId = areaId;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCoor() {
            return coor;
        }

        public void setCoor(String coor) {
            this.coor = coor;
        }

        public AreaBean getAreaVO() {
            return areaVO;
        }

        public void setAreaVO(AreaBean areaVO) {
            this.areaVO = areaVO;
        }
    }
    public static class AreaBean {
        private String areaCode;//区域编码
        private String areaName;//区域名称
        private String areaType;//区域类型
        private String parentCode;//父级编号
        private String treeNames;//全节点名

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public String getAreaName() {
            if(areaName==null){
                return "";
            }
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public String getAreaType() {
            return areaType;
        }

        public void setAreaType(String areaType) {
            this.areaType = areaType;
        }

        public String getParentCode() {
            return parentCode;
        }

        public void setParentCode(String parentCode) {
            this.parentCode = parentCode;
        }

        public String getTreeNames() {
            if(treeNames==null){
                return "";
            }
            return treeNames;
        }

        public void setTreeNames(String treeNames) {
            this.treeNames = treeNames;
        }
    }
}
