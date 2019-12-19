package cn.xmzt.www.smartteam.vm;

import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.ActivityUtils;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.smartteam.bean.TripSignInDetailBean;
import cn.xmzt.www.utils.ToastUtils;
import io.reactivex.Observable;

public class TripSignInViewModel extends BaseViewModel {
    public MutableLiveData<TripSignInDetailBean> mSignInCheckData;
    public MutableLiveData<Object> mSignIn;

    public TripSignInViewModel() {
        mSignInCheckData = new MutableLiveData<TripSignInDetailBean>();
        mSignIn = new MutableLiveData<Object>();
    }

    /**
     * 群组-目的地-集合时间 检测是否签到
     */
    public void getSignInChecked(String token,String desId){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getSignInCheck(token,desId);
        mObservable.compose(ComposeUtil.compose(mView,false))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<TripSignInDetailBean>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<TripSignInDetailBean> body) {
                        if(body.isSuccess()){
                            mSignInCheckData.setValue(body.getRel());
                        } else {
                            ToastUtils.showText(ActivityUtils.getTopActivity(),body.getReMsg());
                        }
                    }
                });
    }

    /**
     * 群组-目的地-集合时间 用户签到
     */
    public void setSignIn(String token,String desId){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.setSignIn(token,desId);
        mObservable.compose(ComposeUtil.compose(mView,false))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<Object> body) {
                        if(body.isSuccess()){
                            ToastUtils.showText(ActivityUtils.getTopActivity(),"签到成功");
                            mSignIn.setValue(body.getRel());
                        }else {
                            ToastUtils.showText(ActivityUtils.getTopActivity(),body.getReMsg());
                        }
                    }
                });
    }
}
