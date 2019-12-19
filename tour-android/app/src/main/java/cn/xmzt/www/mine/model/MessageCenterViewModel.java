package cn.xmzt.www.mine.model;

import androidx.databinding.ObservableInt;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.mine.bean.MessageTypeReadBean;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;

import java.util.List;

import io.reactivex.Observable;

/**
 * @describe
 */
public class MessageCenterViewModel extends BaseViewModel {
    public ObservableInt ddmsgRead = new ObservableInt(0);//订单消息 0已读 1未读
    public ObservableInt hdmsgRead = new ObservableInt(0);//活动消息 0已读 1未读
    /**
     * 获取消息类型未读数
     * @return
     */
    public void  getNewCount(){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getSysMsgNewCount(TourApplication.getToken());
        mObservable.compose(ComposeUtil.compose())
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<List<MessageTypeReadBean>>>() {
                    @Override
                    public void onNext(BaseDataBean<List<MessageTypeReadBean>> body) {
                        if(body.isSuccess()){
                            List<MessageTypeReadBean> list=body.getRel();
                            if(list!=null){
                                for(MessageTypeReadBean readBean:list){
                                    //消息类型1系统通知2订单消息3活动服务4咨询服务
                                    if(readBean.getTypes()==2){
                                        if(readBean.getNewCount()>0){
                                            ddmsgRead.set(1);
                                        }else {
                                            ddmsgRead.set(0);
                                        }
                                    }else if(readBean.getTypes()==3){
                                        if(readBean.getNewCount()>0){
                                            hdmsgRead.set(1);
                                        }else {
                                            hdmsgRead.set(0);
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
    }
    /**
     * 获取消息类型未读数
     * @param types 消息类型1系统通知2订单消息3活动服务4咨询服务
     * @return
     */
    public void  markReadSysMsgTypesUpdate(String types){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.markReadSysMsgTypesUpdate(TourApplication.getToken(),types);
        mObservable.compose(ComposeUtil.compose())
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>() {
                    @Override
                    public void onNext(BaseDataBean<Object> body) {
                        if(body.isSuccess()){
                            if("2".equals(types)){
                                ddmsgRead.set(0);
                            }else if("3".equals(types)){
                                hdmsgRead.set(0);
                            }
                        }
                    }
                });
    }
}
