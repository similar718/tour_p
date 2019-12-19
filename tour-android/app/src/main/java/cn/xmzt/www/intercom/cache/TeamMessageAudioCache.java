package cn.xmzt.www.intercom.cache;

import android.text.TextUtils;

import cn.xmzt.www.intercom.common.util.log.LogUtil;
import cn.xmzt.www.intercom.impl.NimUIKitImpl;
import cn.xmzt.www.main.globals.MsgExtensionType;
import cn.xmzt.www.main.globals.TalkManage;
import cn.xmzt.www.utils.SPUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.NIMSDK;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.AudioAttachment;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.MessageReceipt;

import org.webrtc.Logging;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 群聊消息(语音)缓存
 * @author Averysk
 */
public class TeamMessageAudioCache {

    private static final String TAG = "AnyRtc";

    public static TeamMessageAudioCache getInstance() {
        return InstanceHolder.instance;
    }

    /** ************************************ 单例 *************************************** */
    private static class InstanceHolder {
        private final static TeamMessageAudioCache instance = new TeamMessageAudioCache();
    }

    /** ************************************ 缓存消息语音 *************************************** */
    private Map<String, IMMessage> cacheMessageAudio = new LinkedHashMap<>();


    /** ************************************ 清除所有 *************************************** */
    public void clearCacheAll() {
        cacheMessageAudio.clear();
    }





    /** ************************************ 删除消息 *************************************** */
    public void deleteMessage(IMMessage message) {
        cacheMessageAudio.remove(message.getUuid());
    }


    /** ************************************ 保存指定消息 *************************************** */
    public void saveMessageAudio(IMMessage message) {
        String uuid=message.getUuid();
        cacheMessageAudio.put(uuid, message);
    }

    /** ************************************ 获取指定消息 *************************************** */
    public IMMessage getMessageAudio(String uuid) {
        if (cacheMessageAudio.containsKey(uuid)) {
            return cacheMessageAudio.get(uuid);
        }
        return null;
    }

    /** ************************************ 获取消息列表 *************************************** */
    public List<IMMessage> getCacheMessageAudioList(){
        List<IMMessage> imMessageList = new ArrayList<>();
        for (Iterator<IMMessage> iterator = cacheMessageAudio.values().iterator(); iterator.hasNext(); ) {
            imMessageList.add(iterator.next());
        }
        return imMessageList;
    }

    /** ************************************ 获取消息单个 *************************************** */
    public IMMessage getCacheMessageAudio(){
        IMMessage imMessage = null;
        if (cacheMessageAudio != null) {
            for (Iterator<IMMessage> iterator = cacheMessageAudio.values().iterator(); iterator.hasNext(); ) {
                imMessage = iterator.next();
                break;
            }
        }
        return imMessage;
    }



    /** ************************************ 监听事件 *************************************** */
    public void registerObservers(boolean register) {
        // 消息接收监听
        //NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(messageObserver, register);
        // 已读回执监听
        if (NimUIKitImpl.getOptions().shouldHandleReceipt) {
            NIMClient.getService(MsgServiceObserve.class).observeMessageReceipt(messageReceiptObserver, register);
        }
        // 监听消息状态
        NIMClient.getService(MsgServiceObserve.class).observeMsgStatus(statusObserver, register);
    }

    /** ************************************ 监听消息下载状态观察者 *************************************** */

    private Observer<IMMessage> statusObserver = new Observer<IMMessage>() {
        @Override
        public void onEvent(IMMessage msg) {
            if (msg.getAttachStatus() == AttachStatusEnum.transferred && isOriginDataHasDownloaded(msg)){
                // 传输成功, 附件下载成功标志
                if (shouldFilter(msg)) {
                    if (checkMessageAudioIsPlay(msg)){
                        //保存要播放的条信息
                        saveMessageAudio(msg);
                        if (isUnreadAudioMessage(msg)) {
                            msg.setStatus(MsgStatusEnum.read);
                            NIMClient.getService(MsgService.class).updateIMMessageStatus(msg);
                        }
                        // 开始播放语音读取
                        TalkManage.getInstance().openAudioRecordPlay();
                        LogUtil.e(TAG, "receive team message save ok");
                        return;
                    }
                }
            }
            LogUtil.e(TAG, "receive team message no audio");
        }
    };

    private boolean isOriginDataHasDownloaded(final IMMessage message) {
        if (message.getAttachment() instanceof AudioAttachment){
            if (!TextUtils.isEmpty(((AudioAttachment) message.getAttachment()).getPath())) {
                return true;
            }
        }
        return false;
    }

    /** ************************************ 监听已读回执观察者 *************************************** */
    private Observer<List<MessageReceipt>> messageReceiptObserver = new Observer<List<MessageReceipt>>() {
        @Override
        public void onEvent(List<MessageReceipt> messageReceipts) {
//            messageListPanel.receiveReceipt();
        }
    };

    /** ************************************ 判断消息 *************************************** */
    private static boolean shouldFilter(IMMessage message) {
        // 判断是否是有效消息 判断是否是群聊消息,并且是语音消息
        return message != null // 表示不是空的消息
                && message.getDirect() == MsgDirectionEnum.In // 表示是接收到的消息
                && !TextUtils.isEmpty(message.getSessionId())   // 表示是有效的群ID的消息
                && !TextUtils.isEmpty(message.getUuid())    // 表示是有效的消息ID的消息
                && message.getSessionType() == SessionTypeEnum.Team // 表示是群内的消息
                && message.getAttachment() != null  // 表示获取的消息附件对象不是空的
                && message.getAttachment() instanceof AudioAttachment // 表示获取的消息附件对象是语音消息
                && message.getMsgType() == MsgTypeEnum.audio; // 表示获取的消息类型 是 语音消息
    }

    // 检查当前群内的语音消息是否允许播放
    protected boolean checkMessageAudioIsPlay(IMMessage message){
        // 判断附近消息
        if (message.getRemoteExtension() != null && message.getRemoteExtension().size() > 0){
            Map<String, Object> val = message.getRemoteExtension();
            String type = (String) val.get(MsgExtensionType.Extension_Type);
            String groupId = (String) val.get(MsgExtensionType.Extension_GroupId);
            Logging.d("AnyRtc", "语音事件: type: " + type + " groupId: " + groupId);
            // 判断type是否对讲录音
            if (type.equals(MsgExtensionType.Extension_Type_101)){
                if (!TextUtils.isEmpty(MsgExtensionType.groupId)){
                    // 判断groupid是否在自己对讲组中
                    if (groupId.equals(MsgExtensionType.groupId)){
                        // 将未读标识去掉,更新数据库
                        if (isUnreadAudioMessage(message)) {
                            message.setStatus(MsgStatusEnum.read);
                            NIMClient.getService(MsgService.class).updateIMMessageStatus(message);
                        }
                        TeamMessageAudioCache.getInstance().sendReceipt(message);
                        return false;
                    }else {
                        String autoPlayGroup= SPUtils.getAutoPlayGroup();
                        //判断这个群是否可用自动播放对讲录播的语音
                        if(!autoPlayGroup.contains(groupId)){
                            return false;
                        }
                    }
                }
            }

            // 这个消息是否是进入系统后的消息
            if (message.getTime() <= MsgExtensionType.entry_system_time) {
                return false;
            }
        }
        return true;
    }

    public boolean isUnreadAudioMessage(IMMessage message) {
        if ((message.getMsgType() == MsgTypeEnum.audio)
                && message.getDirect() == MsgDirectionEnum.In
                && message.getAttachStatus() == AttachStatusEnum.transferred
                && message.getStatus() != MsgStatusEnum.read) {
            return true;
        } else {
            return false;
        }
    }
    /** ************************************ 发送已读回执 *************************************** */
    public void sendReceipt(IMMessage message){
        if (sendReceiptCheck(message)) {
            NIMSDK.getTeamService().sendTeamMessageReceipt(message);
        }
    }

    /** ************************************ 判断消息是否可发送已读回执 *************************************** */
    private boolean sendReceiptCheck(final IMMessage msg) {
        return msg != null
                && msg.getDirect() == MsgDirectionEnum.In
                && msg.getMsgType() == MsgTypeEnum.audio;
    }

}
