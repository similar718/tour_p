package cn.xmzt.www.main.vm;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.ToastUtils;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.intercom.activity.TeamRoomActivity;
import cn.xmzt.www.intercom.bean.GroupRoomBean;
import cn.xmzt.www.intercom.event.RefreshSchedulingListBus;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observable;

/**
 * Scheme  协议的ViewModel
 */
public class SchemeViewModel extends BaseViewModel {
    public MutableLiveData<GroupRoomBean> result;
    public SchemeViewModel() {
        result = new MutableLiveData<GroupRoomBean>();
    }
    /**
     * 扫描群主二维码入群
     * @param refCode  群主推荐码
     * @param groupId 群id
     * @return
     */
    public void scanQRInvited(String refCode,String groupId) {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.scanQRInvited(TourApplication.getToken(),refCode,groupId);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<String>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<String> body) {
                        if(body.isSuccess()){
                            getGroupRoomInfo(groupId,false);
                            EventBus.getDefault().post(new RefreshSchedulingListBus(1));
                        }else {
                            mView.showLoadFail(body.getReMsg());
                        }
                    }
                });

    }
    /**
     * 获取入群信息
     */
    public void getGroupRoomInfo(String groupId,boolean isShowLoading) {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getGroupRoomInfo(TourApplication.getToken(), groupId);
        mObservable.compose(ComposeUtil.compose(mView,isShowLoading))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<GroupRoomBean>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<GroupRoomBean> body) {
                        if(body.isSuccess()){
                            result.setValue(body.getRel());
                        }else {
                            mView.showLoadFail(body.getReMsg());
                        }
                    }
                });
    }
}
