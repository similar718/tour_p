package cn.xmzt.www.intercom.vm;

import android.text.Editable;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.roomdb.TourDatabase;
import cn.xmzt.www.roomdb.beans.GroupUserInfo;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;

import androidx.lifecycle.MutableLiveData;

import cn.xmzt.www.utils.MatcherUtils;
import io.reactivex.Observable;

/**
 * 添加车辆ViewModel
 */
public class AddCarViewModel extends BaseViewModel {
    public MutableLiveData<BaseDataBean<String>> result;
    public AddCarViewModel() {
        result = new MutableLiveData<BaseDataBean<String>>();
    }
    public String groupId;
    public int userId;
    public String licencePlate;

    public void carIDAfterTextChanged(Editable s) {
        licencePlate = s.toString();
    }
    public boolean check() {
        if(!MatcherUtils.isCarCard(licencePlate)){
            ToastUtils.showShort("请输入有效车牌号");
            return false;
        }
        if(userId<=0){
            ToastUtils.showShort("请输入选择司机");
            return false;
        }
        return true;
    }
    /**
     * 添加车辆
     */
    public void addDriver() {
        if(!check()){
            return;
        }
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.addDriver(groupId,licencePlate,userId);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<String>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<String> body) {
                        if(body.isSuccess()){
                            ToastUtils.showShort("新增车辆成功");
                            result.setValue(body);
                            saveDataBase(groupId, String.valueOf(userId),licencePlate);
                        }else {
                            mView.showLoadFail(body.getReMsg());
                        }
                    }
                });
    }

    /**
     * 将修改的车辆信息添加到数据库
     * @param groupId
     * @param userid
     * @param licencePlate
     */
    private void saveDataBase(String groupId,String userid,String licencePlate){
        GroupUserInfo info = TourDatabase.getDefault(ActivityUtils.getTopActivity()).getGroupUserInfoDao().getData(groupId,userid);
        if (info != null){
            info.numberPlate = licencePlate;
            TourDatabase.getDefault(ActivityUtils.getTopActivity()).getGroupUserInfoDao().update(info);
        }
    }
}
