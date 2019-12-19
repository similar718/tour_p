package cn.xmzt.www.intercom.bean;

import android.text.TextUtils;

import cn.xmzt.www.home.bean.RecommendLineBean;
import cn.xmzt.www.utils.TimeUtil;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.List;

/**
 * 行程线路实体
 * @author Averysk
 */
public class TourTripBean {
    /**
     * 群id
     */
    private String groupId;
    /**
     * 是否官方创建
     */
    private boolean official;
    /**
     * 是否是我的群
     */
    private boolean isMy;
    /**
     * 组队人数
     */
    private int groupNumber;
    /**
     * 行程的群主的名称
     */
    private String groupAdminName;
    private int lineType;//线路类型：1自驾游（自由行程） 2跟团游（跟团出行）
    /**
     * 行程id
     */
    private int id;
    /**
     * 出发日期
     */
    private String departDate;
    /**
     * 结束日期
     */
    private String endDate;
    /**
     * 订单号
     */
    private String orderId;
    /**
     * 线路封面图片资源ID
     */
    private String photoUrl;
    /**
     * 线路id
     */
    private String lineId;
    /**
     * 线路名字
     */
    private String lineName;
    /**
     * 状态：1待出行 2出行中 3已结束
     */
    private int state;
    /**
     * 用户id
     */
    private int userId;
    /**
     * 途经信息，例如：张家界-凤凰古城-岳阳楼-橘子洲头
     */
    private String viaInfo;
    /**
     * 出发地城市
     */
    private String departCity;
    /**
     * 会话信息
     */
    private RecentContact recentContact;
    private boolean isSelect=false;
    /**
     * 是否正在讲话中
     */
    private boolean isSpeaking=false;
    /**
     * 线路推荐信息列表
     */
    private List<RecommendLineBean> recommendLineList;

    public String getGroupId() {
        if(groupId==null){
            return "";
        }
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public boolean isOfficial() {
        return official;
    }

    public void setOfficial(boolean official) {
        this.official = official;
    }

    public boolean isMy() {
        return isMy;
    }

    public void setMy(boolean my) {
        isMy = my;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    public String getGroupAdminName() {
        return groupAdminName;
    }

    public void setGroupAdminName(String groupAdminName) {
        this.groupAdminName = groupAdminName;
    }
    public String getCreatorName() {
        if(official){
            return "官方";
        }else if(!TextUtils.isEmpty(groupAdminName)){
            return groupAdminName;
        }else{
            return "";
        }
    }
    public int getLineType() {
        return lineType;
    }

    public void setLineType(int lineType) {
        this.lineType = lineType;
    }

    public String getLineTypeName() {
        if(lineType==1){
            return "自由行程";
        }else if(lineType==2){
            return "跟团行程";
        }
        return "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getStartEndDate() {
        if(departDate==null){
            departDate="";
        }
        if(endDate==null){
            endDate="";
        }
        String startTime=TimeUtil.stringDateToString(departDate.substring(0,10),"yyyy-MM-dd","yyyy.MM.dd");
        String endTime=TimeUtil.stringDateToString(endDate.substring(0,10),"yyyy-MM-dd","yyyy.MM.dd");
        return startTime+"—"+endTime;
    }
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPhotoUrl() {
        if(photoUrl==null){
            return "";
        }
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public int getState() {
        return state;
    }
    public String getStateName() {
        //1待出行 2出行中 3已结束
        if(state==1){
            return "待出行";
        }else if(state==2){
            return "旅途中\n("+groupNumber+")";
        }else if(state==3){
            return "已结束";
        }
        return "待出行";
    }
    public void setState(int state) {
        this.state = state;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getViaInfo() {
        return viaInfo;
    }

    public void setViaInfo(String viaInfo) {
        this.viaInfo = viaInfo;
    }

    public List<RecommendLineBean> getRecommendLineList() {
        return recommendLineList;
    }

    public void setRecommendLineList(List<RecommendLineBean> recommendLineList) {
        this.recommendLineList = recommendLineList;
    }

    public RecentContact getRecentContact() {
        return recentContact;
    }

    public void setRecentContact(RecentContact recentContact) {
        this.recentContact = recentContact;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isSpeaking() {
        return isSpeaking;
    }

    public void setSpeaking(boolean speaking) {
        isSpeaking = speaking;
    }

    public String getDepartCity() {
        return departCity;
    }
    public String getDepartSiteStr() {
        if(departCity==null){
            return "出发地：";
        }
        return "出发地："+departCity;
    }
    public void setDepartCity(String departCity) {
        this.departCity = departCity;
    }
}
