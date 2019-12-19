package cn.xmzt.www.mine.model;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.base.WebActivity;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.main.activity.MainActivity;
import cn.xmzt.www.mine.activity.RegisterActivity;
import cn.xmzt.www.mine.activity.WxBindPhoneActivity;
import cn.xmzt.www.mine.bean.WxRegisterBean;
import cn.xmzt.www.mine.event.LoginEvent;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.APKVersionCodeUtils;
import cn.xmzt.www.utils.ClipboardUtils;
import cn.xmzt.www.utils.NetWorkUtils;
import cn.xmzt.www.utils.SPUtils;
import cn.xmzt.www.utils.ToastUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import androidx.databinding.ObservableBoolean;
import cn.jpush.android.api.JPushInterface;
import cn.xmzt.www.utils.VersionUtils;
import io.reactivex.schedulers.Schedulers;

/**
 * @author tanlei
 * @date 2019/8/5
 * @describe 注册的ViewModel
 */

public class RegisterViewModel extends BaseViewModel {
    public RegisterActivity activity;

    public void setActivity(RegisterActivity activity) {
        this.activity = activity;
    }

    /**
     * 是否展示清空手机号按钮(右侧按钮)
     */
    public ObservableBoolean isDelete = new ObservableBoolean(false);
    /**
     * 是否可以发送验证码
     */
    public ObservableBoolean isGetCode = new ObservableBoolean(false);
    /**
     * 判断验证码是否符合规则
     */
    public ObservableBoolean isCode = new ObservableBoolean(false);
    /**
     * 判断密码是否符合规则
     */
    public ObservableBoolean isPsw = new ObservableBoolean(false);

    /**
     * 手机号输入的监听
     *
     * @param s
     * @param start
     * @param before
     * @param count
     */
    public void onPhoneChanged(CharSequence s, int start, int before, int count) {
        //判断是否输入字符
        if (s.toString().length() > 0) {
            isDelete.set(true);
        } else {
            isDelete.set(false);
        }
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
     * 密码输入的监听
     *
     * @param s
     * @param start
     * @param before
     * @param count
     */
    public void onPswChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().length() > 5) {
            isPsw.set(true);
        } else {
            isPsw.set(false);
        }
    }

    @SuppressLint("CheckResult")
    UMAuthListener authListener = new UMAuthListener() {
        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
            Log.e("lee", "onStart");
        }

        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Log.e("lee", "onComplete");
            ApiRepertory.getInstance().getApiService().wxRegister(data.get("screen_name"), data.get("openid"), data.get("unionid"), data.get("iconurl"), data.get("city"))
                    .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<WxRegisterBean>>() {
                @Override
                public void onNext(BaseDataBean<WxRegisterBean> objectBaseDataBean) {
                    if (null != objectBaseDataBean) {
                        if (objectBaseDataBean.isSuccess()) {
                            Log.e("lee", objectBaseDataBean.getRel().toString());
                            if (objectBaseDataBean.getRel().getIsTel() == 0) {//未绑定手机号
                                EventBus.getDefault().postSticky(objectBaseDataBean.getRel());
                                activity.startActivity(new Intent(activity, WxBindPhoneActivity.class));
                            } else {//已绑定手机号 登录成功
                                TourApplication.setToken(objectBaseDataBean.getRel().getToken());
                                SPUtils.setToken(objectBaseDataBean.getRel().getToken());
                                TourApplication.isLogin = true;
                                EventBus.getDefault().post(new LoginEvent());
                                activity.finish();
                            }
                        } else {
                            ToastUtils.showText(activity, objectBaseDataBean.getReMsg());
                        }
                    }
                }
            });
        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Log.e("lee", "onError");
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Log.e("lee", "onCancel");
        }
    };

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.account_delete_iv://清空手机号
                activity.dataBinding.accountEt.setText("");
                isDelete.set(false);
                break;
            case R.id.title_back_iv://返回
                activity.finish();
                break;
            case R.id.register_tv://注册
                String ipAddress= NetWorkUtils.getIPAddress(TourApplication.context);
                //获取剪贴板內容中的邀请码
                String refCode=ClipboardUtils.getClipboardRefCode();
                int versionCode=VersionUtils.getVersionCode(TourApplication.context);
                ApiRepertory.getInstance().getApiService().register(activity.dataBinding.accountEt.getText().toString(),
                        activity.dataBinding.passwordEt.getText().toString(), activity.dataBinding.verificationCodeEt.getText().toString(),
                        "android", ""+versionCode,ipAddress,refCode)
                        .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<Object> verificationCodeBean) {
                        if (null != verificationCodeBean) {
                            if (verificationCodeBean.isSuccess()) {
                                if (verificationCodeBean.getRel() instanceof String) {
                                    TourApplication.setToken((String) verificationCodeBean.getRel());
                                    SPUtils.setToken((String) verificationCodeBean.getRel());
                                    TourApplication.isLogin = true;
                                    activity.finish();
                                    Intent intent = new Intent(activity, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    activity.startActivity(intent);
                                    sendJgRid();
                                }
                            } else {
                                ToastUtils.showText(activity, verificationCodeBean.getReMsg());
                            }
                        }

                    }
                });
                break;
            case R.id.iv_wechat://微信
                UMShareAPI.get(activity).getPlatformInfo(activity, SHARE_MEDIA.WEIXIN, authListener);
                break;
            case R.id.tv_register_agreement:
                Intent intent = new Intent(activity, WebActivity.class);
                intent.putExtra("title", "用户注册协议");
                intent.putExtra("url", Constants.getXzUrl(12));
                activity.startActivity(intent);
                break;
            case R.id.get_verification_code_tv://获取验证码
                ApiRepertory.getInstance().getApiService().getVerificationCode(activity.dataBinding.accountEt.getText().toString())
                        .subscribeOn(Schedulers.io())
                        .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<Object> verificationCodeBean) {
                        if (null != verificationCodeBean) {
                            if (verificationCodeBean.isSuccess()) {
                                TimeCount timeCount = new TimeCount(60000, 1000);
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
     * 向后台发送推送的rid
     */
    private void sendJgRid() {
        if (!TextUtils.isEmpty(TourApplication.getToken())) {
            if (TextUtils.isEmpty(TourApplication.rid)) {
                TourApplication.rid = JPushInterface.getRegistrationID(activity);
            }
            ApiRepertory.getInstance().getApiService().sendJgRid(TourApplication.rid, TourApplication.getToken())
                    .compose(ComposeUtil.compose()).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>() {
                @Override
                public void onNext(BaseDataBean<Object> userInfoBean) {
                    Log.e("lee", "***********");
                }
            });
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
