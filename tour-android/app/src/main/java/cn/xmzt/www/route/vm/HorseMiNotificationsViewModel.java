package cn.xmzt.www.route.vm;

import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.ActivityUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.mine.bean.HorseMiMessageBean;
import cn.xmzt.www.mine.bean.HorseMiMessageFilterBean;
import cn.xmzt.www.mine.bean.WeatherInfoBean;
import cn.xmzt.www.mine.event.PushMessageEvent;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.ToastUtils;
import io.reactivex.Observable;

/**
 * 路线详情ViewModel
 */
public class HorseMiNotificationsViewModel extends BaseViewModel {
    public MutableLiveData<List<HorseMiMessageBean>> mMessageList;
    public MutableLiveData<WeatherInfoBean> mMessageWeatherInfo;
    public MutableLiveData<Object> mAllReadData;
    public HorseMiNotificationsViewModel() {
        mMessageList = new MutableLiveData<List<HorseMiMessageBean>>();
        mMessageWeatherInfo = new MutableLiveData<WeatherInfoBean>();
        mAllReadData = new MutableLiveData<Object>();
    }
    //是否是刷新
    public boolean isRefresh = true;
    public String findMonth=null;
    public String msgType=null;
    /**
     * 获取消息列表
     * @param isShowLoading
     * @param isRefresh
     * @param pageNum
     * @return
     */
    public void  getSysMsgList(boolean isShowLoading,boolean isRefresh,int pageNum){
        this.isRefresh=isRefresh;
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getSysMsgList(TourApplication.getToken(),msgType,findMonth,pageNum,20);
        mObservable.compose(ComposeUtil.compose(mView,isShowLoading))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<List<HorseMiMessageBean>>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<List<HorseMiMessageBean>> body) {
                        if(body.isSuccess()){
                            mMessageList.setValue(body.getRel());
                        }else {
                            mView.showLoadFail(null);
                        }
                    }
                });
    }
    public List<HorseMiMessageFilterBean> filterList = new ArrayList<HorseMiMessageFilterBean>();

    /**
     * 获取筛选条件
     * @return
     */
    public void  getMsgScreeContentList(){
        if(filterList.size()>0){
            return;
        }
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getMsgScreeContentList();
        mObservable.compose(ComposeUtil.compose(mView,false))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<List<HorseMiMessageFilterBean>>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<List<HorseMiMessageFilterBean>> body) {
                        if(body.isSuccess()){
                            List<HorseMiMessageFilterBean> messageType = new ArrayList<HorseMiMessageFilterBean>();
                            List<HorseMiMessageFilterBean> searchMonth = new ArrayList<HorseMiMessageFilterBean>();
                            List<HorseMiMessageFilterBean> list=body.getRel();
                            if(list!=null&&list.size()>0){
                                for(int i=0;i<list.size();i++){
                                    HorseMiMessageFilterBean filter=list.get(i);
                                    if(filter.getContentType()==HorseMiMessageFilterBean.TYPE_MSG_TYPE){
                                        messageType.add(filter);
                                    }else if(filter.getContentType()==HorseMiMessageFilterBean.TYPE_DATE){
                                        searchMonth.add(filter);
                                    }
                                }
                                HorseMiMessageFilterBean filterBean1=new HorseMiMessageFilterBean();
                                filterBean1.setContentType(0);
                                filterBean1.setContentValue("消息类型");
                                filterList.add(filterBean1);
                                filterList.addAll(messageType);
                                HorseMiMessageFilterBean filterBean2=new HorseMiMessageFilterBean();
                                filterBean2.setContentType(0);
                                filterBean2.setContentValue("查询月份");
                                filterList.add(filterBean2);
                                filterList.addAll(searchMonth);

                            }
                        }
                    }
                });
    }
    public int unreadCount;
    /**
     * 消息标记为已读
     * @return
     */
    public void  msgSignReadById(int id){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.msgSignReadById(TourApplication.getToken(),id);
        mObservable.compose(ComposeUtil.compose(mView,false))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<List<HorseMiMessageFilterBean>>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<List<HorseMiMessageFilterBean>> body) {
                        if(body.isSuccess()){
                            if(unreadCount>0){
                                unreadCount=unreadCount-1;
                            }
                            EventBus.getDefault().post(new PushMessageEvent(false,null));
                        }
                    }
                });
    }

    /**
     * 根据经纬度获取当前城市的天气信息
     * @return
     */
    public void getWeatherInfo(String lat,String lng){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getWeather(TourApplication.getToken(),lat,lng);
        mObservable.compose(ComposeUtil.compose(mView,false))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<WeatherInfoBean>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<WeatherInfoBean> body) {
                        if (body.isSuccess()) {
                            mMessageWeatherInfo.setValue(body.getRel());
                        } else {
                            ToastUtils.showText(ActivityUtils.getTopActivity(),body.getReMsg());
                        }
                    }
                });
    }

    /**
     * 根据消息类型批量设为已读
     * @return
     */
    public void allMsgAlreadyRead(){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.allMsgAlreadyRead(TourApplication.getToken());
        mObservable.compose(ComposeUtil.compose(mView,false))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<Object> body) {
                        if (body.isSuccess()) {
                            unreadCount=0;
                            mAllReadData.setValue(body.getRel());
                            EventBus.getDefault().post(new PushMessageEvent(false,null));
                        } else {
                            ToastUtils.showText(ActivityUtils.getTopActivity(),body.getReMsg());
                        }
                    }
                });
    }
}
