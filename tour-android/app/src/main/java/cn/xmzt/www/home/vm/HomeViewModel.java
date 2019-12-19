package cn.xmzt.www.home.vm;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.dialog.HomeSignDialog;
import cn.xmzt.www.home.bean.HomeIndexBean;
import cn.xmzt.www.home.fragment.HomeFragment;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.mine.event.SignEvent;
import cn.xmzt.www.roomdb.TourDatabase;
import cn.xmzt.www.roomdb.beans.PageData;
import cn.xmzt.www.roomdb.beans.PageKey;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.JsonUtil;
import cn.xmzt.www.utils.SPUtils;
import cn.xmzt.www.utils.TimeUtil;
import io.reactivex.Observable;

/**
 * 首页Model
 */
public class HomeViewModel extends BaseViewModel {
    public MutableLiveData<HomeIndexBean> mutableLiveNewData;
    public MutableLiveData<String> smartTeamGroup;
    HomeSignDialog dialog;
    private HomeFragment fragment;
    public void setFragment(HomeFragment fragment) {
        this.fragment = fragment;
    }
    public HomeViewModel(){
        mutableLiveNewData = new MutableLiveData<HomeIndexBean>();
        smartTeamGroup = new MutableLiveData<String>();
    }
    private int signDialogUserId;//签到弹框用户id（signDialogUserId 等于当前登录用户的id表示这个用户签到弹框显示过,反之）
    //是否是刷新
    public boolean isRefresh = true;
    /**
     * 获取首页Index(新版)
     * @param context
     * @param isShowLoading
     */
    public void postHomeNewIndex(Context context,boolean isShowLoading) {
        //mView.showLoading();
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.postHomeNewIndex(TourApplication.getToken());
        mObservable.compose(ComposeUtil.compose(mView,isShowLoading))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<HomeIndexBean>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<HomeIndexBean> body) {
                        if(body.isSuccess()){
                            mView.hideLoading();
                            HomeIndexBean homeIndexBean=body.getRel();
                            mutableLiveNewData.setValue(homeIndexBean);
                            setAddHomeContentsToDataBase(context, homeIndexBean);
                            int userId=TourApplication.getUserId();
                            if(userId!=signDialogUserId&&!TextUtils.isEmpty(TourApplication.getToken())&&homeIndexBean.isSign()){
                                signDialogUserId=userId;
                                if(dialog==null){
                                    dialog = new HomeSignDialog(fragment.getActivity(), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            goSignIn();
                                        }
                                    });
                                }
                                dialog.setData(body.getRel());
                                if(!dialog.isShowing()){
                                    dialog.show();
                                }
                            }
                        }else {
                            mView.showLoadFail("");
                        }
                    }
                });
    }

    /**
     * 去签到
     */
    private void goSignIn() {
        ApiRepertory.getInstance().getApiService().signIn(TourApplication.getToken())
                .compose(ComposeUtil.compose()).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>() {
            @Override
            public void onNext(BaseDataBean<Object> objectBaseDataBean) {
                if (objectBaseDataBean.isSuccess()) {
                    ToastUtils.showShort("签到成功！！！");
                    EventBus.getDefault().postSticky(new SignEvent());
                    dialog.dismiss();
                } else {
                    ToastUtils.showShort(objectBaseDataBean.getReMsg());
                }
            }
        });
    }

    /**
     * 将首页内容用json的格式加载到数据库
     * @param context 上下文
     * @param bean 首页内容类
     */
    public void setAddHomeContentsToDataBase(Context context, HomeIndexBean bean){
        // 拿到数据将实体类转化为json数据
        String jsonStr = JsonUtil.toString(bean);
        // 查询当前数据有无在数据库有信息
        PageData info = TourDatabase.getDefault(context).getPageDataDao().getPageData(PageKey.PAGE_KEY_HOME);
        if (info == null) { // 没有当前数据
            info = new PageData();
            info.funTypes=PageKey.PAGE_KEY_HOME;
            info.content=jsonStr;
            TourDatabase.getDefault(context).getPageDataDao().insert(info);
        } else {
            info.content=jsonStr;
            TourDatabase.getDefault(context).getPageDataDao().update(info);
        }

    }

    /**
     *  如果遇到后台返回错误 或者网络的问题 需要加载到本地的数据
     * @param context 上下文
     * @return
     */
    public HomeIndexBean getHomeContentsToDataBase(Context context){
        // 查询当前在数据库里面是否有缓存信息
        PageData info = TourDatabase.getDefault(context).getPageDataDao().getPageData(PageKey.PAGE_KEY_HOME);
        if (info == null){
            return null;
        } else {
            HomeIndexBean bean = null;
            try {
                bean = JsonUtil.getObject(info.content, HomeIndexBean.class);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return bean;
        }
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
                            smartTeamGroup.setValue(body.getRel());
                        } else {
                            smartTeamGroup.setValue(null);
                        }
                    }
                });
    }
}
