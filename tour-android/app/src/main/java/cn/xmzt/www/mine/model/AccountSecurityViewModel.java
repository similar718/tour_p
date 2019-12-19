package cn.xmzt.www.mine.model;

import android.content.Intent;
import androidx.databinding.ObservableBoolean;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.mine.activity.AccountSecurityActivity;
import cn.xmzt.www.mine.activity.BindContactActivity;
import cn.xmzt.www.mine.activity.BindPhoneActivity;
import cn.xmzt.www.mine.activity.ChangePassWordActivity;
import cn.xmzt.www.mine.activity.CloseAccountActivity;
import cn.xmzt.www.mine.activity.SettingPassWordActivity;
import cn.xmzt.www.mine.activity.SettingPayPwdActivity;
import cn.xmzt.www.mine.activity.VerificationPhoneActivity;
import cn.xmzt.www.mine.bean.AccountSecurityBean;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.ToastUtils;

/**
 * @author tanlei
 * @date 2019/8/9
 * @describe
 */

public class AccountSecurityViewModel extends BaseViewModel {
    public AccountSecurityActivity activity;
    /**
     * 是否绑定了手机
     */
    public ObservableBoolean bindPhone = new ObservableBoolean();
    /**
     * 是否绑定了社交账号
     */
    public ObservableBoolean bindWechat = new ObservableBoolean();
    /**
     * 是否设置了登录密码
     */
    public ObservableBoolean setLoginPsw = new ObservableBoolean();
    /**
     * 是否设置了支付密码
     */
    public ObservableBoolean setPayPsw = new ObservableBoolean();

    public void setActivity(AccountSecurityActivity activity) {
        this.activity = activity;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_iv:
                activity.finish();
                break;
            case R.id.bind_phone_rl://绑定或者修改手机号
                Intent intent = new Intent();
                if (bindPhone.get()) {//去修改
                    intent.setClass(activity, VerificationPhoneActivity.class);
                } else {//去绑定
                    intent.putExtra("tag", AccountSecurityActivity.TAG);
                    intent.setClass(activity, BindPhoneActivity.class);
                }
                activity.startActivity(intent);
                break;
            case R.id.bind_contact_rl://绑定社交账号
                activity.startActivity(new Intent(activity, BindContactActivity.class));
                break;
            case R.id.modification_login_pwd_rl://修改登录密码
                if (setLoginPsw.get()) {
                    activity.startActivity(new Intent(activity, ChangePassWordActivity.class));
                    activity.dataBinding.tvLoginPassword.setText("修改登录密码");
                } else {
                    activity.startActivity(new Intent(activity, SettingPassWordActivity.class));
                    activity.dataBinding.tvLoginPassword.setText("设置登录密码");
                }
                break;
            case R.id.modification_pay_pwd_rl://修改或设置支付密码
                if (setPayPsw.get()) {
                    activity.dataBinding.tvPayPassword.setText("修改支付密码");
                } else {
                    activity.dataBinding.tvPayPassword.setText("设置支付密码");
                }
                Intent payIntent = new Intent();
                payIntent.setClass(activity, SettingPayPwdActivity.class);
                payIntent.putExtra("setOrChange", setPayPsw.get());
                activity.startActivity(payIntent);
                break;
            case R.id.close_account_rl://注销
                activity.startActivity(new Intent(activity, CloseAccountActivity.class));
                break;
            default:
                break;
        }
    }

    /**
     * 查询正常用户的账号与安全（校验是否填写信息）
     */
    public void accountSecurity() {
        ApiRepertory.getInstance().getApiService().accountSecurity(TourApplication.getToken())
                .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<AccountSecurityBean>>() {
            @Override
            public void onNext(BaseDataBean<AccountSecurityBean> userInfoBean) {
                if (null != userInfoBean && null != userInfoBean.getRel()) {
                    if (userInfoBean.isSuccess()) {
                        bindPhone.set(userInfoBean.getRel().isBindTel());
                        bindWechat.set(userInfoBean.getRel().isBindWeChatAccount());
                        setLoginPsw.set(userInfoBean.getRel().isBindLoginPassword());
                        setPayPsw.set(userInfoBean.getRel().isSetPayPassword());
                        if (setLoginPsw.get()) {
                            activity.dataBinding.tvLoginPassword.setText("修改登录密码");
                        } else {
                            activity.dataBinding.tvLoginPassword.setText("设置登录密码");
                        }
                        if (setPayPsw.get()) {
                            activity.dataBinding.tvPayPassword.setText("修改支付密码");
                        } else {
                            activity.dataBinding.tvPayPassword.setText("设置支付密码");
                        }
                    } else {
                        ToastUtils.showText(activity, userInfoBean.getReMsg());
                    }
                }
            }
        });
    }
}
