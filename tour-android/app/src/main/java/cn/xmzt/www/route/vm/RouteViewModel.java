package cn.xmzt.www.route.vm;

import androidx.lifecycle.MutableLiveData;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.route.bean.RouteFiltrateList;
import cn.xmzt.www.route.bean.RoutePage;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.rxjava2.QzdsException;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * 路线ViewModel
 */
public class RouteViewModel extends BaseViewModel {
    public MutableLiveData<RoutePage> mRoutePage;
    public MutableLiveData<RouteFiltrateList> mRouteFiltrateList;
    public RouteViewModel() {
        mRoutePage = new MutableLiveData<RoutePage>();
        mRouteFiltrateList = new MutableLiveData<RouteFiltrateList>();
    }
    public void filtrateList() {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.filtrateList();
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<RouteFiltrateList>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<RouteFiltrateList> body) {
                        if(body.isSuccess()){
                            mRouteFiltrateList.setValue(body.getRel());
                        }else {
                            mView.showLoadFail(body.getReMsg());
                        }
                    }
                });
    }
    private RouteFiltrateList routeFiltrateList=null;
    public String arrivalCity,city, daySize, departDate,keyword,locationCity,theme,discounts,guessLike,isTactics,popPlay,purchase, sortType;
    public void initData() {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.filtrateList();
        mObservable.flatMap(new Function<BaseDataBean<RouteFiltrateList>, Observable<BaseDataBean<RoutePage>>>() {
            @Override
            public Observable<BaseDataBean<RoutePage>> apply(BaseDataBean<RouteFiltrateList> body) throws Exception {
                if(body.isSuccess()){
                    routeFiltrateList=body.getRel();
                    return mService.lineSearch(arrivalCity,city,daySize,departDate,
                            discounts, guessLike,isTactics,keyword,locationCity,1,pageSize,
                            popPlay,purchase,sortType,theme);
                }else {
                    throw new QzdsException(body.getReMsg(),body.getReCode());
                }
            }
        }).compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<RoutePage>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<RoutePage> body) {
                        if(routeFiltrateList!=null){
                            mRouteFiltrateList.setValue(routeFiltrateList);
                        }
                        if(body.isSuccess()){
                            mRoutePage.setValue(body.getRel());
                        }else {
                            mView.showLoadFail(body.getReMsg());
                        }
                    }
                });
    }
    /**
     * 线路搜索列表
     * @param arrivalCity 目的城市
     * @param city 起始城市
     * @param daySize 日期天数
     * @param departDate 出发时间yyyy-MM
     * @param discounts 是否特惠推荐(0:否,1:是)
     * @param guessLike 是否猜你喜欢(0:否,1:错峰出行,2:亲子时光,3:休闲度假)
     * @param isTactics 是否有领队(0:没有,1有)
     * @param keyword 关键字
     * @param locationCity 当前定位城市
     * @param pageNum 当前第几页(默认1，从1开始)
     * @param pageSize 每页大小(默认20)
     * @param popPlay 是否人气必玩(0:否,1:是)
     * @param purchase 是否超值抢购(0:否,1:是)
     * @param sortType 排序 (1:销量优先,2:价格从低到高,3:价格从高到低)
     * @param theme 主题code，多个用英文逗号隔开
     */
    private void lineSearch(String arrivalCity,String city, String daySize,
                           String departDate, String discounts, String guessLike,
                           String isTactics, String keyword, String locationCity,
                           int pageNum, int pageSize, String popPlay,
                           String purchase, String sortType, String theme ){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.lineSearch(arrivalCity,city,daySize,departDate,
                discounts, guessLike,isTactics,keyword,locationCity,pageNum,pageSize,
                popPlay,purchase,sortType,theme);

        mObservable.compose(ComposeUtil.compose(mView,false))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<RoutePage>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<RoutePage> body) {
                        mView.hideLoading();
                        if(body.isSuccess()){
                            mRoutePage.setValue(body.getRel());
                        }else {
                            mView.showLoadFail("");
                        }
                    }
                });
    }

    //是否是刷新
    public boolean isRefresh = true;
    private int pageSize=15;
    public void lineSearch(boolean isRefresh,int pageNum){
        this.isRefresh=isRefresh;
        lineSearch(arrivalCity,city,daySize,departDate,
                discounts, guessLike,isTactics,keyword,locationCity,pageNum,pageSize,
                popPlay,purchase,sortType,theme);
    }
}
