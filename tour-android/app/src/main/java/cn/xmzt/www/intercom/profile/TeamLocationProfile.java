package cn.xmzt.www.intercom.profile;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import cn.xmzt.www.intercom.bean.TeamLocationBean;
import cn.xmzt.www.roomdb.TourDatabase;
import cn.xmzt.www.roomdb.beans.GroupUserInfo;
import cn.xmzt.www.selfdrivingtools.event.UpdateDesAndTimeEvent;

import com.blankj.utilcode.util.ActivityUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.msg.model.CustomNotificationConfig;

import org.greenrobot.eventbus.EventBus;


/**
 * 群内位置消息通知
 * Created by Averysk
 */
public class TeamLocationProfile {

    public static final String TYPE_1 = "1";                             // 类型:  1: 位置消息共享通知
    public static final String TYPE_2 = "2";                             // 类型:  1: 位置消息关闭通知
    public static final String TYPE_3 = "3";                             // 类型:  1: 位置消息关闭通知

    private static final String KEY_TYPE = "type";                      // 类型:  1: 位置消息通知
    private static final String KEY_RESULT = "result";                  // 消息结果对象
    private static final String KEY_RESULT_ID = "userId";               // 发送消息者id
    private static final String KEY_RESULT_GROUP_ID = "groupId";        // 发送消息者群id
    private static final String KEY_RESULT_NICK = "nickname";               // 发送消息者昵称
    private static final String KEY_RESULT_TYPE = "role";               // 发送消息者身份: 1: 队长  2: 成员
    private static final String KEY_RESULT_AVATAR = "avatar";           // 发送消息者头像
    private static final String KEY_RESULT_NUMBER_PLATE = "licencePlate";// 发送消息者车牌号
    private static final String KEY_RESULT_TIMESTAMP = "timestamp";     // 当前时间戳 long time = TimeUtil.currentTimeMillis()
    private static final String KEY_RESULT_LATITUDE = "latitude";       // 当前纬度
    private static final String KEY_RESULT_LONGITUDE = "longitude";     // 当前经度

    public static TeamLocationProfile getInstance() {
        return InstanceHolder.instance;
    }

    private static class InstanceHolder {
        private final static TeamLocationProfile instance = new TeamLocationProfile();
    }

    public String buildContent(TeamLocationBean bean) {
        JSONObject data = new JSONObject();
        data.put(KEY_TYPE, bean.getMsg_type());
        JSONObject result = new JSONObject();
        result.put(KEY_RESULT_ID, bean.getSend_msg_id());
        result.put(KEY_RESULT_GROUP_ID, bean.getGroup_id());
        result.put(KEY_RESULT_NICK, bean.getSend_msg_nick());
        result.put(KEY_RESULT_TYPE, bean.getSend_msg_type());
        result.put(KEY_RESULT_AVATAR, bean.getSend_msg_avatar());
        result.put(KEY_RESULT_NUMBER_PLATE, bean.getSend_msg_numberPlate());
        result.put(KEY_RESULT_TIMESTAMP, bean.getSend_msg_timestamp());
        result.put(KEY_RESULT_LATITUDE, bean.getSend_msg_latitude());
        result.put(KEY_RESULT_LONGITUDE, bean.getSend_msg_longitude());
        data.put(KEY_RESULT, result);
        return data.toString();
    }

    private JSONObject parseContentJson(CustomNotification notification) {
        if (notification != null) {
            String content = notification.getContent();
            return JSONObject.parseObject(content);
        }
        return null;
    }

    private boolean isTeamLocationInvite(CustomNotification customNotification, JSONObject json) {
        if (customNotification.getSessionType() == SessionTypeEnum.Team){
            if (json != null) {
                if (json != null && json.containsKey(KEY_TYPE)){
                    String type = json.getString(KEY_TYPE);
                    return type.equals(TYPE_1) || type.equals(TYPE_2)|| type.equals(TYPE_3);
                }
            }
        }
        return false;
    }

    /**
     * 监听自定义通知消息，id = 3 是群视频邀请
     *
     * @param register
     */

    private Observer<CustomNotification> customNotificationObserver = new Observer<CustomNotification>() {
        @Override
        public void onEvent(CustomNotification customNotification) {
            try {
                JSONObject jsonObject = parseContentJson(customNotification);
                String type = jsonObject.getString("type");
                if (type.equals(TYPE_3)){ // 如果为3的情况表示更新了集结点
                    EventBus.getDefault().post(new UpdateDesAndTimeEvent(1,customNotification.getSessionId()));
                    return;  // 478926 乌拉拉的队伍
                }
                // 收到群位置消息
                if (isTeamLocationInvite(customNotification, jsonObject)) {
                    TeamLocationBean locationBean = new TeamLocationBean();
                    locationBean.setGroup_id(customNotification.getSessionId());
                    locationBean.setMsg_type(jsonObject.getString(KEY_TYPE));

                    JSONObject result = jsonObject.getJSONObject(KEY_RESULT);
                    locationBean.setSend_msg_id(result.getString(KEY_RESULT_ID));
                    locationBean.setSend_msg_nick(result.getString(KEY_RESULT_NICK));
                    locationBean.setSend_msg_type(result.getString(KEY_RESULT_TYPE));
                    locationBean.setSend_msg_avatar(result.getString(KEY_RESULT_AVATAR));
                    locationBean.setSend_msg_numberPlate(result.getString(KEY_RESULT_NUMBER_PLATE));
                    locationBean.setSend_msg_timestamp(result.getString(KEY_RESULT_TIMESTAMP));
                    locationBean.setSend_msg_latitude(result.getString(KEY_RESULT_LATITUDE));
                    locationBean.setSend_msg_longitude(result.getString(KEY_RESULT_LONGITUDE));
                    // 接收到群位置系统消息通知，保存到缓存中

                    // 如果获取的群id是空的就不保存
                    if (TextUtils.isEmpty(locationBean.getGroup_id())){
                        return;
                    }
                    // 获取的发送人的userid是空的就不保存
                    if (TextUtils.isEmpty(locationBean.getSend_msg_id())){
                        return;
                    }
                    if (TextUtils.isEmpty(locationBean.getSend_msg_avatar())){
                        return;
                    }

                    GroupUserInfo userInfo = TourDatabase.getDefault(ActivityUtils.getTopActivity()).getGroupUserInfoDao().getData(locationBean.getGroup_id(),locationBean.getSend_msg_id());
                    if (userInfo != null) {
                        userInfo.groupId = locationBean.getGroup_id();
                        userInfo.userId = locationBean.getSend_msg_id();
                        userInfo.type = locationBean.getSend_msg_type_int();
                        userInfo.avatar = locationBean.getSend_msg_avatar();
                        userInfo.numberPlate = locationBean.getSend_msg_numberPlate();
                        userInfo.nickName = TextUtils.isEmpty(locationBean.getSend_msg_nick()) ? userInfo.nickName : locationBean.getSend_msg_nick();
                        userInfo.time = locationBean.getSend_msg_timestamp_long();
                        userInfo.latitude = locationBean.getSend_msg_latitude_double();
                        userInfo.longitude = locationBean.getSend_msg_longitude_double();
                        TourDatabase.getDefault(ActivityUtils.getTopActivity()).getGroupUserInfoDao().insert(userInfo);
                    } else {
                        // 保存到本地数据库
                        userInfo = new GroupUserInfo();
                        userInfo.groupId = locationBean.getGroup_id();
                        userInfo.userId = locationBean.getSend_msg_id();
                        userInfo.type = locationBean.getSend_msg_type_int();
                        userInfo.avatar = locationBean.getSend_msg_avatar();
                        userInfo.numberPlate = locationBean.getSend_msg_numberPlate();
                        userInfo.nickName = locationBean.getSend_msg_nick();
                        userInfo.time = locationBean.getSend_msg_timestamp_long();
                        userInfo.latitude = locationBean.getSend_msg_latitude_double();
                        userInfo.longitude = locationBean.getSend_msg_longitude_double();
                        TourDatabase.getDefault(ActivityUtils.getTopActivity()).getGroupUserInfoDao().insert(userInfo);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    public void registerObserver(boolean register) {
        NIMClient.getService(MsgServiceObserve.class).observeCustomNotification(customNotificationObserver, register);
    }

    /**
     * 发送自定义消息通知
     * @param locationBean
     */
    public void sendMessageNotificationTeamLocation(TeamLocationBean locationBean) {
        /**
         * 构造自定义通知，指定接收者
         */
        CustomNotification notification = new CustomNotification();
        notification.setSessionId(locationBean.getGroup_id());
        notification.setSessionType(SessionTypeEnum.Team);
        /**
         * 设置通知配置
         */
        CustomNotificationConfig config = new CustomNotificationConfig();
        /**
         * 该通知是否进行推送（消息提醒）。默认为 true
         */
        config.enablePush = false;
        /**
         * 该通知是否需要推送昵称（针对iOS客户端有效），
         * 如果为false，那么对方收到通知后，iOS端将不显示推送昵称。
         * 默认为 false
         */
        config.enablePushNick = false;
        /**
         * 该通知是否要计入未读数，
         * 如果为true，那么对方收到通知后，可以通过读取此配置项决定自己业务的未读计数变更。
         * 默认为 true
         */
        config.enableUnreadCount = false;
        notification.setConfig(config);
        /**
         * 构建通知的具体内容。为了可扩展性，这里采用 json 格式，以 "type" 作为类型区分。
         */
        String content = buildContent(locationBean);
        notification.setContent(content);
        /**
         * 设置 APNS 的推送文本
         */
        notification.setApnsText("位置消息");
        /**
         * 设置该消息需要保证送达
         */
        notification.setSendToOnlineUserOnly(false);
        /**
         * 发送自定义通知
         */
        NIMClient.getService(MsgService.class).sendCustomNotification(notification);
    }
}
