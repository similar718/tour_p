package cn.xmzt.www.intercom.vm;


import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.intercom.bean.GroupMemberBean;
import cn.xmzt.www.intercom.event.GroupManageEvent;
import cn.xmzt.www.roomdb.TourDatabase;
import cn.xmzt.www.roomdb.beans.GroupUserInfo;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import io.reactivex.Observable;

/**
 * 群车辆ViewModel
 */
public class GroupCarViewModel extends BaseViewModel {
    public MutableLiveData<List<GroupMemberBean>> carList;
    public MutableLiveData<BaseDataBean<String>> result;
    public GroupCarViewModel() {
        carList = new MutableLiveData<List<GroupMemberBean>>();
        result = new MutableLiveData<BaseDataBean<String>>();
    }
    /**
     *群车辆列表
     * @param groupId
     */
    public void getGroupCarList(String groupId) {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getMemberList(groupId,TourApplication.getToken(),true,null,null,null,null,null,null);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<List<GroupMemberBean>>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<List<GroupMemberBean>> body) {
                        if(body.isSuccess()){
                            List<GroupMemberBean> list=body.getRel();
                            if(list==null){
                                list=new ArrayList<>();
                            }
                            carList.setValue(list);
                        }
                    }
                });
    }
    /**
     *群车辆列表
     * @param groupId
     */
    public void getGroupCarListVehicle(String groupId) {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getVehicleInfo(TourApplication.getToken(),groupId);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<List<GroupMemberBean>>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<List<GroupMemberBean>> body) {
                        if(body.isSuccess()){
                            List<GroupMemberBean> list=body.getRel();
                            if(list==null){
                                list=new ArrayList<>();
                            }
                            carList.setValue(list);
                        }
                    }
                });
    }

    /**
     * 移除车辆
     * @param groupId
     * @param driverIds 司机用户id 移除多个用英文逗号隔开
     */
    public void delDriver(String groupId,String driverIds) {
        if(TextUtils.isEmpty(driverIds)){
            return;
        }
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.delDriver(groupId,driverIds);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<String>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<String> body) {
                        if(body.isSuccess()){
                            ToastUtils.showShort("移除车辆成功");
                            EventBus.getDefault().post(new GroupManageEvent(2));//提示上一个页面刷新
                            result.setValue(body);
                        }else {
                            mView.showLoadFail(body.getReMsg());
                        }
                    }
                });

    }

    /**
     * 添加车辆
     */
    public void addDriver(String groupId,String licencePlate,int userId) {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.addDriver(groupId,licencePlate,userId);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<String>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<String> body) {
                        if(body.isSuccess()){
                            ToastUtils.showShort("设置车辆成功");
                            getGroupCarListVehicle(groupId);
                            saveDataBase(groupId, String.valueOf(userId),licencePlate);
                        }else {
                            mView.showLoadFail(body.getReMsg());
                        }
                    }
                });
    }

    /**
     * 将修改的车辆信息添加到数据库
     * @param groupId
     * @param userid
     * @param licencePlate
     */
    private void saveDataBase(String groupId,String userid,String licencePlate){
        GroupUserInfo info = TourDatabase.getDefault(ActivityUtils.getTopActivity()).getGroupUserInfoDao().getData(groupId,userid);
        if (info != null){
            info.numberPlate = licencePlate;
            TourDatabase.getDefault(ActivityUtils.getTopActivity()).getGroupUserInfoDao().update(info);
        }
    }
}
