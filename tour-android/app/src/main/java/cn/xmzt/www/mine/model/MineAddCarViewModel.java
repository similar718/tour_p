package cn.xmzt.www.mine.model;

import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.ToastUtils;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class MineAddCarViewModel extends BaseViewModel {
    public MutableLiveData<String> mVehicleInfo;
    public MutableLiveData<String> mVehicleDelInfo;

    public MineAddCarViewModel() {
        mVehicleInfo = new MutableLiveData<String>();
        mVehicleDelInfo = new MutableLiveData<String>();
    }

    /**
     * 修车辆信息
     */
    public void getsysUserPlateNumberSave(String token,String linePlate,Integer id,int isDefault){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getsysUserPlateNumberSave(token,linePlate,id,isDefault);
        mObservable.subscribeOn(Schedulers.io())
                .compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<String>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<String> body) {
                        if (body.isSuccess()) {
                            mVehicleInfo.setValue(body.getRel());
                        } else {
                            ToastUtils.showShort(body.getReMsg());
                        }
                    }
                });
    }

    /**
     * 删除车辆信息
     */
    public void getsysUserPlateNumberDelete(String token,Integer id){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getsysUserPlateNumberDelete(token,id);
        mObservable.subscribeOn(Schedulers.io())
                .compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<String>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<String> body) {
                        if (body.isSuccess()) {
                            mVehicleDelInfo.setValue(body.getRel());
                        } else {
                            ToastUtils.showShort(body.getReMsg());
                        }
                    }
                });
    }

}
