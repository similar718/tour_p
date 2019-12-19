package cn.xmzt.www.mine.bean;

/**
 * @describe
 */
public class MessageTypeReadBean {
    private int newCount;//未读记录数
    private int types;//消息类型1系统通知2订单消息3活动服务4咨询服务
    private int userId;//用户id

    public int getNewCount() {
        return newCount;
    }

    public void setNewCount(int newCount) {
        this.newCount = newCount;
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
}
