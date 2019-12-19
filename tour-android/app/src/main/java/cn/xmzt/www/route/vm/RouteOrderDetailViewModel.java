package cn.xmzt.www.route.vm;

import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.ToastUtils;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.route.bean.OrderInvoiceForm;
import cn.xmzt.www.route.bean.RouteOrderDetailBean;
import cn.xmzt.www.route.eventbus.RefreshEvent;
import cn.xmzt.www.route.eventbus.RefreshOrderBus;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observable;

/**
 * 路线详情ViewModel
 */
public class RouteOrderDetailViewModel extends BaseViewModel {
    public MutableLiveData<RouteOrderDetailBean> mRouteOrderDetail;
    public RouteOrderDetailViewModel() {
        mRouteOrderDetail = new MutableLiveData<RouteOrderDetailBean>();
    }
    public void getOrderDetail(String orderId) {
        ApiService mService =ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getOrderDetail(orderId,TourApplication.getToken());
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<RouteOrderDetailBean>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<RouteOrderDetailBean> body) {
                        if(body.isSuccess()){
                            mRouteOrderDetail.setValue(body.getRel());
                        }else {
                            mView.showLoadFail(null);
                        }
                    }
                });


    }
    public void sendInvoice(String orderCode,String email) {
        ApiService mService =ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.sendInvoice(orderCode,email,TourApplication.getToken());
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<RouteOrderDetailBean>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<RouteOrderDetailBean> body) {
                        if(body.isSuccess()){
                            ToastUtils.showShort("操作成功，将在24小时内收到邮件");
                            getOrderDetail(orderCode);
                        }else {
                            ToastUtils.showShort(body.getReMsg());
                        }
                    }
                });
    }
    /**
     *添加发票
     * @param orderCode
     * @param mOrderInvoiceForm
     * @return
     */
    public void addInvoice(String orderCode, OrderInvoiceForm mOrderInvoiceForm) {
        ApiService mService =ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.addInvoice(orderCode,mOrderInvoiceForm.getInvoiceTitleId(),mOrderInvoiceForm.getItemTitle(),mOrderInvoiceForm.getReceiveEmail());
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<RouteOrderDetailBean>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<RouteOrderDetailBean> body) {
                        if(body.isSuccess()){
                            ToastUtils.showShort("发票信息提交成功");
                            getOrderDetail(orderCode);
                        }else {
                            ToastUtils.showShort(body.getReMsg());
                        }
                    }
                });
    }
    public void cancelOrder(int orderId) {
        ApiService mService =ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.cancelOrder(TourApplication.getToken(),orderId);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<String>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<String> body) {
                        if(body.isSuccess()){
                            ToastUtils.showShort("取消订单成功");
                            EventBus.getDefault().post(new RefreshOrderBus(1,1));
                        }else {
                            mView.showLoadFail(body.getReMsg());
                        }
                    }
                });
    }
    public void applyRefund(String orderCode) {
        ApiService mService =ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.applyRefund(TourApplication.getToken(),orderCode);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<String>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<String> body) {
                        if(body.isSuccess()){
                            ToastUtils.showShort("您已提交退款申请，请等待客服为您确认");
                            EventBus.getDefault().post(new RefreshOrderBus(2));
                        }else {
                            mView.showLoadFail(body.getReMsg());
                        }
                    }
                });
    }
    public void deleteOrder(int orderId) {
        ApiService mService =ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.deleteOrder(TourApplication.getToken(),orderId);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<String>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<String> body) {
                        if(body.isSuccess()){
                            ToastUtils.showShort("删除订单成功");
                            EventBus.getDefault().post(new RefreshEvent());
                            EventBus.getDefault().post(new RefreshOrderBus(1,3));
                        }else {
                            mView.showLoadFail(body.getReMsg());
                        }
                    }
                });
    }
}
