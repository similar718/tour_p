package cn.xmzt.www.route.vm;

import androidx.lifecycle.MutableLiveData;
import androidx.databinding.ObservableInt;

import com.blankj.utilcode.util.ToastUtils;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.route.bean.RouteDetailPage;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * 路线详情ViewModel
 */
public class RouteDetailViewModel extends BaseViewModel {
    /**
     * 1 表示选中特色产品
     * 2表示选中行程
     * 3表示选中费用
     * 4表示选中须知
     */
    public ObservableInt selected= new ObservableInt(1);
    /**
     * 0表示标题栏没有收起,没有收藏
     * 1表示标题栏没有收起,收藏
     * 2表示标题栏收起,没有收藏
     * 3表示标题栏收起,收藏
     */
    public ObservableInt isCollection= new ObservableInt(0);//

    public MutableLiveData<RouteDetailPage> mRouteDetailPage;
    public RouteDetailPage mRouteDetail;
    public RouteDetailViewModel() {
        mRouteDetailPage = new MutableLiveData<RouteDetailPage>();
    }
    public void getRouteDetail(String token,int lineId) {
        ApiService mService =ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getRouteDetail(token,lineId,true);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<RouteDetailPage>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<RouteDetailPage> body) {
                        if(body.isSuccess()){
                            mRouteDetail=body.getRel();
                            mRouteDetailPage.setValue(mRouteDetail);
                        }else {
                            mView.showLoadFail(null);
                        }
                    }
                });
    }
    public void collection(String token,int lineId) {
        ApiService mService =ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.addCollectionInfo(token,lineId,2);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<Long>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<Long> body) {
                        if(body.isSuccess()){
                            if(mRouteDetail!=null){
                                if(mRouteDetail.isHasCollection()){
                                    ToastUtils.showShort("取消收藏成功");
                                }else {
                                    ToastUtils.showShort("收藏成功");
                                }
                                mRouteDetail.setHasCollection(!mRouteDetail.isHasCollection());
                                mRouteDetailPage.setValue(mRouteDetail);
                            }
                        }else {
                            mView.showLoadFail(body.getReMsg());
                        }
                    }
                });
    }

    private List<RouteDetailPage.ResourceListBean> resourceList;//路宣传图或视频

    /**
     * 把带图片的对象数组转换为图片数组
     * @param resourceList
     * @return
     */
    public ArrayList<String> resourcetoStringArrayList(List<RouteDetailPage.ResourceListBean> resourceList){
        ArrayList<String> list=new ArrayList<String>();
        for(int i=0;i<resourceList.size();i++){
            list.add(resourceList.get(i).getThumb());
        }
        return list;
    }
}
