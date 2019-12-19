package cn.xmzt.www.route.vm;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;

import cn.xmzt.www.selfdrivingtools.bean.TourTripDetailNewBean;
import io.reactivex.Observable;

/**
 * 线路预览ViewModel
 */
public class RoutePreviewViewModel extends BaseViewModel {
    public MutableLiveData<List<TourTripDetailNewBean.LineRouteListVOBean>> routePreviewMap;

    public RoutePreviewViewModel() {
        routePreviewMap = new MutableLiveData<List<TourTripDetailNewBean.LineRouteListVOBean>>();
    }

    public void getRoutedetail(long lineId) {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getRoutedetail(lineId);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<List<TourTripDetailNewBean.LineRouteListVOBean>>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<List<TourTripDetailNewBean.LineRouteListVOBean>> body) {
                        if(body.isSuccess()){
                            routePreviewMap.setValue(body.getRel());
                        } else {
                            mView.showLoadFail(null);
                        }
                    }
                });

    }
}
