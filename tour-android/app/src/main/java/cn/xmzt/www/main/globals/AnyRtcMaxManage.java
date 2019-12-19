package cn.xmzt.www.main.globals;

import android.app.Activity;
import android.app.Service;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import org.ar.common.enums.ARNetQuality;
import org.ar.rtmax_kit.ARMaxEvent;
import org.ar.rtmax_kit.ARMaxKit;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import cn.xmzt.www.anyrtc.SoundPlayUtils;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.intercom.event.AnyRtcStatusEvent;
import cn.xmzt.www.popup.IntercomChronometerPopupWindow;
import cn.xmzt.www.rxjava2.AsyncUtil;
import cn.xmzt.www.utils.audiomanager.ARAudioManager;

import static android.content.pm.PackageManager.PERMISSION_DENIED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * 对讲功能管理器
 * @author Averysk
 */
public class AnyRtcMaxManage {
    private AnyRtcMaxManage() {
        if (mRTMaxKit == null) {
            mRTMaxKit = new ARMaxKit(arMaxEvent);
            mRTMaxKit.setAudioActiveCheck(true,true);
        }
    }

    public static AnyRtcMaxManage getInstance() {
        return InstanceHolder.instance;
    }

    private static class InstanceHolder {
        private final static AnyRtcMaxManage instance = new AnyRtcMaxManage();
    }

    private static final String TAG = "AnyRtc";
    private ARMaxKit mRTMaxKit;
    private ARAudioManager mRTCAudioManager;
    private Vibrator vb;
    private IntercomChronometerPopupWindow chronometerPopupWindow; // 对讲按钮上面的录音时间记录表

    // 初始化对讲
    public void initARMaxKit() {
        // 对讲组
        if (mRTMaxKit == null) {
            mRTMaxKit = new ARMaxKit(arMaxEvent);
            mRTMaxKit.setAudioActiveCheck(true,true);
            mRTMaxKit.setLocalAudioEnable(true);
        }
        if (vb == null) {
            vb = (Vibrator) TourApplication.context.getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        }
        if (mRTCAudioManager == null) {
            mRTCAudioManager = ARAudioManager.create(TourApplication.context.getApplicationContext());
            // Store existing audio settings and change audio mode to
            // MODE_IN_COMMUNICATION for best possible VoIP performance.用MODE_IN_COMMUNICATION模式性能要好一点
//            mRTCAudioManager.init();
            mRTCAudioManager.start(new ARAudioManager.AudioManagerEvents() {
                @Override
                public void onAudioDeviceChanged(ARAudioManager.AudioDevice var1, Set<ARAudioManager.AudioDevice> var2) {
                    Log.e("ARAudioManager","onAudioDeviceChanged AudioDevice="+var1);
                }
            });
        }
    }

    // 加入对讲组
    public void joinIntercomGroup() {
        if (TextUtils.isEmpty(MsgExtensionType.groupId)) {
            return;
        }
        String userId = TourApplication.tempUserid;
        if (TourApplication.getUser() != null){
            if (TourApplication.getUser().getUserId() > 0){
                userId = TourApplication.getUser().getUserId() + "";
            }
        }
        Log.d(TAG, "对讲事件: 已加入对讲组ID: " + MsgExtensionType.groupId);
        if(mRTMaxKit!=null){
            mRTMaxKit.joinTalkGroup(MsgExtensionType.groupId, userId, "");
        }else {
            Log.d(TAG, "对讲事件: mRTMaxKit== null");
        }
    }

    // 切换对讲组
    public void switchIntercomGroup() {
        if (TextUtils.isEmpty(MsgExtensionType.groupId)) {
            return;
        }
        Log.d(TAG, "对讲事件: 已切换对讲组ID: " + MsgExtensionType.groupId);
        if(mRTMaxKit!=null){
            mRTMaxKit.switchTalkGroup(MsgExtensionType.groupId, "");
        }
    }

    // 设置音频录制路径
    public void setAudioRecordPath(){
        // 先获取系统默认的文档存放目录
        File dir = new File(Constants.TALK_AUDIO_RECORD_PATH);
        if(!dir.exists()){
            dir.mkdirs(); // mkdirs 与 mkdir 区别 mkdirs()可以建立多级文件夹， mkdir()只会建立一级的文件夹 如果找不到上面级的目录 是不会自动创建目录的 会返回false
        }
        dir = new File(Constants.TALK_AUDIO_RECORD_PATH + "callPath/");
        if(!dir.exists()){
            dir.mkdirs();
        }
        String callPath = dir.getAbsolutePath();
        dir = new File(Constants.TALK_AUDIO_RECORD_PATH + "talkPath/");
        if(!dir.exists()){
            dir.mkdirs();
        }
        String talkPath = dir.getAbsolutePath();
        dir = new File(Constants.TALK_AUDIO_RECORD_PATH + "talkP2PPath/");
        if(!dir.exists()){
            dir.mkdirs();
        }
        String talkP2PPath = dir.getAbsolutePath();
        Log.d(TAG, "对讲路径: callPath = " + callPath);
        Log.d(TAG, "对讲路径: talkPath = " + talkPath);
        Log.d(TAG, "对讲路径: talkP2PPath = " + talkP2PPath);
        int result = mRTMaxKit.setRecordPath(callPath, talkPath, talkP2PPath);
        Log.d(TAG, "对讲路径: 设置结果 result = " + result);
/*
        if(checkPremission(WRITE_EXTERNAL_STORAGE)) {
            int result = mRTMaxKit.setRecordPath(callPath, talkPath, talkP2PPath);
            Logging.d(TAG, "对讲路径: 设置结果 result = " + result);
        }
*/

/*
        //录音存放路径（文件夹路径地址）
        String path = Environment.getExternalStorageDirectory().getPath() + "/Android/anyrtc";
        Logging.d(TAG, "对讲路径: path = " + path);
        if(checkPremission(WRITE_EXTERNAL_STORAGE)) {
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            int result = mRTMaxKit.setRecordPath(path, path, path);
            Logging.d(TAG, "对讲路径: 设置结果 result = " + result);
        }
*/
    }

    // 权限检测
    public boolean checkPremission(Activity activity, String... permissions) {
        boolean allHave = true;
        PackageManager pm = activity.getPackageManager();
        for (String permission : permissions) {
            switch (pm.checkPermission(permission, activity.getApplication().getPackageName())) {
                case PERMISSION_GRANTED:
                    allHave = allHave && true;
                    continue;
                case PERMISSION_DENIED:
                    allHave = allHave && false;
                    continue;
            }
        }
        return allHave;
    }

    /**
     * 申请对讲
     *
     * @param nPriority 申请抢麦用户的级别（0权限最大（数值越大，权限越小）；除0意外，可以后台设置0-10之间的抢麦权限大小））
     * @return 0: 调用OK  -1:未登录  -2:正在对讲中  -3: 资源还在释放中 -4: 操作太过频繁 -5：未连接成功 -6：上次上麦还未结束  -7：网络延时太高， 无法对讲 -8：未开始对讲
     */
    // 申请对讲
    public int applyTalk(int nPriority) {
        if (mRTMaxKit != null){
            mRTMaxKit.setForceAecEnable(true);
            return mRTMaxKit.applyTalk(nPriority);
        } else {
            return -3;
        }
    }

    // 开始对讲
    private void startApplyTalk(){
        AnyRtcMaxManage.getInstance().requestAudioFocus(true);
        long[] pattern = {100, 100};   // 停止 开启 停止 开启
        if(vb!=null){
            vb.vibrate(pattern, -1);
        }else {
            vb = (Vibrator) TourApplication.getInstance().getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        }
        SoundPlayUtils.getInstance().play(SoundPlayUtils.getInstance().burst_talk_voiceId);//对讲的提示声音
    }

    // 关闭对讲
    public void cancelTalk() {
        if (mRTMaxKit != null){
            mRTMaxKit.cancelTalk();
        }
    }

    // 离开通话
    public void leaveCall(){
        if (mRTMaxKit != null){
            mRTMaxKit.leaveCall();
        }
    }

    // 退出对讲组
    public void leaveTalkGroup(){
        if (mRTMaxKit != null){
            mRTMaxKit.leaveTalkGroup();
        }
    }
    /**
     *注册音频焦点.
     * @param isTalk 是否是对讲
     * @return AUDIOFOCUS_REQUEST_GRANTED：申请成功；AUDIOFOCUS_REQUEST_FAILED：申请失败；
     */
    public int requestAudioFocus(boolean isTalk) {
        if(mRTCAudioManager==null){
            return 0;
        }
        return mRTCAudioManager.requestAudioFocus(isTalk);
    }
    
    /**
     * 注销音频焦点.
     * @return
     */
    public int unRequestAudioFocus(){
        if(mRTCAudioManager==null){
            return 0;
        }
        Log.d(TAG, "音频焦点: unRequestAudioFocus ");
        return mRTCAudioManager.unRequestAudioFocus();
    }
    // 注销对讲
    public void logoutTalk(){
        //销毁对讲对象
        if (mRTMaxKit != null){
            mRTMaxKit.clear();
            mRTMaxKit = null;
        }
        //销毁音频管理器对象
        if (mRTCAudioManager != null) {
            mRTCAudioManager.stop();
            mRTCAudioManager = null;
        }
    }

    // 开始对讲和记时
    private void startTalkAndTimer(){
        if (TalkManage.isPressed) {
            Log.d(TAG, "对讲事件: 申请对讲成功: " + System.currentTimeMillis());
            // 开始对讲
            startApplyTalk();
            // 显示时间记录表
            showTimerPop();
        } else {
            // 结束对讲说话
            cancelTalk();
        }
    }
    // 开始录音和记时
    public void startRecordingAndTimer(){
        if ( !TalkManage.isKitAudioRecord ){
            Log.d("AnyRtc", "判断事件: 当前对讲申请失败: 则全程用云信功能,清除对讲说话功能");
            TalkManage.isHadSomeOneSpeaking=false;
            ToastUtils.showShort("别人正在讲话，开始录音");
            SoundPlayUtils.getInstance().play(SoundPlayUtils.getInstance().burst_alarm_voiceId);//录音开始的提示声音
            // 结束对讲说话
            AnyRtcMaxManage.getInstance().cancelTalk();
            // 离开通话
            AnyRtcMaxManage.getInstance().leaveCall();
            // 离开对讲组
            AnyRtcMaxManage.getInstance().leaveTalkGroup();
            // 停止语音播放状态
            AudioPlayManage.getInstance().stopAudioPlay();
            Log.d("AnyRtc", "音频录制: 开启语音录制");
            // 初始化录音
            AudioRecordManage.getInstance().initAudioRecord();
            // 开始启动录音
            AudioRecordManage.getInstance().onStartAudioRecord();
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

    // 收听方: 收听到别人正在发言中
    public void setAudioPlayUserSpeaking(String userId, String userData) {
        // 设置默认使用扬声器播放为通话
//        activity.setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
        requestAudioFocus(false);
        showAudioPlayFloatView(MsgExtensionType.groupId, userId);
    }

    // 显示音频播放View
    private void showAudioPlayFloatView(String groupId, String userId){
        FloatIntercomManage.getInstance().getView().playAnim(true);//对讲动画播放
//        FloatAudioPlayManage.getInstance().showFloatView(activity, groupId, userId);
        FloatAudioPlayManage.getInstance().showFloatView(ActivityUtils.getTopActivity(),groupId, userId);
    }

    // 隐藏音频播放View
    private void hideAudioPlayFloatView() {
        unRequestAudioFocus();
        FloatIntercomManage.getInstance().getView().playAnim(false);//对讲动画播放
//        FloatAudioPlayManage.getInstance().hideFloatView(activity);
        FloatAudioPlayManage.getInstance().hideFloatView(ActivityUtils.getTopActivity());
    }

    // 发送音频消息
    private void sendMessageAudio(int nRecType, String filePath) {
        Log.d(TAG, "对讲事件: nRecType = " + nRecType);
        Log.d(TAG, "对讲事件: filePath = " + filePath);
        if (nRecType == 0){ // 对讲本地录音
            if (!TextUtils.isEmpty(filePath)){
                File audioFile = new File(filePath);
                long audioLength = 2000;
                if(audioFile.exists()){
                    Log.d("AnyRtc", "对讲事件: audioFileLength : " + audioFile.length());

                    MediaPlayer mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(filePath);
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                        audioFile.delete();
                        return;
                    }
                    //获取音频的时间
                    audioLength = mediaPlayer.getDuration();
                    Log.d("AnyRtc", "对讲事件: audioLength : " + audioLength);
                    if(audioLength>300){
                        //记得释放资源
                        mediaPlayer.release();
                        Log.d("AnyRtc", "对讲事件: 对讲本地录音成功了");
                        IMMessage audioMessage = MessageBuilder.createAudioMessage(MsgExtensionType.groupId, SessionTypeEnum.Team, audioFile, audioLength);
                        Map<String, Object> var = new HashMap<>();
                        var.put(MsgExtensionType.Extension_Type, MsgExtensionType.Extension_Type_101);
                        var.put(MsgExtensionType.Extension_GroupId, MsgExtensionType.groupId);
                        audioMessage.setRemoteExtension(var);

                        CustomMessageConfig config = new CustomMessageConfig();
//                        config.enableUnreadCount = false;//不加入记录未读数
                        config.enablePush = false;//不推送
                        audioMessage.setConfig(config);

                        NIMClient.getService(MsgService.class).sendMessage(audioMessage, false);
                        // 发送事件,更新UI
                        EventBus.getDefault().postSticky(new AnyRtcStatusEvent(1041, audioMessage));
                    }else {
                        audioFile.delete();
                    }
                }
            }
        }
    }

    private ARMaxEvent arMaxEvent = new ARMaxEvent() {
        @Override
        public void onRTCJoinTalkGroupOK(String groupId) {
            Log.d(TAG, "对讲事件: 加入对讲组成功");
            TalkManage.isJoinarSuccess = true;
        }

        @Override
        public void onRTCJoinTalkGroupFailed(String groupId, int code, String reason) {
            Log.d(TAG, "加入对讲组"+groupId+"失败"+"code="+code+"reason="+reason);
            TalkManage.isJoinarSuccess = false;
        }

        @Override
        public void onRTCLeaveTalkGroup(int code) {
            Log.d(TAG, "对讲事件: 离开对讲组成功");
            TalkManage.isJoinarSuccess = false;
        }

        @Override
        public void onRTCApplyTalkOk() {
            Log.d(TAG, "对讲事件: 申请对讲成功: " + System.currentTimeMillis());
            AsyncUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 开始对讲和记时
                    startTalkAndTimer();
                }
            });
        }

        @Override
        public void onRTCTalkCouldSpeak() {
            Log.d(TAG, "对讲事件: 我正在发言: " + System.currentTimeMillis());
        }

        @Override
        public void onRTCTalkOn(String userId, String userData) {
            Log.d(TAG, "其他用户" + userId + "正在发言: " + System.currentTimeMillis());
            TalkManage.isHadSomeOneSpeaking = true;
            AsyncUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (TalkManage.isBroadcastListen){
                        TalkManage.getInstance().openBroadcastListenStart();
                    }
                    // 收听方: 收听到别人正在发言中
                    Log.d(TAG, "其他用户" + userId + "正在发言:打开播放弹框 " + System.currentTimeMillis());
                    setAudioPlayUserSpeaking(userId, userData);
                }
            });
        }

        @Override
        public void onRTCTalkP2POn(final String userId, final String userData) {
            Log.d(TAG, "对点对用户" + userId + "正在发言: " + System.currentTimeMillis());
            TalkManage.isHadSomeOneSpeaking = true;
            if (TalkManage.isBroadcastListen){
                TalkManage.getInstance().openBroadcastListenStart();
            }
            AsyncUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 收听方: 收听到别人正在发言中
                    setAudioPlayUserSpeaking(userId, userData);
                }
            });
        }

        @Override
        public void onRTCTalkP2POff(final String userData) {
            Log.d(TAG, "对点对用户结束发言" + "userData : " + userData);
            TalkManage.isHadSomeOneSpeaking = false;
            if (TalkManage.isBroadcastListening){
                TalkManage.getInstance().openBroadcastListenEnd();
            }
            AsyncUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 隐藏音频播放View
                    hideAudioPlayFloatView();
                }
            });
        }

        @Override
        public void onRTCTalkClosed(final int code, final String userId, String userData) {
            Log.d(TAG, "对讲事件: 对讲发言结束 : code  " + code);
            TalkManage.isKitAudioRecord = false;
            TalkManage.isHadSomeOneSpeaking = false;
            AsyncUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 隐藏音频播放View
                    hideAudioPlayFloatView();
                    // 隐藏时间记录表
                    hideTimerPop();
                    if(code!=0){
                        ToastUtils.showShort("发言结束 : code  ==" + code);
                        startRecordingAndTimer();
                    }
                }
            });
        }

        @Override
        public void onRTCEndCall(final String strCallId, final String userId, int nCode) {
            Log.d(TAG, "对讲事件: 主叫方已挂断本次通话");
        }

        @Override
        public void onRTCMemberNum(final int nNum) {
            Log.d(TAG, "当前在线人数" + nNum);
        }

        /**
         * 录像地址回调信息
         * @param nRecType  录音的类型（0/1/2/3：对讲本地录音/对讲远端录音/强插P2P录音/语音通话呼叫录音）
         * @param userData  对讲录音所有者的自定义数据
         * @param filePath  录音文件保存路径
         */
        @Override
        public void onRTCGotRecordFile(final int nRecType, final String userData, final String filePath) {
            AsyncUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 发送音频消息
                    sendMessageAudio(nRecType, filePath);
                }
            });
        }

        @Override
        public void onRTCVideoMonitorRequest(final String userId, String userData) {
            //Logging.d(TAG, "对讲事件: 监看请求");
        }

        @Override
        public void onRTCVideoMonitorClose(final String userId, String userData) {
            //Logging.d(TAG, "对讲事件: 停止监看");
        }

        @Override
        public void onRTCVideoMonitorResult(final String userId, int code, String userData) {
            //Logging.d(TAG, "对讲事件: 监看结果");
        }

        @Override
        public void onRTCVideoReportRequest(final String userId, String strUserData) {
            //Logging.d(TAG, "对讲事件: 报告请求");
        }

        @Override
        public void onRTCVideoReportClose(final String userId) {
            //Logging.d(TAG, "对讲事件: 报告关闭");
        }

        @Override
        public void onRTCMakeCallOK(final String strCallId) {
            //Logging.d(TAG, "对讲事件: 拨打电话成功");
        }

        @Override
        public void onRTCAcceptCall(final String userId, String strUserData) {
            //Logging.d(TAG, "对讲事件: 接听电话");
        }

        @Override
        public void onRTCRejectCall(final String userId, int nCode, String strUserData) {
            //Logging.d(TAG, "对讲事件: 拒接电话");
        }

        @Override
        public void onRTCLeaveCall(final String userId) {
            //Logging.d(TAG, "对讲事件: 挂断电话, 主叫方收到通话结束的回调（被叫方和被邀请方已全部退出或者主叫方挂断所有参与者）");
        }

        @Override
        public void onRTCReleaseCall(final String strCallId) {
            //Logging.d(TAG, "对讲事件: 发启电话");
        }

        @Override
        public void onRTCMakeCall(final String strCallId, final int nCallType, final String userId, String strUserData) {
            //Logging.d(TAG, "对讲事件: 正在通话中");
        }

        @Override
        public void onRTCOpenRemoteVideoRender(final String peerId, final String publishId, final String userId, String userData) {
            //Logging.d(TAG, "对讲事件: 打开远程视频");
        }

        @Override
        public void onRTCCloseRemoteVideoRender(final String peerId, final String publishId, final String userId) {
            //Logging.d(TAG, "对讲事件: 关闭远程视频");
        }

        @Override
        public void onRTCOpenRemoteAudioTrack(final String peerId, final String userId, String userData) {
            //Logging.d(TAG, "对讲事件: 打开远程音频");
        }

        @Override
        public void onRTCCloseRemoteAudioTrack(final String peerId, final String userId) {
            //Logging.d(TAG, "对讲事件: 关闭远程音频");
        }

        @Override
        public void onRTCLocalAudioPcmData(String s, byte[] bytes, int i, int i1, int i2) {
            Log.d(TAG, "对讲事件: 本地音频数据 s=="+s);
        }

        @Override
        public void onRTCRemoteAudioPcmData(String s, byte[] bytes, int i, int i1, int i2) {

        }

        @Override
        public void onRTCRemoteAVStatus(final String peerId, boolean audio, boolean video) {
            //Logging.d(TAG, "对讲事件: 远程音视频状态");
        }

        @Override
        public void onRTCLocalAVStatus(boolean audio, boolean video) {
            //Logging.d(TAG, "对讲事件: 本地音视频状态");
        }

        @Override
        public void onRTCRemoteAudioActive(final String peerId, String userId, final int nLevel, int nShowtime) {
            //Logging.d(TAG, "对讲事件: 远程音频激活");
        }

        @Override
        public void onRTCLocalAudioActive(int nLevel, int nTime) {
            Log.d(TAG, "对讲事件: 本地音频激活 nLevel="+nLevel+"  nTime"+nTime);
            AsyncUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(chronometerPopupWindow!=null){
                        chronometerPopupWindow.setCurrentRecordAmplitude(nLevel);
                    }
                }
            });
        }

        @Override
        public void onRTCRemoteNetworkStatus(String peerId, String userId, int nNetSpeed, int nPacketLost, ARNetQuality netQuality) {
            if(netQuality==ARNetQuality.ARNetQualityExcellent){
                Log.d(TAG, "对讲事件: 远程网络状态 异常");
            }else if(netQuality==ARNetQuality.ARNetQualityGood||netQuality==ARNetQuality.ARNetQualityAccepted){
                Log.d(TAG, "对讲事件: 远程网络状态 好");
            }else {
                Log.d(TAG, "对讲事件: 远程网络状态 不好");
            }

        }


        @Override
        public void onRTCLocalNetworkStatus(int nNetSpeed, int nPacketLost, ARNetQuality netQuality) {
            //Logging.d(TAG, "对讲事件: 本地网络状态");
            if(netQuality==ARNetQuality.ARNetQualityExcellent){
                Log.d(TAG, "对讲事件: 本地网络状态异常 ");
            }else if(netQuality==ARNetQuality.ARNetQualityGood||netQuality==ARNetQuality.ARNetQualityAccepted){
                Log.d(TAG, "对讲事件: 本地网络状态 好");
            }else {
                Log.d(TAG, "对讲事件: 本地网络状态 不好");
            }
        }

        @Override
        public void onRTCUserMessage(final String userId, String userName, String headerUrl, final String content) {
            //Logging.d(TAG, "对讲事件: 用户消息");
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
    private Random rand =null;

    /**
     * 获取当前录音的最大振幅
     * @return
     */
    public int getCurrentRecordMaxAmplitude(){
        if(rand==null){
            rand = new Random();
        }
        return rand.nextInt(100)*100;
    }
}
