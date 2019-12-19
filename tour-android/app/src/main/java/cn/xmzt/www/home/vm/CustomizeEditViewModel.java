package cn.xmzt.www.home.vm;

import android.text.Editable;
import android.text.TextUtils;
import android.view.View;

import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.databinding.ActivityCustomizeEditBinding;
import cn.xmzt.www.home.bean.CustomizeForm;
import cn.xmzt.www.home.event.CustomizeRefreshBus;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.MatcherUtils;
import io.reactivex.Observable;

/**
 * 私人定制编辑ViewModel
 */
public class CustomizeEditViewModel extends BaseViewModel {
    public MutableLiveData<BaseDataBean<Object>> result;
    public ActivityCustomizeEditBinding dataBinding;
    public CustomizeEditViewModel(ActivityCustomizeEditBinding dataBinding) {
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
     * 修改私人定制
     */
    public void modifyCustomize() {
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
                            EventBus.getDefault().post(new CustomizeRefreshBus(CustomizeRefreshBus.TYPE_REFRESH));//提示上一个页面刷新
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
        int customizeId=0;
        Long customizeLongId=customizeForm.getId();
        if(customizeLongId!=null){
            customizeId=customizeLongId.intValue();
        }
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

