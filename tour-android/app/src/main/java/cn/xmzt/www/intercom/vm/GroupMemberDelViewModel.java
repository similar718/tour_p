package cn.xmzt.www.intercom.vm;


import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.ToastUtils;
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
 * 群成员移除ViewModel
 */
public class GroupMemberDelViewModel extends BaseViewModel {
    public MutableLiveData<List<GroupMemberBean>> memberList;
    public MutableLiveData<BaseDataBean<Object>> result;

    public GroupMemberDelViewModel() {
        memberList = new MutableLiveData<List<GroupMemberBean>>();
        result = new MutableLiveData<BaseDataBean<Object>>();
    }
    public String groupId=null;

    /**
     *移除群成员列表
     */
    public void getMemberList() {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getMemberList(groupId, TourApplication.getToken(),null,false,null,null,false,0,null);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<List<GroupMemberBean>>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<List<GroupMemberBean>> body) {
                        if(body.isSuccess()){
                            List<GroupMemberBean> list=body.getRel();
                            if(list==null){
                                list=new ArrayList<>();
                            }
                            memberList.setValue(list);
                        }else {
                            mView.showLoadFail(body.getReMsg());
                        }
                    }
                });

    }
    /**
     *设为/取消领队
     * @param groupId 群id
     * @param leader 是否领队
     * @param userId 成员id
     */
    public void updtDriverLeaderInfo(String groupId,boolean leader,int userId) {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.updtDriverLeaderInfo(groupId, leader,userId);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<Object> body) {
                        if(body.isSuccess()){
                            if(leader){
                                ToastUtils.showShort("将这位成员设为领队成功");
                            }else {
                                ToastUtils.showShort("取消这位成员领队身份成功");
                            }
                            result.setValue(body);
                        }else {
                            if(leader){
                                mView.showLoadFail("将这位成员设为领队失败");
                            }else {
                                mView.showLoadFail("取消这位成员领队身份失败");
                            }
                        }
                    }
                });

    }
    /**
     *设为/取消领队
     * @param groupId 群id
     * @param userIds 成员id 被移除的用户id 多个用英文逗号,隔开
     */
    public void removeMembers(String groupId,String userIds) {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.removeMembers(groupId,userIds);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<Object> body) {
                        if(body.isSuccess()){
                            ToastUtils.showShort("移除成员成功");
                            result.setValue(body);
                        }else {
                            mView.showLoadFail("移除成员失败");
                        }
                    }
                });

    }
}
