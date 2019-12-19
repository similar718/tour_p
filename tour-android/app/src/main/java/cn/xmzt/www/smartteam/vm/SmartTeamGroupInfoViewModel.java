package cn.xmzt.www.smartteam.vm;

import android.content.Intent;
import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.dialog.GroupAddDelayDaysDialog;
import cn.xmzt.www.dialog.TextTitleDialog;
import cn.xmzt.www.helper.UserHelper;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.intercom.activity.GroupInfoActivity;
import cn.xmzt.www.intercom.bean.GroupMemberBean;
import cn.xmzt.www.intercom.bean.GroupRoomBean;
import cn.xmzt.www.intercom.event.AudioListenStatusEvent;
import cn.xmzt.www.intercom.event.GroupManageEvent;
import cn.xmzt.www.intercom.event.LocationShareStatusEvent;
import cn.xmzt.www.intercom.event.RefreshMyTalkGroupList;
import cn.xmzt.www.intercom.event.RefreshSchedulingListBus;
import cn.xmzt.www.main.activity.MainActivity;
import cn.xmzt.www.roomdb.TourDatabase;
import cn.xmzt.www.roomdb.beans.GroupUserInfo;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.rxjava2.QzdsException;
import cn.xmzt.www.smartteam.bean.SmartTeamInfoBean;
import cn.xmzt.www.utils.TimeUtil;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * 智能组队的群详情ViewModel
 */
public class SmartTeamGroupInfoViewModel extends BaseViewModel {
    public ObservableBoolean isAutoAnswer = new ObservableBoolean();
    public ObservableBoolean isShareLocation = new ObservableBoolean();
    public ObservableBoolean isDissolutionShow = new ObservableBoolean();
    public MutableLiveData<BaseDataBean<String>> result;

    public MutableLiveData<SmartTeamInfoBean> teamInfoLiveData;//成员列表
    public SmartTeamInfoBean teamInfo;//群信息

    public SmartTeamGroupInfoViewModel() {
        result = new MutableLiveData<BaseDataBean<String>>();
        teamInfoLiveData = new MutableLiveData<SmartTeamInfoBean>();
    }

    public String groupId=null;
    /**
     * 智能组队-队伍详情
     */
    public void getSmartTeamGroupInfo() {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getSmartTeamGroupInfo(TourApplication.getToken(), groupId);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<SmartTeamInfoBean>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<SmartTeamInfoBean> body) {
                        if (body.isSuccess()) {
                            teamInfo=body.getRel();
                            teamInfoLiveData.setValue(teamInfo);
                        } else {
                            mView.showLoadFail(null);
                        }
                    }
                });

    }

    /**
     * 退群(主动退群)
     * @return
     */
    public void removeTeam() {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.removeTeam(TourApplication.getToken(),groupId);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<Object> body) {
                        if(body.isSuccess()){
                            UserHelper.clearTalkBusiness(false);
                            EventBus.getDefault().post(new RefreshSchedulingListBus(1));
                            EventBus.getDefault().post(new RefreshMyTalkGroupList()); // 更新对讲群列表信息
                            Intent intent = new Intent();
                            intent.setClass(ActivityUtils.getTopActivity(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            ActivityUtils.getTopActivity().startActivity(intent);
                            ActivityUtils.getTopActivity().finish();
                        }
                    }
                });
    }

    /**
     * 延长解散日期
     * @param dissolveDate      dissolveDate 延长天数
     */
    public void getExtendGroupDissolveDate(int dissolveDate){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getExtendGroupDissolveDate(TourApplication.getToken(),groupId,dissolveDate);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<Object> body) {
                        if(body.isSuccess()){
                            String date=teamInfo.getDissolveDate();
                            EventBus.getDefault().post(new GroupManageEvent(24));//提示上一个页面刷新
                            if(date!=null){
                                long dateTimes=TimeUtil.strToDate(date,"yyyy年MM月dd日HH点").getTime();
                                long dissolveDateTimes=dateTimes+dissolveDate*24*60*60*1000;
                                teamInfo.setDissolveDate(TimeUtil.dateToStr(dissolveDateTimes,"yyyy年MM月dd日HH点"));
                            }
                            teamInfoLiveData.setValue(teamInfo);
                            ToastUtils.showShort("设置延长解散日期成功");
                        }else {
                            ToastUtils.showShort(body.getReMsg());
                        }
                    }
                });
    }
    /**
     * 是否分享位置-是否自动播放
     * @param groupId
     * @param isAutoplay  是否自动播放
     * @param isShareLoc 是否分享位置
     */
    public void getIsShareLocationOrISAutoplay(String groupId,boolean isAutoplay,boolean isShareLoc){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getIsShareLocationOrISAutoplay(TourApplication.getToken(),groupId,isAutoplay,isShareLoc);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<Object> body) {
                        if(body.isSuccess()){
                            teamInfo.setAutoplay(isAutoplay);
                            isAutoAnswer.set(isAutoplay);
                            teamInfo.setShareLocation(isShareLoc);
                            isShareLocation.set(isShareLoc);
                            EventBus.getDefault().post(new LocationShareStatusEvent(groupId,""+TourApplication.getUser().getUserId(),isShareLoc));
                            EventBus.getDefault().post(new AudioListenStatusEvent(groupId,""+TourApplication.getUser().getUserId(),isAutoplay));
                        }
                    }
                });
    }

    /**
     * 智能组队-添加,修改,删除车辆
     * @param addOrUpdtOrDel 添加传：1,修改传：2，删除传：3
     */
    public void smartTeamAddOrUpdtOrDelDriver(int addOrUpdtOrDel,String groupId,String licencePlate) {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.smartTeamAddOrUpdtOrDelDriver(TourApplication.getToken(),groupId,addOrUpdtOrDel,licencePlate);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<String>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<String> body) {
                        if(body.isSuccess()){
                            if(addOrUpdtOrDel==1){
                                ToastUtils.showShort("新增车辆成功");
                                Constants.mMyCarInfo = licencePlate;
                            }else if(addOrUpdtOrDel==2){
                                ToastUtils.showShort("修改车辆成功");
                                Constants.mMyCarInfo = licencePlate;
                            }else if(addOrUpdtOrDel==3){
                                ToastUtils.showShort("删除车辆成功");
                                Constants.mMyCarInfo = "";
                            }
                            saveDataBase(groupId,Constants.mMyCarInfo);
                            result.setValue(body);
                        }else {
                            ToastUtils.showShort(body.getReMsg());
                        }
                    }
                });
    }

    private void saveDataBase(String groupId,String licencePlate){
        GroupUserInfo info = TourDatabase.getDefault(ActivityUtils.getTopActivity()).getGroupUserInfoDao().getData(groupId, String.valueOf(TourApplication.getUser().getUserId()));
        if (info != null){
            info.numberPlate = licencePlate;
            TourDatabase.getDefault(ActivityUtils.getTopActivity()).getGroupUserInfoDao().update(info);
        }
    }
}
