package cn.xmzt.www.view.floatview.audioplay;

import android.content.Context;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import cn.xmzt.www.R;
import cn.xmzt.www.glide.GlideUtil;
import cn.xmzt.www.intercom.api.NimUIKit;
import cn.xmzt.www.intercom.impl.NimUIKitImpl;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;

/**
 * @ClassName EnFloatingView
 * @Description 悬浮窗
 * @Author Averysk
 */
public class FloatView extends FloatViewMagnet {

    private final Chronometer timerPlayAudio; // 语音播放时间
    private final ImageView ivPlayAudioAvatar; // 语音播放对象头像
    private final TextView tvPlayAudioName; // 语音播放对象名称
    private final TextView tvPlayAudioIntro; // 语音播放对象简介
    private final TextView tvPlayAudioNext; // 语音播放对象下一条
    private ImageView ivHide;// 语音播放对象隐藏
    private boolean isShowNext = false; // 是否显示下一条

    public FloatView(@NonNull Context context) {
        super(context, null);
        inflate(context, R.layout.layout_intercom_float_audio_play, this);
        //浮动窗口按钮
        timerPlayAudio = findViewById(R.id.timer_play_audio);
        ivPlayAudioAvatar = findViewById(R.id.iv_play_audio_avatar);
        tvPlayAudioName = findViewById(R.id.tv_play_audio_title);
        tvPlayAudioIntro = findViewById(R.id.tv_play_audio_intro);
        tvPlayAudioNext = findViewById(R.id.tv_play_audio_next);
        ivHide = findViewById(R.id.iv_hide);
        tvPlayAudioNext.setVisibility(isShowNext ? View.VISIBLE : View.GONE);
        tvPlayAudioNext.setOnClickListener(v -> onClickEvent(v));
        ivHide.setOnClickListener(v -> onClickEvent(v));
    }

    // 计时开始
    public void setTimerStart() {
        timerPlayAudio.setBase(SystemClock.elapsedRealtime());
        timerPlayAudio.start();
    }

    // 计时结束
    public void setTimerStop() {
        timerPlayAudio.stop();
        timerPlayAudio.setBase(SystemClock.elapsedRealtime());
    }

    // 设置头像
    public void setAvatar(String url){
        GlideUtil.setImageCircle(url,ivPlayAudioAvatar,R.drawable.icon_head_default);
    }

    // 设置群组名称
    public void setGroupIdName(String title){
        setTextGroupName(title);
    }

    // 设置群人员名称
    public void setMemberName(String name){
        tvPlayAudioIntro.setText(name);
    }

    // 设置是否显示下一条
    public void setIsShowNext(boolean isShow){
        isShowNext = isShow;
    }

    public void setViewData(IMMessage imMessage) {
        Team t = NimUIKit.getTeamProvider().getTeamById(imMessage.getSessionId());
        UserInfo userInfo = NimUIKit.getUserInfoProvider().getUserInfo(imMessage.getFromAccount());
        if (userInfo != null){
            // 收听到的人的头像
            GlideUtil.setImageCircle(userInfo.getAvatar(),ivPlayAudioAvatar,R.drawable.icon_head_default);
        }
        if (t != null){
            // 收听到的群组名称
            setTextGroupName(t.getName());
        }
        // 收听到的人员名称
        tvPlayAudioIntro.setText(imMessage.getFromNick());
        // 将当前消息进行处理不做 录音
        if (receiveReceiptCheck(imMessage)) {
            NIMClient.getService(MsgService.class).sendMessageReceipt(imMessage.getSessionId(), imMessage);
        }
    }

    private boolean receiveReceiptCheck(final IMMessage msg) {
        return msg != null
                && msg.getSessionType() == SessionTypeEnum.P2P
                && msg.getDirect() == MsgDirectionEnum.Out
                && msg.getMsgType() != MsgTypeEnum.tip
                && msg.getMsgType() != MsgTypeEnum.notification
                && msg.isRemoteRead();

    }

//    public void sendReceipt() {
//        // 查询全局已读回执功能开关配置
//        if (!NimUIKitImpl.getOptions().shouldHandleReceipt) {
//            return;
//        }
//
//        if (container.account == null || container.sessionType != SessionTypeEnum.P2P) {
//            return;
//        }
//
//        IMMessage message = getLastReceivedMessage();
//        if (!sendReceiptCheck(message)) {
//            return;
//        }
//        NIMClient.getService(MsgService.class).sendMessageReceipt(container.account, message);
//    }

    public void setViewData(String groupId, String userId) {
        Team t = NimUIKit.getTeamProvider().getTeamById(groupId);
        UserInfo userInfo = NimUIKit.getUserInfoProvider().getUserInfo(userId);
        if (userInfo != null){
            // 收听到的人的头像
            GlideUtil.setImageCircle(userInfo.getAvatar(),ivPlayAudioAvatar,R.drawable.icon_head_default);
            // 收听到的人员名称
            tvPlayAudioIntro.setText(userInfo.getName());
        }
        if (t != null){
            // 收听到的群组名称
            setTextGroupName(t.getName());
        }
        // 将当前对讲的消息进行处理 对讲
    }

    private void setTextGroupName(String data){
        tvPlayAudioName.setText(data);
//        tvPlayAudioName.setText("行程群："+data);
    }

}
