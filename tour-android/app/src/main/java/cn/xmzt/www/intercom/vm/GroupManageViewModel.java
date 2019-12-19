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

import io.reactivex.Observable;

/**
 * 群管理ViewModel
 */
public class GroupManageViewModel extends BaseViewModel {
    public MutableLiveData<GroupMemberBean> selfGroupMemberBean;//自己在群组中的消息

    public GroupManageViewModel() {
        selfGroupMemberBean = new MutableLiveData<GroupMemberBean>();
    }
    public String groupId=null;

    /**
     *用户在指定群的个人信息
     */
    public void getGroupUser() {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getGroupUser(TourApplication.getToken(),groupId,null);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<GroupMemberBean>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<GroupMemberBean> body) {
                        if(body.isSuccess()){
                            selfGroupMemberBean.setValue(body.getRel());
                        }
                    }
                });
    }
}
