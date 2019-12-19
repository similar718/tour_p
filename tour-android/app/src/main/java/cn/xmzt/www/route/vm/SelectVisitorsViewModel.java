package cn.xmzt.www.route.vm;


import androidx.lifecycle.MutableLiveData;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.bean.OftenVisitorsBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;

import java.util.List;

import io.reactivex.Observable;

/**
 * 选择出游人
 */
public class SelectVisitorsViewModel extends BaseViewModel {
    public MutableLiveData<List<OftenVisitorsBean>> visitorsList;
    public SelectVisitorsViewModel() {
        visitorsList = new MutableLiveData<List<OftenVisitorsBean>>();
    }
    public void getVisitorsList() {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getOftenVisitors(TourApplication.getToken(),1);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<List<OftenVisitorsBean>>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<List<OftenVisitorsBean>> body) {
                        if(body.isSuccess()){
                            visitorsList.setValue(body.getRel());
                        }else {
                            mView.showLoadFail(null);
                        }
                    }
                });

    }
}
