package cn.xmzt.www.mine.bean;

/**
 * 马小秘消息筛选条件
 */
public class HorseMiMessageFilterBean {
    public static final int TYPE_CATEGORY=0;//分类
    public static final int TYPE_MSG_TYPE=1;//消息类型
    public static final int TYPE_DATE=2;//日期
    private int contentType;//查询条件类型：1 消息类型 2 查询月份
    private String contentKey;
    private String contentValue;

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public String getContentKey() {
        return contentKey;
    }

    public void setContentKey(String contentKey) {
        this.contentKey = contentKey;
    }

    public String getContentValue() {
        return contentValue;
    }

    public void setContentValue(String contentValue) {
        this.contentValue = contentValue;
    }
}