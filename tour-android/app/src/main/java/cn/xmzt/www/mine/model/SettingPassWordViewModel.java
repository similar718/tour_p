package cn.xmzt.www.mine.model;

import androidx.databinding.ObservableBoolean;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.mine.activity.SettingPassWordActivity;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.ToastUtils;

/**
 * @author tanlei
 * @date 2019/9/16
 * @describe
 */

public class SettingPassWordViewModel extends BaseViewModel {
    private SettingPassWordActivity activity;
    public ObservableBoolean isPassWord = new ObservableBoolean();
    public ObservableBoolean isAgain = new ObservableBoolean();

    public void setActivity(SettingPassWordActivity activity) {
        this.activity = activity;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_iv:
                activity.finish();
                break;
            case R.id.tv_setting://设置密码
                if (!activity.dataBinding.etPassword.getText().toString().equals(activity.dataBinding.etAgainPassword.getText().toString())) {
                    ToastUtils.showText(activity, "两次密码输入不一致请重新输入！");
                } else {
                    ApiRepertory.getInstance().getApiService().settingPassword(TourApplication.getToken(), activity.dataBinding.etPassword.getText().toString())
                            .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                        @Override
                        public void onNext(BaseDataBean<Object> verificationCodeBean) {
                            if (null != verificationCodeBean) {
                                if (verificationCodeBean.isSuccess()) {
                                    activity.finish();
                                } else {
                                    ToastUtils.showText(activity, verificationCodeBean.getReMsg());
                                }
                            }
                        }
                    });
                }
                break;
            default:
                break;
        }
    }

    /**
     * 第一次密码输入的监听
     *
     * @param s
     * @param start
     * @param before
     * @param count
     */
    public void onOldPassword(CharSequence s, int start, int before, int count) {
        //判断是否为大于6位
        if (s.toString().length() > 5) {
            isPassWord.set(true);
        } else {
            isPassWord.set(false);
        }
    }

    /**
     * 新密码输入的监听
     *
     * @param s
     * @param start
     * @param before
     * @param count
     */
    public void onNewPassword(CharSequence s, int start, int before, int count) {
        //判断是否为大于6位
        if (s.toString().length() > 5) {
            isAgain.set(true);
        } else {
            isAgain.set(false);
        }
    }
}
