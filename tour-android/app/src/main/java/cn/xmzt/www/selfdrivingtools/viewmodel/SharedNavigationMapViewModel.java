package cn.xmzt.www.selfdrivingtools.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.intercom.bean.GroupRoomBean;
import cn.xmzt.www.intercom.bean.TeamLocationBean;
import cn.xmzt.www.intercom.common.util.sys.TimeUtil;
import cn.xmzt.www.intercom.profile.TeamLocationProfile;
import cn.xmzt.www.roomdb.TourDatabase;
import cn.xmzt.www.roomdb.beans.GroupUserInfo;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.selfdrivingtools.bean.GetDestinationBean;
import cn.xmzt.www.selfdrivingtools.bean.GuideMapMyInfo;
import cn.xmzt.www.selfdrivingtools.bean.SharedNavigationMapRightInfo;

import java.util.ArrayList;
import java.util.List;

import cn.xmzt.www.selfdrivingtools.bean.TourTripDetailNewBean;
import cn.xmzt.www.smartteam.bean.TripSignInDetailBean;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class SharedNavigationMapViewModel extends BaseViewModel {

    public MutableLiveData<Object> mSettingDestination;
    public MutableLiveData<GetDestinationBean> mGetSettingDestination;
    public MutableLiveData<TourTripDetailNewBean> mTripDetailInfo;
    public MutableLiveData<Object> mIsShareLocationOrIsAutoplay;
    public MutableLiveData<GuideMapMyInfo> mGroupMemberBean;
    public MutableLiveData<GroupRoomBean> mGroupInfoBean;

    public MutableLiveData<TripSignInDetailBean> mSignInCheckData;
    public MutableLiveData<Object> mSignIn;

    public SharedNavigationMapViewModel() {
        mSettingDestination = new MutableLiveData<Object>();
        mIsShareLocationOrIsAutoplay = new MutableLiveData<Object>();
        mGetSettingDestination = new MutableLiveData<GetDestinationBean>();
        mTripDetailInfo = new MutableLiveData<TourTripDetailNewBean>();
        mGroupMemberBean = new MutableLiveData<GuideMapMyInfo>();
        mGroupInfoBean = new MutableLiveData<GroupRoomBean>();

        mSignInCheckData = new MutableLiveData<TripSignInDetailBean>();
        mSignIn = new MutableLiveData<Object>();
    }

    public void setGroupSettingDestination(String token, String destination, String destinationCoordinate, String gatherTime,Long desId, String groupId){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.setGroupSettingDestination(token,destination,destinationCoordinate,gatherTime,desId,groupId);
        mObservable.subscribeOn(Schedulers.io())
                .compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<Object> body) {
                        if (body.isSuccess()) {
                            mSettingDestination.setValue(body.getReCode());
                        } else {
                            ToastUtils.showShort(body.getReMsg());
                        }
                    }
                });
    }

    public void getGroupSettingDestination(String token,String Groupid){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getGroupSettingDestination(token,Groupid);
        mObservable.subscribeOn(Schedulers.io())
                .compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<GetDestinationBean>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<GetDestinationBean> body) {
                        if (body.isSuccess()) {
                            mGetSettingDestination.setValue(body.getRel());
                        } else {
                            ToastUtils.showShort(body.getReMsg());
                            mGetSettingDestination.setValue(null);
                        }
                    }
                });
    }


    public void getIsShareLocationOrIsAutoplay(String token,String groupId, boolean isAutoPlay, boolean isShareLoc){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getIsShareLocationOrISAutoplay(token,groupId,isAutoPlay,isShareLoc);
        mObservable.subscribeOn(Schedulers.io())
                .compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<Object> body) {
                        if (body.isSuccess()) {
                            mIsShareLocationOrIsAutoplay.setValue(body.getReCode());
                        } else {
                            mIsShareLocationOrIsAutoplay.setValue(body.getReMsg());
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mIsShareLocationOrIsAutoplay.setValue(null);
                    }
                });
    }

    public void getMyUser(String token,String groupId, String memberId){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getMyUser(token,groupId,memberId);
        mObservable.subscribeOn(Schedulers.io())
                .compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<GuideMapMyInfo>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<GuideMapMyInfo> body) {
                        if (body.isSuccess()) {
                            mGroupMemberBean.setValue(body.getRel());
                        } else {
                            mGroupMemberBean.setValue(null);
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mGroupMemberBean.setValue(null);
                    }
                });
    }

    public void getTripDetail(String token,int id, String refCode, String share,String groupId){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getTripDetail(token,id,refCode,groupId,share,true);
        mObservable.subscribeOn(Schedulers.io())
                .compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<TourTripDetailNewBean>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<TourTripDetailNewBean> body) {
                        if (body.isSuccess()) {
                            mTripDetailInfo.setValue(body.getRel());
                        } else {
                            ToastUtils.showShort(body.getReMsg());
                        }
                    }
                });
    }

    public void getGroupRoomInfo(String token,String groupId){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getGroupRoomInfo(token,groupId);
        mObservable.subscribeOn(Schedulers.io())
                .compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<GroupRoomBean>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<GroupRoomBean> body) {
                        if (body.isSuccess()) {
                            mGroupInfoBean.setValue(body.getRel());
                        }
                    }
                });
    }

    private long TIME_OUT = 60 * 1000 * 5L;

    /**
     * request right view car share info
     * @return
     */
    public List<SharedNavigationMapRightInfo> getRightCarInfo(List<SharedNavigationMapRightInfo> info){
        List<SharedNavigationMapRightInfo> infos = new ArrayList<>();
        if (info == null){
            return infos;
        }
        for (int i = 0; i < info.size(); i++ ){
            if (!"".equals(info.get(i).getBean().numberPlate) && null != info.get(i).getBean().numberPlate) {
                SharedNavigationMapRightInfo bean = info.get(i);
                float distance = AMapUtils.calculateLineDistance(new LatLng(bean.getBean().latitude,bean.getBean().longitude), new LatLng(Constants.mLatitude, Constants.mLongitude));
                bean.setDistance(distance);
                bean.setType(0);
                infos.add(bean);
            }
        }
        if (infos.size() < 2){
            return infos;
        }
        // 获取到数据之后将数据按照距离由近及远的排列出来
        List<SharedNavigationMapRightInfo> bubbleList = new ArrayList<>();
        List<SharedNavigationMapRightInfo> bubbleLists = new ArrayList<>(infos);
        bubbleList = bubbleSort(bubbleLists);
        // 将排列的数据在进行排列显示
        infos.clear();// 将数据进行重新排列
        // 1 群主 2 领队 3 超过距离的人 4 其他人  5 离线人员 之前都是在线人员
        // 群主 和 领队 是一类
        int len = bubbleList.size();
        // 当前时间戳
        long time = System.currentTimeMillis();

        for (int i = 0;i < len ; i++){
            SharedNavigationMapRightInfo info1 = bubbleList.get(i);
            if (info1.getBean().type < 2 && (time-info1.getBean().time) < TIME_OUT){
                infos.add(info1);
            }
        }
        if (infos.size() == len){
            return infos;
        }
        // 超过距离的人
        for (int i = 0;i < len ; i++){
            SharedNavigationMapRightInfo info1 = bubbleList.get(i);
            if (info1.getDistance() > 10000 && info1.getBean().type == 2 && (time-info1.getBean().time) < TIME_OUT){
                infos.add(info1);
            }
        }
        if (infos.size() == len){
            return infos;
        }
        // 其他人
        for (int i = 0;i < len ; i++){
            SharedNavigationMapRightInfo info1 = bubbleList.get(i);
            if (info1.getDistance() < 10000 && info1.getBean().type == 2 && (time-info1.getBean().time) < TIME_OUT){
                infos.add(info1);
            }
        }
        if (infos.size() == len){
            return infos;
        }
        // 离线人员
        for (int i = 0;i < len ; i++){
            SharedNavigationMapRightInfo info1 = bubbleList.get(i);
            if ((time-info1.getBean().time) > TIME_OUT){
                infos.add(info1);
            }
        }
        return infos;
    }

    public List<SharedNavigationMapRightInfo> bubbleSort(List<SharedNavigationMapRightInfo> arrays) {
        if (arrays.size() == 0)
            return arrays;
        if (arrays.size() == 1)
            return arrays;
        int length = arrays.size() - 1;
        for (int i = 0; i < length; i++) {
            for (int j = 1; j <= length - i; j++) {
                if (arrays.get(j - 1).getDistance() > arrays.get(j).getDistance()) {
                    swap(arrays, j - 1, j);
                }
            }
        }
        return arrays;
    }

    private void swap(List<SharedNavigationMapRightInfo> arrays, int arg1, int arg2) {
        SharedNavigationMapRightInfo temp = arrays.get(arg1);
        arrays.set(arg1, arrays.get(arg2));
        arrays.set(arg2, temp);
    }

    /**
     * request right view member info
     * @return
     */
    public List<SharedNavigationMapRightInfo> getRightPersonInfo(List<SharedNavigationMapRightInfo> info){
        List<SharedNavigationMapRightInfo> infos = new ArrayList<>();
        if (info == null){
            return infos;
        }
        for (int i = 0; i < info.size(); i++ ){
            SharedNavigationMapRightInfo bean = info.get(i);
            float distance = AMapUtils.calculateLineDistance(new LatLng(bean.getBean().latitude,bean.getBean().longitude), new LatLng(Constants.mLatitude, Constants.mLongitude));
            bean.setDistance(distance);
            bean.setType(1);
            infos.add(bean);
        }
        if (infos.size() < 2){
            return infos;
        }
        // 获取到数据之后将数据按照距离由近及远的排列出来
        List<SharedNavigationMapRightInfo> bubbleList = new ArrayList<>();
        List<SharedNavigationMapRightInfo> bubbleLists = new ArrayList<>(infos);
        bubbleList = bubbleSort(bubbleLists);
        // 将排列的数据在进行排列显示
        infos.clear();// 将数据进行重新排列
        // 1 群主 2 领队 3 超过距离的人 4 其他人 5 离线人员 之前都是在线人员
        // 群主 和 领队 是一类
        int len = bubbleList.size();
        // 当前时间戳
        long time = System.currentTimeMillis();
        for (int i = 0;i < len ; i++){
            SharedNavigationMapRightInfo info1 = bubbleList.get(i);
            if (info1.getBean().type < 2 && (time-info1.getBean().time) < TIME_OUT){
                infos.add(info1);
            }
        }
        if (infos.size() == len){
            return infos;
        }
        // 超过距离的人
        for (int i = 0;i < len ; i++){
            SharedNavigationMapRightInfo info1 = bubbleList.get(i);
            if (info1.getDistance() > 10000 && info1.getBean().type == 2 && (time-info1.getBean().time) < TIME_OUT){
                infos.add(info1);
            }
        }
        if (infos.size() == len){
            return infos;
        }
        // 其他人
        for (int i = 0;i < len ; i++){
            SharedNavigationMapRightInfo info1 = bubbleList.get(i);
            if (info1.getDistance() < 10000 && info1.getBean().type == 2 && (time-info1.getBean().time) < TIME_OUT){
                infos.add(info1);
            }
        }
        if (infos.size() == len){
            return infos;
        }
        // 离线人员
        for (int i = 0;i < len ; i++){
            SharedNavigationMapRightInfo info1 = bubbleList.get(i);
            if ((time-info1.getBean().time) > TIME_OUT){
                infos.add(info1);
            }
        }
        return infos;
    }

    /**
     * 发送消息群位置
     * @param type 1: 开启位置更新   2: 关闭位置更新
     */
    public void sendMessageTeamLocation(Context context, int type, String groupId, String userId, String nickname, String image, String carinfo, int role, boolean leader) {
        TeamLocationBean teamLocationBean = new TeamLocationBean();
        teamLocationBean.setGroup_id(groupId);
        teamLocationBean.setMsg_type(type + "");
        teamLocationBean.setSend_msg_timestamp(TimeUtil.currentTimeMillis() + "");
        teamLocationBean.setSend_msg_latitude(Constants.mLatitude + "");
        teamLocationBean.setSend_msg_longitude(Constants.mLongitude + "");
        teamLocationBean.setSend_msg_id(userId + "");
        teamLocationBean.setSend_msg_nick(nickname);
        teamLocationBean.setSend_msg_type(role == 1 ? "0" : leader ? "1" : "2");
        teamLocationBean.setSend_msg_avatar(image);
        teamLocationBean.setSend_msg_numberPlate(carinfo);
        TeamLocationProfile.getInstance().sendMessageNotificationTeamLocation(teamLocationBean);// 发送消息
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
        TourDatabase.getDefault(context).getGroupUserInfoDao().insert(userInfo);
    }

    /**
     * 发送消息群位置
     * 3 更新集结点信息
     */
    public void sendMessageTeamLocation3(String groupId) {
        TeamLocationBean teamLocationBean = new TeamLocationBean();
        teamLocationBean.setGroup_id(groupId);
        teamLocationBean.setMsg_type("3");
        teamLocationBean.setSend_msg_timestamp("");
        teamLocationBean.setSend_msg_latitude("");
        teamLocationBean.setSend_msg_longitude("");
        teamLocationBean.setSend_msg_id("");
        teamLocationBean.setSend_msg_nick("");
        teamLocationBean.setSend_msg_type("");
        teamLocationBean.setSend_msg_avatar("");
        teamLocationBean.setSend_msg_numberPlate("");
        TeamLocationProfile.getInstance().sendMessageNotificationTeamLocation(teamLocationBean);// 发送消息
    }


    /**
     * 群组-目的地-集合时间 检测是否签到
     */
    public void getSignInChecked(String token,String desId){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getSignInCheck(token,desId);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<TripSignInDetailBean>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<TripSignInDetailBean> body) {
                        if(body.isSuccess()){
                            mSignInCheckData.setValue(body.getRel());
                        } else {
                            cn.xmzt.www.utils.ToastUtils.showText(ActivityUtils.getTopActivity(),body.getReMsg());
                        }
                    }
                });
    }

    /**
     * 群组-目的地-集合时间 用户签到
     */
    public void setSignIn(String token,String desId){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.setSignIn(token,desId);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<Object> body) {
                        if(body.isSuccess()){
                            mSignIn.setValue(body.getRel());
                        }else {
                            cn.xmzt.www.utils.ToastUtils.showText(ActivityUtils.getTopActivity(),body.getReMsg());
                        }
                    }
                });
    }
}
