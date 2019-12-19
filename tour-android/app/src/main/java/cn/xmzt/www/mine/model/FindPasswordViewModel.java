package cn.xmzt.www.mine.model;

import androidx.databinding.ObservableBoolean;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.InputType;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.mine.activity.FindPasswordActivity;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.MatcherUtils;
import cn.xmzt.www.utils.ToastUtils;

/**
 * @author tanlei
 * @date 2019/8/6
 * @describe
 */

public class FindPasswordViewModel extends BaseViewModel {
    public FindPasswordActivity activity;
    /**
     * 是否可以发送验证码
     */
    public ObservableBoolean isGetCode = new ObservableBoolean(false);
    /**
     * 验证码是否符合格式
     */
    public ObservableBoolean isCode = new ObservableBoolean(false);
    /**
     * 新密码是否符合格式
     */
    public ObservableBoolean isNewPassWord = new ObservableBoolean();
    /**
     * 确认密码是否符合格式
     */
    public ObservableBoolean isConfirmPassWord = new ObservableBoolean();
    /**
     * 第一次密码是否为可见状态
     */
    public ObservableBoolean isFirstEye = new ObservableBoolean();
    /**
     * 第二次密码是否为可见状态
     */
    public ObservableBoolean isSecondEye = new ObservableBoolean();

    public void setActivity(FindPasswordActivity activity) {
        this.activity = activity;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_verification_code_tv://获取验证码
                ApiRepertory.getInstance().getApiService().getVerificationCode(activity.dataBinding.accountEt.getText().toString())
                        .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<Object> verificationCodeBean) {
                        if (null != verificationCodeBean) {
                            if (verificationCodeBean.isSuccess()) {
                                FindPasswordViewModel.TimeCount timeCount = new FindPasswordViewModel.TimeCount(60000, 1000);
                                timeCount.start();
                            } else {
                                ToastUtils.showText(activity, verificationCodeBean.getReMsg());
                            }
                        }
                    }
                });
                break;
            case R.id.confirm_tv://确认
                if (!activity.dataBinding.newPasswordEt.getText().toString().equals(activity.dataBinding.confirmPasswordEt.getText().toString())) {
                    ToastUtils.showText(activity, "两次输入的密码不一致");
                    return;
                }
                ApiRepertory.getInstance().getApiService().findPassword(activity.dataBinding.accountEt.getText().toString(),
                        activity.dataBinding.confirmPasswordEt.getText().toString(),
                        activity.dataBinding.verificationCodeEt.getText().toString())
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
                break;
            case R.id.view_new_password_iv://切换第一遍密码是否为可见状态
                isFirstEye.set(!isFirstEye.get());
                if (isFirstEye.get()) {
                    activity.dataBinding.newPasswordEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    activity.dataBinding.newPasswordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;
            case R.id.view_confirm_password_iv://切换第二遍密码是否为可见状态
                isSecondEye.set(!isSecondEye.get());
                if (isSecondEye.get()) {
                    activity.dataBinding.confirmPasswordEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    activity.dataBinding.confirmPasswordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;
            case R.id.title_back_iv:
                activity.finish();
                break;
            default:
                break;
        }
    }

    /**
     * 手机号输入的监听
     *
     * @param s
     * @param start
     * @param before
     * @param count
     */
    public void onPhoneChanged(CharSequence s, int start, int before, int count) {
        //判断是否为11位
        if (s.toString().length() == 11) {
            //判断是否符合手机格式
            if (MatcherUtils.isMobile(s.toString())) {
                isGetCode.set(true);
            } else {
                isGetCode.set(false);
            }
        } else {
            isGetCode.set(false);
        }
    }

    /**
     * 验证码输入的监听
     *
     * @param s
     * @param start
     * @param before
     * @param count
     */
    public void onCodeChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().length() == 6) {
            isCode.set(true);
        } else {
            isCode.set(false);
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
    public void onNewPswChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().length() > 5) {
            isNewPassWord.set(true);
        } else {
            isNewPassWord.set(false);
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
    public void onConfirmPswChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().length() > 5) {
            isConfirmPassWord.set(true);
        } else {
            isConfirmPassWord.set(false);
        }
    }

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            activity.dataBinding.getVerificationCodeTv.setTextColor(Color.parseColor("#8824A4FF"));
            activity.dataBinding.getVerificationCodeTv.setClickable(false);
            activity.dataBinding.getVerificationCodeTv.setText(millisUntilFinished / 1000 + " s后重新发送");
        }

        @Override
        public void onFinish() {
            activity.dataBinding.getVerificationCodeTv.setText("重新获取验证码");
            activity.dataBinding.getVerificationCodeTv.setClickable(true);
            activity.dataBinding.getVerificationCodeTv.setTextColor(Color.parseColor("#24A4FF"));

        }
    }
}
