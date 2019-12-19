package cn.xmzt.www.main.vm;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.ActivityUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.jpush.android.api.JPushInterface;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.helper.UserHelper;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.intercom.bean.MyTalkGroupsBean;
import cn.xmzt.www.intercom.bean.MyTalkGroupBean;
import cn.xmzt.www.intercom.bean.TalkUserInfoBean;
import cn.xmzt.www.intercom.bean.UserBasicInfoBean;
import cn.xmzt.www.intercom.cache.TalkGroupListCache;
import cn.xmzt.www.main.globals.MsgExtensionType;
import cn.xmzt.www.main.globals.NimLoginManage;
import cn.xmzt.www.main.globals.TalkManage;
import cn.xmzt.www.mine.bean.AppVersionBean;
import cn.xmzt.www.mine.event.UnreadMessageEvent;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.APKVersionCodeUtils;
import cn.xmzt.www.utils.SPUtils;
import cn.xmzt.www.utils.ToastUtils;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Scheme  主页面Main的ViewModel
 */
public class MainViewModel extends BaseViewModel {
    public MutableLiveData<AppVersionBean> versionResult;
    public MainViewModel() {
        versionResult = new MutableLiveData<AppVersionBean>();
    }
    /**
     * 获取用户基本信息
     */
    public void getUserBasicInfo() {
        MsgExtensionType.entry_system_time=System.currentTimeMillis();
        TalkManage.getInstance().initTalkManage();
        ApiRepertory.getInstance().getApiService().getUserBasicInfo(TourApplication.getToken())
                .compose(ComposeUtil.compose()).subscribeWith(new CommonDisposableObserver<BaseDataBean<UserBasicInfoBean>>() {
            @Override
            public void onNext(BaseDataBean<UserBasicInfoBean> userInfoBean) {
                if (null != userInfoBean) {
                    if (BaseDataBean.CODE_TOKEN_INVALID.equals(userInfoBean.getReCode())) {
//                        SPUtils.clear();
                        TourApplication.setToken(null);
                        TourApplication.setUser(null);
                        UserHelper.logout();
                    }else {
                        loginNim();
                        TourApplication.setUser(userInfoBean.getRel());
                    }
                }
            }
        });
        getMsgUnreadCount();
        sendJgRid();
        getAutoPlayGroups();
        getTalkGroupList(true);
    }
    /**
     * 自动/不自动播放群录播的群列表
     */
    public void getAutoPlayGroups() {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getAutoPlayGroups(TourApplication.getToken(),1);
        mObservable.compose(ComposeUtil.compose())
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>() {
                    @Override
                    public void onNext(BaseDataBean<Object> body) {
                        if(body.getRel()!=null){
                            SPUtils.putAutoPlayGroup(body.getRel().toString());
                        }else {
                            SPUtils.putAutoPlayGroup("");
                        }
                    }
                });

    }
    /**
     * 登录云信
     */
    private void loginNim() {
        ApiRepertory.getInstance()
                .getApiService()
                .getTalkUserAccount(TourApplication.getToken())
                .compose(ComposeUtil.compose())
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<TalkUserInfoBean>>() {
                    @Override
                    public void onNext(BaseDataBean<TalkUserInfoBean> body) {
                        if (body.isSuccess()) {
                            TalkUserInfoBean bean = body.getRel();
                            if (bean != null) {
                                NimLoginManage.getInstance().loginNim(bean.getAccid(), bean.getToken());
                            }
                        }
                    }

                });
    }
    /**
     * 向后台发送推送的rid
     */
    private void sendJgRid() {
        if (!TextUtils.isEmpty(TourApplication.getToken())) {
            if (TextUtils.isEmpty(TourApplication.rid)) {
                TourApplication.rid = JPushInterface.getRegistrationID(ActivityUtils.getTopActivity());
            }
            ApiRepertory.getInstance().getApiService().sendJgRid(TourApplication.rid, TourApplication.getToken())
                    .compose(ComposeUtil.compose()).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>() {
                @Override
                public void onNext(BaseDataBean<Object> userInfoBean) {
                    Log.e("lee", "***********");
                }
            });
        }
    }

    /**
     * 获取我的群列表
     */
    public void getTalkGroupList(boolean isInit) {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getUserGroupSetups(TourApplication.getToken(), null);
        mObservable.subscribeOn(Schedulers.io())
                .compose(ComposeUtil.compose())
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<MyTalkGroupsBean>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<MyTalkGroupsBean> body) {
                        if(body.isSuccess()){
                            TalkGroupListCache.getInstance().clearCacheAll();
                            MyTalkGroupsBean myTalkGroupsBean=body.getRel();
                            if(myTalkGroupsBean!=null){
                                List<MyTalkGroupBean> list=myTalkGroupsBean.getGroupUserSetups();
                                TalkGroupListCache.getInstance().saveTalkGroupList(list);
                                if(isInit&&!TextUtils.isEmpty(myTalkGroupsBean.getGroupId())){
                                    TalkManage.getInstance().joinOrSwitchIntercomGroup(myTalkGroupsBean.getGroupId());
                                    TalkManage.isShowSelectGroup=true;
                                }
                            }

                        }
                    }
                });
    }

    /**
     *获取当前服务器App最新版本
     */
    public void getLatestVersion(Context mContext) {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getLatestVersion(1,1);
        mObservable.compose(ComposeUtil.compose())
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<AppVersionBean>>() {
                    @Override
                    public void onNext(BaseDataBean<AppVersionBean> body) {
                        if(body.isSuccess()){
                            AppVersionBean mAppVersionBean=body.getRel();
                            String versionName= APKVersionCodeUtils.getVerName(mContext);
                            if(mAppVersionBean!=null&&!versionName.equals(mAppVersionBean.getVersionReplaceV())){
                                versionResult.setValue(mAppVersionBean);
                            }
                        }
                    }
                });
    }
    /**
     *消息未读数量
     */
    public void getMsgUnreadCount() {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getMsgUnreadCount(TourApplication.getToken());
        mObservable.compose(ComposeUtil.compose())
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<Integer>>() {
                    @Override
                    public void onNext(BaseDataBean<Integer> body) {
                        if(body.isSuccess()){
                            EventBus.getDefault().postSticky(new UnreadMessageEvent(body.getRel()));
                        }
                    }
                });
    }

    /**
     *  智慧导览-设置当前队伍，智慧导览-设置是否显示成员位置开关
     */
    public void setUserGroupOrPosition(String token,String groupId,Integer isPosition) {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.setUserGroupOrPosition(token, groupId,isPosition);
        mObservable.compose(ComposeUtil.compose())
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<String>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<String> body) {

                    }
                });
    }

}
