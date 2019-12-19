package cn.xmzt.www.mine.bean;

import cn.xmzt.www.utils.TimeUtil;

/**
 * @author tanlei
 * @date 2019/8/29
 * @describe
 */

public class MessageBean {
    private String content;//消息文本内容
    private String gmtCreate;//创建时间
    private String gmtModified;//最后修改时间
    private int id;//自增编号
    private int ifRead;//是否已读0未读，1已读
    private String listPic;//列表图
    private String themePic;//主题图
    private String title;//标题
    private int types;//消息类型1系统通知2订单消息3活动服务4咨询服务
    private String url;//消息URL
    private int userId;//用户id -1为所有用户

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }
    public String getGmtCreateDate() {
        if(TimeUtil.isStrTimeToday(gmtCreate,"yyyy-MM-dd HH:mm:ss")){
            return TimeUtil.stringDateToString(gmtCreate,"yyyy-MM-dd HH:mm:ss","HH:mm");
        }else if(TimeUtil.isYesterday(gmtCreate,"yyyy-MM-dd HH:mm:ss")){
            return "昨天 "+TimeUtil.stringDateToString(gmtCreate,"yyyy-MM-dd HH:mm:ss","HH:mm");
        }else if(TimeUtil.isBeforeYesterday(gmtCreate,"yyyy-MM-dd HH:mm:ss")){
            return "前天 "+TimeUtil.stringDateToString(gmtCreate,"yyyy-MM-dd HH:mm:ss","HH:mm");
        }
        return TimeUtil.stringDateToString(gmtCreate,"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm");
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(String gmtModified) {
        this.gmtModified = gmtModified;
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

    public String getListPic() {
        return listPic;
    }

    public void setListPic(String listPic) {
        this.listPic = listPic;
    }

    public String getThemePic() {
        return themePic;
    }

    public void setThemePic(String themePic) {
        this.themePic = themePic;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
