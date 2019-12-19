package cn.xmzt.www.route.vm;


import androidx.lifecycle.MutableLiveData;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.bean.InvoiceTitleBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;

import java.util.List;

import io.reactivex.Observable;

/**
 * 选择发票抬头
 */
public class SelectInvoiceTitleViewModel extends BaseViewModel {
    public MutableLiveData<List<InvoiceTitleBean>> invoiceTitleList;
    public SelectInvoiceTitleViewModel() {
        invoiceTitleList = new MutableLiveData<List<InvoiceTitleBean>>();
    }
    public void getInvoiceTitleList() {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getInvoiceTitleList(TourApplication.getToken());
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<List<InvoiceTitleBean>>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<List<InvoiceTitleBean>> body) {
                        if(body.isSuccess()){
                            invoiceTitleList.setValue(body.getRel());
                        }else {
                            mView.showLoadFail(null);
                        }
                    }
                });

    }
}
