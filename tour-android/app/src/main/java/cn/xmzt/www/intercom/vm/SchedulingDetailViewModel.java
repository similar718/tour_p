package cn.xmzt.www.intercom.vm;

import androidx.lifecycle.MutableLiveData;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;

import cn.xmzt.www.selfdrivingtools.bean.TourTripDetailNewBean;
import io.reactivex.Observable;

/**
 * 行程详情ViewModel
 */
public class SchedulingDetailViewModel extends BaseViewModel {
    public MutableLiveData<TourTripDetailNewBean> tripDetail;
    public SchedulingDetailViewModel() {
        tripDetail = new MutableLiveData<TourTripDetailNewBean>();
    }
    public void getTripDetail(int tripId,String groupId) {
        ApiService mService =ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getTripDetail(TourApplication.getToken(),tripId,TourApplication.getRefCode(),groupId,null,true);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<TourTripDetailNewBean>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<TourTripDetailNewBean> body) {
                        if(body.isSuccess()){
                            tripDetail.setValue(body.getRel());
                        }else {
                            mView.showLoadFail(null);
                        }
                    }
                });
    }
}
