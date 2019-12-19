package cn.xmzt.www.route.vm;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.mine.bean.CommonVehicleBean;
import cn.xmzt.www.mine.bean.MyCouponBean;
import cn.xmzt.www.route.bean.LineOrderForm;
import cn.xmzt.www.route.bean.RouteFiltrateList;
import cn.xmzt.www.route.bean.RoutePage;
import cn.xmzt.www.route.bean.TourInsurance;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.ActivityUtils;

import cn.xmzt.www.rxjava2.QzdsException;
import cn.xmzt.www.utils.ToastUtils;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * 路线时间人数选择ViewModel
 */
public class RouteFillinOrderViewModel extends BaseViewModel {
    public MutableLiveData<BaseDataBean<String>> result;
    public MutableLiveData<List<MyCouponBean>> couponList;
    public MutableLiveData<CommonVehicleBean> mCarInfo;
    public String currentTime;
    public RouteFillinOrderViewModel() {
        result = new MutableLiveData<BaseDataBean<String>>();
        couponList = new MutableLiveData<List<MyCouponBean>>();
        mCarInfo = new MutableLiveData<CommonVehicleBean>();
    }

    /**
     * 创建线路订单
     */
    public void postOrderLine(LineOrderForm orderForm){
        ApiService mService =ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.postOrderLine(orderForm);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<String>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<String> body) {
                        if(body.isSuccess()){
                            result.setValue(body);
                        }else {
                            mView.showLoadFail(body.getReMsg());
                        }
                    }
                });
    }
    public TourInsurance tourInsurance=null;
    public void getData(int lineId){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getInsuranceInfo(lineId);
        mObservable.flatMap(new Function<BaseDataBean<TourInsurance>, Observable<BaseDataBean<List<MyCouponBean>>>>() {
            @Override
            public Observable<BaseDataBean<List<MyCouponBean>>> apply(BaseDataBean<TourInsurance> body) throws Exception {
                if(body.isSuccess()){
                    tourInsurance=body.getRel();
                    return mService.getMyCouponList(1,0, TourApplication.getToken(),"1");
                }else {
                    throw new QzdsException(body.getReMsg(),body.getReCode());
                }
            }
        }).compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<List<MyCouponBean>>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<List<MyCouponBean>> body) {
                        if(body.isSuccess()){
                            List<MyCouponBean> list=body.getRel();
                            if(list==null){
                                list=new ArrayList<>();
                            }
                            couponList.setValue(list);
                        }else {
                            mView.showLoadFail(body.getReMsg());
                        }
                    }
                });
    }
    /**
     * 获取使用优惠券列表
     */
    public void getUseCouponList() {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getMyCouponList(1,0, TourApplication.getToken(),"1");
        mObservable.compose(ComposeUtil.compose())
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<List<MyCouponBean>>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<List<MyCouponBean>> body) {
                        if(body.isSuccess()){
                            List<MyCouponBean> list=body.getRel();
                            if(list==null){
                                list=new ArrayList<>();
                            }
                            couponList.setValue(list);
                        }else {
                            mView.showLoadFail(null);
                        }
                    }
                });

    }

    /**
     * 获取默认车辆信息
     */
    public void getSysUserPlateSelectDefault() {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getsysUserPlateNumberSelectDefault(TourApplication.getToken());
        mObservable.compose(ComposeUtil.compose())
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<CommonVehicleBean>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<CommonVehicleBean> body) {
                        if(body.isSuccess()){
                            mCarInfo.setValue(body.getRel());
                        }else {
                            ToastUtils.showText(ActivityUtils.getTopActivity(),body.getReMsg());
                        }
                    }
                });

    }
}
