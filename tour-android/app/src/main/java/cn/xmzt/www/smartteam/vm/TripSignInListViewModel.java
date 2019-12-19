package cn.xmzt.www.smartteam.vm;

import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.ActivityUtils;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.smartteam.bean.TripSignInListBean;
import cn.xmzt.www.utils.ToastUtils;
import io.reactivex.Observable;

public class TripSignInListViewModel extends BaseViewModel {
    public MutableLiveData<TripSignInListBean> mSignIn;

    public TripSignInListViewModel() {
        mSignIn = new MutableLiveData<TripSignInListBean>();
    }
    /**
     * 群组-目的地-集合时间 用户签到记录信息
     */
    public void getSigninInfos(String token,String desId){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getSigninInfos(token,desId);
        mObservable.compose(ComposeUtil.compose(mView,false))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<TripSignInListBean>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<TripSignInListBean> body) {
                        if(body.isSuccess()){
                            mSignIn.setValue(body.getRel());
                        } else {
                            ToastUtils.showText(ActivityUtils.getTopActivity(),body.getReMsg());
                        }
                    }
                });
    }

}
