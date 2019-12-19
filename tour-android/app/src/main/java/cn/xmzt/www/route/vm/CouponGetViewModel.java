package cn.xmzt.www.route.vm;


import android.annotation.SuppressLint;
import androidx.lifecycle.MutableLiveData;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.bean.CouponBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.rxjava2.QzdsException;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * 领取优惠券
 */
public class CouponGetViewModel extends BaseViewModel {
    public MutableLiveData<List<CouponBean>> couponList;
    public CouponGetViewModel() {
        couponList = new MutableLiveData<List<CouponBean>>();
    }

    /**
     * @param type 优惠券类型 (0:通用,1:线路,2:酒店,3:门票)
     */
    public void getCouponList(int type) {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getCouponList(type);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<List<CouponBean>>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<List<CouponBean>> body) {
                        if(body.isSuccess()){
                            couponList.setValue(body.getRel());
                        }else {
                            mView.showLoadFail(null);
                        }
                    }
                });

    }
    /**
     * @param couponId 优惠券Id
     * @param type 优惠券类型 (0:通用,1:线路,2:酒店,3:门票)
     */
    @SuppressLint("CheckResult")
    public void getCoupon(int couponId, int type){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getCoupon(TourApplication.getToken(),couponId);
        mObservable.flatMap(new Function<BaseDataBean<String>, ObservableSource<BaseDataBean<List<CouponBean>>>>() {
                    @Override
                    public ObservableSource<BaseDataBean<List<CouponBean>>> apply(BaseDataBean<String> baseDataBean) throws QzdsException {
                        if(baseDataBean.isSuccess()){
                            return mService.getCouponList(type);
                        }else {
                            throw new QzdsException(baseDataBean.getReMsg(),baseDataBean.getReCode());
                        }
                    }
                })
                .compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<List<CouponBean>>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<List<CouponBean>> body) {
                        if(body.isSuccess()){
                            couponList.setValue(body.getRel());
                        }else {
                            mView.showLoadFail(null);
                        }
                    }
                });

    }
}
