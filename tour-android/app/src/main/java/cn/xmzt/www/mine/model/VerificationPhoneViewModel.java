package cn.xmzt.www.mine.model;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;

import androidx.databinding.ObservableBoolean;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.mine.activity.BindPhoneActivity;
import cn.xmzt.www.mine.activity.PhoneLoseActivity;
import cn.xmzt.www.mine.activity.VerificationPhoneActivity;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.MatcherUtils;
import cn.xmzt.www.utils.ToastUtils;

/**
 * @author tanlei
 * @date 2019/8/10
 * @describe
 */

public class VerificationPhoneViewModel extends BaseViewModel {
    private VerificationPhoneActivity activity;
    /**
     * 是否可以获取验证码
     */
    public ObservableBoolean isGetCode = new ObservableBoolean();
    /**
     * 判断验证码格式是否正确
     */
    public ObservableBoolean isCode = new ObservableBoolean();

    public void setActivity(VerificationPhoneActivity activity) {
        this.activity = activity;
        if (TourApplication.getUser() != null) {
            if (!TextUtils.isEmpty(TourApplication.getUser().getTel())) {
                if (TourApplication.getUser().getTel().contains("****")){
                    activity.dataBinding.etPhone.setText(TourApplication.getUser().getTel());
                } else {
                    String tel = TourApplication.getUser().getTel();
                    tel = tel.substring(0,3) + "****" + tel.substring(7,tel.length());
                    activity.dataBinding.etPhone.setText(tel);
                }
                activity.dataBinding.etPhone.setEnabled(false);
                getCode(TourApplication.getUser().getTel());
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
    private String getTel(){
        String tel=activity.dataBinding.etPhone.getText().toString();
        if(!activity.dataBinding.etPhone.isEnabled()){
            tel= TourApplication.getUser().getTel();
        }
        return tel;
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_iv://返回
                activity.finish();
                break;
            case R.id.tv_next: {
                //下一步(验证原手机号)
                String tel = getTel();
                if (!MatcherUtils.isMobile(tel)) {
                    ToastUtils.showText("手机号无效");
                    return;
                }
                ApiRepertory.getInstance().getApiService().verificationPhone(tel, activity.dataBinding.etCode.getText().toString())
                        .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<Object> verificationCodeBean) {
                        if (null != verificationCodeBean) {
                            if (verificationCodeBean.isSuccess()) {
                                if (verificationCodeBean.getRel() instanceof String) {
                                    Intent intent = new Intent(activity, BindPhoneActivity.class);
                                    intent.putExtra("tag", (String) verificationCodeBean.getRel());
                                    activity.startActivity(intent);
                                }
                            } else {
                                ToastUtils.showText(activity, verificationCodeBean.getReMsg());
                            }
                        }

                    }
                });
                break;
            }
            case R.id.get_verification_code_tv://获取验证码
                String tel = getTel();
                if (!MatcherUtils.isMobile(tel)) {
                    ToastUtils.showText("手机号无效");
                    return;
                }
                ApiRepertory.getInstance().getApiService().getVerificationCode(tel)
                        .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<Object> verificationCodeBean) {
                        if (null != verificationCodeBean) {
                            if (verificationCodeBean.isSuccess()) {
                                VerificationPhoneViewModel.TimeCount timeCount = new VerificationPhoneViewModel.TimeCount(60000, 1000);
                                timeCount.start();
                            } else {
                                ToastUtils.showText(activity, verificationCodeBean.getReMsg());
                            }
                        }
                    }
                });
                break;
            case R.id.tv:
                activity.startActivity(new Intent(activity, PhoneLoseActivity.class));
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
