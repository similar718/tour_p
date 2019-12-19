package cn.xmzt.www.mine.model;

import android.content.Intent;
import androidx.databinding.ObservableBoolean;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.dialog.LogoffConfirmDialog;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.main.activity.MainActivity;
import cn.xmzt.www.mine.activity.VerifyActivity;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.SPUtils;
import cn.xmzt.www.utils.ToastUtils;

import io.reactivex.schedulers.Schedulers;

/**
 * @author tanlei
 * @date 2019/8/12
 * @describe
 */

public class VerifyViewModel extends BaseViewModel {
    private VerifyActivity activity;
    /**
     * 是否可以发送验证码
     */
    public ObservableBoolean isGetCode = new ObservableBoolean(false);
    /**
     * 判断验证码是否符合规则
     */
    public ObservableBoolean isCode = new ObservableBoolean(false);
    private LogoffConfirmDialog dialog;

    public void setActivity(VerifyActivity activity) {
        this.activity = activity;
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
            case R.id.logout_tv://注销
                dialog = new LogoffConfirmDialog(activity, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (view.getId() == R.id.tv_confirm) {
                            ApiRepertory.getInstance().getApiService().logout(TourApplication.getToken(), activity.dataBinding.phoneEt.getText().toString(),
                                    activity.dataBinding.verificationCodeEt.getText().toString())
                                    .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                                @Override
                                public void onNext(BaseDataBean<Object> verificationCodeBean) {
                                    if (null != verificationCodeBean) {
                                        if (verificationCodeBean.isSuccess()) {
                                            dialog.dismiss();
                                            TourApplication.setToken("");
                                            SPUtils.setToken("");
                                            activity.finish();
                                            Intent intent = new Intent(activity, MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            activity.startActivity(intent);
                                        } else {
                                            ToastUtils.showText(activity, verificationCodeBean.getReMsg());
                                        }
                                    }

                                }
                            });
                        }
                    }
                });
                dialog.show();
                break;

            case R.id.get_verification_code_tv://获取验证码
                ApiRepertory.getInstance().getApiService().getVerificationCode(activity.dataBinding.phoneEt.getText().toString())
                        .subscribeOn(Schedulers.io())
                        .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<Object> verificationCodeBean) {
                        if (null != verificationCodeBean) {
                            if (verificationCodeBean.isSuccess()) {
                                VerifyViewModel.TimeCount timeCount = new VerifyViewModel.TimeCount(60000, 1000);
                                timeCount.start();
                            } else {
                                ToastUtils.showText(activity, verificationCodeBean.getReMsg());
                            }
                        }
                    }
                });
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
