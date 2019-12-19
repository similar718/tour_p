package cn.xmzt.www.home.vm;


import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.home.bean.CustomizeBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import io.reactivex.Observable;

/**
 * 私人定制ViewModel
 */
public class CustomizeViewModel extends BaseViewModel{
    public MutableLiveData<List<CustomizeBean>> customizeList;
    public CustomizeViewModel() {
        customizeList = new MutableLiveData<List<CustomizeBean>>();
    }
    /**
     * 获取私人订制列表
     * @param pageNum
     * @param pageSize
     */
    public void getCustomizeList(int pageNum,int pageSize) {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getCustomizeList(TourApplication.getToken(),pageNum,pageSize);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<List<CustomizeBean>>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<List<CustomizeBean>> body) {
                        if(body.isSuccess()){
                            List<CustomizeBean> list=body.getRel();
                            if(list==null){
                                list=new ArrayList<>();
                            }
                            customizeList.setValue(list);
                        }else {
                            mView.showLoadFail(null);
                        }
                    }
                });

    }
}
