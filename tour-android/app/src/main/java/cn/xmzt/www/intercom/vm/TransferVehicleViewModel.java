package cn.xmzt.www.intercom.vm;


import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.ActivityUtils;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.intercom.bean.GroupMemberBean;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;

import java.util.ArrayList;
import java.util.List;

import cn.xmzt.www.utils.ToastUtils;
import io.reactivex.Observable;

/**
 * 群转让车辆ViewModel
 */
public class TransferVehicleViewModel extends BaseViewModel {
    public MutableLiveData<GroupMemberBean> selfGroupMemberBean;//自己在群组中的信息
    public MutableLiveData<List<GroupMemberBean>> driverList;
    public MutableLiveData<BaseDataBean<Object>> result;
    public TransferVehicleViewModel() {
        selfGroupMemberBean = new MutableLiveData<GroupMemberBean>();
        driverList = new MutableLiveData<List<GroupMemberBean>>();
        result = new MutableLiveData<BaseDataBean<Object>>();
    }
    public String groupId=null;
    /**
     *用户在指定群的个人信息
     */
    public void getGroupUser() {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getGroupUser(TourApplication.getToken(),groupId,null);
        mObservable.compose(ComposeUtil.compose())
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<GroupMemberBean>>() {
                    @Override
                    public void onNext(BaseDataBean<GroupMemberBean> body) {
                        if(body.isSuccess()){
                            selfGroupMemberBean.setValue(body.getRel());
                        }
                    }
                });

    }
    /**
     *选择司机列表
     */
    public void getChooseDriverList() {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getMemberList(groupId,TourApplication.getToken(),false,null,null,null,true,null,null);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<List<GroupMemberBean>>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<List<GroupMemberBean>> body) {
                        if(body.isSuccess()){
                            List<GroupMemberBean> list=body.getRel();
                            if(list==null){
                                list=new ArrayList<>();
                            }
                            driverList.setValue(list);
                        }else {
                            mView.showLoadFail(null);
                        }
                    }
                });

    }
    /**
     * 设置/更换司机
     * @param driverUserId  当前车辆用户id
     * @param groupId 群组id
     * @param licencePlate 车牌号
     * @param userId 更换车辆的用户id
     */
    public void updtDriverUser(int driverUserId,String groupId,String licencePlate,int userId) {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.updtDriverUser(TourApplication.getToken(),""+driverUserId,groupId,licencePlate,""+userId);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<Object> body) {
                        if(body.isSuccess()){
                            result.setValue(body);
                        }else {
                            ToastUtils.showText(ActivityUtils.getTopActivity(),body.getReMsg());
                        }
                    }
                });
    }
}
