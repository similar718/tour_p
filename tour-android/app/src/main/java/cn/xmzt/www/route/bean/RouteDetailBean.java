package cn.xmzt.www.route.bean;

import cn.xmzt.www.bean.BaseBean;

import java.util.List;

/**
 * 线路详情
 */

public class RouteDetailBean extends BaseBean {

    private RelBean rel;

    public RelBean getRel() {
        return rel;
    }

    public void setRel(RelBean rel) {
        this.rel = rel;
    }

    public static class RelBean {

        private ArrivalAreaBean arrivalArea;//
        private String backRule;//退改规则
        private int collection;//收藏量
        private CostBean cost;//
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
        private List<LineRouteListBean> lineRouteList;//行程列表
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

        public List<LineRouteListBean> getLineRouteList() {
            return lineRouteList;
        }

        public void setLineRouteList(List<LineRouteListBean> lineRouteList) {
            this.lineRouteList = lineRouteList;
        }

        public List<RecommendsBean> getRecommends() {
            return recommends;
        }

        public void setRecommends(List<RecommendsBean> recommends) {
            this.recommends = recommends;
        }

        public List<ResourceListBean> getResourceList() {
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

            private int carPrice;//拼车价格
            private String costContain;//费用包含信息
            private String costNocontain;//费用不包含信息
            private int crPrice;//成人门市价
            private int fangPrice;//单房差
            private int jcrPrice;//成人结算价
            private int jxhPrice;//儿童结算价
            private int xhPrice;//儿童门市价

            public int getCarPrice() {
                return carPrice;
            }

            public void setCarPrice(int carPrice) {
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

            public int getCrPrice() {
                return crPrice;
            }

            public void setCrPrice(int crPrice) {
                this.crPrice = crPrice;
            }

            public int getFangPrice() {
                return fangPrice;
            }

            public void setFangPrice(int fangPrice) {
                this.fangPrice = fangPrice;
            }

            public int getJcrPrice() {
                return jcrPrice;
            }

            public void setJcrPrice(int jcrPrice) {
                this.jcrPrice = jcrPrice;
            }

            public int getJxhPrice() {
                return jxhPrice;
            }

            public void setJxhPrice(int jxhPrice) {
                this.jxhPrice = jxhPrice;
            }

            public int getXhPrice() {
                return xhPrice;
            }

            public void setXhPrice(int xhPrice) {
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
            private int maxSubtract;//最高减额度
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

            public int getMaxSubtract() {
                return maxSubtract;
            }

            public void setMaxSubtract(int maxSubtract) {
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

        public static class LineRouteListBean {

            private String accommodation;//住宿
            private AccommodationCoorBean accommodationCoor;//
            private String breakfast;//早餐
            private String car;//交通计划
            private String carXy;//交通计划坐标逗号分隔
            private String content;//内容
            private int dayNum;//第几天
            private String dinner;//晚餐
            private EndBean end;//
            private EndCoorBean endCoor;//
            private int hotelId;//酒店Id
            private int id;//
            private int lineId;//路线ID
            private String lunch;//午餐
            private StaginCoorBean staginCoor;//
            private StagingBean staging;//
            private StartBean start;//
            private StartCoorBean startCoor;//
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

            public void setDayNum(int dayNum) {
                this.dayNum = dayNum;
            }

            public String getDinner() {
                return dinner;
            }

            public void setDinner(String dinner) {
                this.dinner = dinner;
            }

            public EndBean getEnd() {
                return end;
            }

            public void setEnd(EndBean end) {
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

            public StagingBean getStaging() {
                return staging;
            }

            public void setStaging(StagingBean staging) {
                this.staging = staging;
            }

            public StartBean getStart() {
                return start;
            }

            public void setStart(StartBean start) {
                this.start = start;
            }

            public StartCoorBean getStartCoor() {
                return startCoor;
            }

            public void setStartCoor(StartCoorBean startCoor) {
                this.startCoor = startCoor;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public List<SpotsListBean> getSpotsList() {
                return spotsList;
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

            public static class EndBean {

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

            public static class StagingBean {

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

            public static class StartBean {

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

            public static class StartCoorBean {

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
                private CoordinateBean coordinate;//经纬度
                private String title;//标题
                private List<String> images;//图集

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public CoordinateBean getCoordinate() {
                    return coordinate;
                }

                public void setCoordinate(CoordinateBean coordinate) {
                    this.coordinate = coordinate;
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

                public static class CoordinateBean {

//                    private int latitude;//纬度
//                    private int longitude;//经度
//
//                    public int getLatitude() {
//                        return latitude;
//                    }
//
//                    public void setLatitude(int latitude) {
//                        this.latitude = latitude;
//                    }
//
//                    public int getLongitude() {
//                        return longitude;
//                    }
//
//                    public void setLongitude(int longitude) {
//                        this.longitude = longitude;
//                    }
                }
            }
        }

        public static class RecommendsBean {

            private String area;
            private String city;
            private String departTime;
            private int id;
            private String language;
            private String lineName;
            private String photoUrl;
            private int price;
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

            public int getPrice() {
                return price;
            }

            public void setPrice(int price) {
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
            private String href;
            private String thumb;
            private String type;
            private String url;

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
    }
}
