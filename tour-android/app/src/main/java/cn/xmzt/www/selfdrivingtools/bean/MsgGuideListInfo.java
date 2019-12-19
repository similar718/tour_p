package cn.xmzt.www.selfdrivingtools.bean;

public class MsgGuideListInfo {
    /**
     * title : 检查车辆
     * content : 如果是驾驶自己爱车出游，请在出行前，检查好自己的爱车，特献上检查指南一份，仅供参考
     * liveType : 3
     * id : 7
     * type : 2
     * liveTime : 12:00:00
     */
    private String title; // 类型标题
    private String content; // 类型中的内容
    private int liveType; // 酒店类型
    private int id; // 酒店编号
    private int type; // 类型  1美食指南 2酒店入住指南 3检查车辆 4 出行清单
    private String liveTime; // 酒店入住时间 1 之前 2 之后

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLiveTime() {
        return liveTime;
    }

    public void setLiveTime(String liveTime) {
        this.liveTime = liveTime;
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

    public int getLiveType() {
        return liveType;
    }

    public void setLiveType(int liveType) {
        this.liveType = liveType;
    }
}
