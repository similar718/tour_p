package cn.xmzt.www.intercom.vm;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.MemberPushOption;
import com.netease.nimlib.sdk.msg.model.MessageReceipt;
import com.netease.nimlib.sdk.robot.model.NimRobotInfo;
import com.netease.nimlib.sdk.robot.model.RobotAttachment;
import com.netease.nimlib.sdk.robot.model.RobotMsgType;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.databinding.ActivityTeamRoomBinding;
import cn.xmzt.www.dialog.ConfirmDialog;
import cn.xmzt.www.dialog.TextTitleDialog;
import cn.xmzt.www.helper.UserHelper;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.intercom.actions.BaseAction;
import cn.xmzt.www.intercom.actions.BroadcastAction;
import cn.xmzt.www.intercom.actions.CameraAction;
import cn.xmzt.www.intercom.actions.ContactLeaderAction;
import cn.xmzt.www.intercom.actions.VideoAction;
import cn.xmzt.www.intercom.activity.GroupInfoActivity;
import cn.xmzt.www.intercom.activity.TeamRoomActivity;
import cn.xmzt.www.intercom.adapter.ChatMsgInputPanel;
import cn.xmzt.www.intercom.adapter.ChatMsgListPanel;
import cn.xmzt.www.intercom.api.NimUIKit;
import cn.xmzt.www.intercom.api.UIKitOptions;
import cn.xmzt.www.intercom.api.model.contact.ContactChangedObserver;
import cn.xmzt.www.intercom.api.model.main.CustomPushContentProvider;
import cn.xmzt.www.intercom.api.model.team.TeamDataChangedObserver;
import cn.xmzt.www.intercom.api.model.team.TeamMemberDataChangedObserver;
import cn.xmzt.www.intercom.bean.GroupMemberBean;
import cn.xmzt.www.intercom.bean.GroupRoomBean;
import cn.xmzt.www.intercom.bean.TalkUserInfoBean;
import cn.xmzt.www.intercom.bean.TeamLocationBean;
import cn.xmzt.www.intercom.bean.UserBasicInfoBean;
import cn.xmzt.www.intercom.business.ait.AitManager;
import cn.xmzt.www.intercom.common.CommonUtil;
import cn.xmzt.www.intercom.common.ToastHelper;
import cn.xmzt.www.intercom.common.util.sys.TimeUtil;
import cn.xmzt.www.intercom.event.BackMapAndChatStatusEvent;
import cn.xmzt.www.intercom.event.LocationSendEvent;
import cn.xmzt.www.intercom.impl.NimUIKitImpl;
import cn.xmzt.www.intercom.preference.Preferences;
import cn.xmzt.www.intercom.profile.TeamLocationProfile;
import cn.xmzt.www.intercom.session.module.Container;
import cn.xmzt.www.intercom.session.module.ModuleProxy;
import cn.xmzt.www.main.activity.MainActivity;
import cn.xmzt.www.main.globals.NimLoginManage;
import cn.xmzt.www.main.globals.TalkManage;
import cn.xmzt.www.manager.IntentManager;
import cn.xmzt.www.roomdb.TourDatabase;
import cn.xmzt.www.roomdb.beans.GroupUserInfo;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.selfdrivingtools.activity.SharedNavigationMapActivity;
import cn.xmzt.www.smartteam.activity.SmartTeamMapActivity;
import cn.xmzt.www.utils.NetWorkUtils;
import cn.xmzt.www.utils.PermissionUtil;
import cn.xmzt.www.utils.SPUtils;
import io.reactivex.Observable;

//import cn.xmzt.www.intercom.cache.TeamMessageLocationCacheManager;

/**
 * 聊天界面Model
 * @author Averysk
 */
@SuppressWarnings("ALL")
public class TeamRoomViewModel extends BaseViewModel implements ModuleProxy, View.OnTouchListener {

    private TeamRoomActivity activity;
    public MutableLiveData<GroupRoomBean> mutableLiveData;
    public MutableLiveData<GroupMemberBean> mutableLiveDataMember;
    public Team team;
    public boolean isMyTeam=true;//我是否在这个群里
    public int tripId;
    public String groupId;
    public boolean success = false;
    public boolean isShareLocation = false;         // 是否共享位置
    public boolean isBroadcast = TalkManage.isBroadcast;             // 是否广播中,  false:结束广播了 ,  true: 发起广播中
    public GroupRoomBean groupRoomBean;
    public GroupMemberBean groupMemberBean;
    public ChatMsgInputPanel inputPanel;
    public ChatMsgListPanel messageListPanel;
    public AitManager aitManager;
    public ConfirmDialog mHintDialog = null;
    public Container container;
    public TeamRoomViewModel viewModel;
    public ActivityTeamRoomBinding dataBinding;
    public TextTitleDialog dialogBroadcast;
    public boolean isSmartTeam=false;//是否是智能组队的功能

    public TeamRoomViewModel(TeamRoomActivity activity, ActivityTeamRoomBinding dataBinding){
        this.viewModel = this;
        this.activity = activity;
        this.dataBinding = dataBinding;
        mutableLiveData = new MutableLiveData<GroupRoomBean>();
        mutableLiveDataMember = new MutableLiveData<GroupMemberBean>();
    }

    public void setActivity(TeamRoomActivity activity) {
        this.activity = activity;
    }
    /**
     * 获取云信的群基本信息
     */
    public void getNimTeamInfo() {
        NIMClient.getService(TeamService.class).queryTeam(viewModel.groupId).setCallback(new RequestCallbackWrapper<Team>() {
            @Override
            public void onResult(int code, Team t, Throwable exception) {
                if (code == ResponseCode.RES_SUCCESS) {
                    viewModel.team = t;
                    if (viewModel.team != null) {
                        viewModel.success = true;
                        if(groupRoomBean!=null){
                            mutableLiveData.setValue(groupRoomBean);
                        }
                    }
                    dataBinding.rlPromptTeam.setVisibility(View.GONE);
                    viewModel.registerTeamUpdateObserver(true);
                } else {
                    dataBinding.rlPromptTeam.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    /**
     * 获取入群信息
     * @param cityCode
     */
    public void getGroupRoomInfo(TeamRoomActivity teamRoomActivity) {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getGroupRoomInfo(TourApplication.getToken(), groupId);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<GroupRoomBean>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<GroupRoomBean> body) {
                        if(body.isSuccess()){
                            mutableLiveData.setValue(body.getRel());
                            getGroupUserInfo();
                        }else {
                            if(BaseDataBean.CODE_NO_GROUP_MEMBER.equals(body.getReCode())){
                               ToastUtils.showShort("非群成员,入群失败");
                            }else {
                                if(TextUtils.isEmpty(body.getReMsg())){
                                    ToastUtils.showShort("入群失败");
                                }else {
                                    ToastUtils.showShort(body.getReMsg());
                                }
                            }
                            if(!teamRoomActivity.isFinishing()){
                                teamRoomActivity.onBackPressed();
                            }
                        }
                    }
                });
    }

    /**
     * 获取群成员信息
     * @param cityCode
     */
    public void getGroupUserInfo() {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getGroupUser(SPUtils.getToken(), groupId, "");
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<GroupMemberBean>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<GroupMemberBean> body) {
                        if(body.isSuccess()){
                            mutableLiveDataMember.setValue(body.getRel());
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    public void back(){
        EventBus.getDefault().post(new BackMapAndChatStatusEvent(groupId,1)); // finishing掉地图界面
        activity.onBackPressed();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                back();
                break;
            case R.id.iv_group_info:
                if(isSmartTeam){
                    //启动智能组队的群详情页
                    IntentManager.getInstance().goSmartTeamGroupInfoActivity(activity,groupId);
                }else {
                    if(isMyTeam){
                        GroupInfoActivity.start(activity, groupId, "", 0); // 启动固定群资料页
                    }else {
                        ToastUtils.showShort("您已经被移除群了！");
                    }
                }
                break;
            case R.id.rl_prompt_position: // 顶部共享位置
                break;
            case R.id.iv_goto_position: // 顶部共享位置右侧箭头
                break;
            case R.id.rl_prompt_broadcast: // 顶部广播提示
                break;
            case R.id.iv_cancel_broadcast: // 顶部广播提示右侧叉叉
                // 结束广播
                TalkManage.getInstance().closeBroadcast();
                break;
            case R.id.btn_new_msg: // 未读消息
                break;
            case R.id.btn_location: // 导航入口
                // 当前是要去智能组队
                if (isSmartTeam) {
                    if (ActivityUtils.isActivityExistsInStack(SharedNavigationMapActivity.class)) {
                        // 在的情况
                        gotoSharedNavigationMap(true, true); // 关闭当前界面并且跳转
                        gotoSmartTeamMap(false,false);
                        activity.onBackPressed();
                    } else {
                        gotoSmartTeamMap(false,false);
                        activity.onBackPressed();
                    }
                } else {
                    if (ActivityUtils.isActivityExistsInStack(SmartTeamMapActivity.class)) {
                        // 在的情况
                        gotoSmartTeamMap(true, true); // 关闭当前界面并且跳转
                        gotoSharedNavigationMap(false,false);
                        activity.onBackPressed();
                    } else {
                        gotoSharedNavigationMap(false,false);
                        activity.onBackPressed();
                    }
                }
                break;
            case R.id.tv_bottom_send: // 底部输入-发送按钮
                inputPanel.onTextMessageSendButtonPressed();
                break;
            case R.id.iv_bottom_photo: // 底部输入-照片按钮
                if(AndPermission.hasPermissions(ActivityUtils.getTopActivity(),Permission.READ_EXTERNAL_STORAGE,Permission.WRITE_EXTERNAL_STORAGE)){
                    inputPanel.openPhotoAction();
                }else {
                    ToastUtils.showShort("需要获取存储权限，请到权限中心设置");
                }
                break;
            case R.id.iv_bottom_position: // 底部输入-位置按钮
                inputPanel.openLocationAction();
                break;
            case R.id.iv_bottom_emoticon: // 底部输入-表情包按钮
                inputPanel.toggleEmojiLayout();
                break;
            case R.id.iv_bottom_plus: // 底部输入-加号:显示更多功能
                inputPanel.toggleActionPanelLayout(true);
                break;
            case R.id.iv_bottom_cross: // 底部输入-叉号:隐藏更多功能
                inputPanel.toggleActionPanelLayout(false);
                break;
            case R.id.iv_bottom_voice: // 点击底部对讲按钮
                shouldCollapseInputPanel();
                break;
        }
    }


    public void initViewPanel() {
        container = new Container(activity, groupId, SessionTypeEnum.Team, this, true);
        if (messageListPanel == null) {
            messageListPanel = new ChatMsgListPanel(activity, viewModel, dataBinding, container);
        } else {
            messageListPanel.reload(container, null);
        }
        if (inputPanel == null) {
            inputPanel = new ChatMsgInputPanel(activity, viewModel, dataBinding, groupId, container);
        } else {
            inputPanel.reload(container);
        }
        dataBinding.messageListView.setOnTouchListener(this);
        initAitManager();
        // 设置发送消息容器(必须先设置)
//        TalkManage.getInstance().initTalkManage(activity);
        // 入群后,加入或切换对讲组
        TalkManage mTalkManage=TalkManage.getInstance();
        mTalkManage.joinOrSwitchIntercomGroup(groupId);
    }


    private void initAitManager() {
        UIKitOptions options = NimUIKitImpl.getOptions();
        if (options.aitEnable) {
            aitManager = new AitManager(activity, groupId, options.aitIMRobot);
            inputPanel.addAitTextWatcher(aitManager);
            aitManager.setTextChangeListener(inputPanel);
        }
    }


    /**
     * 注册群信息更新监听
     *
     * @param register
     */
    public void registerTeamUpdateObserver(boolean register) {
        // 监听群数据更新监听
        NimUIKit.getTeamChangedObservable().registerTeamDataChangedObserver(teamDataChangedObserver, register);
        // 监听群成员更新监听
        NimUIKit.getTeamChangedObservable().registerTeamMemberDataChangedObserver(teamMemberDataChangedObserver, register);
        // 监听好友数据更新监听
        NimUIKit.getContactChangedObservable().registerObserver(friendDataChangedObserver, register);
        // 监听群消息接收观察者
        NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(incomingMessageObserver, register);
        // 监听已读回执观察者
        if (NimUIKitImpl.getOptions().shouldHandleReceipt) {
            NIMClient.getService(MsgServiceObserve.class).observeMessageReceipt(messageReceiptObserver, register);
        }
    }

    /**
     * 群资料变动通知和移除群的通知（包括自己退群和群被解散）
     */
    TeamDataChangedObserver teamDataChangedObserver = new TeamDataChangedObserver() {
        @Override
        public void onUpdateTeams(List<Team> teams) {
            if (team == null) {
                return;
            }
            for (Team t : teams) {
                if (t.getId().equals(team.getId())) {
                    team = t;
                    activity.updateTeamInfo();
                    break;
                }
            }
        }

        @Override
        public void onRemoveTeam(Team t) {
            if (t == null) {
                return;
            }
            String teamId=t.getId();
            String oldTeamId="";
            if(team!=null){
                oldTeamId=team.getId();
            }
            if (teamId!=null&&teamId.equals(oldTeamId)) {
                team = t;
                isMyTeam=t.isMyTeam();
                if(!isMyTeam){
                    // 相除对讲相关业务
                    UserHelper.clearTalkBusiness(false);
                    Intent intent = new Intent(activity, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    activity.startActivity(intent);
                }
                activity.updateTeamInfo();
            }
        }
    };

    /**
     * 群成员资料变动通知和移除群成员通知
     */
    TeamMemberDataChangedObserver teamMemberDataChangedObserver = new TeamMemberDataChangedObserver() {

        @Override
        public void onUpdateTeamMember(List<TeamMember> members) {
            refreshMessageList();
        }

        @Override
        public void onRemoveTeamMember(List<TeamMember> member) {
        }
    };

    /**
     * 群成员数据改变通知
     */
    ContactChangedObserver friendDataChangedObserver = new ContactChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            refreshMessageList();
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            refreshMessageList();
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            refreshMessageList();
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            refreshMessageList();
        }
    };

    /**
     * 消息接收观察者
     */
    Observer<List<IMMessage>> incomingMessageObserver = new Observer<List<IMMessage>>() {

        @Override
        public void onEvent(List<IMMessage> messages) {
            onMessageIncoming(messages);
        }
    };

    private void onMessageIncoming(List<IMMessage> messages) {
        if (CommonUtil.isEmpty(messages)) {
            return;
        }
        messageListPanel.onIncomingMessage(messages);
        // 发送已读回执
        messageListPanel.sendReceipt();
    }

    /**
     * 已读回执观察者
     */
    private Observer<List<MessageReceipt>> messageReceiptObserver = new Observer<List<MessageReceipt>>() {
        @Override
        public void onEvent(List<MessageReceipt> messageReceipts) {
            messageListPanel.receiveReceipt();
        }
    };

    // 是否允许发送消息
    protected boolean isAllowSendMessage(final IMMessage message) {
        if (team == null) {
            team = NimUIKit.getTeamProvider().getTeamById(groupId);
        }
        if (team != null && !team.isMyTeam()) {
            ToastHelper.showToast(activity, R.string.team_send_message_not_allow);
            return false;
        }
        return true;
    }

    // 追加群成员推送
    private void appendTeamMemberPush(IMMessage message) {
        if (aitManager == null) {
            return;
        }
        List<String> pushList = aitManager.getAitTeamMember();
        if (pushList == null || pushList.isEmpty()) {
            return;
        }
        MemberPushOption memberPushOption = new MemberPushOption();
        memberPushOption.setForcePush(true);
        memberPushOption.setForcePushContent(message.getContent());
        memberPushOption.setForcePushList(pushList);
        message.setMemberPushOption(memberPushOption);
    }

    // 改为机器人消息(如果为机器人消息)
    private IMMessage changeToRobotMsg(IMMessage message) {
        if (aitManager == null) {
            return message;
        }
        if (message.getMsgType() == MsgTypeEnum.robot) {
            return message;
        }
        if (isChatWithRobot()) {
            if (message.getMsgType() == MsgTypeEnum.text && message.getContent() != null) {
                String content = message.getContent().equals("") ? " " : message.getContent();
                message = MessageBuilder.createRobotMessage(message.getSessionId(), message.getSessionType(), message.getSessionId(), content, RobotMsgType.TEXT, content, null, null);
            }
        } else {
            String robotAccount = aitManager.getAitRobot();
            if (TextUtils.isEmpty(robotAccount)) {
                return message;
            }
            String text = message.getContent();
            String content = aitManager.removeRobotAitString(text, robotAccount);
            content = content.equals("") ? " " : content;
            message = MessageBuilder.createRobotMessage(message.getSessionId(), message.getSessionType(), robotAccount, text, RobotMsgType.TEXT, content, null, null);
        }
        return message;
    }

    // 判断是否在与机器人聊天
    private boolean isChatWithRobot() {
        return NimUIKitImpl.getRobotInfoProvider().getRobotByAccount(groupId) != null;
    }

    // 追加推送配置
    private void appendPushConfig(IMMessage message) {
        CustomPushContentProvider customConfig = NimUIKitImpl.getCustomPushContentProvider();
        if (customConfig == null) {
            return;
        }
        String content = customConfig.getPushContent(message);
        Map<String, Object> payload = customConfig.getPushPayload(message);
        if (!TextUtils.isEmpty(content)) {
            message.setPushContent(content);
        }
        if (payload != null) {
            message.setPushPayload(payload);
        }
    }

    // 被对方拉入黑名单后，发消息失败的交互处理
    private void sendFailWithBlackList(int code, IMMessage msg) {
        if (code == ResponseCode.RES_IN_BLACK_LIST) {
            // 如果被对方拉入黑名单，发送的消息前不显示重发红点
            msg.setStatus(MsgStatusEnum.success);
            NIMClient.getService(MsgService.class).updateIMMessageStatus(msg);
            refreshMessageList();
            // 同时，本地插入被对方拒收的tip消息
            IMMessage tip = MessageBuilder.createTipMessage(msg.getSessionId(), msg.getSessionType());
            tip.setContent(activity.getString(R.string.black_list_send_tip));
            tip.setStatus(MsgStatusEnum.success);
            CustomMessageConfig config = new CustomMessageConfig();
            config.enableUnreadCount = false;
            tip.setConfig(config);
            NIMClient.getService(MsgService.class).saveMessageToLocal(tip, true);
        }
    }

    /************************ implements ModuleProxy bagin**********************/
    @Override
    public boolean sendMessage(IMMessage message) {
        if (isAllowSendMessage(message)) {
            appendTeamMemberPush(message);
            message = changeToRobotMsg(message);
            final IMMessage msg = message;
            appendPushConfig(message);
            // send message to server and save to db
            NIMClient.getService(MsgService.class).sendMessage(message, false).setCallback(new RequestCallback<Void>() {
                @Override
                public void onSuccess(Void param) {}

                @Override
                public void onFailed(int code) {
                    sendFailWithBlackList(code, msg);
                }

                @Override
                public void onException(Throwable exception) {}
            });

        } else {
            // 替换成tip
            message = MessageBuilder.createTipMessage(message.getSessionId(), message.getSessionType());
            message.setContent("该消息无法发送");
            message.setStatus(MsgStatusEnum.success);
            NIMClient.getService(MsgService.class).saveMessageToLocal(message, false);
        }
        messageListPanel.onMsgSend(message);
        if (aitManager != null) {
            aitManager.reset();
        }
        return true;

    }

    @Override
    public void onInputPanelExpand() {
        messageListPanel.scrollToBottom();
    }

    @Override
    public void shouldCollapseInputPanel() {
        inputPanel.collapse(false);
    }

    @Override
    public boolean isLongClickEnabled() {
        // 是否正在录音
        return false;
    }

    @Override
    public void onItemFooterClick(IMMessage message) {
        if (aitManager == null) {
            return;
        }
        if (messageListPanel.isSessionMode()) {
            RobotAttachment attachment = (RobotAttachment) message.getAttachment();
            NimRobotInfo robot = NimUIKitImpl.getRobotInfoProvider().getRobotByAccount(attachment.getFromRobotAccount());
            aitManager.insertAitRobot(robot.getAccount(), robot.getName(), inputPanel.getEditSelectionStart());
        }
    }
    /************************ implements ModuleProxy end**********************/
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (aitManager != null) {
            aitManager.onActivityResult(requestCode, resultCode, data);
        }
        inputPanel.onActivityResult(requestCode, resultCode, data);
        messageListPanel.onActivityResult(requestCode, resultCode, data);
    }

    public boolean onBackPressed() {
        if (isBroadcast&&dataBinding.ivCancelBroadcast.getVisibility()==View.VISIBLE){
            showBackTeamMenu();
            return true;
        }
        return inputPanel.collapse(true) || messageListPanel.onBackPressed();
    }

    public void refreshMessageList() {
        messageListPanel.refreshMessageList();
    }

    // 进入导航入口
    private void gotoSharedNavigationMap(boolean isfinish,boolean istz) {
        // 跳转到行程详请界面 需要传入群id  行程id  推荐码 是否分享界面  GC76F3G9 37414
        if ( ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (NetWorkUtils.isNetConnected(activity)) {
//                if (GPSUtils.isOPen(activity)) {
                    IntentManager.getInstance().goSharedNavigationMapActivitys(activity, groupId,isfinish,istz);
                    activity.onBackPressed();
//                } else {
//                    cn.xmzt.www.utils.ToastUtils.showText(activity,"请打开位置权限/GPS位置信息");
//                }
            } else {
                cn.xmzt.www.utils.ToastUtils.showText(activity,"网络异常");
            }
        } else {
            showConfirmDialog();
        }
    }

    // 进入导航入口
    private void gotoSmartTeamMap(boolean isfinish,boolean istz) {
        // 跳转到行程详请界面 需要传入群id  行程id  推荐码 是否分享界面  GC76F3G9 37414
        if ( ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (NetWorkUtils.isNetConnected(activity)) {
//                if (GPSUtils.isOPen(activity)) {
                    IntentManager.getInstance().goSmartTeamMapActivitys(activity, groupId,false,isfinish,istz);
                    activity.onBackPressed();
//                } else {
//                    cn.xmzt.www.utils.ToastUtils.showText(activity,"请打开位置权限/GPS位置信息");
//                }
            } else {
                cn.xmzt.www.utils.ToastUtils.showText(activity,"网络异常");
            }
        } else {
            showConfirmDialog();
        }
    }

    private void showConfirmDialog() {
        mHintDialog = new ConfirmDialog(activity, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionUtil.gotoPermission(activity);
                mHintDialog.dismiss();
            }
        });
        mHintDialog.setCancelable(false);
        mHintDialog.setViewData("获取位置信息权限失败，不能正常使用");
        mHintDialog.show();
    }
    public BroadcastAction broadcastAction;
    public ContactLeaderAction contactLeaderAction;
    public void setGroupMemberToView() {
        if (groupRoomBean != null&&groupRoomBean.getLeaderList().size()>0){
            contactLeaderAction=new ContactLeaderAction(groupRoomBean.getLeaderList());
        }else {
            contactLeaderAction=null;
        }
        if (groupMemberBean != null){
            isShareLocation = groupMemberBean.isShareLocation();
            if (groupMemberBean.isLeader()){
                List<BaseAction> actions = new ArrayList<>();
                actions.add(new VideoAction());
                actions.add(new CameraAction());
                broadcastAction=new BroadcastAction();
                actions.add(broadcastAction);
                if(contactLeaderAction!=null){
                    actions.add(contactLeaderAction);
                }
                inputPanel.setActionsPanel(actions);
            } else {
                List<BaseAction> actions = new ArrayList<>();
                actions.add(new VideoAction());
                actions.add(new CameraAction());
                if(contactLeaderAction!=null){
                    actions.add(contactLeaderAction);
                }
                inputPanel.setActionsPanel(actions);
            }
        }
    }

    // 倒计时器
    public CountDownTimer mTimer;
    //发送延迟消息(默认延迟1秒)
    public void postDelayed() {
        if (mTimer == null) {
            // 倒计时器 总时5秒 间隔5秒
            mTimer = new CountDownTimer((long) (1 * 1000), (long) (1 * 1000)) {

                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    // 关闭计时器
                    cancelTimer();
                    EventBus.getDefault().post(new LocationSendEvent(groupId));
                }
            };
            mTimer.start();
        }
    }
    //发送延迟消息
    public void postDelayed(long s) {
        if (mTimer == null) {
            // 倒计时器 总时5秒 间隔5秒
            mTimer = new CountDownTimer((long) (s * 1000), (long) (s * 1000)) {

                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    // 关闭计时器
                    cancelTimer();
                    EventBus.getDefault().post(new LocationSendEvent(groupId));
                }
            };
            mTimer.start();
        }
    }

    /**
     * 关闭计时器
     */
    public void cancelTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    /**
     * 发送消息群位置
     * @param type 1: 开启位置更新   2: 关闭位置更新
     */
    public void sendMessageTeamLocation(int type) {
        TeamLocationBean teamLocationBean = new TeamLocationBean();
        teamLocationBean.setGroup_id(groupId);
        teamLocationBean.setMsg_type(type + "");
        teamLocationBean.setSend_msg_id(Preferences.getUserAccount());
        teamLocationBean.setSend_msg_nick("");
        teamLocationBean.setSend_msg_type("2");
        teamLocationBean.setSend_msg_avatar("");
        teamLocationBean.setSend_msg_numberPlate("");
        teamLocationBean.setSend_msg_timestamp(TimeUtil.currentTimeMillis() + "");
        teamLocationBean.setSend_msg_latitude(Constants.mLatitude + "");
        teamLocationBean.setSend_msg_longitude(Constants.mLongitude + "");
        if (groupMemberBean != null){
            UserBasicInfoBean userBasicInfoBean = TourApplication.getUser();
            int userId = 0;
            if (groupMemberBean.getUserId() == 0){
                if (userBasicInfoBean != null){
                    if ( !TextUtils.isEmpty(userBasicInfoBean.getUsername())){
                        userId = userBasicInfoBean.getUserId();
                    }
                }
            } else {
                userId = groupMemberBean.getUserId();
            }
            teamLocationBean.setSend_msg_id(userId + "");
            String nick = "";
            if ( !TextUtils.isEmpty(groupMemberBean.getUserName())){
                nick = groupMemberBean.getUserName();
            } else {
                if (userBasicInfoBean != null){
                    if ( !TextUtils.isEmpty(userBasicInfoBean.getUsername())){
                        nick = userBasicInfoBean.getUsername();
                    } else {
                        nick = userBasicInfoBean.getTel();
                    }
                }
            }
            String avatar = "";
            if ( !TextUtils.isEmpty(groupMemberBean.getImage())){
                avatar = groupMemberBean.getImage();
            } else {
                if (userBasicInfoBean != null){
                    if ( !TextUtils.isEmpty(userBasicInfoBean.getHead())){
                        avatar = userBasicInfoBean.getHead();
                    }
                }
            }
            teamLocationBean.setSend_msg_nick(nick);
            teamLocationBean.setSend_msg_type(groupMemberBean.getRole() == 1 ? "0" : groupMemberBean.isLeader() ? "1" : "2");
            teamLocationBean.setSend_msg_avatar(avatar);
            teamLocationBean.setSend_msg_numberPlate(groupMemberBean.getLicencePlate());
        }

        TeamLocationProfile.getInstance().sendMessageNotificationTeamLocation(teamLocationBean);

        // 保存到本地数据库
        GroupUserInfo userInfo = new GroupUserInfo();
        userInfo.groupId = teamLocationBean.getGroup_id();
        userInfo.userId = teamLocationBean.getSend_msg_id();
        userInfo.type = teamLocationBean.getSend_msg_type_int();
        userInfo.avatar = teamLocationBean.getSend_msg_avatar();
        userInfo.numberPlate = teamLocationBean.getSend_msg_numberPlate();
        userInfo.nickName = teamLocationBean.getSend_msg_nick();
        userInfo.time = teamLocationBean.getSend_msg_timestamp_long();
        userInfo.latitude = teamLocationBean.getSend_msg_latitude_double();
        userInfo.longitude = teamLocationBean.getSend_msg_longitude_double();
        TourDatabase.getDefault(ActivityUtils.getTopActivity()).getGroupUserInfoDao().insert(userInfo);
//        TeamMessageLocationCacheManager.sendMessageNotificationTeamLocation(teamLocationBean);
    }

    /**
     * 返回时显示广播是否在线的提示菜单
     */
    private void showBackTeamMenu() {
        dialogBroadcast = new TextTitleDialog(activity, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 取消返回
                dialogBroadcast.dismiss();

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 确认返回
                // 结束广播
                TalkManage.getInstance().closeBroadcast();
                isBroadcast = false;
                activity.onBackPressed();
                back();
            }
        });
        dialogBroadcast.setTitle("当前正在发起广播，是否结束广播？");
        dialogBroadcast.show();
    }

    // 点击更多布局中的 "小视频" 或 "相机" 或 "广播"
    public void toggleActionsLayout(){
        inputPanel.toggleActionsLayout();
    }

    // 加载数据云信
    public void loadDataNim(){
//        if (!TextUtils.isEmpty(Preferences.getUserAccount()) && !TextUtils.isEmpty(Preferences.getUserToken())){
//            nimLoginManage.loginNim(Preferences.getUserAccount(), Preferences.getUserToken());
//        } else {
//        }
        getNimTeamInfo();
        getTalkUserAccount();
    }

    private void getTalkUserAccount() {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getTalkUserAccount(TourApplication.getToken());
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<TalkUserInfoBean>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<TalkUserInfoBean> body) {
                        if (body.isSuccess()) {
                            //mView.hideLoading();
                            TalkUserInfoBean bean = body.getRel();
                            if (bean != null) {
                                NimLoginManage.getInstance().loginNim(bean.getAccid(), bean.getToken());
                            }
                        }
                    }
                });
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            shouldCollapseInputPanel();
        }
        return false;
    }
}
