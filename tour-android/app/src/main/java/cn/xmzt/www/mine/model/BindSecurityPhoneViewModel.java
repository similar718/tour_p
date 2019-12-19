package cn.xmzt.www.mine.model;

import androidx.databinding.ObservableBoolean;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.mine.activity.BindSecurityPhoneActivity;
import cn.xmzt.www.mine.activity.VerificationSecurityPhoneActivity;
import cn.xmzt.www.mine.event.ChangeSecurityEvent;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.schedulers.Schedulers;

/**
 * @author tanlei
 * @date 2019/8/10
 * @describe
 */

public class BindSecurityPhoneViewModel extends BaseViewModel {
    private BindSecurityPhoneActivity activity;
    private String tag;

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setActivity(BindSecurityPhoneActivity activity) {
        this.activity = activity;
    }

    /**
     * 是否可以获取验证码
     */
    public ObservableBoolean isGetCode = new ObservableBoolean();
    /**
     * 判断验证码格式是否正确
     */
    public ObservableBoolean isCode = new ObservableBoolean();

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

    /**
     * 修改绑定安全手机号
     */
    private void changeBindPhone(String operationToken) {
        ApiRepertory.getInstance().getApiService().safePhoneChange(operationToken,
                activity.dataBinding.etPhone.getText().toString(), activity.dataBinding.etCode.getText().toString())
                .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
            @Override
            public void onNext(BaseDataBean<Object> verificationCodeBean) {
                if (null != verificationCodeBean) {
                    if (verificationCodeBean.isSuccess()) {
                        for (int i = 0; i < activity.list.size(); i++) {
                            if (activity.list.get(i) instanceof VerificationSecurityPhoneActivity) {
                                activity.list.get(i).finish();
                            }
                        }
                        EventBus.getDefault().post(new ChangeSecurityEvent(activity.dataBinding.etPhone.getText().toString()));
                        TourApplication.getUser().setSafeTel(activity.dataBinding.etPhone.getText().toString());
                        activity.finish();
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
                changeBindPhone(tag);
                break;
            case R.id.get_verification_code_tv://获取验证码
                ApiRepertory.getInstance().getApiService().getVerificationCode(activity.dataBinding.etPhone.getText().toString())
                        .subscribeOn(Schedulers.io())
                        .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<Object> verificationCodeBean) {
                        if (null != verificationCodeBean) {
                            if (verificationCodeBean.isSuccess()) {
                                BindSecurityPhoneViewModel.TimeCount timeCount = new BindSecurityPhoneViewModel.TimeCount(60000, 1000);
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
