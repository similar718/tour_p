package cn.xmzt.www.smartteam.vm;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.intercom.bean.TeamLocationBean;
import cn.xmzt.www.intercom.common.util.sys.TimeUtil;
import cn.xmzt.www.intercom.event.LocationSendEvent;
import cn.xmzt.www.intercom.profile.TeamLocationProfile;
import cn.xmzt.www.mine.bean.CommonVehicleBean;
import cn.xmzt.www.roomdb.TourDatabase;
import cn.xmzt.www.roomdb.beans.GroupUserInfo;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.selfdrivingtools.bean.SharedNavigationMapRightInfo;
import cn.xmzt.www.smartteam.bean.SmartTeamInfoBean;
import cn.xmzt.www.smartteam.bean.TripSignInDetailBean;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class SmartTeamMapViewModel extends BaseViewModel {
    public MutableLiveData<Object> mSettingDestination;
    public MutableLiveData<SmartTeamInfoBean> mGroupInfo;
    public MutableLiveData<Integer> mCheckPosition;
    public MutableLiveData<BaseDataBean<String>> result;
    public MutableLiveData<CommonVehicleBean> mCarInfo;

    public MutableLiveData<TripSignInDetailBean> mSignInCheckData;
    public MutableLiveData<Object> mSignIn;

    public SmartTeamMapViewModel() {
        mSettingDestination = new MutableLiveData<Object>();
        mGroupInfo = new MutableLiveData<SmartTeamInfoBean>();
        mCheckPosition = new MutableLiveData<Integer>();
        result = new MutableLiveData<BaseDataBean<String>>();
        mCarInfo = new MutableLiveData<CommonVehicleBean>();

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

    /**
     * 智能组队-队伍详情
     * @param token
     * @param Groupid
     */
    public void getSmartTeamGroupInfo(String token,String Groupid,boolean isShow){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getSmartTeamGroupInfo(token,Groupid);
        mObservable.subscribeOn(Schedulers.io())
                .compose(ComposeUtil.compose(mView,isShow))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<SmartTeamInfoBean>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<SmartTeamInfoBean> body) {
                        if (body.isSuccess()) {
                            mGroupInfo.setValue(body.getRel());
                        } else {
                            ToastUtils.showShort(body.getReMsg());
                            mGroupInfo.setValue(null);
                        }
                    }
                });
    }

    /**
     * 获取默认车辆信息
     */
    public void getSysUserPlateSelectDefault() {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getsysUserPlateNumberSelectDefault(TourApplication.getToken());
        mObservable.compose(ComposeUtil.compose())
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<CommonVehicleBean>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<CommonVehicleBean> body) {
                        if(body.isSuccess()){
                            mCarInfo.setValue(body.getRel());
                        }else {
                            cn.xmzt.www.utils.ToastUtils.showText(ActivityUtils.getTopActivity(),body.getReMsg());
                        }
                    }
                });

    }

    private long TIME_OUT = 60 * 1000 * 5L;

    /**
     * request right view car share info
     * @return
     */
    public List<SharedNavigationMapRightInfo> getRightCarInfo(List<SharedNavigationMapRightInfo> info,SharedNavigationMapRightInfo myInfo,String checkeduserid){
        boolean misnull = TextUtils.isEmpty(checkeduserid);
        myInfo.setType(0);
        if (!misnull) {
            if (myInfo.getBean().userId.equals(checkeduserid)) {
                myInfo.setCheck(true);
            } else {
                myInfo.setCheck(false);
            }
        } else {
            myInfo.setCheck(false);
        }
        List<SharedNavigationMapRightInfo> infos = new ArrayList<>();
        if (info == null){
            infos.add(myInfo);
            findCheckId(infos,checkeduserid);
            return infos;
        }
        if (info.size() == 0){
            infos.add(myInfo);
            findCheckId(infos,checkeduserid);
            return infos;
        }
        for (int i = 0; i < info.size(); i++ ){
            if (!"".equals(info.get(i).getBean().numberPlate) && null != info.get(i).getBean().numberPlate) {
                SharedNavigationMapRightInfo bean = info.get(i);
                if (!misnull){
                    if (bean.getBean().userId.equals(checkeduserid)){
                        bean.setCheck(true);
                    } else {
                        bean.setCheck(false);
                    }
                } else {
                    bean.setCheck(false);
                }
                float distance = AMapUtils.calculateLineDistance(new LatLng(bean.getBean().latitude,bean.getBean().longitude), new LatLng(Constants.mLatitude, Constants.mLongitude));
                bean.setDistance(distance);
                bean.setType(0);
                infos.add(bean);
            }
        }

        if (infos.size() < 2){
            List<SharedNavigationMapRightInfo> infosnum = new ArrayList<>();
            infosnum.add(myInfo);
            if (infos.size() > 0) { // 表示获取的数据是有的情况
                infosnum.add(infos.get(0));
            }
            findCheckId(infosnum,checkeduserid);
            return infosnum;
        }
        // 获取到数据之后将数据按照距离由近及远的排列出来
        List<SharedNavigationMapRightInfo> bubbleList = new ArrayList<>();
        List<SharedNavigationMapRightInfo> bubbleLists = new ArrayList<>(infos);
        bubbleList = bubbleSort(bubbleLists);
        // 将排列的数据在进行排列显示
        infos.clear();// 将数据进行重新排列
        // 显示我的
        infos.add(myInfo);
        // 1 群主 2 领队 3 超过距离的人 4 其他人  5 离线人员 之前都是在线人员
        // 群主 和 领队 是一类
        int len = bubbleList.size();
        int lenall = bubbleList.size() + 1;
        // 当前时间戳
        long time = System.currentTimeMillis();

        for (int i = 0;i < len ; i++){
            SharedNavigationMapRightInfo info1 = bubbleList.get(i);
            if (info1.getBean().type < 2 && (time-info1.getBean().time) < TIME_OUT){
                infos.add(info1);
            }
        }
        if (infos.size() == lenall){
            findCheckId(infos,checkeduserid);
            return infos;
        }
        // 超过距离的人
        for (int i = 0;i < len ; i++){
            SharedNavigationMapRightInfo info1 = bubbleList.get(i);
            if (info1.getDistance() > 10000 && info1.getBean().type == 2 && (time-info1.getBean().time) < TIME_OUT){
                infos.add(info1);
            }
        }
        if (infos.size() == lenall){
            findCheckId(infos,checkeduserid);
            return infos;
        }
        // 其他人
        for (int i = 0;i < len ; i++){
            SharedNavigationMapRightInfo info1 = bubbleList.get(i);
            if (info1.getDistance() < 10000 && info1.getBean().type == 2 && (time-info1.getBean().time) < TIME_OUT){
                infos.add(info1);
            }
        }
        if (infos.size() == lenall){
            findCheckId(infos,checkeduserid);
            return infos;
        }
        // 离线人员
        for (int i = 0;i < len ; i++){
            SharedNavigationMapRightInfo info1 = bubbleList.get(i);
            if ((time-info1.getBean().time) > TIME_OUT){
                infos.add(info1);
            }
        }
        findCheckId(infos,checkeduserid);
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
    public List<SharedNavigationMapRightInfo> getRightPersonInfo(List<SharedNavigationMapRightInfo> info,SharedNavigationMapRightInfo myInfo,String checkeduserid){
        boolean misnull = TextUtils.isEmpty(checkeduserid);
        myInfo.setType(1);
        if (!misnull) {
            if (myInfo.getBean().userId.equals(checkeduserid)) {
                myInfo.setCheck(true);
            } else {
                myInfo.setCheck(false);
            }
        } else {
            myInfo.setCheck(false);
        }
        List<SharedNavigationMapRightInfo> infos = new ArrayList<>();
        if (info == null){
            infos.add(myInfo);
            findCheckId(infos,checkeduserid);
            return infos;
        }
        if (info.size() == 0){
            infos.add(myInfo);
            findCheckId(infos,checkeduserid);
            return infos;
        }
        for (int i = 0; i < info.size(); i++ ){
            SharedNavigationMapRightInfo bean = info.get(i);
            if (!misnull){
                if (bean.getBean().userId.equals(checkeduserid)){
                    bean.setCheck(true);
                } else {
                    bean.setCheck(false);
                }
            } else {
                bean.setCheck(false);
            }
            float distance = AMapUtils.calculateLineDistance(new LatLng(bean.getBean().latitude,bean.getBean().longitude), new LatLng(Constants.mLatitude, Constants.mLongitude));
            bean.setDistance(distance);
            bean.setType(1);
            infos.add(bean);
        }
        if (infos.size() < 2){
            List<SharedNavigationMapRightInfo> infosnum = new ArrayList<>();
            infosnum.add(myInfo);
            if (infos.size() > 0) { // 表示获取的数据是有的情况
                infosnum.add(infos.get(0));
            }
            findCheckId(infosnum,checkeduserid);
            return infosnum;
        }
        // 获取到数据之后将数据按照距离由近及远的排列出来
        List<SharedNavigationMapRightInfo> bubbleList = new ArrayList<>();
        List<SharedNavigationMapRightInfo> bubbleLists = new ArrayList<>(infos);
        bubbleList = bubbleSort(bubbleLists);
        // 将排列的数据在进行排列显示
        infos.clear();// 将数据进行重新排列
        // 显示我的
        infos.add(myInfo);
        // 1 群主 2 领队 3 超过距离的人 4 其他人 5 离线人员 之前都是在线人员
        // 群主 和 领队 是一类
        int len = bubbleList.size();
        int lenall = bubbleList.size()+1;
        // 当前时间戳
        long time = System.currentTimeMillis();
        for (int i = 0;i < len ; i++){
            SharedNavigationMapRightInfo info1 = bubbleList.get(i);
            if (info1.getBean().type < 2 && (time-info1.getBean().time) < TIME_OUT){
                infos.add(info1);
            }
        }
        if (infos.size() == lenall){
            findCheckId(infos,checkeduserid);
            return infos;
        }
        // 超过距离的人
        for (int i = 0;i < len ; i++){
            SharedNavigationMapRightInfo info1 = bubbleList.get(i);
            if (info1.getDistance() > 10000 && info1.getBean().type == 2 && (time-info1.getBean().time) < TIME_OUT){
                infos.add(info1);
            }
        }
        if (infos.size() == lenall){
            findCheckId(infos,checkeduserid);
            return infos;
        }
        // 其他人
        for (int i = 0;i < len ; i++){
            SharedNavigationMapRightInfo info1 = bubbleList.get(i);
            if (info1.getDistance() < 10000 && info1.getBean().type == 2 && (time-info1.getBean().time) < TIME_OUT){
                infos.add(info1);
            }
        }
        if (infos.size() == lenall){
            findCheckId(infos,checkeduserid);
            return infos;
        }
        // 离线人员
        for (int i = 0;i < len ; i++){
            SharedNavigationMapRightInfo info1 = bubbleList.get(i);
            if ((time-info1.getBean().time) > TIME_OUT){
                infos.add(info1);
            }
        }
        findCheckId(infos,checkeduserid);
        return infos;
    }

    private void findCheckId(List<SharedNavigationMapRightInfo> list,String user){
        if (TextUtils.isEmpty(user)) {
            return;
        }
        for (int i = 0;i < list.size();i++){
            SharedNavigationMapRightInfo info = list.get(i);
            if (user.equals(info.getBean().userId)){
                mCheckPosition.setValue(i);
                break;
            }
        }
    }

    /**
     * 发送消息群位置
     * @param type 1: 开启位置更新   2: 关闭位置更新
     */
    public void sendMessageTeamLocation(Context context,int type, String groupId, String userId, String nickname, String image, String carinfo, int role, boolean leader) {
        TeamLocationBean teamLocationBean = new TeamLocationBean();
        teamLocationBean.setGroup_id(groupId);
        teamLocationBean.setMsg_type(type + "");
        teamLocationBean.setSend_msg_timestamp(TimeUtil.currentTimeMillis() + "");
        teamLocationBean.setSend_msg_latitude(Constants.mLatitude + "");
        teamLocationBean.setSend_msg_longitude(Constants.mLongitude + "");
        teamLocationBean.setSend_msg_id(userId + "");
        teamLocationBean.setSend_msg_nick(nickname);
        teamLocationBean.setSend_msg_type(role == 1 ? "1" : leader ? "1" : "2");
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

    // 倒计时器
    public CountDownTimer mTimer;
    //发送延迟消息
    public void postDelayed(long s,String groupId) {
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
