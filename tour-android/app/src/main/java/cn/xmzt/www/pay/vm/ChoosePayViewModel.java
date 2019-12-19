package cn.xmzt.www.pay.vm;

import androidx.lifecycle.MutableLiveData;
import androidx.databinding.ObservableInt;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;

import cn.xmzt.www.pay.bean.OrderUnpaid;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;

import io.reactivex.Observable;

/**
 * 选择支付方式ViewModel
 */
public class ChoosePayViewModel extends BaseViewModel {
    public MutableLiveData<OrderUnpaid> orderUnpaid;
    public MutableLiveData<BaseDataBean<Object>> placeOrder;
    public ChoosePayViewModel() {
        orderUnpaid = new MutableLiveData<OrderUnpaid>();
        placeOrder = new MutableLiveData<BaseDataBean<Object>>();
    }
    /**
     * 下单类型：
     * 1微信 2支付宝
     */
    public ObservableInt typeSelected= new ObservableInt(1);
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wechat_layout:
                typeSelected.set(1);
                break;
            case R.id.alipay_layout:
                typeSelected.set(2);
                break;
            default:
                break;
        }
    }

    /**
     * 待支付订单(选择支付页面的数据)
     * @param orderCode
     */
    public void getOrderUnpaid(String orderCode) {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getOrderUnpaid(orderCode,TourApplication.getToken());
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<OrderUnpaid>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<OrderUnpaid> body) {
                        if(body.isSuccess()){
                            orderUnpaid.setValue(body.getRel());
                        }else {
                            mView.showLoadFail(body.getReMsg());
                        }
                    }
                });

    }
    /**
     * 预下单(获取第三方支付信息)
     * @param orderCode 订单id编码
     * @param type 下单类型：1微信 2支付宝
     * @return
     */
    public void postPlaceOrder(String orderCode,int type) {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.postPlaceOrder(null,orderCode,1,type,TourApplication.getToken());
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<Object> body) {
                        placeOrder.setValue(body);
                    }
                });

    }
}
