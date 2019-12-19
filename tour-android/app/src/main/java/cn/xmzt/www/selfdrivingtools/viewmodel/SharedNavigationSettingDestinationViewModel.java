package cn.xmzt.www.selfdrivingtools.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.ToastUtils;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.selfdrivingtools.bean.GetTripDaysInfo;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class SharedNavigationSettingDestinationViewModel extends BaseViewModel {

    public MutableLiveData<GetTripDaysInfo> mTripInfo;


    public SharedNavigationSettingDestinationViewModel() {
        mTripInfo = new MutableLiveData<GetTripDaysInfo>();
    }

    public void getTripPoint(String token, int lineId){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getTripPoint(token,lineId,true);
        mObservable.subscribeOn(Schedulers.io())
                .compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<GetTripDaysInfo>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<GetTripDaysInfo> body) {
                        if (body.isSuccess()) {
                            mTripInfo.setValue(body.getRel());
                        } else {
                            mTripInfo.setValue(null);
                            ToastUtils.showShort(body.getReMsg());
                        }
                    }
                });
    }

}
