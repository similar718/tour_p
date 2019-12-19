package cn.xmzt.www.home.vm;

import android.text.Editable;
import android.text.TextUtils;
import android.view.View;

import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.ToastUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.databinding.ActivityCustomizeAddBinding;
import cn.xmzt.www.home.bean.CustomizeForm;
import cn.xmzt.www.home.bean.SelectDateTime;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.FastJsonUtil;
import cn.xmzt.www.utils.MatcherUtils;
import cn.xmzt.www.utils.TimeUtil;
import io.reactivex.Observable;

/**
 * 私人定制添加ViewModel
 */
public class CustomizeAddViewModel extends BaseViewModel {
    public MutableLiveData<BaseDataBean<Object>> result;
    public ActivityCustomizeAddBinding dataBinding;
    public CustomizeAddViewModel(ActivityCustomizeAddBinding dataBinding) {
        this.dataBinding=dataBinding;
        result = new MutableLiveData<BaseDataBean<Object>>();
    }
    public CustomizeForm customizeForm=new CustomizeForm();
    public void phoneAfterTextChanged(Editable s) {
        String tel=s.toString();
        if(TextUtils.isEmpty(tel)){
            dataBinding.ivDelPhone.setVisibility(View.GONE);
        }else {
            dataBinding.ivDelPhone.setVisibility(View.VISIBLE);
        }
        customizeForm.setTel(tel);
    }

    public void contactsAfterTextChanged(Editable s) {
        String name=s.toString();
        if(TextUtils.isEmpty(name)){
            dataBinding.ivDelName.setVisibility(View.GONE);
        }else {
            dataBinding.ivDelName.setVisibility(View.VISIBLE);
        }
        customizeForm.setName(name);
    }
    public void remarkAfterTextChanged(Editable s) {
        String remark=s.toString();
        if(TextUtils.isEmpty(remark)){
            dataBinding.ivDelRemark.setVisibility(View.GONE);
            dataBinding.tvRemarkHint.setVisibility(View.VISIBLE);
        }else {
            dataBinding.ivDelRemark.setVisibility(View.VISIBLE);
            dataBinding.tvRemarkHint.setVisibility(View.GONE);
        }
        customizeForm.setRemark(remark);
    }

    public boolean check() {
        if(customizeForm.getDepartArea()==null||TextUtils.isEmpty(customizeForm.getDepartArea().getCityCode())){
            ToastUtils.showShort("请选择出发地");
            return false;
        }else if(customizeForm.getArrivalArea().size()==0){
            ToastUtils.showShort("请选择目的地");
            return false;
        }else if(TextUtils.isEmpty(customizeForm.getDepartDate())||TextUtils.isEmpty(customizeForm.getEndDate())){
            ToastUtils.showShort("请选择游玩时间段");
            return false;
        }else if(customizeForm.getCrNumber()<=0&&customizeForm.getXhNumber()<=0){
            ToastUtils.showShort("不能没有游玩人数");
            return false;
        }else if(customizeForm.getAccommodationType()<=0||customizeForm.getAccommodationLevel()<=0){
            ToastUtils.showShort("请选择住宿标准");
            return false;
        }else if(TextUtils.isEmpty(customizeForm.getName())){
            ToastUtils.showShort("请输入联系人姓名");
            return false;
        }else if(!MatcherUtils.isMobile(customizeForm.getTel())){
            ToastUtils.showShort("请输入有效联系电话");
            return false;
        }
        return true;
    }
    /**
     * 添加私人定制
     */
    public void addCustomize() {
        if(!check()){
            return;
        }
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.addNewCustomize(TourApplication.getToken(),customizeForm);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<Object> body) {
                        if(body.isSuccess()){
                            result.setValue(body);
                        }else {
                            mView.showLoadFail(body.getReMsg());
                        }
                    }
                });

    }
    public int intervalDays=8;//间隔天数
    public void initDateTime() {
        /*String month =TimeUtil.getLast12MonthsDate(0,"yyyy年MM月");
        int days =TimeUtil.getLastMonthsDay(0);//这个月又多少天
        int todayDay=TimeUtil.getTodayDay(0);//今天是几号
        if(days-todayDay>=8){

        }
        for(int i=1;i<=days;i++){
            int betweenIntervalDays=TimeUtil.getBetweenIntervalDays(startDate,date,"yyyy年MM月dd日");
            if(betweenIntervalDays>=0&&betweenIntervalDays<intervalDays){
                mSelectDateTime.setSelected(true);
                if(betweenIntervalDays==0){
                    mSelectDateTime.setFirstSelected(true);
                }
                if(betweenIntervalDays==intervalDays-1){
                    mSelectDateTime.setLastSelected(true);
                }
            }
            list.add(mSelectDateTime);
        }
        return list;*/
    }
}
