package cn.xmzt.www.intercom.bean;

/**
 * 群领队
 */
public class GroupLeaderBean {
    private String avatar;//领队头像
    private String completePhone;//领队电话
    private String name;//领队名称

    public String getAvatar() {
        if(avatar==null){
            return "";
        }
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCompletePhone() {
        return completePhone;
    }

    public void setCompletePhone(String completePhone) {
        this.completePhone = completePhone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}