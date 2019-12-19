package cn.xmzt.www.mine.model;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.dialog.TextTitleDialog;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.mine.activity.LoginActivity;
import cn.xmzt.www.mine.activity.WxBindPhoneActivity;
import cn.xmzt.www.mine.bean.WxRegisterBean;
import cn.xmzt.www.mine.event.LoginEvent;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.SPUtils;
import cn.xmzt.www.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import androidx.databinding.ObservableBoolean;

import io.reactivex.schedulers.Schedulers;

/**
 * @author tanlei
 * @date 2019/8/30
 * @describe
 */

public class WxBindPhoneViewModel extends BaseViewModel {
    private WxBindPhoneActivity activity;
    /**
     * 是否可以获取验证码
     */
    public ObservableBoolean isGetCode = new ObservableBoolean();
    /**
     * 判断验证码格式是否正确
     */
    public ObservableBoolean isCode = new ObservableBoolean();
    private WxRegisterBean bean;

    public void setBean(WxRegisterBean bean) {
        this.bean = bean;
    }

    public void setActivity(WxBindPhoneActivity activity) {
        this.activity = activity;
    }

    private boolean isForce = false;

    /**
     * 绑定手机号
     */
    private void bindPhone(boolean isForce) {
        ApiRepertory.getInstance().getApiService().bindPhone(bean.getToken(),
                activity.dataBinding.etPhone.getText().toString(), activity.dataBinding.etCode.getText().toString(), isForce)
                .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
            @Override
            public void onNext(BaseDataBean<Object> verificationCodeBean) {
                if (null != verificationCodeBean) {
                    if (verificationCodeBean.isSuccess()) {
                        TourApplication.setToken(bean.getToken());
                        SPUtils.setToken(bean.getToken());
                        TourApplication.isLogin = true;
                        EventBus.getDefault().post(new LoginEvent());
                        for (int i = 0; i < activity.list.size(); i++) {
                            if (activity.list.get(i) instanceof WxBindPhoneActivity || activity.list.get(i) instanceof LoginActivity) {
                                activity.list.get(i).finish();
                            }
                        }
                    } else if (verificationCodeBean.getReCode().equals(BaseDataBean.CODE_UNBIND)) {//该手机号已绑定了其他微信账号，是否确认继续绑定新的微信账号
                        TextTitleDialog dialog = new TextTitleDialog(activity, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                WxBindPhoneViewModel.this.isForce = true;
                                bindPhone(isForce);
                            }
                        });
                        dialog.setTitle("该手机号已绑定了其他微信账号，是否确认继续绑定新的微信账号");
                        dialog.show();
                    } else {
                        ToastUtils.showText(activity, verificationCodeBean.getReMsg());
                    }
                }

            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_iv://返回
                activity.finish();
                break;
            case R.id.tv_bind://绑定
                bindPhone(isForce);
                break;
            case R.id.get_verification_code_tv://获取验证码
                ApiRepertory.getInstance().getApiService().getVerificationCode(activity.dataBinding.etPhone.getText().toString())
                        .subscribeOn(Schedulers.io())
                        .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<Object> verificationCodeBean) {
                        if (null != verificationCodeBean) {
                            if (verificationCodeBean.isSuccess()) {
                                WxBindPhoneViewModel.TimeCount timeCount = new WxBindPhoneViewModel.TimeCount(60000, 1000);
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
