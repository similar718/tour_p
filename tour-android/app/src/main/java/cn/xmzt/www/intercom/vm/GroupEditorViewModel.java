package cn.xmzt.www.intercom.vm;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.ToastUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.intercom.event.AnyRtcStatusEvent;
import cn.xmzt.www.main.globals.MsgExtensionType;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class GroupEditorViewModel extends BaseViewModel {
    public MutableLiveData<Object> mSetNickname;
    public MutableLiveData<Object> mSetGroupInfo;
    public String menderName;

    public GroupEditorViewModel() {
        mSetNickname = new MutableLiveData<Object>();
        mSetGroupInfo = new MutableLiveData<Object>();
    }

    /**
     * 修改自己在群中的昵称
     * @param token
     * @param groupId
     * @param nickname
     */
    public void getUpdateGroupNickname(String token, String groupId, String nickname){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getUpdateGroupNickname(token,groupId,nickname);
        mObservable.subscribeOn(Schedulers.io())
                .compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<Object> body) {
                        if(body.isSuccess()){
                            mSetNickname.setValue(body.getReCode());
                            Constants.mMyNickName = nickname;
                        }else {
                            ToastUtils.showShort(body.getReMsg());
                        }
                    }
                });
    }

    /**
     * 修改群介绍和群名称
     * @param token
     * @param groupId
     * @param intro
     * @param groupTitle
     */
    public void getUpdateGroupTitleAdIntro(String token,String groupId, String intro, String groupTitle){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getUpdateGroupTitleAdIntro(token,groupId,intro,groupTitle);
        mObservable.subscribeOn(Schedulers.io())
                .compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<Object> body) {
                        if(body.isSuccess()){
                            mSetGroupInfo.setValue(body.getReCode());
                            if(!TextUtils.isEmpty(groupTitle)){
                                //修改群名称
                                Map<TeamFieldEnum, Serializable> fieldsMap = new HashMap<>();
                                fieldsMap.put(TeamFieldEnum.Name, groupTitle);
                                NIMClient.getService(TeamService.class).updateTeamFields(groupId, fieldsMap);
                                sendMessageModifyGroup(groupTitle);
                                Constants.mGroupName = groupTitle;
                            }
                        }else {
                            ToastUtils.showShort(body.getReMsg());
                        }
                    }
                });
    }
    /**
     * 向云信发送群名称的消息
     * @param groupName 群名称
     */
    public void sendMessageModifyGroup(String groupName) {
        IMMessage message = MessageBuilder.createTipMessage(MsgExtensionType.groupId, SessionTypeEnum.Team);
        message.setContent(menderName+"将群名称修改为“"+groupName+"”");
        Map<String, Object> content = new HashMap<>();
        content.put(MsgExtensionType.Extension_Type, MsgExtensionType.Extension_Type_201);
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
        EventBus.getDefault().postSticky(new AnyRtcStatusEvent(1041, message));
    }
}
