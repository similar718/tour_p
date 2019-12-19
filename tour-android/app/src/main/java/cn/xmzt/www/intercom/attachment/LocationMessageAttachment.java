package cn.xmzt.www.intercom.attachment;

import com.alibaba.fastjson.JSONObject;

/**
 * 位置消息
 * Created by Averysk
 */
public class LocationMessageAttachment extends CustomAttachment {

    private String msg_type;
    private String send_msg_id;
    private String send_msg_nick;
    private String send_msg_type;
    private String send_msg_avatar;
    private String send_msg_numberPlate;
    private String send_msg_timestamp;
    private String send_msg_latitude;
    private String send_msg_longitude;

    private static final String KEY_TYPE = "type";                      // 类型:  1: 位置消息通知
    private static final String KEY_RESULT = "result";                  // 消息结果对象
    private static final String KEY_RESULT_ID = "id";                   // 发送消息者id
    private static final String KEY_RESULT_NICK = "nickname";               // 发送消息者昵称
    private static final String KEY_RESULT_TYPE = "type";               // 发送消息者身份: 1: 队长  2: 成员
    private static final String KEY_RESULT_AVATAR = "avatar";           // 发送消息者头像
    private static final String KEY_RESULT_NUMBER_PLATE = "licencePlate";// 发送消息者车牌号
    private static final String KEY_RESULT_TIMESTAMP = "timestamp";     // 当前时间戳
    private static final String KEY_RESULT_LATITUDE = "latitude";       // 当前纬度
    private static final String KEY_RESULT_LONGITUDE = "longitude";     // 当前经度

    public LocationMessageAttachment() {
        super(0);
    }

    @Override
    protected void parseData(JSONObject data) {
        msg_type = data.getString(KEY_TYPE);
        JSONObject result = data.getJSONObject(KEY_RESULT);
        send_msg_id = result.getString(KEY_RESULT_ID);
        send_msg_nick = result.getString(KEY_RESULT_NICK);
        send_msg_type = result.getString(KEY_RESULT_TYPE);
        send_msg_avatar = result.getString(KEY_RESULT_AVATAR);
        send_msg_numberPlate = result.getString(KEY_RESULT_NUMBER_PLATE);
        send_msg_timestamp = result.getString(KEY_RESULT_TIMESTAMP);
        send_msg_latitude = result.getString(KEY_RESULT_LATITUDE);
        send_msg_longitude = result.getString(KEY_RESULT_LONGITUDE);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(KEY_TYPE, msg_type);
        JSONObject result = new JSONObject();
        result.put(KEY_RESULT_ID, send_msg_id);
        result.put(KEY_RESULT_NICK, send_msg_nick);
        result.put(KEY_RESULT_TYPE, send_msg_type);
        result.put(KEY_RESULT_AVATAR, send_msg_avatar);
        result.put(KEY_RESULT_NUMBER_PLATE, send_msg_numberPlate);
        result.put(KEY_RESULT_TIMESTAMP, send_msg_timestamp);
        result.put(KEY_RESULT_LATITUDE, send_msg_latitude);
        result.put(KEY_RESULT_LONGITUDE, send_msg_longitude);
        data.put(KEY_RESULT, result);
        return data;
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

    public void setSend_msg_numberPlate(String send_msg_numberPlate) {
        this.send_msg_numberPlate = send_msg_numberPlate;
    }

    public String getSend_msg_timestamp() {
        return send_msg_timestamp;
    }

    public void setSend_msg_timestamp(String send_msg_timestamp) {
        this.send_msg_timestamp = send_msg_timestamp;
    }

    public String getSend_msg_latitude() {
        return send_msg_latitude;
    }

    public void setSend_msg_latitude(String send_msg_latitude) {
        this.send_msg_latitude = send_msg_latitude;
    }

    public String getSend_msg_longitude() {
        return send_msg_longitude;
    }

    public void setSend_msg_longitude(String send_msg_longitude) {
        this.send_msg_longitude = send_msg_longitude;
    }
}
