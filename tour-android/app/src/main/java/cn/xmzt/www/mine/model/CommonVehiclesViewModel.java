package cn.xmzt.www.mine.model;

import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.ToastUtils;

import java.util.List;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.mine.activity.CommonVehiclesActivity;
import cn.xmzt.www.mine.bean.CommonVehicleBean;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class CommonVehiclesViewModel extends BaseViewModel {

    public MutableLiveData<List<CommonVehicleBean>> mVehicleInfoList;

    public CommonVehiclesViewModel() {
        mVehicleInfoList = new MutableLiveData<List<CommonVehicleBean>>();
    }

    /**
     * 获取个人中心中的常用车辆信息
     */
    public void getsysUserPlateNumberList(String token, int pagenum, int pagesize, CommonVehiclesActivity context){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getsysUserPlateNumberList(token,pagenum,pagesize);
        mObservable.subscribeOn(Schedulers.io())
                .compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<List<CommonVehicleBean>>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<List<CommonVehicleBean>> body) {
                        if (body.isSuccess()) {
                            mVehicleInfoList.setValue(body.getRel());
                            if (pagenum == 1) {
                                context.dataBinding.refreshLayout.finishRefresh(true);
                            } else {
                                context.dataBinding.refreshLayout.finishLoadMore(true);
                            }
                        } else {
                            ToastUtils.showShort(body.getReMsg());
                        }
                    }
                });
    }

}
