package cn.xmzt.www.route.vm;

import androidx.lifecycle.MutableLiveData;
import androidx.databinding.ObservableInt;
import android.text.Editable;
import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.MatcherUtils;

import io.reactivex.Observable;

/**
 * 路线填写发票ViewModel
 */
public class AddInvoiceTitleViewModel extends BaseViewModel {
    public MutableLiveData<BaseDataBean<String>> result;
    public AddInvoiceTitleViewModel() {
        result = new MutableLiveData<BaseDataBean<String>>();
    }
    /**
     * 抬头类型(1:公司,2:个人)
     */
    public ObservableInt selected= new ObservableInt(1);
    public String typeCompany= "公司";
    public String typePerson = "个人";
    public String companyName;
    public String dutyId;
    public String address;
    public String phone;
    public String depositBank;
    public String bankAccount;

    public void companyAfterTextChanged(Editable s) {
        companyName=s.toString();
    }
    public void dutyIdAfterTextChanged(Editable s) {
        dutyId=s.toString();
    }
    public void addressAfterTextChanged(Editable s) {
        address=s.toString();
    }
    public void phoneAfterTextChanged(Editable s) {
        phone=s.toString();
    }
    public void depositBankAfterTextChanged(Editable s) {
        depositBank=s.toString();
    }
    public void bankAccountAfterTextChanged(Editable s) {
        bankAccount=s.toString();
    }
    public boolean check() {
        if(TextUtils.isEmpty(companyName)){
            if(selected.get()==2){
                ToastUtils.showShort("请输入个人姓名");
            }else {
                ToastUtils.showShort("请输入公司名称，必填");
            }
            return false;
        }else if(selected.get()==1&&!MatcherUtils.isTaxNo(dutyId)){
            ToastUtils.showShort("请输入有效纳税人识别号，必填");
            return false;
        }
        if(!TextUtils.isEmpty(phone)){
            if(!MatcherUtils.isMobile(phone)){
                ToastUtils.showShort("请输入有效电话或者不填");
                return false;
            }
        }
        return true;
    }
    /**
     * 添加发票抬头
     */
    public void addInvoiceTitle() {
        if(!check()){
            return;
        }
        String dutyNO="";
        if(selected.get()==1){
            dutyNO=dutyId.toUpperCase();
        }
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.addInvoiceTitle(bankAccount,depositBank,dutyNO,address,phone,companyName,selected.get(), TourApplication.getToken());
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<String>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<String> body) {
                        if(body.isSuccess()){
                            ToastUtils.showShort("新增成功");
                            result.setValue(body);
                        }else {
                            mView.showLoadFail(null);
                        }
                    }
                });

    }
    /**
     * 更新发票抬头
     */
    public void updateInvoiceTitle(int id) {
        if(!check()){
            return;
        }
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.updateInvoiceTitle(bankAccount,depositBank,dutyId.toUpperCase(),id,address,phone,companyName,selected.get(), TourApplication.getToken());
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<String>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<String> body) {
                        if(body.isSuccess()){
                            ToastUtils.showShort("编辑成功");
                            result.setValue(body);
                        }else {
                            mView.showLoadFail(null);
                        }
                    }
                });

    }
    /**
     * 删除发票抬头
     */
    public void delInvoiceTitle(int id) {
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.deleteInvoiceTitle(id,TourApplication.getToken());
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<String>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<String> body) {
                        if(body.isSuccess()){
                            ToastUtils.showShort("删除成功");
                            result.setValue(body);
                        }else {
                            mView.showLoadFail(null);
                        }
                    }
                });

    }
}
