package cn.xmzt.www.selfdrivingtools.bean;

import android.text.TextUtils;

import java.util.List;

import cn.xmzt.www.intercom.bean.GroupMemberBean;
import cn.xmzt.www.utils.TimeUtil;

public class TourTripDetailNewBean {
    public String cfd="出发地：";
    public String mdd="目的地：";
    /**
     * lineId : 38
     * groupId : 2714346281
     * departDate : 2019-12-31
     * endDate : 2020-01-03
     * state : 1
     * lineName : 芙蓉镇30年前就是“网红”这里的夜景超好看！
     * photoUrl : https://pf.xmzt.cn/img/20191017/de9f2a3399d046cb93cfd1909fde4896.gif
     * departArea : {"areaCode":"430000","parentCode":"0","treeNames":"湖南省","areaName":"湖南省","areaType":1}
     * arrivalArea : {"areaCode":"433127","parentCode":"433100","treeNames":"湖南省/湘西土家族苗族自治州/永顺县","areaName":"永顺县","areaType":3}
     * viaInfo : 边城，红石林，不二门森林温泉
     * lineRouteListVO : [{"id":121,"lineId":38,"title":"各地区-边城镇-芙蓉镇","dayNum":1,"date":"2019-12-31","detailVOList":[{"dataType":2,"spot":{"title":"边城","content":"这里是大文豪沈从文笔下《边城》的原型地\n这里是秀山苗族土家族等多民族的聚居点\n这里的建筑均为百年建筑\n这里就是边城洪安","images":"[{\"url\":\"img/20191017/14731e742cb74821aacfd26bfc72d6e9.jpg\"}]","imageArray":["https://pf.xmzt.cn/img/20191017/14731e742cb74821aacfd26bfc72d6e9.jpg"]}},{"dataType":1,"content":{"content":"08:00 指定地点集合，贴车贴，发放对讲机，出发前往边城镇（450KM，车程约5.5小时）12:00 中餐自理\r\n14:30 乘坐翠翠当年摆动的拉拉渡船，荡过那条清澈至底的流在两山之间的酉水河流，对岸就是重庆秀山的洪安古镇，穿过小巷会看到立在桥边的界碑。桥的另一头是贵州，河边的吊角楼，郁郁的青山，一切是那么的涤荡身心。\r\n16:30 乘车前往芙蓉镇（103KM，车程约1.5小时）\r\n18:00 抵达酒店，办理入住\r\n18:30 晚餐\r\n20:00 自行前往芙蓉镇景区欣赏芙蓉镇夜景"}},{"dataType":5,"staging":{"areaId":"430100","coor":"112.939814,28.226081","address":"市政府","areaVO":{"areaCode":"430100","parentCode":"430000","treeNames":"湖南省/长沙市","areaName":"长沙市","areaType":2},"coordinateVO":{"longitude":112.939814,"latitude":28.226081}}},{"dataType":6,"start":{"areaId":"460000","coor":"110.349228,20.017377","address":"长沙市岳麓大道218号","areaVO":{"areaCode":"460000","parentCode":"0","treeNames":"海南省","areaName":"海南省","areaType":1},"coordinateVO":{"longitude":110.349228,"latitude":20.017377}}},{"dataType":7,"end":{"areaId":"433127","coor":"109.974459,28.767369","address":"芙蓉镇","areaVO":{"areaCode":"433127","parentCode":"433100","treeNames":"湖南省/湘西土家族苗族自治州/永顺县","areaName":"永顺县","areaType":3},"coordinateVO":{"longitude":109.974459,"latitude":28.767369}}},{"dataType":3,"car":{"car":"路况良好"}},{"dataType":4,"hotel":{"areaId":"433127","coor":"109.851254,29.001440","address":"","areaVO":{"areaCode":"433127","parentCode":"433100","treeNames":"湖南省/湘西土家族苗族自治州/永顺县","areaName":"永顺县","areaType":3},"coordinateVO":{"longitude":109.851254,"latitude":29.00144},"accommodation":"芙蓉镇之恋/边城故事酒店"}},{"dataType":8,"breakfast":{"meal":"自理"}},{"dataType":9,"lunch":{"meal":"自理"}},{"dataType":10,"dinner":{"meal":"统一用餐"}}]},{"id":122,"lineId":38,"title":"芙蓉镇-红石林-老司城-司城山水酒店-野溪温泉","dayNum":2,"date":"2020-01-01","detailVOList":[{"dataType":2,"spot":{"title":"红石林","content":"整个景区融红、秀、峻、奇、绝、古于一身，堪称\u201c武陵第一奇观\u201d, 进入石林区，石峰林立，万峰迭嶂，千姿百态斯特地貌向世人展现了其独特而迷人的风采。","images":"[{\"url\":\"img/20191017/bb9dfaac76e14b4e9a704d20b1581f67.jpg\"}]","imageArray":["https://pf.xmzt.cn/img/20191017/bb9dfaac76e14b4e9a704d20b1581f67.jpg"]}},{"dataType":1,"content":{"content":"08:00 起床、早餐\r\n09:00 乘车前往【红石林】（22KM，车程约30分钟）\r\n09:30 游玩【红石林景区】，整个景区融红、秀、峻、奇、绝、古于一身，堪称\u201c武陵第一奇观\u201d, 进入石林区，石峰林立，万峰迭嶂，千姿百态斯特地貌向世人展现了其独特而迷人的风采。\r\n11:30 中餐\r\n12:30 乘车前往【老司城】（84KM，车程约2小时），\r\n14:30 【老司城】参观，老司城位于猛洞河漂流景区上游，这里是自唐、宋、元、明、清朝代以来留下的众多古文化遗址，也是湘西历代土家族土司王经营了八百多年的历代古都。\r\n16:30 乘车前往司城山水酒店（18KM，车程约30分钟）\r\n17:00 抵达酒店，办理入住\r\n18:00 晚餐\r\n19:00可自行前往不二门景区享受纯天然野溪温泉（请提前备好换洗衣服）"}},{"dataType":5,"staging":{"areaId":"433127","coor":"109.851254,29.001440","address":"酒店","areaVO":{"areaCode":"433127","parentCode":"433100","treeNames":"湖南省/湘西土家族苗族自治州/永顺县","areaName":"永顺县","areaType":3},"coordinateVO":{"longitude":109.851254,"latitude":29.00144}}},{"dataType":6,"start":{"areaId":"433127","coor":"109.734918,28.322341","address":"酒店停车场","areaVO":{"areaCode":"433127","parentCode":"433100","treeNames":"湖南省/湘西土家族苗族自治州/永顺县","areaName":"永顺县","areaType":3},"coordinateVO":{"longitude":109.734918,"latitude":28.322341}}},{"dataType":7,"end":{"areaId":"433127","coor":"109.976571,28.994077","address":"老司城","areaVO":{"areaCode":"433127","parentCode":"433100","treeNames":"湖南省/湘西土家族苗族自治州/永顺县","areaName":"永顺县","areaType":3},"coordinateVO":{"longitude":109.976571,"latitude":28.994077}}},{"dataType":3,"car":{"car":"道路畅通"}},{"dataType":4,"hotel":{"areaId":"433127","coor":"109.844283,28.978652","address":"永顺灵溪镇培英路188号","areaVO":{"areaCode":"433127","parentCode":"433100","treeNames":"湖南省/湘西土家族苗族自治州/永顺县","areaName":"永顺县","areaType":3},"coordinateVO":{"longitude":109.844283,"latitude":28.978652},"accommodation":"司城山水酒店"}},{"dataType":8,"breakfast":{"meal":"酒店自助餐"}},{"dataType":9,"lunch":{"meal":"统一用餐"}},{"dataType":10,"dinner":{"meal":"统一用餐"}}]},{"id":123,"lineId":38,"title":"不二门森林公园-各地区","dayNum":3,"date":"2020-01-02","detailVOList":[{"dataType":2,"spot":{"title":"不二门森林温泉","content":"不二门野溪温泉水温常年保持在39.8\u201441摄氏度之间，富含多种矿物质，另外还含有多种微量元素。此处常年水清透明，加之四周环境清幽，绿水青山，景气宜人，风尘仆仆而至的游客，在此一浴，无不感到疲劳顿去，是休闲、娱乐的绝佳去处。","images":"[{\"url\":\"img/20191017/0d779cd2368f4360ab7ef6307dd62e6f.png\"}]","imageArray":["https://pf.xmzt.cn/img/20191017/0d779cd2368f4360ab7ef6307dd62e6f.png"]}},{"dataType":1,"content":{"content":"08:00 起床、早餐\r\n09:00 游玩【不二门森林公园】，《乌龙山剿匪记》、与《湘西剿匪记》均在此拍摄。不二门取自佛家\"不二法门\"之意，正门的右壁上有沈从文先生题写的\"石门天凿\"四字，为沈先生生前最后一次回乡时题写。\r\n11:00 返程，散团，结束愉快行程！"}},{"dataType":5,"staging":{"areaId":"433127","coor":"109.851254,29.001440","address":"酒店","areaVO":{"areaCode":"433127","parentCode":"433100","treeNames":"湖南省/湘西土家族苗族自治州/永顺县","areaName":"永顺县","areaType":3},"coordinateVO":{"longitude":109.851254,"latitude":29.00144}}},{"dataType":6,"start":{"areaId":"433127","coor":"109.734918,28.322341","address":"酒店停车场","areaVO":{"areaCode":"433127","parentCode":"433100","treeNames":"湖南省/湘西土家族苗族自治州/永顺县","areaName":"永顺县","areaType":3},"coordinateVO":{"longitude":109.734918,"latitude":28.322341}}},{"dataType":7,"end":{"areaId":"433127","coor":"109.848028,28.984494","address":"不二门森林公园","areaVO":{"areaCode":"433127","parentCode":"433100","treeNames":"湖南省/湘西土家族苗族自治州/永顺县","areaName":"永顺县","areaType":3},"coordinateVO":{"longitude":109.848028,"latitude":28.984494}}},{"dataType":3,"car":{"car":"道路畅通"}},{"dataType":4,"hotel":{"areaId":"","address":"","accommodation":"无"}},{"dataType":8,"breakfast":{"meal":"酒店自助餐"}},{"dataType":9,"lunch":{"meal":"自理"}},{"dataType":10,"dinner":{"meal":"自理"}}]}]
     * groupMemberList : [{"userId":186509,"userName":"嗯嗯嗯","image":"https://xmzt-user.oss-cn-shanghai.aliyuncs.com/20191108/9396152314224f67beb654cd0b1ca341.jpg","phone":"158****0006","orderId":"1134710638692798464","role":1,"leader":false,"driver":true,"official":false,"licencePlate":"皖N34567","payingUser":true,"createDate":"2019-11-07 17:36:05"},{"userId":186487,"userName":"hvvggjhv get","image":"https://xmzt-user.oss-cn-shanghai.aliyuncs.com/20191115/bc0ac77efcde481790281dc357112956.jpg","phone":"158****0000","role":0,"leader":true,"driver":true,"official":false,"licencePlate":"川D456GR","payingUser":true,"createDate":"2019-11-08 10:51:43"},{"userId":186544,"userName":"嘻嘻嘻","image":"https://pf.xmzt.cn//img/20190924/afc2e77e7a7f4a58a7555dca979d7cbc.jpg","phone":"134****1207","orderId":"1134710638692798464","role":0,"leader":false,"driver":true,"official":false,"licencePlate":"渝P4632E","payingUser":true,"createDate":"2019-11-07 17:36:05"},{"userId":186836,"userName":"150****0000","image":"https://pf.xmzt.cn/head/defaultHeader.png","phone":"150****0000","role":0,"leader":false,"driver":false,"official":false,"payingUser":false,"createDate":"2019-11-08 17:03:47"},{"userId":186413,"userName":"similar718","image":"https://xmzt-user.oss-cn-shanghai.aliyuncs.com/20191109/3cb02803eda94350b78879657373c2b0.jpg","phone":"134****1206","role":0,"leader":false,"driver":false,"official":false,"payingUser":false,"createDate":"2019-11-15 11:34:53"}]
     * join : true
     * groupAdminPhone : 15800000006
     */

    private int lineId;
    private String groupId;
    private String departDate;
    private String endDate;
    private int state;
    private String lineName;
    private String photoUrl;
    private DepartAreaBean departArea;
    private ArrivalAreaBean arrivalArea;
    private String viaInfo;
    private boolean join;
    private String groupAdminPhone;
    private List<LineRouteListVOBean> lineRouteListVO;
    private List<GroupMemberBean> groupMemberList;

    public int getLineId() {
        return lineId;
    }

    public void setLineId(int lineId) {
        this.lineId = lineId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getDepartDate() {
        return departDate;
    }

    public void setDepartDate(String departDate) {
        this.departDate = departDate;
    }

    public String getStartEndDate() {
        if(departDate==null){
            departDate="";
        }
        if(endDate==null){
            endDate="";
        }
        String startTime= TimeUtil.stringDateToString(departDate.substring(0,10),"yyyy-MM-dd","yyyy.MM.dd");
        String endTime=TimeUtil.stringDateToString(endDate.substring(0,10),"yyyy-MM-dd","yyyy.MM.dd");
        return startTime+"—"+endTime;
    }
    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getState() {
        return state;
    }

    public String getStateName() {
        //状态：1待出行 2出行中 3已结束
        if(state==1){
            return "待出行";
        }else if(state==2){
            return "出行中";
        }else if(state==3){
            return "已结束";
        }
        return "待出行";
    }
    public void setState(int state) {
        this.state = state;
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

    public DepartAreaBean getDepartArea() {
        return departArea;
    }

    public void setDepartArea(DepartAreaBean departArea) {
        this.departArea = departArea;
    }

    public ArrivalAreaBean getArrivalArea() {
        return arrivalArea;
    }

    public void setArrivalArea(ArrivalAreaBean arrivalArea) {
        this.arrivalArea = arrivalArea;
    }

    public String getViaInfoStr() {
        if(TextUtils.isEmpty(viaInfo)){
            return "";
        }
        return "("+viaInfo+")";
    }

    public String getViaInfo(){
        return viaInfo;
    }

    public void setViaInfo(String viaInfo) {
        this.viaInfo = viaInfo;
    }

    public boolean isJoin() {
        return join;
    }

    public void setJoin(boolean join) {
        this.join = join;
    }

    public String getGroupAdminPhone() {
        return groupAdminPhone;
    }

    public void setGroupAdminPhone(String groupAdminPhone) {
        this.groupAdminPhone = groupAdminPhone;
    }

    public List<LineRouteListVOBean> getLineRouteListVO() {
        return lineRouteListVO;
    }

    public void setLineRouteListVO(List<LineRouteListVOBean> lineRouteListVO) {
        this.lineRouteListVO = lineRouteListVO;
    }

    public List<GroupMemberBean> getGroupMemberList() {
        return groupMemberList;
    }

    public void setGroupMemberList(List<GroupMemberBean> groupMemberList) {
        this.groupMemberList = groupMemberList;
    }
    public int getGroupMemberCounts() {
        if(groupMemberList!=null){
            return groupMemberList.size();
        }
        return 0;
    }
    public String getGroupMemberCount() {
        if(groupMemberList!=null){
            return "已结伴("+groupMemberList.size()+"人)";
        }
        return "已结伴(0人)";
    }
    public static class DepartAreaBean {
        /**
         * areaCode : 430000
         * parentCode : 0
         * treeNames : 湖南省
         * areaName : 湖南省
         * areaType : 1
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

    public static class ArrivalAreaBean {
        /**
         * areaCode : 433127
         * parentCode : 433100
         * treeNames : 湖南省/湘西土家族苗族自治州/永顺县
         * areaName : 永顺县
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

    public static class LineRouteListVOBean {
        /**
         * id : 121
         * lineId : 38
         * title : 各地区-边城镇-芙蓉镇
         * dayNum : 1
         * date : 2019-12-31
         * detailVOList : [{"dataType":2,"spot":{"title":"边城","content":"这里是大文豪沈从文笔下《边城》的原型地\n这里是秀山苗族土家族等多民族的聚居点\n这里的建筑均为百年建筑\n这里就是边城洪安","images":"[{\"url\":\"img/20191017/14731e742cb74821aacfd26bfc72d6e9.jpg\"}]","imageArray":["https://pf.xmzt.cn/img/20191017/14731e742cb74821aacfd26bfc72d6e9.jpg"]}},{"dataType":1,"content":{"content":"08:00 指定地点集合，贴车贴，发放对讲机，出发前往边城镇（450KM，车程约5.5小时）12:00 中餐自理\r\n14:30 乘坐翠翠当年摆动的拉拉渡船，荡过那条清澈至底的流在两山之间的酉水河流，对岸就是重庆秀山的洪安古镇，穿过小巷会看到立在桥边的界碑。桥的另一头是贵州，河边的吊角楼，郁郁的青山，一切是那么的涤荡身心。\r\n16:30 乘车前往芙蓉镇（103KM，车程约1.5小时）\r\n18:00 抵达酒店，办理入住\r\n18:30 晚餐\r\n20:00 自行前往芙蓉镇景区欣赏芙蓉镇夜景"}},{"dataType":5,"staging":{"areaId":"430100","coor":"112.939814,28.226081","address":"市政府","areaVO":{"areaCode":"430100","parentCode":"430000","treeNames":"湖南省/长沙市","areaName":"长沙市","areaType":2},"coordinateVO":{"longitude":112.939814,"latitude":28.226081}}},{"dataType":6,"start":{"areaId":"460000","coor":"110.349228,20.017377","address":"长沙市岳麓大道218号","areaVO":{"areaCode":"460000","parentCode":"0","treeNames":"海南省","areaName":"海南省","areaType":1},"coordinateVO":{"longitude":110.349228,"latitude":20.017377}}},{"dataType":7,"end":{"areaId":"433127","coor":"109.974459,28.767369","address":"芙蓉镇","areaVO":{"areaCode":"433127","parentCode":"433100","treeNames":"湖南省/湘西土家族苗族自治州/永顺县","areaName":"永顺县","areaType":3},"coordinateVO":{"longitude":109.974459,"latitude":28.767369}}},{"dataType":3,"car":{"car":"路况良好"}},{"dataType":4,"hotel":{"areaId":"433127","coor":"109.851254,29.001440","address":"","areaVO":{"areaCode":"433127","parentCode":"433100","treeNames":"湖南省/湘西土家族苗族自治州/永顺县","areaName":"永顺县","areaType":3},"coordinateVO":{"longitude":109.851254,"latitude":29.00144},"accommodation":"芙蓉镇之恋/边城故事酒店"}},{"dataType":8,"breakfast":{"meal":"自理"}},{"dataType":9,"lunch":{"meal":"自理"}},{"dataType":10,"dinner":{"meal":"统一用餐"}}]
         */

        private int id;
        private int lineId;
        private String title;
        private int dayNum;
        private String date;
        private List<DetailVOListBean> detailVOList;

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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getDayNum() {
            return dayNum;
        }

        public void setDayNum(int dayNum) {
            this.dayNum = dayNum;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDateMonth() {
            if(date==null){
                return "";
            }
            return TimeUtil.stringDateToString(date,"yyyy-MM-dd","MM");
        }
        public String getDateDay() {
            if(date==null){
                return "";
            }
            return "/"+TimeUtil.stringDateToString(date,"yyyy-MM-dd","dd");
        }


        public List<DetailVOListBean> getDetailVOList() {
            return detailVOList;
        }

        public void setDetailVOList(List<DetailVOListBean> detailVOList) {
            this.detailVOList = detailVOList;
        }

        public static class DetailVOListBean {
            /**
             * dataType : 2
             * spot : {"title":"边城","content":"这里是大文豪沈从文笔下《边城》的原型地\n这里是秀山苗族土家族等多民族的聚居点\n这里的建筑均为百年建筑\n这里就是边城洪安","images":"[{\"url\":\"img/20191017/14731e742cb74821aacfd26bfc72d6e9.jpg\"}]","imageArray":["https://pf.xmzt.cn/img/20191017/14731e742cb74821aacfd26bfc72d6e9.jpg"]}
             * content : {"content":"08:00 指定地点集合，贴车贴，发放对讲机，出发前往边城镇（450KM，车程约5.5小时）12:00 中餐自理\r\n14:30 乘坐翠翠当年摆动的拉拉渡船，荡过那条清澈至底的流在两山之间的酉水河流，对岸就是重庆秀山的洪安古镇，穿过小巷会看到立在桥边的界碑。桥的另一头是贵州，河边的吊角楼，郁郁的青山，一切是那么的涤荡身心。\r\n16:30 乘车前往芙蓉镇（103KM，车程约1.5小时）\r\n18:00 抵达酒店，办理入住\r\n18:30 晚餐\r\n20:00 自行前往芙蓉镇景区欣赏芙蓉镇夜景"}
             * staging : {"areaId":"430100","coor":"112.939814,28.226081","address":"市政府","areaVO":{"areaCode":"430100","parentCode":"430000","treeNames":"湖南省/长沙市","areaName":"长沙市","areaType":2},"coordinateVO":{"longitude":112.939814,"latitude":28.226081}}
             * start : {"areaId":"460000","coor":"110.349228,20.017377","address":"长沙市岳麓大道218号","areaVO":{"areaCode":"460000","parentCode":"0","treeNames":"海南省","areaName":"海南省","areaType":1},"coordinateVO":{"longitude":110.349228,"latitude":20.017377}}
             * end : {"areaId":"433127","coor":"109.974459,28.767369","address":"芙蓉镇","areaVO":{"areaCode":"433127","parentCode":"433100","treeNames":"湖南省/湘西土家族苗族自治州/永顺县","areaName":"永顺县","areaType":3},"coordinateVO":{"longitude":109.974459,"latitude":28.767369}}
             * car : {"car":"路况良好"}
             * hotel : {"areaId":"433127","coor":"109.851254,29.001440","address":"","areaVO":{"areaCode":"433127","parentCode":"433100","treeNames":"湖南省/湘西土家族苗族自治州/永顺县","areaName":"永顺县","areaType":3},"coordinateVO":{"longitude":109.851254,"latitude":29.00144},"accommodation":"芙蓉镇之恋/边城故事酒店"}
             * breakfast : {"meal":"自理"}
             * lunch : {"meal":"自理"}
             * dinner : {"meal":"统一用餐"}
             */

            private int dataType;
            private SpotBean spot;
            private ContentBean content;
            private StagingBean staging;
            private StartBean start;
            private EndBean end;
            private CarBean car;
            private HotelBean hotel;
            private BreakfastBean breakfast;
            private LunchBean lunch;
            private DinnerBean dinner;
            private FirstBean first;
            private EatBean eat;

            public EatBean getEat() {
                return eat;
            }

            public void setEat(EatBean eat) {
                this.eat = eat;
            }

            public FirstBean getFirst() {
                return first;
            }

            public void setFirst(FirstBean first) {
                this.first = first;
            }

            public int getDataType() {
                return dataType;
            }

            public void setDataType(int dataType) {
                this.dataType = dataType;
            }

            public SpotBean getSpot() {
                return spot;
            }

            public void setSpot(SpotBean spot) {
                this.spot = spot;
            }

            public ContentBean getContent() {
                return content;
            }

            public void setContent(ContentBean content) {
                this.content = content;
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

            public EndBean getEnd() {
                return end;
            }

            public void setEnd(EndBean end) {
                this.end = end;
            }

            public CarBean getCar() {
                return car;
            }

            public void setCar(CarBean car) {
                this.car = car;
            }

            public HotelBean getHotel() {
                return hotel;
            }

            public void setHotel(HotelBean hotel) {
                this.hotel = hotel;
            }

            public BreakfastBean getBreakfast() {
                return breakfast;
            }

            public void setBreakfast(BreakfastBean breakfast) {
                this.breakfast = breakfast;
            }

            public LunchBean getLunch() {
                return lunch;
            }

            public void setLunch(LunchBean lunch) {
                this.lunch = lunch;
            }

            public DinnerBean getDinner() {
                return dinner;
            }

            public void setDinner(DinnerBean dinner) {
                this.dinner = dinner;
            }

            public static class SpotBean {
                /**
                 * title : 边城
                 * content : 这里是大文豪沈从文笔下《边城》的原型地
                 这里是秀山苗族土家族等多民族的聚居点
                 这里的建筑均为百年建筑
                 这里就是边城洪安
                 * images : [{"url":"img/20191017/14731e742cb74821aacfd26bfc72d6e9.jpg"}]
                 * imageArray : ["https://pf.xmzt.cn/img/20191017/14731e742cb74821aacfd26bfc72d6e9.jpg"]
                 */
                private String title;
                private String content;
                private String images;
                private String address; // 详细地址
                private String areaId; // 区域ID
                private int spotId; // 系统景点id
                private int scenicId; // 系统景区ID
                private String coor; // 坐标
                private List<String> imageArray;
                private CoordinateVOBean coordinateVO;
                private AreaVOBean areaVO;

                public AreaVOBean getAreaVO() {
                    return areaVO;
                }

                public void setAreaVO(AreaVOBean areaVO) {
                    this.areaVO = areaVO;
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

                public int getSpotId() {
                    return spotId;
                }

                public void setSpotId(int spotId) {
                    this.spotId = spotId;
                }

                public int getScenicId() {
                    return scenicId;
                }

                public void setScenicId(int scenicId) {
                    this.scenicId = scenicId;
                }

                public String getCoor() {
                    return coor;
                }

                public void setCoor(String coor) {
                    this.coor = coor;
                }

                public CoordinateVOBean getCoordinateVO() {
                    return coordinateVO;
                }

                public void setCoordinateVO(CoordinateVOBean coordinateVO) {
                    this.coordinateVO = coordinateVO;
                }

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

                public String getImages() {
                    return images;
                }

                public void setImages(String images) {
                    this.images = images;
                }

                public List<String> getImageArray() {
                    return imageArray;
                }

                public void setImageArray(List<String> imageArray) {
                    this.imageArray = imageArray;
                }

                public static class CoordinateVOBean {
                    /**
                     * longitude : 112.939814
                     * latitude : 28.226081
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

                public static class AreaVOBean {
                    /**
                     * areaCode : 430100
                     * parentCode : 430000
                     * treeNames : 湖南省/长沙市
                     * areaName : 长沙市
                     * areaType : 2
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
            }

            public static class ContentBean {
                /**
                 * content : 08:00 指定地点集合，贴车贴，发放对讲机，出发前往边城镇（450KM，车程约5.5小时）12:00 中餐自理
                 14:30 乘坐翠翠当年摆动的拉拉渡船，荡过那条清澈至底的流在两山之间的酉水河流，对岸就是重庆秀山的洪安古镇，穿过小巷会看到立在桥边的界碑。桥的另一头是贵州，河边的吊角楼，郁郁的青山，一切是那么的涤荡身心。
                 16:30 乘车前往芙蓉镇（103KM，车程约1.5小时）
                 18:00 抵达酒店，办理入住
                 18:30 晚餐
                 20:00 自行前往芙蓉镇景区欣赏芙蓉镇夜景
                 */

                private String content;

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }
            }

            public static class FirstBean {
                private int day;
                private String date;
                private String content;

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public int getDay() {
                    return day;
                }

                public void setDay(int day) {
                    this.day = day;
                }

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public FirstBean(int day, String date, String content) {
                    this.day = day;
                    this.date = date;
                    this.content = content;
                }
            }

            public static class EatBean {
                private String breakfast;
                private String lunch;
                private String dinner;

                public String getBreakfast() {
                    return breakfast;
                }

                public void setBreakfast(String breakfast) {
                    this.breakfast = breakfast;
                }

                public String getLunch() {
                    return lunch;
                }

                public void setLunch(String lunch) {
                    this.lunch = lunch;
                }

                public String getDinner() {
                    return dinner;
                }

                public void setDinner(String dinner) {
                    this.dinner = dinner;
                }
            }

            public static class StagingBean {
                /**
                 * areaId : 430100
                 * coor : 112.939814,28.226081
                 * address : 市政府
                 * areaVO : {"areaCode":"430100","parentCode":"430000","treeNames":"湖南省/长沙市","areaName":"长沙市","areaType":2}
                 * coordinateVO : {"longitude":112.939814,"latitude":28.226081}
                 */

                private String areaId;
                private String coor;
                private String address;
                private AreaVOBean areaVO;
                private CoordinateVOBean coordinateVO;

                public String getAreaId() {
                    return areaId;
                }

                public void setAreaId(String areaId) {
                    this.areaId = areaId;
                }

                public String getCoor() {
                    return coor;
                }

                public void setCoor(String coor) {
                    this.coor = coor;
                }

                public String getAddress() {
                    return address;
                }

                public void setAddress(String address) {
                    this.address = address;
                }

                public AreaVOBean getAreaVO() {
                    return areaVO;
                }

                public void setAreaVO(AreaVOBean areaVO) {
                    this.areaVO = areaVO;
                }

                public CoordinateVOBean getCoordinateVO() {
                    return coordinateVO;
                }

                public void setCoordinateVO(CoordinateVOBean coordinateVO) {
                    this.coordinateVO = coordinateVO;
                }

                public static class AreaVOBean {
                    /**
                     * areaCode : 430100
                     * parentCode : 430000
                     * treeNames : 湖南省/长沙市
                     * areaName : 长沙市
                     * areaType : 2
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

                public static class CoordinateVOBean {
                    /**
                     * longitude : 112.939814
                     * latitude : 28.226081
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
            }

            public static class StartBean {
                /**
                 * areaId : 460000
                 * coor : 110.349228,20.017377
                 * address : 长沙市岳麓大道218号
                 * areaVO : {"areaCode":"460000","parentCode":"0","treeNames":"海南省","areaName":"海南省","areaType":1}
                 * coordinateVO : {"longitude":110.349228,"latitude":20.017377}
                 */

                private String areaId;
                private String coor;
                private String address;
                private AreaVOBeanX areaVO;
                private CoordinateVOBeanX coordinateVO;

                public String getAreaId() {
                    return areaId;
                }

                public void setAreaId(String areaId) {
                    this.areaId = areaId;
                }

                public String getCoor() {
                    return coor;
                }

                public void setCoor(String coor) {
                    this.coor = coor;
                }

                public String getAddress() {
                    return address;
                }

                public void setAddress(String address) {
                    this.address = address;
                }

                public AreaVOBeanX getAreaVO() {
                    return areaVO;
                }

                public void setAreaVO(AreaVOBeanX areaVO) {
                    this.areaVO = areaVO;
                }

                public CoordinateVOBeanX getCoordinateVO() {
                    return coordinateVO;
                }

                public void setCoordinateVO(CoordinateVOBeanX coordinateVO) {
                    this.coordinateVO = coordinateVO;
                }

                public static class AreaVOBeanX {
                    /**
                     * areaCode : 460000
                     * parentCode : 0
                     * treeNames : 海南省
                     * areaName : 海南省
                     * areaType : 1
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

                public static class CoordinateVOBeanX {
                    /**
                     * longitude : 110.349228
                     * latitude : 20.017377
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
            }

            public static class EndBean {
                /**
                 * areaId : 433127
                 * coor : 109.974459,28.767369
                 * address : 芙蓉镇
                 * areaVO : {"areaCode":"433127","parentCode":"433100","treeNames":"湖南省/湘西土家族苗族自治州/永顺县","areaName":"永顺县","areaType":3}
                 * coordinateVO : {"longitude":109.974459,"latitude":28.767369}
                 */

                private String areaId;
                private String coor;
                private String address;
                private AreaVOBeanXX areaVO;
                private CoordinateVOBeanXX coordinateVO;

                public String getAreaId() {
                    return areaId;
                }

                public void setAreaId(String areaId) {
                    this.areaId = areaId;
                }

                public String getCoor() {
                    return coor;
                }

                public void setCoor(String coor) {
                    this.coor = coor;
                }

                public String getAddress() {
                    return address;
                }

                public void setAddress(String address) {
                    this.address = address;
                }

                public AreaVOBeanXX getAreaVO() {
                    return areaVO;
                }

                public void setAreaVO(AreaVOBeanXX areaVO) {
                    this.areaVO = areaVO;
                }

                public CoordinateVOBeanXX getCoordinateVO() {
                    return coordinateVO;
                }

                public void setCoordinateVO(CoordinateVOBeanXX coordinateVO) {
                    this.coordinateVO = coordinateVO;
                }

                public static class AreaVOBeanXX {
                    /**
                     * areaCode : 433127
                     * parentCode : 433100
                     * treeNames : 湖南省/湘西土家族苗族自治州/永顺县
                     * areaName : 永顺县
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

                public static class CoordinateVOBeanXX {
                    /**
                     * longitude : 109.974459
                     * latitude : 28.767369
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
            }

            public static class CarBean {
                /**
                 * car : 路况良好
                 */

                private String car;

                public String getCar() {
                    return car;
                }

                public void setCar(String car) {
                    this.car = car;
                }
            }

            public static class HotelBean {
                /**
                 * areaId : 433127
                 * coor : 109.851254,29.001440
                 * address :
                 * areaVO : {"areaCode":"433127","parentCode":"433100","treeNames":"湖南省/湘西土家族苗族自治州/永顺县","areaName":"永顺县","areaType":3}
                 * coordinateVO : {"longitude":109.851254,"latitude":29.00144}
                 * accommodation : 芙蓉镇之恋/边城故事酒店
                 */

                private String areaId;
                private String coor;
                private String address;
                private AreaVOBeanXXX areaVO;
                private CoordinateVOBeanXXX coordinateVO;
                private String accommodation;

                public String getAreaId() {
                    return areaId;
                }

                public void setAreaId(String areaId) {
                    this.areaId = areaId;
                }

                public String getCoor() {
                    return coor;
                }

                public void setCoor(String coor) {
                    this.coor = coor;
                }

                public String getAddress() {
                    return address;
                }

                public void setAddress(String address) {
                    this.address = address;
                }

                public AreaVOBeanXXX getAreaVO() {
                    return areaVO;
                }

                public void setAreaVO(AreaVOBeanXXX areaVO) {
                    this.areaVO = areaVO;
                }

                public CoordinateVOBeanXXX getCoordinateVO() {
                    return coordinateVO;
                }

                public void setCoordinateVO(CoordinateVOBeanXXX coordinateVO) {
                    this.coordinateVO = coordinateVO;
                }

                public String getAccommodation() {
                    return accommodation;
                }

                public void setAccommodation(String accommodation) {
                    this.accommodation = accommodation;
                }

                public static class AreaVOBeanXXX {
                    /**
                     * areaCode : 433127
                     * parentCode : 433100
                     * treeNames : 湖南省/湘西土家族苗族自治州/永顺县
                     * areaName : 永顺县
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

                public static class CoordinateVOBeanXXX {
                    /**
                     * longitude : 109.851254
                     * latitude : 29.00144
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
            }

            public class BreakfastBean {
                /**
                 * meal : 自理
                 */

                private String meal;

                public String getMeal() {
                    return meal;
                }

                public void setMeal(String meal) {
                    this.meal = meal;
                    setBreakFast(meal);
                }
                private String areaId;
                private String coor;
                private String address;
                private AreaVOBeanXXX areaVO;
                private CoordinateVOBeanXXX coordinateVO;

                public String getAreaId() {
                    return areaId;
                }

                public void setAreaId(String areaId) {
                    this.areaId = areaId;
                }

                public String getCoor() {
                    return coor;
                }

                public void setCoor(String coor) {
                    this.coor = coor;
                }

                public String getAddress() {
                    return address;
                }

                public void setAddress(String address) {
                    this.address = address;
                }

                public AreaVOBeanXXX getAreaVO() {
                    return areaVO;
                }

                public void setAreaVO(AreaVOBeanXXX areaVO) {
                    this.areaVO = areaVO;
                }

                public CoordinateVOBeanXXX getCoordinateVO() {
                    return coordinateVO;
                }

                public void setCoordinateVO(CoordinateVOBeanXXX coordinateVO) {
                    this.coordinateVO = coordinateVO;
                }

                public class CoordinateVOBeanXXX {
                    /**
                     * longitude : 109.851254
                     * latitude : 29.00144
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
                public class AreaVOBeanXXX {
                    /**
                     * areaCode : 433127
                     * parentCode : 433100
                     * treeNames : 湖南省/湘西土家族苗族自治州/永顺县
                     * areaName : 永顺县
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
            }

            public void setBreakFast(String data){
                eat.setBreakfast(data);
            }

            public void setLunch(String data){
                eat.setLunch(data);
            }

            public void setDinner(String data){
                eat.setDinner(data);
            }

            public class LunchBean {
                /**
                 * meal : 自理
                 */

                private String meal;

                public String getMeal() {
                    return meal;
                }

                public void setMeal(String meal) {
                    this.meal = meal;
                    setLunch(meal);
                }

                private String areaId;
                private String coor;
                private String address;
                private AreaVOBeanXXX areaVO;
                private CoordinateVOBeanXXX coordinateVO;

                public String getAreaId() {
                    return areaId;
                }

                public void setAreaId(String areaId) {
                    this.areaId = areaId;
                }

                public String getCoor() {
                    return coor;
                }

                public void setCoor(String coor) {
                    this.coor = coor;
                }

                public String getAddress() {
                    return address;
                }

                public void setAddress(String address) {
                    this.address = address;
                }

                public AreaVOBeanXXX getAreaVO() {
                    return areaVO;
                }

                public void setAreaVO(AreaVOBeanXXX areaVO) {
                    this.areaVO = areaVO;
                }

                public CoordinateVOBeanXXX getCoordinateVO() {
                    return coordinateVO;
                }

                public void setCoordinateVO(CoordinateVOBeanXXX coordinateVO) {
                    this.coordinateVO = coordinateVO;
                }

                public class CoordinateVOBeanXXX {
                    /**
                     * longitude : 109.851254
                     * latitude : 29.00144
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
                public class AreaVOBeanXXX {
                    /**
                     * areaCode : 433127
                     * parentCode : 433100
                     * treeNames : 湖南省/湘西土家族苗族自治州/永顺县
                     * areaName : 永顺县
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
            }

            public class DinnerBean {
                /**
                 * meal : 统一用餐
                 */

                private String meal;

                public String getMeal() {
                    return meal;
                }

                public void setMeal(String meal) {
                    this.meal = meal;
                    setDinner(meal);
                }

                private String areaId;
                private String coor;
                private String address;
                private AreaVOBeanXXX areaVO;
                private CoordinateVOBeanXXX coordinateVO;

                public String getAreaId() {
                    return areaId;
                }

                public void setAreaId(String areaId) {
                    this.areaId = areaId;
                }

                public String getCoor() {
                    return coor;
                }

                public void setCoor(String coor) {
                    this.coor = coor;
                }

                public String getAddress() {
                    return address;
                }

                public void setAddress(String address) {
                    this.address = address;
                }

                public AreaVOBeanXXX getAreaVO() {
                    return areaVO;
                }

                public void setAreaVO(AreaVOBeanXXX areaVO) {
                    this.areaVO = areaVO;
                }

                public CoordinateVOBeanXXX getCoordinateVO() {
                    return coordinateVO;
                }

                public void setCoordinateVO(CoordinateVOBeanXXX coordinateVO) {
                    this.coordinateVO = coordinateVO;
                }

                public class CoordinateVOBeanXXX {
                    /**
                     * longitude : 109.851254
                     * latitude : 29.00144
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
                public class AreaVOBeanXXX {
                    /**
                     * areaCode : 433127
                     * parentCode : 433100
                     * treeNames : 湖南省/湘西土家族苗族自治州/永顺县
                     * areaName : 永顺县
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
            }
        }
    }

    public static class GroupMemberListBean {
        /**
         * userId : 186509
         * userName : 嗯嗯嗯
         * image : https://xmzt-user.oss-cn-shanghai.aliyuncs.com/20191108/9396152314224f67beb654cd0b1ca341.jpg
         * phone : 158****0006
         * orderId : 1134710638692798464
         * role : 1
         * leader : false
         * driver : true
         * official : false
         * licencePlate : 皖N34567
         * payingUser : true
         * createDate : 2019-11-07 17:36:05
         */

        private int userId;
        private String userName;
        private String image;
        private String phone;
        private String orderId;
        private int role;
        private boolean leader;
        private boolean driver;
        private boolean official;
        private String licencePlate;
        private boolean payingUser;
        private String createDate;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }

        public boolean isLeader() {
            return leader;
        }

        public void setLeader(boolean leader) {
            this.leader = leader;
        }

        public boolean isDriver() {
            return driver;
        }

        public void setDriver(boolean driver) {
            this.driver = driver;
        }

        public boolean isOfficial() {
            return official;
        }

        public void setOfficial(boolean official) {
            this.official = official;
        }

        public String getLicencePlate() {
            return licencePlate;
        }

        public void setLicencePlate(String licencePlate) {
            this.licencePlate = licencePlate;
        }

        public boolean isPayingUser() {
            return payingUser;
        }

        public void setPayingUser(boolean payingUser) {
            this.payingUser = payingUser;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }
    }
}
