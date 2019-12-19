package cn.xmzt.www.selfdrivingtools.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.ToastUtils;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.selfdrivingtools.bean.ScenicSpotDetailBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class ScenicSpotDetailViewModel extends BaseViewModel {

    public MutableLiveData<ScenicSpotDetailBean> mScenicSpotDetailInfo;
    public MutableLiveData<String> mScenicSpotDetailErrorInfo;

    public ScenicSpotDetailViewModel() {
        mScenicSpotDetailInfo = new MutableLiveData<ScenicSpotDetailBean>();
        mScenicSpotDetailErrorInfo = new MutableLiveData<String>();
    }

    /**
     * 获取问题类型信息
     */
    public void getScenicSpotData(int id){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getScenicSpotData(id);
        mObservable.subscribeOn(Schedulers.io())
                .compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<ScenicSpotDetailBean>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<ScenicSpotDetailBean> body) {
                        if (body.isSuccess()) {
                            mScenicSpotDetailInfo.setValue(body.getRel());
                        } else {
                            mScenicSpotDetailErrorInfo.setValue(null);
                            ToastUtils.showShort(body.getReMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mScenicSpotDetailErrorInfo.setValue(null);
                    }
                });
    }
}
