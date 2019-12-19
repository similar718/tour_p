package cn.xmzt.www.selfdrivingtools.viewmodel;


import androidx.lifecycle.MutableLiveData;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.selfdrivingtools.bean.AKeySOSBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * 一键救援ViewModel
 */
public class AKeySOSViewModel extends BaseViewModel {
    public MutableLiveData<List<AKeySOSBean>> sosList;
    public MutableLiveData<Object> mSosData;

    public AKeySOSViewModel() {
        sosList = new MutableLiveData<List<AKeySOSBean>>();
        mSosData = new MutableLiveData<Object>();
    }
    /**
     * 一键救援列表
     */
    public void getAKeySOSList() {
//        List<Object> list=new ArrayList<>();
//        list.add("紧急联系");
//        list.add(new AKeySOSBean(1,"行程领队","紧急情况，一键救援",""));
//        list.add(new AKeySOSBean(2,"官方客服","平台客服，在线支持",""));
//        list.add("免费救援");
//        list.add(new AKeySOSBean(0,"交通事故报警","112","112"));
//        list.add(new AKeySOSBean(0,"安全事故报警","110","110"));
//        list.add(new AKeySOSBean(0,"大陆汽车救援","400-818-1010","4008181010"));
//        list.add(new AKeySOSBean(0,"中石化免费救援","95105988转7","95105988"));
//        list.add(new AKeySOSBean(0,"平安车险救援","95511转5转2","95511"));
//        list.add(new AKeySOSBean(0,"人保车险救援","95518转9","95518"));
//        sosList.setValue(list);
        List<AKeySOSBean> list=new ArrayList<>();
        list.add(new AKeySOSBean(1,"行程领队","紧急情况，一键救援","", R.drawable.sos_1));
        list.add(new AKeySOSBean(2,"官方客服","平台客服，在线支持", Constants.CLIENTTELNUM, R.drawable.sos_2));
        list.add(new AKeySOSBean(3,"交通事故报警","呼叫120","120", R.drawable.sos_3));
        list.add(new AKeySOSBean(4,"安全事故报警","呼叫110","110", R.drawable.sos_4));
        list.add(new AKeySOSBean(5,"平安车险救援","95511转5转2","95511", R.drawable.sos_5));
        list.add(new AKeySOSBean(6,"人保车险救援","95518转9","95518", R.drawable.sos_6));
        sosList.setValue(list);
        /*ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getAKeySOSList(TourApplication.getToken(),pageNum,pageSize);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<List<Object>>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<List<Object>> body) {
                        if(body.isSuccess()){
                            List<Object> list=body.getRel();
                            if(list==null){
                                list=new ArrayList<>();
                            }
                            sosList.setValue(list);
                        }else {
                            mView.showLoadFail(null);
                        }
                    }
                });*/

    }


    public void getInfo(String token) {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.rescue(token);
        mObservable.subscribeOn(Schedulers.io())
                .compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<Object> body) {
                        if(body.isSuccess()){
                            mSosData.setValue(body.getRel());
                        }else {
                            mSosData.setValue(null);
                        }
                    }
                });
    }
}
