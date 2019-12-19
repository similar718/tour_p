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
import cn.xmzt.www.mine.activity.BindALiPayActivity;
import cn.xmzt.www.mine.activity.VerificationSecurityPhoneActivity;
import cn.xmzt.www.mine.bean.CashWithdrawalBean;
import cn.xmzt.www.intercom.bean.UserBasicInfoBean;
import cn.xmzt.www.mine.event.ALiPayEvent;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.schedulers.Schedulers;

/**
 * @author tanlei
 * @date 2019/8/15
 * @describe
 */

public class BindALiPayViewModel extends BaseViewModel {
    private BindALiPayActivity activity;
    private CashWithdrawalBean.SysUserExtractionAccountEntity entity;
    /**
     * 阿里账号是否符合格式
     */
    public ObservableBoolean isAli = new ObservableBoolean();
    public ObservableBoolean isName = new ObservableBoolean();
    public ObservableBoolean isPhone = new ObservableBoolean();
    public ObservableBoolean isCode = new ObservableBoolean();

    public void setEntity(CashWithdrawalBean.SysUserExtractionAccountEntity entity) {
        this.entity = entity;
        if (entity.getId() != 0) {//修改
            activity.dataBinding.etAli.setText(entity.getAccount());
            activity.dataBinding.etName.setText(entity.getRealName());
            activity.dataBinding.etPayPhone.setText(entity.getExtractTel());
            setAli(entity.getAccount());
            setName(entity.getRealName());
            setPhone(entity.getExtractTel());
            activity.dataBinding.titleNameTv.setText("更改支付宝账号");
            activity.dataBinding.tvBind.setText("确认绑定");
            activity.dataBinding.tvPhoneText.setVisibility(View.VISIBLE);
            activity.dataBinding.tvChange.setVisibility(View.GONE);
        } else {//添加
            activity.dataBinding.titleNameTv.setText("立即绑定");
            activity.dataBinding.tvBind.setText("立即绑定");
            activity.dataBinding.tvPhoneText.setVisibility(View.GONE);
            activity.dataBinding.tvChange.setVisibility(View.VISIBLE);
        }
    }

    public void setActivity(BindALiPayActivity activity) {
        this.activity = activity;
        UserBasicInfoBean userBasicInfoBean = TourApplication.getUser();
        if (userBasicInfoBean != null) {
            if (TextUtils.isEmpty(userBasicInfoBean.getSafeTel())) {//安全手机号为空
                activity.dataBinding.etPayPhone.setText(userBasicInfoBean.getTel());
                activity.dataBinding.tvChange.setVisibility(View.VISIBLE);
                activity.dataBinding.tvPhoneText.setVisibility(View.GONE);
                activity.dataBinding.etPayPhone.setEnabled(false);
                setPhone(userBasicInfoBean.getTel());
            } else {//安全手机号不为空
                activity.dataBinding.etPayPhone.setText(userBasicInfoBean.getSafeTel());
                activity.dataBinding.tvChange.setVisibility(View.GONE);
                activity.dataBinding.tvPhoneText.setVisibility(View.VISIBLE);
                activity.dataBinding.etPayPhone.setEnabled(false);
                setPhone(userBasicInfoBean.getSafeTel());
            }
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_iv:
                activity.finish();
                break;
            case R.id.tv_change:
                if (TourApplication.getUser() != null) {
                    if (TextUtils.isEmpty(TourApplication.getUser().getSafeTel())) {
                        activity.dataBinding.etPayPhone.setEnabled(true);
                        activity.dataBinding.etPayPhone.setFocusable(true);
                        activity.dataBinding.etPayPhone.setText("");
                    }
                }
                break;
            case R.id.tv_phone_text:
                activity.startActivity(new Intent(activity, VerificationSecurityPhoneActivity.class));
                break;
            case R.id.get_verification_code_tv:
                ApiRepertory.getInstance().getApiService().getVerificationCode(activity.dataBinding.etPayPhone.getText().toString())
                        .subscribeOn(Schedulers.io())
                        .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<Object> verificationCodeBean) {
                        if (null != verificationCodeBean) {
                            if (verificationCodeBean.isSuccess()) {
                                BindALiPayViewModel.TimeCount timeCount = new BindALiPayViewModel.TimeCount(60000, 1000);
                                timeCount.start();
                            } else {
                                ToastUtils.showText(activity, verificationCodeBean.getReMsg());
                            }
                        }
                    }
                });
                break;
            case R.id.tv_bind://修改或绑定支付宝账号
                if (entity.getId() == 0) {
                    addCashAccount();
                } else {
                    updateCashAccount();
                }
                break;
        }
    }

    /**
     * 修改提现账号
     */
    private void updateCashAccount() {
        ApiRepertory.getInstance().getApiService().updateCashAccount(TourApplication.getToken(), activity.dataBinding.etAli.getText().toString(),
                activity.dataBinding.etPayPhone.getText().toString(), entity.getId(), activity.dataBinding.etName.getText().toString(),
                2, activity.dataBinding.etPayCode.getText().toString())
                .subscribeOn(Schedulers.io())
                .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
            @Override
            public void onNext(BaseDataBean<Object> verificationCodeBean) {
                if (null != verificationCodeBean) {
                    if (verificationCodeBean.isSuccess()) {
                        EventBus.getDefault().post(new ALiPayEvent());
                        TourApplication.getUser().setTel(activity.dataBinding.etPayPhone.getText().toString());
                        activity.finish();
                    } else {
                        ToastUtils.showText(activity, verificationCodeBean.getReMsg());
                    }
                }
            }
        });
    }

    /**
     * 添加提现账号
     */
    private void addCashAccount() {
        ApiRepertory.getInstance().getApiService().saveCashAccount(TourApplication.getToken(), activity.dataBinding.etAli.getText().toString(),
                activity.dataBinding.etPayPhone.getText().toString(), activity.dataBinding.etName.getText().toString(),
                2, activity.dataBinding.etPayCode.getText().toString())
                .subscribeOn(Schedulers.io())
                .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
            @Override
            public void onNext(BaseDataBean<Object> verificationCodeBean) {
                if (null != verificationCodeBean) {
                    if (verificationCodeBean.isSuccess()) {
                        EventBus.getDefault().post(new ALiPayEvent());
                        TourApplication.getUser().setTel(activity.dataBinding.etPayPhone.getText().toString());
                        activity.finish();
                    } else {
                        ToastUtils.showText(activity, verificationCodeBean.getReMsg());
                    }
                }
            }
        });
    }

    private void setAli(String s) {
        if (s.length() > 5) {
            isAli.set(true);
        } else {
            isAli.set(false);
        }
    }

    private void setName(String s) {
        if (s.length() > 1) {
            isName.set(true);
        } else {
            isName.set(false);
        }
    }

    private void setPhone(String s) {
        if (s.length() > 10) {
            isPhone.set(true);
        } else {
            isPhone.set(false);
        }
    }

    public void onAliChanged(CharSequence s, int start, int before, int count) {
        setAli(s.toString());
    }

    public void onNameChanged(CharSequence s, int start, int before, int count) {
        setName(s.toString());
    }

    public void onPhoneChanged(CharSequence s, int start, int before, int count) {
        setPhone(s.toString());
    }

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
