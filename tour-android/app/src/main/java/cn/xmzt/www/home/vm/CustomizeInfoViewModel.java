package cn.xmzt.www.home.vm;

import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.databinding.ActivityCustomizeInfoBinding;
import cn.xmzt.www.home.bean.CustomizeForm;
import cn.xmzt.www.home.event.CustomizeRefreshBus;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.LocalDataUtils;
import cn.xmzt.www.utils.TimeUtil;
import io.reactivex.Observable;

/**
 * 私人定制添加ViewModel
 */
public class CustomizeInfoViewModel extends BaseViewModel {
    public ActivityCustomizeInfoBinding dataBinding;
    public MutableLiveData<BaseDataBean<Object>> result;
    public CustomizeInfoViewModel(ActivityCustomizeInfoBinding dataBinding) {
        this.dataBinding=dataBinding;
        result=new MutableLiveData<BaseDataBean<Object>>();
    }
    public int customizeId;
    public CustomizeForm customizeForm;
    /**
     * 添加私人定制
     */
    public void getCustomizeInfo() {
        if(customizeId<=0){
            return;
        }
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.getCustomizeInfo(TourApplication.getToken(),customizeId);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<CustomizeForm>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<CustomizeForm> body) {
                        if(body.isSuccess()){
                            customizeForm=body.getRel();
                            if(customizeForm!=null){
                                CustomizeForm.CityArea departArea=customizeForm.getDepartArea();
                                if(departArea!=null){
                                    dataBinding.tvCfd.setText(departArea.getCityName());
                                }
                                List<CustomizeForm.CityArea> arrivalArea=customizeForm.getArrivalArea();
                                StringBuilder arrivalAreaSB=new StringBuilder();
                                for(int i=0;i<arrivalArea.size();i++){
                                    CustomizeForm.CityArea cityArea=arrivalArea.get(i);
                                    if(arrivalAreaSB.length()>0){
                                        arrivalAreaSB.append("、");
                                        arrivalAreaSB.append(cityArea.getCityName());
                                    }else {
                                        arrivalAreaSB.append(cityArea.getCityName());
                                    }
                                }
                                dataBinding.tvMdd.setText(arrivalAreaSB.toString());

                                String departDate=customizeForm.getDepartDate();
                                String endDate=customizeForm.getEndDate();
                                int betweenIntervalDays= TimeUtil.getBetweenIntervalDays(departDate,endDate,"yyyy-MM-dd")+1;
                                dataBinding.tvDateContent.setText(TimeUtil.stringDateToString(departDate,"yyyy-MM-dd","MM月dd日")
                                        +" - "+TimeUtil.stringDateToString(endDate,"yyyy-MM-dd","MM月dd日")
                                        +"  "+ betweenIntervalDays+"天");

                                dataBinding.tvPersonContent.setText(customizeForm.getCrNumber() + "成人，" + customizeForm.getXhNumber() + "儿童");

                                dataBinding.tvLeaderTypeContent.setText(customizeForm.getTacticsType() == 1 ? "无需领队" : "金牌领队");
                                dataBinding.tvLuodiTypeContent.setText(customizeForm.getGroundType() == 1 ? "落地自驾" : "自己爱车");
                                int accommodationType=customizeForm.getAccommodationType();
                                String mLiveData= LocalDataUtils.getCustomizeTypeLevelName(accommodationType,customizeForm.getAccommodationLevel());
                                dataBinding.tvLiveContent.setText("");
                                if (accommodationType == 1){ // 酒店
                                    dataBinding.tvLiveContent.setText("酒店   "+mLiveData);
                                } else if (accommodationType == 2){ // 民宿
                                    dataBinding.tvLiveContent.setText("民宿   "+mLiveData);
                                }
                                String mYusuanData= LocalDataUtils.getCustomizeTypeLevelName(3,customizeForm.getBudgetNew());
                                dataBinding.tvBudgetContent.setText(mYusuanData);

                                dataBinding.tvName.setText(customizeForm.getName());
                                dataBinding.tvPhone.setText(customizeForm.getTel());
                                dataBinding.tvRemark.setText(customizeForm.getRemark());
                                // 定制状态 0初始化 1定制中 2订制成功 3订制失败
                                dataBinding.tvModify.setVisibility(View.GONE);
                                if (customizeForm.getState() == 1){
                                    Drawable drawable = dataBinding.getActivity().getResources().getDrawable(R.drawable.customize_sure_ing);
                                    drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                                    dataBinding.customizeStatusTv.setCompoundDrawables(drawable, null,null,null);
                                    dataBinding.customizeStatusTv.setText("确认中");
                                    dataBinding.customizeStatusContentTv.setText("需求已提交，将免费提供2-3套自驾方案，请保持电话畅通!");
                                    dataBinding.tvModify.setVisibility(View.VISIBLE);
                                } else if (customizeForm.getState() == 2){
                                    Drawable drawable = dataBinding.getActivity().getResources().getDrawable(R.drawable.customize_sure_success);
                                    drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                                    dataBinding.customizeStatusTv.setCompoundDrawables(drawable, null,null,null);
                                    dataBinding.customizeStatusTv.setText("定制成功");
                                    dataBinding.customizeStatusContentTv.setText("恭喜，您已完成定制，专属行程管家将全程为您服务!");
                                } else if (customizeForm.getState() == 3){
                                    Drawable drawable = dataBinding.getActivity().getResources().getDrawable(R.drawable.customize_sure_failed);
                                    drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                                    dataBinding.customizeStatusTv.setCompoundDrawables(drawable, null,null,null);
                                    dataBinding.customizeStatusTv.setText("定制失败");
                                    dataBinding.customizeStatusContentTv.setText("很遗憾，未能为您提供后续服务，期待下次为您服务!");
                                }
                            }
                        }else {
                            mView.showLoadFail(body.getReMsg());
                        }
                    }
                });

    }

    /**
     * 更新私人定制
     */
    public void deleteCustomize() {
        if(customizeId<=0){
            return;
        }
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.deleteCustomize(TourApplication.getToken(),customizeId);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<Object> body) {
                        if(body.isSuccess()){
                            result.setValue(body);
                            EventBus.getDefault().post(new CustomizeRefreshBus(CustomizeRefreshBus.TYPE_DEL));//提示上一个页面刷新
                            ToastUtils.showShort("删除成功");
                        }else {
                            mView.showLoadFail(body.getReMsg());
                        }
                    }
                });

    }
}
