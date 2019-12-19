package cn.xmzt.www.intercom.vm;

import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.ToastUtils;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.intercom.bean.TourTripBean;
import cn.xmzt.www.intercom.bean.TourTripListBean;
import cn.xmzt.www.rxjava2.AsyncUtil;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.SPUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * 行程列表Model
 */
public class SchedulingListViewModel extends BaseViewModel {

    public MutableLiveData<TourTripListBean> mutableLiveData;
    public MutableLiveData<BaseDataBean<Object>> result;
    public SchedulingListViewModel(){
        mutableLiveData = new MutableLiveData<TourTripListBean>();
        result = new MutableLiveData<BaseDataBean<Object>>();
    }
    public int isFinish=0;//是否查询结束的行程：0否 1是
    /**
     * 获取行程列表
     */
    public void getTourTripList(int pageNum) {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getTourTripList(SPUtils.getToken(), Constants.mCityCode, isFinish, pageNum, 20);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<TourTripListBean>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<TourTripListBean> body) {
                        if(body.isSuccess()){
                            addRecentContacts(body.getRel());
                        }else {
                            mView.showLoadFail(null);
                        }
                    }
                });
    }

    // 请求会话
    private void addRecentContacts(TourTripListBean mTourTripListBean){
        AsyncUtil.async(new Function<String,TourTripListBean>() {
            @Override
            public TourTripListBean apply(String o) throws Exception {
                List<TourTripBean> tourTripList=mTourTripListBean.getTourTripList();
                if(tourTripList!=null){
                    for(TourTripBean tourTripBean:tourTripList){
                        RecentContact mRecentContact=NIMClient.getService(MsgService.class).queryRecentContact(tourTripBean.getGroupId(),SessionTypeEnum.Team);
                        tourTripBean.setRecentContact(mRecentContact);
                    }
                }
                return mTourTripListBean;
            }
        }, new Consumer<TourTripListBean>() {
            @Override
            public void accept(TourTripListBean mTourTripListBean) throws Exception {
                mutableLiveData.setValue(mTourTripListBean);
            }
        });
    }

    /**
     * 删除行程群
     * @param ids 行程id，多个id用英文逗号隔开
     */
    public void deleteGroup(String ids) {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.deleteGroup(TourApplication.getToken(),ids);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<Object> body) {
                        if(body.isSuccess()){
                            ToastUtils.showShort("删除行程成功");
                            result.setValue(body);
                        }else {
                            ToastUtils.showShort(body.getReMsg());
                        }
                    }
                });
    }
}
