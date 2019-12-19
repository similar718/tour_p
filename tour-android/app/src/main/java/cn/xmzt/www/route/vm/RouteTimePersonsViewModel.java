package cn.xmzt.www.route.vm;

import androidx.lifecycle.MutableLiveData;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.route.bean.TimePriceMonthBean;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.TimeUtil;

import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;

/**
 * 路线时间人数选择ViewModel
 */
public class RouteTimePersonsViewModel extends BaseViewModel {
    public MutableLiveData<List<TimePriceMonthBean>> timePriceMonthList;
    public String currentTime;
    public RouteTimePersonsViewModel() {
        timePriceMonthList = new MutableLiveData<List<TimePriceMonthBean>>();
    }
    public void getLineTimePrices(int lineId) {
        ApiService mService =ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getLineTimePrices(lineId,6);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<Response<BaseDataBean<List<TimePriceMonthBean>>>>(mView) {
                    @Override
                    public void onNext(Response<BaseDataBean<List<TimePriceMonthBean>>> response) {
                        Date date=response.headers().getDate("Date");
                        if(date==null){
                            date=new Date();
                        }
                        currentTime= TimeUtil.dateToStr(date.getTime(),TimeUtil.FORMAT_A);
                        BaseDataBean<List<TimePriceMonthBean>> body= response.body();
                        if(body.isSuccess()){
                            timePriceMonthList.setValue(body.getRel());
                        }else {
                            mView.showLoadFail(null);
                        }
                    }
                });
    }
}
