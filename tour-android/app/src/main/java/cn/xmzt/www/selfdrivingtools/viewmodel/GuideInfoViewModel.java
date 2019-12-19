package cn.xmzt.www.selfdrivingtools.viewmodel;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.selfdrivingtools.bean.MsgGuideListInfo;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class GuideInfoViewModel extends BaseViewModel {

    public MutableLiveData<List<MsgGuideListInfo>> mGuideInfoList;

    public GuideInfoViewModel() {
        mGuideInfoList = new MutableLiveData<List<MsgGuideListInfo>>();
    }

    public void getMsgGuideList(int lineId,int tripId){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getMsgGuideList(lineId,tripId);
        mObservable.subscribeOn(Schedulers.io())
                .compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<List<MsgGuideListInfo>>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<List<MsgGuideListInfo>> body) {
                        if (body.isSuccess()){
                            mGuideInfoList.setValue(body.getRel());
                        }
                    }
                });
    }
}
