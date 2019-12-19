package cn.xmzt.www.intercom.activity;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityTeamRoomBinding;
import cn.xmzt.www.dialog.GuideDialog;
import cn.xmzt.www.intercom.bean.GroupMemberBean;
import cn.xmzt.www.intercom.bean.GroupRoomBean;
import cn.xmzt.www.intercom.event.AnyRtcStatusEvent;
import cn.xmzt.www.intercom.event.LocationSendEvent;
import cn.xmzt.www.intercom.event.LocationShareStatusEvent;
import cn.xmzt.www.intercom.vm.TeamRoomViewModel;
import cn.xmzt.www.main.globals.FloatIntercomManage;
import cn.xmzt.www.main.globals.TalkManage;
import cn.xmzt.www.selfdrivingtools.event.UpdateGroupDefaultEvent;
import cn.xmzt.www.utils.PermissionUtil;
import cn.xmzt.www.utils.SPUtils;
import cn.xmzt.www.utils.ToastUtils;


public class TeamRoomActivity extends TourBaseActivity<TeamRoomViewModel, ActivityTeamRoomBinding> {

    public static void start(Context context, String tid,Boolean show) {
        Intent intent = new Intent();
        intent.putExtra("A", tid);
        intent.putExtra("B", show);
        intent.setClass(context, TeamRoomActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
    public static void start(Context context, String tid,boolean show,boolean isSmartTeam) {
        Intent intent = new Intent();
        intent.putExtra("A", tid);
        intent.putExtra("B", show);
        intent.putExtra("C", isSmartTeam);
        intent.setClass(context, TeamRoomActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
    @Override
    protected int setLayoutId() {
        return R.layout.activity_team_room;
    }

    @Override
    protected TeamRoomViewModel createViewModel() {
        viewModel = new TeamRoomViewModel(this, dataBinding);
        return viewModel;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
            ArrayList<IMMessage> messages= (ArrayList<IMMessage>) intent.getSerializableExtra(NimIntent.EXTRA_NOTIFY_CONTENT); // 可以获取消息的发送者，跳转到指定的单聊、群聊界面。
            if(messages!=null&&messages.size()>0) {
                IMMessage message = messages.get(messages.size() - 1);
                String groupId =  message.getSessionId();
                if(!viewModel.groupId.equals(groupId)){
                    viewModel.groupId=groupId;
                    loadDataFromService();
                    // 入群后,加入或切换对讲组
                    TalkManage mTalkManage=TalkManage.getInstance();
                    mTalkManage.joinOrSwitchIntercomGroup(groupId);
                }
            }
        }

    }

    @Override
    protected void initData() {
        parseIntent();
        EventBus.getDefault().register(this);
        loadDataFromService();
        getDataToView();
        dataBinding.setModel(viewModel);
//        requestAndPermission();
        if (!SPUtils.getBoolean("group", false)) {
            GuideDialog dialog = new GuideDialog(this);
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_guide_group, null);
            ImageView know = view.findViewById(R.id.iv_know);
            know.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.setContentView(view);
            dialog.show();
            SPUtils.putBoolean("group", true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.registerTeamUpdateObserver(true);
        setChattingAccountMsgService(viewModel.groupId, SessionTypeEnum.Team);
        viewModel.messageListPanel.onResume();
        // 在通信音频模式。建立音频/视频聊天或VoIP呼叫。
//        setVolumeControlStream(AudioManager.MODE_IN_COMMUNICATION);
//        setVolumeControlStream(AudioManager.MODE_CURRENT);
    }

    @Override
    protected void onStart() {
        super.onStart();
        TalkManage.isShowSelectGroup=false;//不能切换群聊天
        // 显示小语音录制按钮
        dataBinding.ivBottomVoice.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        setChattingAccountMsgService(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);
        viewModel.messageListPanel.onPause();
        viewModel.inputPanel.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        viewModel.registerTeamUpdateObserver(false);
        if (viewModel.messageListPanel != null) {
            viewModel.messageListPanel.onDestroy();
        }
        if (viewModel.inputPanel != null) {
            viewModel.inputPanel.onDestroy();
        }
        if (viewModel.aitManager != null) {
            viewModel.aitManager.reset();
        }
        if (viewModel.mHintDialog != null) {
            if (viewModel.mHintDialog.isShowing()) {
                viewModel.mHintDialog.dismiss();
            }
            viewModel.mHintDialog = null;
        }
        viewModel.onBackPressed();
        viewModel.cancelTimer();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewModel.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (viewModel.onBackPressed()) {
            return;
        }
        TalkManage.isShowSelectGroup=true;//能切换群聊天
//        TalkManage.isShowTalk = false;
        FloatIntercomManage.getInstance().setNeedMoveBottom(false);
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//如果返回键按下
            //此处写退向后台的处理
            if (viewModel.onBackPressed()) {
                return true;
            } else {
                viewModel.back();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void locationSendEventBus(LocationSendEvent event) {
        if (viewModel.isShareLocation) {
            viewModel.sendMessageTeamLocation(1);
            viewModel.postDelayed(5);
        } else {
            viewModel.sendMessageTeamLocation(2);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void LocationShareStatusEventBus(LocationShareStatusEvent event) { // 在共享导航界面去做发送我的位置的信息
//        if (event.getGroupId().equals(viewModel.groupId)) {
//            viewModel.isShareLocation = event.isStatus();
//            EventBus.getDefault().post(new LocationSendEvent(viewModel.groupId));
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void AnyRtcStatusEventBus2(AnyRtcStatusEvent event) {
        if (event != null) {
            int code = event.getCode();
            switch (code) {
                case 1011: {
                    // 点击开启广播
                    viewModel.isBroadcast = true;
                    setPromptBroadcast(true);
                    viewModel.toggleActionsLayout();
                    if(viewModel.broadcastAction!=null){
                        viewModel.broadcastAction.setBeingBroadcast(true);
                    }
                    break;
                }
                case 1012: {
                    // 点击结束广播
                    viewModel.isBroadcast = false;
                    setPromptBroadcast(true);
                    if(viewModel.broadcastAction!=null){
                        viewModel.broadcastAction.setBeingBroadcast(false);
                    }
                    break;
                }
                case 1013:
                    // 点击打开小视频
                    viewModel.toggleActionsLayout();
                    break;
                case 1014:
                    // 点击打开相机
                    viewModel.toggleActionsLayout();
                    break;
                case 1031: {
                    // 收到有人广播通知
                    if(viewModel.broadcastAction!=null){
                        viewModel.broadcastAction.setBeingBroadcast(true);
                    }
                    viewModel.isBroadcast = true;
                    setPromptBroadcast(false);
                    break;
                }
                case 1032: {
                    // 收到无人广播通知
                    if(viewModel.broadcastAction!=null){
                        viewModel.broadcastAction.setBeingBroadcast(false);
                    }
                    viewModel.isBroadcast = false;
                    setPromptBroadcast(false);
                    break;
                }
                case 1041: {
                    if (event.getMessage() == null) {
                        return;
                    }
                    if (event.getMessage() != null) {
                        // 发送语音消息回调,更新UI
                        viewModel.messageListPanel.onMsgSend(event.getMessage());
                    }
                    break;
                }
                case 1051:
                    // 公共对讲按钮按下
                    dataBinding.etBottomInputMessage.setEnabled(false);
                    break;
                case 1052:
                    // 公共对讲按钮抬起
                    dataBinding.etBottomInputMessage.setEnabled(true);
                   /* dataBinding.etBottomInputMessage.setFocusable(true);
                    dataBinding.etBottomInputMessage.setFocusableInTouchMode(true);*/
                    break;
            }
        }
    }

    public TeamRoomViewModel getViewHodel() {
        return viewModel;
    }

    public ActivityTeamRoomBinding getDataBinding() {
        return dataBinding;
    }

    private boolean isShowLocation=true;
    private void parseIntent() {
        Intent intent=getIntent();
        if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
            ArrayList<IMMessage> messages= (ArrayList<IMMessage>) intent.getSerializableExtra(NimIntent.EXTRA_NOTIFY_CONTENT); // 可以获取消息的发送者，跳转到指定的单聊、群聊界面。
            if(messages!=null&&messages.size()>0) {
                IMMessage message = messages.get(messages.size() - 1);
                viewModel.groupId =  message.getSessionId();

                EventBus.getDefault().post(new UpdateGroupDefaultEvent(viewModel.groupId)); // 设置默认开启或者不开启位置信息
            }
        }else {
            viewModel.groupId = intent.getStringExtra("A");
            viewModel.isSmartTeam = intent.getBooleanExtra("C",false);
        }
        isShowLocation = intent.getBooleanExtra("B",true);
        viewModel.initViewPanel();
        dataBinding.btnLocation.setVisibility(isShowLocation ? View.VISIBLE : View.GONE);
    }

    private void loadDataFromService() {
        viewModel.loadDataNim();
        viewModel.getGroupRoomInfo(this);
    }

    private void getDataToView() {
        viewModel.mutableLiveData.observe(this, new Observer<GroupRoomBean>() {
            @Override
            public void onChanged(@Nullable GroupRoomBean bean) {
                if (bean != null) {
                    viewModel.groupRoomBean = bean;
                    viewModel.tripId = bean.getTripId();
                    if(bean.getGroupType()==1){
                        viewModel.isSmartTeam=true;
                    }else {
                        viewModel.isSmartTeam=false;
                    }
                    updateTeamInfo();
                }
            }
        });
        viewModel.mutableLiveDataMember.observe(this, new Observer<GroupMemberBean>() {
            @Override
            public void onChanged(@Nullable GroupMemberBean bean) {
                if (bean != null) {
                    viewModel.groupMemberBean = bean;
                    viewModel.setGroupMemberToView();
                }
            }
        });
    }

    /**
     * 更新群信息
     */
    public void updateTeamInfo() {
        if (viewModel.groupRoomBean != null) {
            dataBinding.tvTitleName.setText(viewModel.groupRoomBean.getGroupTitle());
            if (viewModel.team != null) {
                dataBinding.tvTitleNumber.setText("(" + viewModel.team.getMemberCount() + ")");
                dataBinding.tvTitleNumber.setVisibility(View.VISIBLE);
            }else {
                dataBinding.tvTitleNumber.setVisibility(View.GONE);
            }
            return;
        }
        if (viewModel.team != null) {
            // 群昵称(26/28)
            dataBinding.tvTitleName.setText(viewModel.team.getName());
            dataBinding.tvTitleNumber.setText("(" + viewModel.team.getMemberCount() + ")");
            dataBinding.tvTitleNumber.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置当前聊天活跃帐户
     *
     * @param account
     * @param sessionType
     */
    private void setChattingAccountMsgService(String account, SessionTypeEnum sessionType) {
        NIMClient.getService(MsgService.class).setChattingAccount(account, sessionType);
    }

    /**
     * 设置顶部提示广播状态
     * @param isSponsor 是否是发起人
     */
    private void setPromptBroadcast(boolean isSponsor) {
        if (viewModel.isBroadcast) {
            dataBinding.rlPromptBroadcast.setVisibility(View.VISIBLE);
            if(isSponsor){
                dataBinding.ivCancelBroadcast.setVisibility(View.VISIBLE);
            }else {
                dataBinding.ivCancelBroadcast.setVisibility(View.GONE);
            }
        } else {
            dataBinding.rlPromptBroadcast.setVisibility(View.GONE);
        }
    }

    private void requestAndPermission() {
//        Permission.READ_PHONE_STATE,
        AndPermission.with(TeamRoomActivity.this).runtime().permission(
                Permission.CAMERA,
                Permission.RECORD_AUDIO,
                Permission.READ_EXTERNAL_STORAGE,
                Permission.WRITE_EXTERNAL_STORAGE,
                Permission.ACCESS_COARSE_LOCATION,
                Permission.ACCESS_FINE_LOCATION
        ).onGranted(new Action<List<String>>() {
            @Override
            public void onAction(List<String> data) {
                // 申请成功
            }
        }).onDenied(new Action<List<String>>() {
            @Override
            public void onAction(List<String> data) {
                ToastUtils.showText(getApplicationContext(),"请打开录音权限");
                PermissionUtil.gotoPermission(getApplicationContext());
                onBackPressed();
            }
        }).start();
    }
}
