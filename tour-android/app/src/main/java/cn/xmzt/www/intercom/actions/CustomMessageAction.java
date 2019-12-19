package cn.xmzt.www.intercom.actions;


import com.alibaba.fastjson.JSONObject;
import cn.xmzt.www.intercom.attachment.DefaultCustomAttachment;
import cn.xmzt.www.intercom.bean.TeamLocationBean;
import cn.xmzt.www.intercom.session.module.ModuleProxy;
import cn.xmzt.www.main.globals.MsgExtensionType;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;


/**
 * 自定义消息集合
 */
public class CustomMessageAction {

    private String sendUserAccount;
    private String sessionId;
    private SessionTypeEnum sessionType;
    private ModuleProxy proxy;

    public CustomMessageAction(String sendUserAccount, String sessionId, SessionTypeEnum sessionType, ModuleProxy proxy) {
        this.sendUserAccount = sendUserAccount;
        this.sessionId = sessionId;
        this.sessionType = sessionType;
        this.proxy = proxy;
    }

    /**
     * 发送自定义通知
     * @param locationBean
     */
    public void sendMessage(TeamLocationBean locationBean) {


    }

    public void sendMessage(String openAccount, String envelopeId, boolean getDone) {
        if (proxy == null) {
            return;
        }
        // 消息简要描述
        String content = "位置消息";
        // 自定义消息附件
        DefaultCustomAttachment attachment = new DefaultCustomAttachment();
        JSONObject data = new JSONObject();
        data.put(MsgExtensionType.Extension_Type, MsgExtensionType.Extension_Type_001);
        data.put(MsgExtensionType.Extension_GroupId, sessionId);
        attachment.fromJson(data);
        // 自定义消息配置
        CustomMessageConfig config = new CustomMessageConfig();
        /**
         * 该消息是否要保存到服务器，如果为false，通过 MsgService#pullMessageHistory(IMMessage, int, boolean)拉取的结果将不包含该条消息。
         * 默认为true。
         */
        config.enableHistory = false;
        /**
         * 该消息是否需要漫游。如果为false，一旦某一个客户端收取过该条消息，其他端将不会再漫游到该条消息。
         * 默认为true
         */
        config.enableRoaming = false;
        /**
         * 多端同时登录时，发送一条自定义消息后，是否要同步到其他同时登录的客户端。
         * 默认为true
         */
        config.enableSelfSync = false;
        /**
         * 该消息是否要消息提醒，如果为true，那么对方收到消息后，系统通知栏会有提醒。
         * 默认为true
         */
        config.enablePush = false;
        /**
         * 该消息是否需要推送昵称（针对iOS客户端有效），如果为true，那么对方收到消息后，iOS端将显示推送昵称。
         * 默认为true
         */
        config.enablePushNick = false;
        /**
         * 该消息是否要计入未读数，如果为true，那么对方收到消息后，最近联系人列表中未读数加1。
         * 默认为true
         */
        config.enableUnreadCount = false;
        /**
         * 该消息是否支持路由，如果为true，默认按照app的路由开关（如果有配置抄送地址则将抄送该消息）
         * 默认为true
         */
        config.enableRoute = false;
        IMMessage imMessage = MessageBuilder.createCustomMessage(sessionId, sessionType, content, attachment, config);
        proxy.sendMessage(imMessage);
    }
}
