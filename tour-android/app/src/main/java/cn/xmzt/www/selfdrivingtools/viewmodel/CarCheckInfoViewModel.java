package cn.xmzt.www.selfdrivingtools.viewmodel;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.selfdrivingtools.bean.MsgCarListInfo;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class CarCheckInfoViewModel extends BaseViewModel {
    public MutableLiveData<List<MsgCarListInfo>> mGuideInfoList;

    public CarCheckInfoViewModel() {
        mGuideInfoList = new MutableLiveData<List<MsgCarListInfo>>();
    }

    public void getMsgGuideList(){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getMsgCarInfo();
        mObservable.subscribeOn(Schedulers.io())
                .compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<List<MsgCarListInfo>>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<List<MsgCarListInfo>> body) {
                        if (body.isSuccess()){
                            mGuideInfoList.setValue(body.getRel());
                        }
                    }
                });
    }
}
