package cn.xmzt.www.selfdrivingtools.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.ToastUtils;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.selfdrivingtools.bean.ScenicFeedBackTypeBean;
import cn.xmzt.www.selfdrivingtools.bean.SuggestAndFeedBackNaviBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.LocalDataUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class ScenicFeedBackViewModel extends BaseViewModel {

    public MutableLiveData<List<ScenicFeedBackTypeBean>> mScenicFeedBack;
    public MutableLiveData<String> mSave;

    public ScenicFeedBackViewModel() {
        mScenicFeedBack = new MutableLiveData<List<ScenicFeedBackTypeBean>>();
        mSave = new MutableLiveData<String>();
    }

    /**
     * 获取问题类型信息
     */
    public void getScenicFeedBack(){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getScenicFeedBack();
        mObservable.subscribeOn(Schedulers.io())
                .compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<List<ScenicFeedBackTypeBean>>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<List<ScenicFeedBackTypeBean>> body) {
                        if (body.isSuccess()) {
                            mScenicFeedBack.setValue(body.getRel());
                        } else {
                            ToastUtils.showShort(body.getReMsg());
                        }
                    }
                });
    }

    /**
     * 提交建议与反馈信息
     * @param bigClass 问题大类
     * @param description 补充描述
     * @param phone 手机号
     * @param id 景区编号
     * @param smallClass 问题小类
     * @param token
     */
    public void getScenicFeedBackSave(String bigClass,String description,String phone,long id,String smallClass,String token){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getScenicFeedBackSave(bigClass,description,phone,id,smallClass,token);
        mObservable.subscribeOn(Schedulers.io())
                .compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<String>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<String> body) {
                        if (body.isSuccess()) {
                            // 操作成功 更新ui
                            mSave.setValue(body.getReMsg());
                        } else {
                            ToastUtils.showShort(body.getReMsg());
                        }
                    }
                });
    }

    /**
     * 获取类型数据
     * @return
     */
    public List<SuggestAndFeedBackNaviBean> getNaviTypeData(){
        return LocalDataUtils.getSuggestAndFeedBackNaviTypeAllData();
    }
}
