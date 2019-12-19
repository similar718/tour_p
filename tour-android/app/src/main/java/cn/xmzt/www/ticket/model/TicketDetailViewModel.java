package cn.xmzt.www.ticket.model;

import androidx.lifecycle.MutableLiveData;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.ticket.bean.TicketDetailInfo;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class TicketDetailViewModel extends BaseViewModel {

    public MutableLiveData<TicketDetailInfo> mTicketDetailList;
    public TicketDetailViewModel() {
        mTicketDetailList = new MutableLiveData<TicketDetailInfo>();
    }
    public void getSpecialTicket(int id){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getTicketDetail(id);
        mObservable.subscribeOn(Schedulers.io())
                .compose(ComposeUtil.compose())
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<TicketDetailInfo>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<TicketDetailInfo> body) {
                        mView.hideLoading();
                        mTicketDetailList.setValue(body.getRel());
                    }
                    @Override
                    protected void onStart() {
                        super.onStart();
                        mView.showLoading();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mView.hideLoading();
                    }
                });
    }
}
