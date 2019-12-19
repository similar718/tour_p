package cn.xmzt.www.main.globals;

import android.media.AudioManager;

import com.blankj.utilcode.util.ActivityUtils;
import cn.xmzt.www.intercom.cache.TeamMessageAudioCacheManager;
import cn.xmzt.www.intercom.cache.TeamMessageAudioControl;
import cn.xmzt.www.nim.uikit.business.session.audio.MessageAudioControl;
import cn.xmzt.www.nim.uikit.common.media.audioplayer.Playable;
import cn.xmzt.www.selfdrivingtools.event.NavigationTypeEvent;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import org.greenrobot.eventbus.EventBus;
import org.webrtc.Logging;

import java.util.List;

import static cn.xmzt.www.nim.uikit.business.session.viewholder.MsgViewHolderAudio.CLICK_TO_PLAY_AUDIO_DELAY;

/**
 * 消息接收,音频播放*
 * 音频接收播放管理
 */
public class AudioPlayManage {

    public static AudioPlayManage getInstance() {
        return InstanceHolder.instance;
    }

    private static class InstanceHolder {
        private final static AudioPlayManage instance = new AudioPlayManage();
    }

    /*********************消息接收,音频播放******************/

    private static final String TAG = "AnyRtc";
    private IMMessage imMessage;
    private List<IMMessage> imMessageList;
    private TeamMessageAudioControl audioControl;

    // 注册和初始化群消息管理
    public void registerObservers() {
        audioControl = TeamMessageAudioControl.getInstance();
        TeamMessageAudioCacheManager.initCache();
    }

    // 播放语音记录弹出框
    public void playAudioRecordDialog(){
        imMessageList = TeamMessageAudioCacheManager.getCacheMessageAudioList();
        if (imMessageList != null && imMessageList.size() > 0){
            imMessage = imMessageList.get(0);
            // 开始播放语音音频
            audioControl.startPlayAudioDelay(CLICK_TO_PLAY_AUDIO_DELAY, imMessage, onPlayListener);
            EventBus.getDefault().postSticky(new NavigationTypeEvent(2));
        } else {
            hideAudioPlayFloatView();
            EventBus.getDefault().postSticky(new NavigationTypeEvent(3));
        }
    }


    // 开始播放语音读取
    private void playAudioRecord(){
        Logging.d(TAG, "音频播放: 开始播放语音读取");
        setAudioPlayUserSpeaking(imMessage);
        TalkManage.isAudioPlaying = true;
        // 设置消息已读
        imMessage.setStatus(MsgStatusEnum.read);
        // 发送消息已读回执
        TeamMessageAudioCacheManager.sendReceipt(imMessage);
    }

    // 结束语音播放(自动)
    private void stopAudioRecord(){
        Logging.d(TAG, "音频播放: 结束语音播放(自动)");
        hideAudioPlayFloatView();
        TeamMessageAudioCacheManager.deleteMessage(imMessage);
        TalkManage.isAudioPlaying = false;
        // 开始播放语音记录
        TalkManage.getInstance().openAudioRecordPlay();
    }

    // 结束语音播放(手动)
    public void stopAudioPlay(){
        Logging.d(TAG, "音频播放: 结束语音播放(手动)");
        hideAudioPlayFloatView();
        if (audioControl != null){
            audioControl.stopAudio();
        }
        TalkManage.isAudioPlaying = false;
    }

    // 清除消息语音缓存
    protected void logoutAudioCache(){
        TeamMessageAudioCacheManager.clearCacheAll();
    }

    // 收到语音消息, 音频播放, 设置数据
    private void setAudioPlayUserSpeaking(IMMessage imMessage) {
        // 设置默认使用扬声器播放为媒体
//        ActivityUtils.getTopActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
        showAudioPlayFloatView(imMessage);
    }
    // 显示语音播放弹出框
    private void showAudioPlayFloatView(IMMessage imMessage){
        FloatAudioPlayManage.getInstance().showFloatView(ActivityUtils.getTopActivity(), imMessage);
    }

    // 隐藏语音播放弹出框
    private void hideAudioPlayFloatView() {
        FloatAudioPlayManage.getInstance().hideFloatView(ActivityUtils.getTopActivity());
    }


    // 播放监听器
    private MessageAudioControl.AudioControlListener onPlayListener = new MessageAudioControl.AudioControlListener() {

        @Override
        public void updatePlayingProgress(Playable playable, long curPosition) {
            if (curPosition > playable.getDuration()) {
                return;
            }
            // 更新当前播放时间
            //updateTime(curPosition);
        }

        @Override
        public void onAudioControllerReady(Playable playable) {
            // 开始播放语音读取
            playAudioRecord();
        }

        @Override
        public void onEndPlay(Playable playable) {
            // 结束播放
            stopAudioRecord();
        }
    };

}
