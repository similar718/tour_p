package cn.xmzt.www.smartteam.vm;

import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;

import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.roomdb.TourDatabase;
import cn.xmzt.www.roomdb.beans.GroupUserInfo;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.MatcherUtils;
import io.reactivex.Observable;

/**
 * 智能组队添加车辆ViewModel
 */
public class SmartTeamAddCarViewModel extends BaseViewModel {
    public MutableLiveData<BaseDataBean<String>> result;
    public SmartTeamAddCarViewModel() {
        result = new MutableLiveData<BaseDataBean<String>>();
    }
    public String groupId;
    public String licencePlate;

    public boolean check() {
        if(!MatcherUtils.isCarCard(licencePlate)){
            ToastUtils.showShort("请输入有效车牌号");
            return false;
        }
        return true;
    }

    /**
     * 智能组队-添加,修改,删除车辆
     * @param addOrUpdtOrDel 添加传：1,修改传：2，删除传：3
     */
    public void smartTeamAddOrUpdtOrDelDriver(int addOrUpdtOrDel ) {
        if(!check()){
            return;
        }
        ApiService mService = ApiRepertory.getInstance().getApiService();
        Observable mObservable = mService.smartTeamAddOrUpdtOrDelDriver(TourApplication.getToken(),groupId,addOrUpdtOrDel,licencePlate);
        mObservable.compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<String>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<String> body) {
                        if(body.isSuccess()){
                            if(addOrUpdtOrDel==1){
                                ToastUtils.showShort("新增车辆成功");
                                Constants.mMyCarInfo = licencePlate;
                            }else if(addOrUpdtOrDel==2){
                                ToastUtils.showShort("修改车辆成功");
                                Constants.mMyCarInfo = licencePlate;
                            }else if(addOrUpdtOrDel==3){
                                ToastUtils.showShort("删除车辆成功");
                                Constants.mMyCarInfo = "";
                            }
                            saveDataBase(groupId,Constants.mMyCarInfo);
                            result.setValue(body);
                        }else {
                            mView.showLoadFail(body.getReMsg());
                        }
                    }
                });
    }

    private void saveDataBase(String groupId,String licencePlate){
        GroupUserInfo info = TourDatabase.getDefault(ActivityUtils.getTopActivity()).getGroupUserInfoDao().getData(groupId, String.valueOf(TourApplication.getUser().getUserId()));
        if (info != null){
            info.numberPlate = licencePlate;
            TourDatabase.getDefault(ActivityUtils.getTopActivity()).getGroupUserInfoDao().update(info);
        }
    }
}
