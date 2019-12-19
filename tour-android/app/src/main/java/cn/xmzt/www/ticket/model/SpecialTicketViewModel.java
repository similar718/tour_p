package cn.xmzt.www.ticket.model;

import androidx.lifecycle.MutableLiveData;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.ticket.bean.SpecialTicketBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;

import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import io.reactivex.schedulers.Schedulers;
import cn.xmzt.www.ticket.bean.SpecialTicketMustPlayBean;

import io.reactivex.Observable;

public class SpecialTicketViewModel extends BaseViewModel {

    public MutableLiveData<SpecialTicketBean> mSpecialTicketList;
    public MutableLiveData<SpecialTicketMustPlayBean> mSpecialTicketMustPlayList;
    public SpecialTicketViewModel() {
        mSpecialTicketList = new MutableLiveData<SpecialTicketBean>();
        mSpecialTicketMustPlayList = new MutableLiveData<SpecialTicketMustPlayBean>();
    }

    public void getSpecialTicket(String loc){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getSpecialTicketHome(loc);
        mObservable
                .subscribeOn(Schedulers.io())
                .compose(ComposeUtil.compose())
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<SpecialTicketBean>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<SpecialTicketBean> body) {
                        mView.hideLoading();
                        mSpecialTicketList.setValue(body.getRel());
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

    public void getSpecialTicketMustPlay(String c){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getSpecialTicketMustPlay(c);
        mObservable.subscribeOn(Schedulers.io())
                .compose(ComposeUtil.compose())
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<SpecialTicketMustPlayBean>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<SpecialTicketMustPlayBean> body) {
                        mView.hideLoading();
                        mSpecialTicketMustPlayList.setValue(body.getRel());
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
