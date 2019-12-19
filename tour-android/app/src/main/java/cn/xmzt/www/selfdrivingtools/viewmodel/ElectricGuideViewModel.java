package cn.xmzt.www.selfdrivingtools.viewmodel;

import androidx.lifecycle.MutableLiveData;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import io.reactivex.Observable;

public class ElectricGuideViewModel extends BaseViewModel {
    public MutableLiveData<String> smartTeamGroup;
    public ElectricGuideViewModel(){
        smartTeamGroup = new MutableLiveData<String>();
    }
    /**
     * 智能组队,获取当前进行中的队伍
     */
    public void getSmartTeamGroup(){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getSmartTeamGroup(TourApplication.getToken());
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<String>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<String> body) {
                        if(body.isSuccess()){
                            smartTeamGroup.setValue(body.getRel());
                        } else {
                            smartTeamGroup.setValue(null);
                        }
                    }
                });
    }
}
