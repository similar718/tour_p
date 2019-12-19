package cn.xmzt.www.mine.model;

import androidx.databinding.ObservableBoolean;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.mine.activity.ChangePassWordActivity;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.ToastUtils;

/**
 * @author tanlei
 * @date 2019/8/10
 * @describe
 */

public class ChangePassWordViewModel extends BaseViewModel {
    private ChangePassWordActivity activity;
    public ObservableBoolean isOld = new ObservableBoolean();
    public ObservableBoolean isNew = new ObservableBoolean();
    public ObservableBoolean isAgain = new ObservableBoolean();

    public void setActivity(ChangePassWordActivity activity) {
        this.activity = activity;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_iv:
                activity.finish();
                break;
            case R.id.tv_change://修改密码
                if (!activity.dataBinding.etNewPassword.getText().toString().equals(activity.dataBinding.etAgainPassword.getText().toString())) {
                    ToastUtils.showText(activity, "两次密码输入不一致请重新输入！");
                } else {
                    ApiRepertory.getInstance().getApiService().modifyPassword(TourApplication.getToken(),
                            activity.dataBinding.etOldPassword.getText().toString(), activity.dataBinding.etNewPassword.getText().toString())
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
     * 旧密码输入的监听
     *
     * @param s
     * @param start
     * @param before
     * @param count
     */
    public void onOldPassword(CharSequence s, int start, int before, int count) {
        //判断是否为大于6位
        if (s.toString().length() > 5) {
            isOld.set(true);
        } else {
            isOld.set(false);
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
            isNew.set(true);
        } else {
            isNew.set(false);
        }
    }

    /**
     * 确认密码输入的监听
     *
     * @param s
     * @param start
     * @param before
     * @param count
     */
    public void onAgainPassword(CharSequence s, int start, int before, int count) {
        //判断是否为大于6位
        if (s.toString().length() > 5) {
            isAgain.set(true);
        } else {
            isAgain.set(false);
        }
    }
}
