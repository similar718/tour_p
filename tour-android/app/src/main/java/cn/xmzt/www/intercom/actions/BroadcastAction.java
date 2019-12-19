package cn.xmzt.www.intercom.actions;

import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import cn.xmzt.www.R;
import cn.xmzt.www.dialog.TextTitleDialog;
import cn.xmzt.www.intercom.event.AnyRtcStatusEvent;
import cn.xmzt.www.main.globals.MsgExtensionType;
import cn.xmzt.www.main.globals.TalkManage;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * 广播
 * Created by Averysk
 */
public class BroadcastAction extends BaseAction {

    public BroadcastAction() {
        super(R.drawable.ic_chat_actions_broadcast, R.string.intercom_input_panel_broadcast);
    }
    private boolean isBeingBroadcast;//是否正在广播

    public boolean isBeingBroadcast() {
        return isBeingBroadcast;
    }

    public void setBeingBroadcast(boolean beingBroadcast) {
        isBeingBroadcast = beingBroadcast;
    }

    @Override
    public void onClick() {
        if(!isBeingBroadcast){
            showRegularTeamMenu();
        }else {
            ToastUtils.showShort("正在广播中");
        }
    }

    private TextTitleDialog dialog;

    /**
     * 显示菜单
     */
    private void showRegularTeamMenu() {
        dialog = new TextTitleDialog(getActivity(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 发送事件,更新UI
                EventBus.getDefault().postSticky(new AnyRtcStatusEvent(1011));
                // 开启广播
                TalkManage.getInstance().openBroadcast();
                dialog.dismiss();
            }
        });
        dialog.setTitle("广播模式下，仅允许发起人发言30秒，未发言将自动关闭广播模式？");
        dialog.show();
    }

    /**
     * 发送广播
     * @param account       //群id
     * @param sessionType   //群类型
     * @param type          //广播类型  301 : 发起广播  302 : 结束广播
     */
    public static void sendMessageTipBroadcast(String account, SessionTypeEnum sessionType, String type) {
        IMMessage message = MessageBuilder.createTipMessage(account, sessionType);
        message.setContent(type.equals(MsgExtensionType.Extension_Type_301) ? "发起了广播" : "结束了广播");

        Map<String, Object> content = new HashMap<>();
        content.put(MsgExtensionType.Extension_Type, type);
        message.setRemoteExtension(content);

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
        message.setConfig(config);

        NIMClient.getService(MsgService.class).sendMessage(message, false);
    }

}

