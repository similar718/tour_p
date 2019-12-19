package cn.xmzt.www.mine.model;

import androidx.lifecycle.MutableLiveData;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.mine.bean.MessageBean;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;

import java.util.List;

import io.reactivex.Observable;

/**
 * 系统消息ViewModel
 */
public class SysMsgViewModel extends BaseViewModel {
    public MutableLiveData<List<MessageBean>> mMessageList;
    private String type="1"; //消息类型1系统通知2订单消息3活动服务4咨询服务
    public SysMsgViewModel() {
        mMessageList = new MutableLiveData<List<MessageBean>>();
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取消息列表
     * @param pageNum
     * @return
     */
    public void  getSysMsgList(int pageNum){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getSysMsgList(TourApplication.getToken(),type,pageNum,20);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<List<MessageBean>>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<List<MessageBean>> body) {
                        if(body.isSuccess()){
                            mMessageList.setValue(body.getRel());
                        }else {
                            mView.showLoadFail(null);
                        }
                    }
                });
    }
}
