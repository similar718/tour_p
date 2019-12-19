package cn.xmzt.www.intercom.bean;

/**
 * 位置共享信息
 *  @author Averysk
 */
public class TeamLocationBean {

    private String group_id;                // 当前群id
    private String msg_type;                // 类型:  1: 位置消息通知
    private String send_msg_id;             // 发送消息者id
    private String send_msg_nick;           // 发送消息者昵称
    private String send_msg_type;           // 发送消息者身份: 1: 队长  2: 成员
    private String send_msg_avatar;         // 发送消息者头像
    private String send_msg_numberPlate;    // 发送消息者车牌号
    private String send_msg_timestamp;      // 当前时间戳 long time = TimeUtil.currentTimeMillis()
    private String send_msg_latitude;       // 当前纬度
    private String send_msg_longitude;      // 当前经度

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }

    public String getSend_msg_id() {
        return send_msg_id;
    }

    public void setSend_msg_id(String send_msg_id) {
        this.send_msg_id = send_msg_id;
    }

    public String getSend_msg_nick() {
        return send_msg_nick;
    }

    public void setSend_msg_nick(String send_msg_nick) {
        this.send_msg_nick = send_msg_nick;
    }

    public String getSend_msg_type() {
        return send_msg_type;
    }

    public int getSend_msg_type_int() {
        if ("".equals(send_msg_type) || null == send_msg_type){
            return 2;
        } else {
            return Integer.parseInt(send_msg_type);
        }
    }

    public String getSend_msg_type_str() {
        if ("".equals(send_msg_type) || null == send_msg_type){
            return "成员";
        } else {
            int type = Integer.parseInt(send_msg_type);
            return type == 0 ? "群主" : (type == 1 ? "领队" : "成员");
        }
    }

    public void setSend_msg_type(String send_msg_type) {
        this.send_msg_type = send_msg_type;
    }

    public String getSend_msg_avatar() {
        return send_msg_avatar;
    }

    public void setSend_msg_avatar(String send_msg_avatar) {
        this.send_msg_avatar = send_msg_avatar;
    }

    public String getSend_msg_numberPlate() {
        return send_msg_numberPlate;
    }

    public void setSend_msg_numberPlate(String send_msg_numberPlate) { //1569032578563.9338
        this.send_msg_numberPlate = send_msg_numberPlate;
    }

    public String getSend_msg_timestamp() {
        return send_msg_timestamp;
    }

    public long getSend_msg_timestamp_long() {
        if ("".equals(send_msg_timestamp) || null == send_msg_timestamp){
            return 0;
        } else {
            if (send_msg_timestamp.contains(".")){
                int position = send_msg_timestamp.indexOf(".");
                String data = send_msg_timestamp.substring(0,position);
                send_msg_timestamp = data;
            }
            return Long.parseLong(send_msg_timestamp);
        }
    }

    public void setSend_msg_timestamp(String send_msg_timestamp) {
        this.send_msg_timestamp = send_msg_timestamp;
    }

    public String getSend_msg_latitude() {
        return send_msg_latitude;
    }

    public double getSend_msg_latitude_double() {
        if ("".equals(send_msg_latitude) || null == send_msg_latitude){
            return 0.0;
        } else {
            return Double.parseDouble(send_msg_latitude);
        }
    }

    public void setSend_msg_latitude(String send_msg_latitude) {
        this.send_msg_latitude = send_msg_latitude;
    }

    public String getSend_msg_longitude() {
        return send_msg_longitude;
    }

    public double getSend_msg_longitude_double() {
        if ("".equals(send_msg_longitude) || null == send_msg_longitude){
            return 0.0;
        } else {
            return Double.parseDouble(send_msg_longitude);
        }
    }

    public void setSend_msg_longitude(String send_msg_longitude) {
        this.send_msg_longitude = send_msg_longitude;
    }

}
