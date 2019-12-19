package cn.xmzt.www.main.globals;

import android.view.View;
import android.view.WindowManager;

import com.blankj.utilcode.util.ActivityUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.media.record.AudioRecorder;
import com.netease.nimlib.sdk.media.record.IAudioRecordCallback;
import com.netease.nimlib.sdk.media.record.RecordType;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import org.greenrobot.eventbus.EventBus;
import org.webrtc.Logging;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.dialog.TextTitleDialog;
import cn.xmzt.www.intercom.api.UIKitOptions;
import cn.xmzt.www.intercom.event.AnyRtcStatusEvent;
import cn.xmzt.www.intercom.impl.NimUIKitImpl;
import cn.xmzt.www.popup.IntercomChronometerPopupWindow;
import cn.xmzt.www.rxjava2.AsyncUtil;

public class AudioRecordManage {
    public static AudioRecordManage getInstance() {
        return InstanceHolder.instance;
    }

    private static class InstanceHolder {
        private final static AudioRecordManage instance = new AudioRecordManage();
    }

    private static final String TAG = "AnyRtc";
    private AudioRecorder audioMessageHelper; // 语音录制
    private IntercomChronometerPopupWindow chronometerPopupWindow; // 对讲按钮上面的录音时间记录表
    private TextTitleDialog dialogRecordMaxTim; // 默认提示框
    // 初始化录音
    public void initAudioRecord() {
        if (audioMessageHelper == null) {
            UIKitOptions options = NimUIKitImpl.getOptions();
            audioMessageHelper = new AudioRecorder(TourApplication.context.getApplicationContext(), options.audioRecordType, options.audioRecordMaxTime, iAudioRecordCallback);
        }
    }

    // 开始语音录制
    public void onStartAudioRecord() {
        try {
            ActivityUtils.getTopActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }catch (Exception e){}
        audioMessageHelper.startRecord();
    }

    // 结束语音录制
    public void onEndAudioRecord(boolean cancel) {
        try {
            ActivityUtils.getTopActivity().getWindow().setFlags(0, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }catch (Exception e){}
        if (audioMessageHelper != null){
            audioMessageHelper.completeRecord(cancel);
        }
        // 隐藏时间记录表
        hideTimerPop();
        if (!TalkManage.isJoinarSuccess){
            Logging.d(TAG, "判断事件: " + "结束结束, 判断是否加入了对讲组 或 离开了对讲组 ,  是: 则重新加入对讲组");
            // 切换对讲组
            TalkManage.getInstance().switchIntercomGroup();
        }
    }

    // 停止语音录音
    public void pauseAudioRecorder() {
        if (audioMessageHelper != null) {
            onEndAudioRecord(true);
        }
    }

    // 注销语音录音
    public void logoutAudioRecorder(){
        if (audioMessageHelper != null) {
            audioMessageHelper.destroyAudioRecorder();
            audioMessageHelper = null;
        }
    }

    // 显示时间记录表
    public void showTimerPop(){
        if(chronometerPopupWindow == null){
            chronometerPopupWindow = new IntercomChronometerPopupWindow(ActivityUtils.getTopActivity());
        }else if(!ActivityUtils.getTopActivity().equals(chronometerPopupWindow.getActivity())){
            chronometerPopupWindow = new IntercomChronometerPopupWindow(ActivityUtils.getTopActivity());
        }
        chronometerPopupWindow.showPop(FloatIntercomManage.getInstance().getView());
        //开启振幅的定时器
        startDelayedAmplitudeTime(40);
    }

    // 隐藏时间记录表
    public void hideTimerPop(){
        if(chronometerPopupWindow != null){
            chronometerPopupWindow.holePop();
        }
        //关闭获取振幅的定时器
        closeAmplitudeTime();
    }

    private IAudioRecordCallback iAudioRecordCallback = new IAudioRecordCallback() {
        // 录音状态回调
        @Override
        public void onRecordReady() {
            // 准备中
            Logging.d("AnyRtc", "录音事件: 录音准备中");
        }

        // 录音开始回调
        @Override
        public void onRecordStart(File audioFile, RecordType recordType) {
            Logging.d("AnyRtc", "录音事件: 录音开始了");
            if (!TalkManage.isPressed) {
                Logging.d("AnyRtc", "录音事件: 录音按钮松了");
                // 停止语音录音
                pauseAudioRecorder();
                return;
            }
            // 显示时间记录表
            showTimerPop();
        }

        // 录音成功回调
        @Override
        public void onRecordSuccess(File audioFile, long audioLength, RecordType recordType) {
            Logging.d("AnyRtc", "录音事件: 录音成功了, ");
            Logging.d("AnyRtc", "录音事件: audioFile : " + audioFile.getAbsolutePath());
            Logging.d("AnyRtc", "录音事件: audioLength : " + audioLength);
            // 隐藏时间记录表
            hideTimerPop();
            IMMessage audioMessage = MessageBuilder.createAudioMessage(MsgExtensionType.groupId, SessionTypeEnum.Team, audioFile, audioLength);
            Map<String, Object> var = new HashMap<>();
            var.put(MsgExtensionType.Extension_Type, MsgExtensionType.Extension_Type_102);
            var.put(MsgExtensionType.Extension_GroupId, MsgExtensionType.groupId);
            audioMessage.setRemoteExtension(var);
            NIMClient.getService(MsgService.class).sendMessage(audioMessage,false);
            // 发送事件,更新UI
            EventBus.getDefault().postSticky(new AnyRtcStatusEvent(1041, audioMessage));
        }

        // 录音失败回调
        @Override
        public void onRecordFail() {
            Logging.d("AnyRtc", "录音事件: 录音失败了");
            // 停止语音录音
            pauseAudioRecorder();
        }

        // 录音取消回调
        @Override
        public void onRecordCancel() {
            Logging.d("AnyRtc", "录音事件: 录音取消了");
            // 停止语音录音
            pauseAudioRecorder();
        }

        // 录音达到最大时间回调
        @Override
        public void onRecordReachedMaxTime(final int maxTime) {
            // 隐藏时间记录表
            hideTimerPop();
            dialogRecordMaxTim = new TextTitleDialog(ActivityUtils.getTopActivity(), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 停止语音录音
                    pauseAudioRecorder();
                    // 取消发送
                    dialogRecordMaxTim.dismiss();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 确认发送 最长时间录音弹出框
                    audioMessageHelper.handleEndRecord(true, maxTime);
                }
            });
            dialogRecordMaxTim.setTitle("对讲达到最大时间，是否发送？");
            dialogRecordMaxTim.show();
        }
    };

    private Timer timer;
    private TimerTask amplitudeTimerTask;
    /**
     * 开启振幅的定时器
     * @param s 延获取录音振幅的间隔时间
     */
    public void startDelayedAmplitudeTime(long s) {
        //如果timer和timerTask已经被置null了
        if (timer == null&&amplitudeTimerTask==null) {
            //新建timer和timerTask
            timer = new Timer();
            amplitudeTimerTask = new TimerTask() {
                @Override
                public void run() {
                    AsyncUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(chronometerPopupWindow!=null){
                                chronometerPopupWindow.setCurrentRecordAmplitude(getCurrentRecordMaxAmplitude());
                            }
                        }
                    });
                }
            };
            timer.schedule(amplitudeTimerTask, 0, s);
        }
    }

    /**
     * 关闭获取振幅的定时器
     */
    public void closeAmplitudeTime() {
        if (timer != null) {
            timer.cancel();
        }
        if (amplitudeTimerTask != null) {
            amplitudeTimerTask.cancel();
        }
        timer=null;
        amplitudeTimerTask=null;
    }

    /**
     * 获取当前录音的最大振幅
     * @return
     */
    public int getCurrentRecordMaxAmplitude(){
        if(audioMessageHelper!=null){
            return audioMessageHelper.getCurrentRecordMaxAmplitude();
        }
        return 0;
    }
}
