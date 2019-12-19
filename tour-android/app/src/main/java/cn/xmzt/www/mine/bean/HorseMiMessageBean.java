package cn.xmzt.www.mine.bean;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import cn.xmzt.www.utils.TimeUtil;

/**
 * 马小秘消息
 */

public class HorseMiMessageBean {
    private String content;//消息文本内容
    private String gmtCreate;//创建时间
    private int id;//自增编号
    private int ifRead;//是否已读 1 未读 2 已读
    private int ifSend;//是否发送成功 1 发送成功 2 发送失败
    private String relationId;//关联id 如行程动态为 行程id 订单消息 为 订单id 消息类型为7 则为xx市美食指南id 为8 则为xx酒店介绍id
    private String title;//消息标题
    /**
     * 消息类型
     * 1 行程创建成功
     * 2 明日出行提醒
     * 3 早睡提醒
     * 4 集结地更新提醒
     * 5 车辆检查提醒
     * 6 出行清单提醒
     * 7 xx市美食指南
     * 8 xxx酒店介绍提醒
     * 9 订单提醒
     * 10 群即将解散提醒
     */
    private int types;//消息类型 1 行程创建成功 2 明日出行提醒 3 早睡提醒 4 集结地更新提醒 5 车辆检查提醒 6 出行清单提醒 7 xx市美食指南 8 xxx酒店介绍提醒 9 订单提醒 10 群即将解散提醒
    private int userId;//用户id -1为所有用户
    private List<MsgDetail> tourMsgDetails;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }
    public String getTimeFormat() {
        if(TextUtils.isEmpty(gmtCreate)){
            return "";
        }
        return  TimeUtil.updateTimeHorseMi(gmtCreate);
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIfRead() {
        return ifRead;
    }

    public void setIfRead(int ifRead) {
        this.ifRead = ifRead;
    }

    public int getIfSend() {
        return ifSend;
    }

    public void setIfSend(int ifSend) {
        this.ifSend = ifSend;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTypes() {
        return types;
    }

    public void setTypes(int types) {
        this.types = types;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<MsgDetail> getTourMsgDetails() {
        if(tourMsgDetails==null){
            tourMsgDetails=new ArrayList<MsgDetail>();
        }
        return tourMsgDetails;
    }

    public void setTourMsgDetails(List<MsgDetail> tourMsgDetails) {
        this.tourMsgDetails = tourMsgDetails;
    }

    public static class MsgDetail{
        private int msgId;
        private String msgKey;
        private int msgSort;
        private String msgValue;
        public void setMsgId(int msgId) {
            this.msgId = msgId;
        }
        public int getMsgId() {
            return msgId;
        }

        public void setMsgKey(String msgKey) {
            this.msgKey = msgKey;
        }
        public String getMsgKey() {
            return msgKey;
        }

        public void setMsgSort(int msgSort) {
            this.msgSort = msgSort;
        }
        public int getMsgSort() {
            return msgSort;
        }

        public void setMsgValue(String msgValue) {
            this.msgValue = msgValue;
        }
        public String getMsgValue() {
            return msgValue;
        }

    }
}
