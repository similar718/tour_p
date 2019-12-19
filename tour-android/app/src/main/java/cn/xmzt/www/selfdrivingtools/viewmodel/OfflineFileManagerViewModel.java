package cn.xmzt.www.selfdrivingtools.viewmodel;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.selfdrivingtools.bean.OfflineFileManagerBean;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class OfflineFileManagerViewModel extends BaseViewModel {

    public MutableLiveData<List<OfflineFileManagerBean>> mPackageDowns;

    public OfflineFileManagerViewModel() {
        mPackageDowns = new MutableLiveData<List<OfflineFileManagerBean>>();
    }

    public void getOfficeLinePackageDowns(String scenicIds) {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getOfficeLinePackageDowns(scenicIds);
        mObservable.subscribeOn(Schedulers.io())
                .compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<List<OfflineFileManagerBean>>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<List<OfflineFileManagerBean>> body) {
                        if(body.isSuccess()){
                            mPackageDowns.setValue(body.getRel());
                        } else {
                            mPackageDowns.setValue(null);
                        }
                    }
                });
    }
}
