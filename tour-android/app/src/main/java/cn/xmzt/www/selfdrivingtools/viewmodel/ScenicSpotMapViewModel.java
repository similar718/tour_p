package cn.xmzt.www.selfdrivingtools.viewmodel;

import androidx.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.blankj.utilcode.util.ActivityUtils;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.intercom.bean.MyTalkGroupsBean;
import cn.xmzt.www.intercom.bean.TeamLocationBean;
import cn.xmzt.www.intercom.common.util.sys.TimeUtil;
import cn.xmzt.www.intercom.profile.TeamLocationProfile;
import cn.xmzt.www.roomdb.beans.GroupUserInfo;
import cn.xmzt.www.selfdrivingtools.bean.FileInfo;
import cn.xmzt.www.selfdrivingtools.bean.ScenicSpotGuideBean;
import cn.xmzt.www.selfdrivingtools.bean.ScenicSpotMapTypeListBean;
import cn.xmzt.www.selfdrivingtools.bean.ScenicVoicePackageBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.roomdb.TourDatabase;
import cn.xmzt.www.roomdb.beans.ScenicContents;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.FastJsonUtil;
import cn.xmzt.www.utils.LocalDataUtils;
import cn.xmzt.www.utils.MD5Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.xmzt.www.utils.ToastUtils;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class ScenicSpotMapViewModel extends BaseViewModel {
    public MutableLiveData<ScenicSpotGuideBean> mScenicSpotMap;
    public MutableLiveData<ScenicVoicePackageBean> mScenicSpotVoicePackage;
    public MutableLiveData<String> mPlayOperation;
    public MutableLiveData<MyTalkGroupsBean> mUserGroupSetups;
    public MutableLiveData<String> mSetGroupSetups;

    public ScenicSpotMapViewModel() {
        mScenicSpotMap = new MutableLiveData<ScenicSpotGuideBean>();
        mScenicSpotVoicePackage = new MutableLiveData<ScenicVoicePackageBean>();
        mPlayOperation = new MutableLiveData<String>();
        mUserGroupSetups = new MutableLiveData<MyTalkGroupsBean>();
        mSetGroupSetups = new MutableLiveData<String>();
    }

    public void getScenicSpotGuideInfo(long id, double latitude, double longitude, Context context) {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getScenicSpotGuideInfo(id, latitude, longitude);
        mObservable.subscribeOn(Schedulers.io())
                .compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<ScenicSpotGuideBean>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<ScenicSpotGuideBean> body) {
                        if (body.isSuccess()) {
                            mScenicSpotMap.setValue(body.getRel());
                            // 保存数据库
                            setAddScenicContentsToDataBase(context, body.getRel());
                        } else {
                            mScenicSpotMap.setValue(getScenicContentsToDataBase(context, id));
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mScenicSpotMap.setValue(getScenicContentsToDataBase(context, id));
                    }
                });
    }

    public void getScenicVoicePackage(long id, Context context) {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getScenicVoicePackage(id);
        mObservable.subscribeOn(Schedulers.io())
                .compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<ScenicVoicePackageBean>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<ScenicVoicePackageBean> body) {
                        if (body.isSuccess()) {
                            mScenicSpotVoicePackage.setValue(body.getRel());
                        } else {
                            // 获取本地数据库里面的内容
                            mScenicSpotVoicePackage.setValue(getScenicVoicePackageToDataBase(context, id));
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        // 获取本地数据库里面的内容
                        mScenicSpotVoicePackage.setValue(getScenicVoicePackageToDataBase(context, id));
                    }
                });
    }


    /**
     * 播放操作 每次播放的时候去请求接口
     * @param id 景区编号
     */
    public void getPlayOperation(long id) {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getPlayOperation(id);
        mObservable.subscribeOn(Schedulers.io())
                .compose(ComposeUtil.compose())
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<String>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<String> body) {
                        mPlayOperation.setValue(body.getRel());
                    }
                });
    }

    /**
     *  智慧导览-进行中队伍
     */
    public void getUserGroupSetups(String token,String groupId) {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getUserGroupSetups(token, groupId);
        mObservable.subscribeOn(Schedulers.io())
                .compose(ComposeUtil.compose())
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<MyTalkGroupsBean>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<MyTalkGroupsBean> body) {
                        if (body.isSuccess()) {
                            mUserGroupSetups.setValue(body.getRel());
                        } else {
                            ToastUtils.showText(ActivityUtils.getTopActivity(),body.getReMsg());
                        }
                    }
                });
    }

    /**
     *  智慧导览-设置当前队伍，智慧导览-设置是否显示成员位置开关
     */
    public void setUserGroupOrPosition(String token,String groupId,int isPosition) {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.setUserGroupOrPosition(token, groupId,isPosition);
        mObservable.subscribeOn(Schedulers.io())
                .compose(ComposeUtil.compose())
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<String>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<String> body) {
                        if (body.isSuccess()) {
                            mSetGroupSetups.setValue(body.getRel());
                        }
                    }
                });
    }

    /**
     *  经过协商 后台给了哪几个类型的数据就只显示哪几个类型的数据 没有数据的类型就不显示
     * @param bean 返回过来的数据
     * @return
     */
    public List<ScenicSpotMapTypeListBean> getTypeList(ScenicSpotGuideBean bean){
        List<ScenicSpotMapTypeListBean> list = LocalDataUtils.getTypeAllData();
        List<ScenicSpotMapTypeListBean> listData = new ArrayList<>();
        // 景点 当获取的景点列表不为空 并且数量大于0
        if (bean.getScenicSpotList() != null)
            if (bean.getScenicSpotList().size()>0)
                listData.add(list.get(0));
        // 路线 当获取的景点列表不为空 并且数量大于0
        if (bean.getScenicLineList() != null)
            if (bean.getScenicLineList().size()>0)
                listData.add(list.get(1));
        // 遍历得到的数据
        if (bean.getScenicServicePointList() != null)
            if (bean.getScenicServicePointList().size() > 0 )
                for (int i = 0 ; i < bean.getScenicServicePointList().size(); i++){
                    listData.add(list.get(bean.getScenicServicePointList().get(i).getType())); // 获取当前type类型的数据
                }
        return listData;
    }

    /**
     * 将景区信息用json的格式加载到数据库
     * @param context 上下文
     * @param bean 景区信息类
     */
    public void setAddScenicContentsToDataBase(Context context,ScenicSpotGuideBean bean) {
        // 拿到数据将实体类转化为json数据
        String jsonStr = FastJsonUtil.objToJson(bean);
        // 查询当前数据有无在数据库有信息
        ScenicContents info = new ScenicContents();
        info.id = bean.getScenicId();
        info.funTypes = Constants.KeywordsType.FUNTYPES_SCENIC;
        info.content = jsonStr;
        TourDatabase.getDefault(context).getScenicContentDao().insert(info);
    }

    /**
     *  如果遇到后台返回错误 或者网络的问题 需要加载到本地的数据
     * @param context 上下文
     * @param id 需要加载的景区编号
     * @return
     */
    public ScenicSpotGuideBean getScenicContentsToDataBase(Context context,long id){
        // 查询当前在数据库里面是否有缓存信息
        ScenicContents info = TourDatabase.getDefault(context).getScenicContentDao().getScenicContents(id,Constants.KeywordsType.FUNTYPES_SCENIC);
        if (info == null){
            return null;
        } else {
            ScenicSpotGuideBean bean = null;
            try {
                bean = FastJsonUtil.parseObject(info.content,ScenicSpotGuideBean.class);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return bean;
        }
    }

    /**
     *  如果遇到后台返回错误 或者网络的问题 需要加载到本地的数据 音频数据
     * @param context 上下文
     * @param id 需要加载的景区编号
     * @return
     */
    public ScenicVoicePackageBean getScenicVoicePackageToDataBase(Context context,long id){
        // 查询当前在数据库里面是否有缓存信息
        ScenicContents info = TourDatabase.getDefault(context).getScenicContentDao().getScenicContents(id,Constants.KeywordsType.FUNTYPES_SCENIC_VOICE);
        if (info == null){
            return null;
        } else {
            ScenicVoicePackageBean bean = null;
            try {
                bean = FastJsonUtil.parseObject(info.content,ScenicVoicePackageBean.class);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return bean;
        }
    }

    /**
     *  获取到景点的音频信息之后 判断数据库里面是否有信息了
     * @param context 上下文
     * @param bean 实体类
     */
    public boolean getIsScenicVoicePackageToDataBase(Context context,ScenicVoicePackageBean bean){
        // 查询当前数据有无在数据库有信息
        ScenicContents info = TourDatabase.getDefault(context).getScenicContentDao().getScenicContents(bean.getScenicId(), Constants.KeywordsType.FUNTYPES_SCENIC_VOICE);
        if (info == null){ // 没有当前数据
            return false;
        } else { // 有当前数据
            return true;
        }
    }

    /**
     *  获取到景点的音频信息之后 加入到数据库
     * @param context 上下文
     * @param bean 实体类
     */
    public void setAddScenicVoicePackageToDataBase(Context context,ScenicVoicePackageBean bean) {
        // 拿到数据将实体类转化为json数据
        String jsonStr = FastJsonUtil.objToJson(bean);
        // 查询当前数据有无在数据库有信息
        ScenicContents info = new ScenicContents();
        info.id = bean.getScenicId();
        info.funTypes = Constants.KeywordsType.FUNTYPES_SCENIC_VOICE;
        info.content = jsonStr;
        TourDatabase.getDefault(context).getScenicContentDao().insert(info);
    }
    /**
     *  获取到景点的音频信息之后 从数据库中删除
     * @param context 上下文
     * @param bean 实体类
     */
    public void deleteScenicVoicePackageToDataBase(Context context,ScenicVoicePackageBean bean) {
        ScenicContents info = TourDatabase.getDefault(context).getScenicContentDao().getScenicContents(bean.getScenicId(),Constants.KeywordsType.FUNTYPES_SCENIC_VOICE);
        if (info != null){
            TourDatabase.getDefault(context).getScenicContentDao().delete(info);
        }
    }

    public List<FileInfo> getLocalFileAllVoice(String url) {
        // 判断当前是否有zip的文件
        String filename = MD5Utils.getMd5Value(url); // 获取音频文件名称
        String destFileDir = Constants.SCENIC_VOICE_FILE_PATH + filename;
        List<FileInfo> list = new ArrayList<>();
        File file = new File(destFileDir);
        File[] files = file.listFiles();
        if (files == null){
            Log.e("error","空目录");
            return null;
        }
        for(int i =0;i<files.length;i++){
            FileInfo info = new FileInfo();
            info.setFilePath(files[i].getAbsolutePath());
            info.setFileName(files[i].getName());
            list.add(info);
        }
        return list;
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
}
