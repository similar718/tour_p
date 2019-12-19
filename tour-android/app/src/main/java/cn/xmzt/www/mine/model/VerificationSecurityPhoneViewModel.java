package cn.xmzt.www.mine.model;

import android.content.Intent;
import androidx.databinding.ObservableBoolean;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.mine.activity.BindSecurityPhoneActivity;
import cn.xmzt.www.mine.activity.PhoneSecurityLoseActivity;
import cn.xmzt.www.mine.activity.VerificationSecurityPhoneActivity;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.ToastUtils;

import io.reactivex.schedulers.Schedulers;

/**
 * @author tanlei
 * @date 2019/8/10
 * @describe
 */

public class VerificationSecurityPhoneViewModel extends BaseViewModel {
    private VerificationSecurityPhoneActivity activity;
    /**
     * 是否可以获取验证码
     */
    public ObservableBoolean isGetCode = new ObservableBoolean();
    /**
     * 判断验证码格式是否正确
     */
    public ObservableBoolean isCode = new ObservableBoolean();

    public void setActivity(VerificationSecurityPhoneActivity activity) {
        this.activity = activity;
        if (TourApplication.getUser() != null) {
            if (!TextUtils.isEmpty(TourApplication.getUser().getSafeTel())) {
                activity.dataBinding.etPhone.setText(TourApplication.getUser().getSafeTel());
                activity.dataBinding.etPhone.setEnabled(false);
                getCode(TourApplication.getUser().getSafeTel());
            }
        }
    }

    private void getCode(String s) {
        //判断是否为11位
        if (s.length() == 11) {
//            //判断是否符合手机格式
//            if (MatcherUtils.isMobile(s.toString())) {
            isGetCode.set(true);
//            } else {
//                isGetCode.set(false);
//            }
        } else {
            isGetCode.set(false);
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
        getCode(s.toString());
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

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_iv://返回
                activity.finish();
                break;
            case R.id.tv_next://下一步(验证原手机号)
                ApiRepertory.getInstance().getApiService().safePhoneSmsCheck(activity.dataBinding.etCode.getText().toString())
                        .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<String>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<String> verificationCodeBean) {
                        if (null != verificationCodeBean) {
                            if (verificationCodeBean.isSuccess()) {
                                if (verificationCodeBean.getRel() instanceof String) {
                                    Intent intent = new Intent(activity, BindSecurityPhoneActivity.class);
                                    intent.putExtra("tag", verificationCodeBean.getRel());
                                    activity.startActivity(intent);
                                }
                            } else {
                                ToastUtils.showText(activity, verificationCodeBean.getReMsg());
                            }
                        }

                    }
                });
                break;
            case R.id.get_verification_code_tv://获取验证码
                ApiRepertory.getInstance().getApiService().getSafePhoneVerificationCode().subscribeOn(Schedulers.io())
                        .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<String>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<String> verificationCodeBean) {
                        if (null != verificationCodeBean) {
                            if (verificationCodeBean.isSuccess()) {
                                VerificationSecurityPhoneViewModel.TimeCount timeCount = new VerificationSecurityPhoneViewModel.TimeCount(60000, 1000);
                                timeCount.start();
                            } else {
                                ToastUtils.showText(activity, verificationCodeBean.getReMsg());
                            }
                        }
                    }
                });
                break;
            case R.id.tv:
                activity.startActivity(new Intent(activity, PhoneSecurityLoseActivity.class));
                break;
            default:
                break;
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
