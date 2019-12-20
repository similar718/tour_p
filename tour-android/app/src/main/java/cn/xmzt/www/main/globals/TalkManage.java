package cn.xmzt.www.main.globals;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.text.TextUtils;

import com.blankj.utilcode.util.ActivityUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.attachment.NotificationAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.model.MemberChangeAttachment;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import org.greenrobot.eventbus.EventBus;
import org.webrtc.Logging;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.xmzt.www.anyrtc.SoundPlayUtils;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.helper.UserHelper;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.intercom.actions.BroadcastAction;
import cn.xmzt.www.intercom.bean.UserBasicInfoBean;
import cn.xmzt.www.intercom.cache.TeamMessageAudioCacheManager;
import cn.xmzt.www.intercom.event.AnyRtcStatusEvent;
import cn.xmzt.www.intercom.preference.Preferences;
import cn.xmzt.www.popup.IntercomSwitchGroupListPopupWindow;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.PermissionUtil;
import cn.xmzt.www.utils.SPUtils;
import cn.xmzt.www.utils.ToastUtils;
import io.reactivex.Observable;

/**
 * 说话控制中心
 */
public class TalkManage {

    private static final String TAG = "AnyRtc";

    public static boolean isShowTalk = false;             // 是否显示对讲按钮
    public static boolean isShowPlay = false;             // 是否显示播放窗口
    public static boolean isShowSelectGroup = false;      // 是否展示切换群对讲
    public static boolean isJoinarSuccess = false;        // 加入对讲组是否成功
    public static boolean isHadSomeOneSpeaking = false;   // 当前是否有人在对讲讲话
    public static boolean isPressed = false;              // 是否按住按钮了
    public static boolean isKitAudioRecord = false;       // 是否是对讲录制,  false: 直接录制语音,  true: 对讲录制语音
    public static boolean isBroadcast = false;            // 是否广播中,  false:结束广播了 ,  true: 发起广播中
    public static boolean isMeBroadcast = false;            // 是否是我在广播
    public static boolean isBroadcastSpeaking = false;    // 在广播中时, 是否正在对讲中,  false:发起了广播,没有开启对讲 ,  true: 发起了广播并且正在对讲中
    public static boolean isBroadcastListen = false;      // 是否接收到广播中,  false:接收别人发起的结束广播通知 ,  true: 接收到别人发起的开启广播通知
    public static boolean isBroadcastListening = false;   // 在广播中时, 是否收听到对讲,  false:发起了广播,没有收听到对讲 ,  true: 发起了广播并且正在收听到对讲
    public static boolean isAudioPlaying = false;         // 是否语音播放中(正在播放语音)
    private boolean isTalkRecording = false;         // 是否正在发起对讲和录音

    private CountDownTimer mTimerBroadcast;
    private CountDownTimer mTimerBroadcastListen;
    private IntercomSwitchGroupListPopupWindow groupListPopupWindow; // 对讲按钮上面的群队列

    public static TalkManage getInstance() {
        SoundPlayUtils.getInstance().init();//提示声音初始化
        return InstanceHolder.instance;
    }

    private TalkManage() {
    }

    private static class InstanceHolder {
        private final static TalkManage instance = new TalkManage();
    }

    // 设置发送消息容器
    public void initTalkManage(){
        // 初始化对讲
        AnyRtcMaxManage.getInstance().initARMaxKit();
        // 设置音频录制路径
        AnyRtcMaxManage.getInstance().setAudioRecordPath();
        AudioPlayManage.getInstance().registerObservers();
        /**
         * 监听群里消息
         */
        NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(receiveMessageObserver, true);
    }
    /**
     *  获取当前广播开关
     */
    private void getTalkbackGroupBroadcast(){
        if (TextUtils.isEmpty(MsgExtensionType.groupId)) {
            return;
        }
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getTalkbackGroupBroadcast(SPUtils.getToken(), MsgExtensionType.groupId);
        mObservable.compose(ComposeUtil.compose())
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>() {
                    @Override
                    public void onNext(BaseDataBean<Object> body) {
                        if(body.isSuccess()){
                            if (body.getRel() == null){
                                // 暂定为 当前没有广播
                                isBroadcast = false;
                                // 收到没有广播通知发送
                                TalkManage.getInstance().closeBroadcastListen();
                            } else {
                                if (body.getRel() instanceof String){
                                    String id = (String) body.getRel();
                                    if (id.equals(Preferences.getUserAccount())){
                                        // 说明是自己在开启了广播
                                        // 目前退出聊天就自动关闭广播
                                    } else {
                                        isBroadcast = true;
                                        TalkManage.getInstance().openBroadcastListen();
                                    }
                                }
                            }
                            if(TalkManage.isMeBroadcast){
                                FloatIntercomManage.getInstance().setCanTalk(true);
                            }else {
                                FloatIntercomManage.getInstance().setCanTalk(!TalkManage.isBroadcast);
                            }

                        }
                    }
                });

    }
    // 入群后,初始化加入或切换对讲组
    public void joinOrSwitchIntercomGroup(String groupId) {
        if (TextUtils.isEmpty(MsgExtensionType.groupId)) {
            MsgExtensionType.groupId = groupId;
            getTalkbackGroupBroadcast();
            // 加入对讲组
            AnyRtcMaxManage.getInstance().joinIntercomGroup();
        } else {
            if (!MsgExtensionType.groupId.equals(groupId)){
                TalkManage.isJoinarSuccess = true;//重新加入对讲组
                MsgExtensionType.groupId = groupId;
                getTalkbackGroupBroadcast();
                // 切换对讲组
                AnyRtcMaxManage.getInstance().switchIntercomGroup();
            }
        }

        TalkManage.isShowTalk = true;
        // 显示对讲按钮
        FloatIntercomManage.getInstance().showFloatView(ActivityUtils.getTopActivity());
    }

    // 切换对讲组
    public void switchIntercomGroup() {
        Logging.d(TAG, "对讲事件: 已加入对讲组,则切换对讲组");
        getTalkbackGroupBroadcast();
        // 切换对讲组
        AnyRtcMaxManage.getInstance().switchIntercomGroup();
    }

    // 对讲按钮移除
    public void intercomButtomRemove() {
        Logging.d(TAG, "按钮消了");
    }

    // 对讲按钮单击
    public void intercomButtomClick() {
        Logging.d(TAG, "单击我了");
        if (isShowSelectGroup){
            FloatIntercomManage.getInstance().setTalkStatus(6);
        }
    }

    // 对讲按钮双击
    public void intercomButtomDoubleClick() {
        Logging.d(TAG, "双击我了");
        if (isShowSelectGroup){
            FloatIntercomManage.getInstance().setTalkStatus(5);
        }
    }
    /**
     * 取消对讲说话或者取消录音
     */
    public void cancelTalkAudioRecord(){
        isPressed = false;
        FloatIntercomManage.getInstance().getView().playAnim(false);//隐藏对讲按钮动画
        AnyRtcMaxManage.getInstance().unRequestAudioFocus();
        if (isBroadcastListen){
            Logging.d(TAG, "判断事件: 当前正在收听广播中,停止一切活动");
            return;
        }
        if (isBroadcastSpeaking){
            Logging.d(TAG, "判断事件: 当前在广播中已开启对讲说话,结束对讲");
            openBroadcastSpeakingEnd();
        }
        if (isKitAudioRecord){ // 如果当前是对讲说话
            Logging.d(TAG, "判断事件: 当前是对讲录制, 则结束对讲说话");
            // 结束对讲说话
            AnyRtcMaxManage.getInstance().cancelTalk();
            // 隐藏时间记录表
            AnyRtcMaxManage.getInstance().hideTimerPop();
        } else {
            Logging.d(TAG, "判断事件: 当前是语音录制, 则结束语音录制");
            // 结束语音录制
            AudioRecordManage.getInstance().onEndAudioRecord(false);
            // 加入对讲组
            AnyRtcMaxManage.getInstance().joinIntercomGroup();
        }
    }
    // 对讲按钮抬起
    public void intercomButtomUpClick() {
        isPressed = false;
        EventBus.getDefault().postSticky(new AnyRtcStatusEvent(1052));
        Logging.d(TAG, "抬起对讲按钮了, 停止一切活动吧: " + System.currentTimeMillis());
        FloatIntercomManage.getInstance().getView().playAnim(false);//对讲动画播放
        AnyRtcMaxManage.getInstance().unRequestAudioFocus();
        if (isBroadcastListen){
            Logging.d(TAG, "判断事件: 当前正在收听广播中,停止一切活动");
            return;
        }
        if (isBroadcastSpeaking){
            Logging.d(TAG, "判断事件: 当前在广播中已开启对讲说话,结束对讲");
            openBroadcastSpeakingEnd();
        }
        if (isKitAudioRecord){ // 如果当前是对讲说话
            Logging.d(TAG, "判断事件: 当前是对讲录制, 则结束对讲说话");
            // 结束对讲说话
            AnyRtcMaxManage.getInstance().cancelTalk();
            // 隐藏时间记录表
            AnyRtcMaxManage.getInstance().hideTimerPop();
        } else {
            Logging.d(TAG, "判断事件: 当前是语音录制, 则结束语音录制");
            // 结束语音录制
            AudioRecordManage.getInstance().onEndAudioRecord(false);
            // 加入对讲组
            AnyRtcMaxManage.getInstance().joinIntercomGroup();
        }
        if(isTalkRecording){
            SoundPlayUtils.getInstance().play(SoundPlayUtils.getInstance().burst_release_voiceId);//对讲录音结束的提示声音
            isTalkRecording=false;
        }
    }

    // 对讲按钮按下
    public void intercomButtomTalkClick() {
        isPressed = true;
        if(AndPermission.hasPermissions(ActivityUtils.getTopActivity()
                ,Permission.READ_EXTERNAL_STORAGE,Permission.WRITE_EXTERNAL_STORAGE,Permission.RECORD_AUDIO)){
            applyTalkOrAudioRecord();
        }else {
            AndPermission.with(ActivityUtils.getTopActivity())
                    .runtime()
                    .permission(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE,Permission.RECORD_AUDIO)
                    .onGranted(new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> data) {
                            applyTalkOrAudioRecord();
                        }
                    }).onDenied(new Action<List<String>>() {
                @Override
                public void onAction(List<String> permissions) {
                    ToastUtils.showText(TourApplication.context,"请开启录音及存储权限");
                    FloatIntercomManage.getInstance().setTalkStatus(6);
                    // 打开权限设置页
                    PermissionUtil.gotoAppDetailSetting(ActivityUtils.getTopActivity());
                }
            }).start();

        }
    }

    /**
     * 申请对讲或者音频录制
     */
    public void applyTalkOrAudioRecord() {
        if(isPressed){
            isTalkRecording=false;
            if (isBroadcastListen){
                Logging.d(TAG, "判断事件: 当前正在收听广播中,停止一切活动");
                return;
            }
            EventBus.getDefault().postSticky(new AnyRtcStatusEvent(1051));
            /*判断是否申请对讲成功,成功: 则全程用对讲功能,消除云信语音功能失败: 则全程用云信功能,清除对讲说话功能*/
            isKitAudioRecord = false;
            if (isJoinarSuccess){
                Logging.d(TAG, "判断事件: 当前已加入对讲组");
                if (!isHadSomeOneSpeaking){
                    Logging.d(TAG, "判断事件: 当前没有人上麦");
                    // 申请对讲
                    int result = AnyRtcMaxManage.getInstance().applyTalk(2);
                    Logging.d(TAG, "对讲事件: 申请返回: result : " + result);
                    if (result == 0){
                        isKitAudioRecord = true;
                        isTalkRecording=true;
                        Logging.d(TAG, "对讲事件: 申请成功, 清除云信语音录制一系列操作,用对讲录制 ");
                        // 停止语音录音
                        AudioRecordManage.getInstance().pauseAudioRecorder();
                        // 注销语音录音
                        AudioRecordManage.getInstance().logoutAudioRecorder();
                    }
                }
            }
            if ( !isKitAudioRecord ){
                Logging.d("AnyRtc", "判断事件: 当前对讲申请失败: 则全程用云信功能,清除对讲说话功能");
                isHadSomeOneSpeaking=false;
                isTalkRecording=true;
                AnyRtcMaxManage.getInstance().requestAudioFocus(false);
                ToastUtils.showText(TourApplication.context,"别人正在讲话，开始录音");
                SoundPlayUtils.getInstance().play(SoundPlayUtils.getInstance().burst_alarm_voiceId);//录音开始的提示声音
                // 结束对讲说话
                AnyRtcMaxManage.getInstance().cancelTalk();
                // 离开通话
                AnyRtcMaxManage.getInstance().leaveCall();
                // 离开对讲组
                AnyRtcMaxManage.getInstance().leaveTalkGroup();
                // 停止语音播放状态
                AudioPlayManage.getInstance().stopAudioPlay();
                Logging.d("AnyRtc", "音频录制: 开启语音录制");
                // 初始化录音
                AudioRecordManage.getInstance().initAudioRecord();
                // 开始启动录音
                AudioRecordManage.getInstance().onStartAudioRecord();
            }
            if (isBroadcast){
                // 在广播中已开启对讲说话
                openBroadcastSpeakingStart();
            }
        }
    }
    // 音频播放下一条按钮点击事件
    public void audioPlayButtomNextClick() {
        Logging.d(TAG, "下一条按钮: 点击下一条了");
    }

    // 音频播放下一条按钮移除
    public void audioPlayButtomNextRemove() {
        Logging.d(TAG, "下一条按钮: 按钮消了");
    }

    // 开启广播
    public void openBroadcast() {
        isBroadcast = true;
        isMeBroadcast = true;
        isBroadcastSpeaking = false;
        // 向服务端发送发起广播通知状态
        postTalkbackGroupBroadcast(true);
        // 发送结束广播提示消息
        sendMessageTipBroadcast(true);
        // 关闭计时器
        cancelTimerBroadcast();
        // 开启倒计时30秒
        postDelayedBroadcast(30);
        // 发送事件,更新UI
        EventBus.getDefault().postSticky(new AnyRtcStatusEvent(1011));
    }

    // 结束广播
    public void closeBroadcast() {
        isBroadcast = false;
        isMeBroadcast = false;
        isBroadcastSpeaking = false;
        // 向服务端发送结束广播通知状态
        postTalkbackGroupBroadcast(false);
        // 发送结束广播提示消息
        sendMessageTipBroadcast(false);
        // 关闭计时器
        cancelTimerBroadcast();
        // 发送事件,更新UI
        EventBus.getDefault().postSticky(new AnyRtcStatusEvent(1012));
    }

    // 在广播中已开启对讲说话
    private void openBroadcastSpeakingStart() {
        isBroadcast = true;
        isBroadcastSpeaking = true;
        // 向服务端发送还在开启广播通知状态
        postTalkbackGroupBroadcast(true);
        // 先关闭计时器
        cancelTimerBroadcast();
        // 再重启倒计时器
        postDelayedBroadcast(30);
    }

    // 在广播中已开启对讲说话,后结束了对讲
    private void openBroadcastSpeakingEnd() {
        isBroadcast = true;
        isBroadcastSpeaking = false;
        // 向服务端发送还在开启广播通知状态
        postTalkbackGroupBroadcast(true);
        // 先关闭计时器
        cancelTimerBroadcast();
        // 再重启倒计时器
        postDelayedBroadcast(30);
    }

    // 收到有人广播通知
    public void openBroadcastListen() {
        isBroadcast = true;
        isBroadcastListen = true;
        isBroadcastListening = false;
        // 禁用对讲按钮
        FloatIntercomManage.getInstance().setTalkStatus(2);
        // 停止自己正在对讲状态(如果正在开启对讲中)
        AnyRtcMaxManage.getInstance().cancelTalk();
        // 停止语音播放状态
        AudioPlayManage.getInstance().stopAudioPlay();
        // 关闭计时器
        cancelTimerBroadcastListen();
        // 开启倒计时60,如果一直没有收听到对讲,则直接关闭广播
        postDelayedBroadcastListen(60);
        //收到其他群主或者领队发送的广播
        EventBus.getDefault().postSticky(new AnyRtcStatusEvent(1031));
    }

    // 收到结束广播通知
    public void closeBroadcastListen() {
        isBroadcast = false;
        isMeBroadcast = false;
        isBroadcastListen = false;
        isBroadcastListening = false;
        // 启动对讲按钮
        FloatIntercomManage.getInstance().setTalkStatus(1);
        // 先关闭计时器
        cancelTimerBroadcastListen();
        //收到其他群主或者领队发送的广播结束通知
        EventBus.getDefault().postSticky(new AnyRtcStatusEvent(1032));
    }

    // 收到有人广播,并开启了对讲说话通知
    public void openBroadcastListenStart() {
        isBroadcastListen = true;
        isBroadcastListening = true;
        // 先关闭计时器
        cancelTimerBroadcastListen();
        // 再开启倒计时60,如果一直没有收听到对讲,则直接关闭广播
        postDelayedBroadcastListen(60);
    }

    // 收到有人广播,并结束了对讲说话通知
    public void openBroadcastListenEnd() {
        isBroadcastListen = true;
        isBroadcastListening = false;
        // 先关闭计时器
        cancelTimerBroadcastListen();
        // 再开启倒计时60,如果一直没有收听到对讲,则直接关闭广播
        postDelayedBroadcastListen(60);
    }

    // 开启播放语音记录
    public void openAudioRecordPlay() {
        // 收到语音消息通知(已下载,从缓存中读取即可)
        if (isPressed){
            Logging.d(TAG, "判断事件: 当前自己正在按住说话");
        } else if (isHadSomeOneSpeaking) {
            Logging.d(TAG, "判断事件: 当前正在收听别人对讲讲话");
        } else if (isAudioPlaying){
            Logging.d(TAG, "判断事件: 当前正在收听音频播放");
        } else if(isBroadcast || isBroadcastListen){
            Logging.d(TAG, "判断事件: 当前正在广播中");
        } else {
            // 开始播放语音记录
            AudioPlayManage.getInstance().playAudioRecordDialog();
        }
    }

    // 注销
    public void logout(){
        try{
            isJoinarSuccess=false;
            AnyRtcMaxManage.getInstance().logoutTalk();
            AudioRecordManage.getInstance().logoutAudioRecorder();
            AudioPlayManage.getInstance().logoutAudioCache();
            TeamMessageAudioCacheManager.clearCacheAll();
            cancelTimerBroadcast();
            cancelTimerBroadcastListen();
        }catch (Exception e){}
    }
    // 清除对讲
    public void clearTalk(){
        //清除对讲
        try{
            isShowTalk=false;
            isJoinarSuccess=false;
            MsgExtensionType.groupId="";
            // 结束对讲说话
            AnyRtcMaxManage.getInstance().cancelTalk();
            // 离开通话
            AnyRtcMaxManage.getInstance().leaveCall();
            // 离开对讲组
            AnyRtcMaxManage.getInstance().leaveTalkGroup();
            // 停止语音播放状态
            AudioPlayManage.getInstance().stopAudioPlay();

            AudioRecordManage.getInstance().logoutAudioRecorder();
            AudioPlayManage.getInstance().logoutAudioCache();
            TeamMessageAudioCacheManager.clearCacheAll();
            cancelTimerBroadcast();
            cancelTimerBroadcastListen();
        }catch (Exception e){}
    }

    /**
     *  广播发起和结束发送给后台
     *  30s调一次，40s后自动清除
     */
    private void postTalkbackGroupBroadcast(boolean on){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.postTalkbackGroupBroadcast(SPUtils.getToken(), MsgExtensionType.groupId, on);
        mObservable.compose(ComposeUtil.compose())
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>() {
                    @Override
                    public void onNext(BaseDataBean<Object> body) {

                    }
                });
    }

    private void sendMessageTipBroadcast(boolean on) {
        if (on){
            // 发送开启广播提示消息
            BroadcastAction.sendMessageTipBroadcast(MsgExtensionType.groupId, SessionTypeEnum.Team, MsgExtensionType.Extension_Type_301);
        } else {
            // 发送结束广播提示消息
            BroadcastAction.sendMessageTipBroadcast(MsgExtensionType.groupId, SessionTypeEnum.Team, MsgExtensionType.Extension_Type_302);
        }
    }

    //发送延迟消息(发送者监听广播)
    public void postDelayedBroadcast(long s) {
        if (mTimerBroadcast == null) {
            // 倒计时器 总时5秒 间隔5秒
            mTimerBroadcast = new CountDownTimer((long) (s * 1000), (long) (s * 1000)) {

                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    // 关闭计时器
                    cancelTimerBroadcast();
                    if (isBroadcastSpeaking){
                        // 在广播中已开启对讲说话
                        openBroadcastSpeakingStart();
                    } else {
                        // 结束广播
                        closeBroadcast();
                    }
                }
            };
            mTimerBroadcast.start();
        }
    }

    /**
     * 关闭计时器(发送者监听广播)
     */
    private void cancelTimerBroadcast() {
        if (mTimerBroadcast != null) {
            mTimerBroadcast.cancel();
            mTimerBroadcast = null;
        }
    }

    //发送延迟消息(收听者监听广播)
    public void postDelayedBroadcastListen(long s) {
        if (mTimerBroadcastListen == null) {
            // 倒计时器 总时5秒 间隔5秒
            mTimerBroadcastListen = new CountDownTimer((long) (s * 1000), (long) (s * 1000)) {

                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    // 关闭计时器
                    cancelTimerBroadcastListen();
                    if (isBroadcastListening){
                        // 收到有人广播,并开启了对讲说话通知
                        openBroadcastListenStart();
                    } else {
                        // 收到无人广播通知
                        closeBroadcastListen();
                    }
                }
            };
            mTimerBroadcastListen.start();
        }
    }

    /**
     * 关闭计时器(收听者监听广播)
     */
    private void cancelTimerBroadcastListen() {
        if (mTimerBroadcastListen != null) {
            mTimerBroadcastListen.cancel();
            mTimerBroadcastListen = null;
        }
    }

    public void showPopGroupList(){
        if (groupListPopupWindow == null){
            groupListPopupWindow = new IntercomSwitchGroupListPopupWindow(ActivityUtils.getTopActivity());
        }else if(!ActivityUtils.getTopActivity().equals(groupListPopupWindow.getActivity())){
            groupListPopupWindow = new IntercomSwitchGroupListPopupWindow(ActivityUtils.getTopActivity());
        }
        groupListPopupWindow.showPop(FloatIntercomManage.getInstance().getView(),MsgExtensionType.groupId);
    }
    /**
     * 消息接收观察者
     */
    Observer<List<IMMessage>> receiveMessageObserver = new Observer<List<IMMessage>>() {

        @Override
        public void onEvent(List<IMMessage> messages) {
            for (IMMessage message : messages) {
                MsgAttachment msgAttachment=message.getAttachment();
                if(msgAttachment instanceof NotificationAttachment){
                    NotificationAttachment notificationAttachment= (NotificationAttachment) msgAttachment;
                    switch (notificationAttachment.getType()) {
                        case KickMember:{
                            //移除群成员
                            if(notificationAttachment instanceof MemberChangeAttachment) {
                                MemberChangeAttachment memberChangeAttachment = (MemberChangeAttachment) msgAttachment;
                                ArrayList<String> targets = memberChangeAttachment.getTargets();
                                UserBasicInfoBean user=TourApplication.getUser();
                                if(user!=null&&targets!=null&&targets.contains(""+user.getUserId())){
                                    UserHelper.clearTalkBusiness(false);
                                    ToastUtils.showText(TourApplication.context,"您已经被移除队伍了！");
                                }
                            }
                            break;
                        }
                        case LeaveTeam:{
                            //有成员离开群
                            UserBasicInfoBean user=TourApplication.getUser();
                            String fromAccount=message.getFromAccount();
                            String attachStr=message.getAttachStr();
                            if(user!=null&&fromAccount!=null&&fromAccount.equals(""+user.getUserId())){
                                UserHelper.clearTalkBusiness(false);
                                ToastUtils.showText(TourApplication.context,"退出队伍成功");
                            }
                            break;
                        }
                        case DismissTeam:{
                            //群被解散
                            UserHelper.clearTalkBusiness(false);
                            UserBasicInfoBean user=TourApplication.getUser();
                            String fromAccount=message.getFromAccount();
                            if(user!=null&&fromAccount!=null&&fromAccount.equals(""+user.getUserId())){
                                ToastUtils.showText(TourApplication.context,"解散队伍成功");
                            }else {
                                ToastUtils.showText(TourApplication.context,"队伍已解散");
                            }
                            break;
                        }
                    }
                }
                if (message.getRemoteExtension() != null && message.getRemoteExtension().size() > 0){
                    Map<String, Object> val = message.getRemoteExtension();
                    String type = (String) val.get(MsgExtensionType.Extension_Type);
                    String groupId = (String) val.get(MsgExtensionType.Extension_GroupId);
                    if(groupId==null){
                        groupId=  message.getSessionId();
                    }
                    if (groupId!=null&&groupId.equals(MsgExtensionType.groupId)){
                        // 判断type是否对讲录音
                        if (type.equals(MsgExtensionType.Extension_Type_101)){
                            // 将未读标识去掉,更新数据库
                            message.setStatus(MsgStatusEnum.read);
                            NIMClient.getService(MsgService.class).updateIMMessageStatus(message);
                        }
                        if (type.equals(MsgExtensionType.Extension_Type_301)){
                            //接收到广播消息
                            openBroadcastListen();
                        }
                        if (type.equals(MsgExtensionType.Extension_Type_302)){
                            //接收到结束广播消息
                            closeBroadcastListen();
                        }
                    }

                }
            }
        }
    };
}
