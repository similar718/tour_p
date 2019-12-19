package cn.xmzt.www.smartteam.vm;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.ToastUtils;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import io.reactivex.Observable;

/**
 * 智能组队ViewModel
 */
public class SmartTeamViewModel extends BaseViewModel {
    public MutableLiveData<String> liveData;
    public SmartTeamViewModel() {
        liveData = new MutableLiveData<String>();
    }

    /**
     * 智能组队,获取当前进行中的队伍
     */
    public void getSmartTeamGroup(){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getSmartTeamGroup(TourApplication.getToken());
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<String>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<String> body) {
                        if(body.isSuccess()){
                            String groupId=body.getRel();
                            if(!TextUtils.isEmpty(groupId)){
                                ToastUtils.showShort("您已拥有队伍，正在前往...");
                                liveData.setValue(groupId);
                            }
                        }
                    }
                });
    }

    /**
     * 智能组队创建群
     */
    public void createGroup(){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.createGroup(TourApplication.getToken());
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<Object> body) {
                        if(body.isSuccess()){
                            ToastUtils.showShort("创建队伍成功");
                            liveData.setValue((String) body.getRel());
                        }else {
                            ToastUtils.showShort(body.getReMsg());
                            if("X9110".equals(body.getReCode())){
                                liveData.setValue((String) body.getRel());
                            }
                        }
                    }
                });
    }
    /**
     * 智能组加入队伍
     * @param groupPwdcard 群口令
     */
    public void joinTeam(String groupPwdcard){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.joinTeam(TourApplication.getToken(),groupPwdcard);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<Object> body) {
                        if(body.isSuccess()){
                            liveData.setValue((String) body.getRel());
                        }else {
                            ToastUtils.showShort(body.getReMsg());
                        }
                    }
                });
    }
}
