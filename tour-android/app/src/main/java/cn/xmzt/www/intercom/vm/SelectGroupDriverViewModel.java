package cn.xmzt.www.intercom.vm;


import androidx.lifecycle.MutableLiveData;

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

import io.reactivex.Observable;

/**
 * 群选择司机ViewModel
 */
public class SelectGroupDriverViewModel extends BaseViewModel {
    public MutableLiveData<List<GroupMemberBean>> driverList;
    public SelectGroupDriverViewModel() {
        driverList = new MutableLiveData<List<GroupMemberBean>>();
    }
    public String groupId=null;
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
}
