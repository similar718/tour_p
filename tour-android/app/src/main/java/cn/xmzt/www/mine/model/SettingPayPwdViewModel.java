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
import cn.xmzt.www.mine.activity.SettingPayPwdActivity;
import cn.xmzt.www.mine.activity.VerificationSecurityPhoneActivity;
import cn.xmzt.www.mine.event.SecurityEvent;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.MatcherUtils;
import cn.xmzt.www.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.schedulers.Schedulers;

/**
 * @author tanlei
 * @date 2019/8/10
 * @describe
 */

public class SettingPayPwdViewModel extends BaseViewModel {
    private SettingPayPwdActivity activity;
    public ObservableBoolean setOrChange = new ObservableBoolean();
    public ObservableBoolean isGetCode = new ObservableBoolean();
    public ObservableBoolean isPay = new ObservableBoolean();
    public ObservableBoolean isAgain = new ObservableBoolean();
    public ObservableBoolean isCode = new ObservableBoolean();

    public void setActivity(SettingPayPwdActivity activity) {
        this.activity = activity;
    }

    public void setSetOrChange(boolean setOrChange) {
        this.setOrChange.set(setOrChange);
        if (TourApplication.getUser() != null) {
            if (TextUtils.isEmpty(TourApplication.getUser().getSafeTel())) {
                activity.dataBinding.tvChange.setVisibility(View.VISIBLE);
                if (TourApplication.getUser().getTel().contains("****")){
                    activity.dataBinding.etPayPhone.setText(TourApplication.getUser().getTel());
                } else {
                    String tel = TourApplication.getUser().getTel();
                    tel = tel.substring(0,3) + "****" + tel.substring(7,tel.length());
                    activity.dataBinding.etPayPhone.setText(tel);
                }
                setGetCode(TourApplication.getUser().getTel());
            } else {
                activity.dataBinding.tvChange.setVisibility(View.GONE);
                if (TourApplication.getUser().getSafeTel().contains("****")){
                    activity.dataBinding.etPayPhone.setText(TourApplication.getUser().getSafeTel());
                } else {
                    String tel = TourApplication.getUser().getSafeTel();
                    tel = tel.substring(0,3) + "****" + tel.substring(7,tel.length());
                    activity.dataBinding.etPayPhone.setText(tel);
                }
                setGetCode(TourApplication.getUser().getSafeTel());
            }
        }
    }

    private void settingPayPassword() {
        String tel = getTel();
        if (!MatcherUtils.isMobile(tel)) {
            ToastUtils.showText("手机号无效");
            return;
        }
        ApiRepertory.getInstance().getApiService().settingPayPassword(TourApplication.getToken(), tel,
                activity.dataBinding.etPayPassword.getText().toString(), activity.dataBinding.etPayCode.getText().toString())
                .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
            @Override
            public void onNext(BaseDataBean<Object> verificationCodeBean) {
                if (null != verificationCodeBean) {
                    if (verificationCodeBean.isSuccess()) {
                        TourApplication.getUser().setSafeTel(tel);
                        EventBus.getDefault().post(new SecurityEvent());
                        ToastUtils.showText(activity, "设置支付密码成功！！！");
                        activity.finish();
                    } else {
                        ToastUtils.showText(activity, verificationCodeBean.getReMsg());
                    }
                }

            }
        });
    }

    private void changePayPassword() {
        String tel = getTel();
        if (!MatcherUtils.isMobile(tel)) {
            ToastUtils.showText("手机号无效");
            return;
        }
        ApiRepertory.getInstance().getApiService().modifyPayPassword(TourApplication.getToken(), tel,
                activity.dataBinding.etPayPassword.getText().toString(), activity.dataBinding.etPayCode.getText().toString())
                .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
            @Override
            public void onNext(BaseDataBean<Object> verificationCodeBean) {
                if (null != verificationCodeBean) {
                    if (verificationCodeBean.isSuccess()) {
                        TourApplication.getUser().setSafeTel(tel);
                        ToastUtils.showText(activity, "修改支付密码成功！！！");
                        activity.finish();
                    } else {
                        ToastUtils.showText(activity, verificationCodeBean.getReMsg());
                    }
                }

            }
        });
    }
    private String getTel(){
        String tel=activity.dataBinding.etPayPhone.getText().toString();
        if(!activity.dataBinding.etPayPhone.isEnabled()){
            if (!TextUtils.isEmpty(TourApplication.getUser().getSafeTel())) {
                tel= TourApplication.getUser().getSafeTel();
            } else {
                tel= TourApplication.getUser().getTel();
            }
        }
        return tel;
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_iv://返回
                activity.finish();
                break;
            case R.id.tv_save://设置支付密码
                if (!activity.dataBinding.etPayPassword.getText().toString().equals(activity.dataBinding.etPayAgain.getText().toString())) {
                    ToastUtils.showText(activity, "两次密码输入不一致请重新输入！");
                } else {
                    if (setOrChange.get()) {
                        changePayPassword();
                    } else {
                        settingPayPassword();
                    }
                }
                break;
            case R.id.tv_replace://更换安全验证手机号
                activity.startActivity(new Intent(activity, VerificationSecurityPhoneActivity.class));
                break;
            case R.id.tv_change://修改
                if (TourApplication.getUser() != null) {
                    if (TextUtils.isEmpty(TourApplication.getUser().getSafeTel())) {
                        activity.dataBinding.etPayPhone.setEnabled(true);
                        activity.dataBinding.etPayPhone.setFocusable(true);
                        activity.dataBinding.etPayPhone.setText("");
                    }
                }
                break;
            case R.id.get_verification_code_tv://获取验证码
                String tel = getTel();
                if (!MatcherUtils.isMobile(tel)) {
                    ToastUtils.showText("手机号无效");
                    return;
                }
                ApiRepertory.getInstance().getApiService().getVerificationCode(tel)
                        .subscribeOn(Schedulers.io())
                        .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<Object> verificationCodeBean) {
                        if (null != verificationCodeBean) {
                            if (verificationCodeBean.isSuccess()) {
                                SettingPayPwdViewModel.TimeCount timeCount = new SettingPayPwdViewModel.TimeCount(60000, 1000);
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

    private void setGetCode(String s) {
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
     */
    public void onPhoneChanged(CharSequence s, int start, int before, int count) {
        setGetCode(s.toString());
    }

    public void onPayChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().length() > 5) {
//            //判断是否符合手机格式
//            if (MatcherUtils.isMobile(s.toString())) {
            isPay.set(true);
//            } else {
//                isGetCode.set(false);
//            }
        } else {
            isPay.set(false);
        }
    }

    public void onAgainChanged(CharSequence s, int start, int before, int count) {
        //判断是否为11位
        if (s.toString().length() > 5) {
//            //判断是否符合手机格式
//            if (MatcherUtils.isMobile(s.toString())) {
            isAgain.set(true);
//            } else {
//                isGetCode.set(false);
//            }
        } else {
            isAgain.set(false);
        }
    }

    public void onCodeChanged(CharSequence s, int start, int before, int count) {
        //判断是否为11位
        if (s.toString().length() > 5) {
//            //判断是否符合手机格式
//            if (MatcherUtils.isMobile(s.toString())) {
            isCode.set(true);
//            } else {
//                isGetCode.set(false);
//            }
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
