package cn.xmzt.www.route.vm;


import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.mine.bean.MyCouponBean;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;

import io.reactivex.Observable;

/**
 * 领取优惠券
 */
public class CouponUseViewModel extends BaseViewModel {
    public MutableLiveData<List<MyCouponBean>> couponList;
    public CouponUseViewModel() {
        couponList = new MutableLiveData<List<MyCouponBean>>();
    }
    /**
     * 获取使用优惠券列表
     * @param pageNum
     * @param pageSize
     * @param type @param type 优惠券类型 (0:通用,1:线路,2:酒店,3:门票)
     */
    public void getUseCouponList(int pageNum,int pageSize,int type) {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getMyCouponList(1,0,TourApplication.getToken(),type+"");
        mObservable.compose(ComposeUtil.compose(mView))
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
}
