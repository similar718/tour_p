package cn.xmzt.www.mine.model;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.http.ApiService;
import cn.xmzt.www.intercom.event.RefreshSchedulingListBus;
import cn.xmzt.www.mine.activity.LoginActivity;
import cn.xmzt.www.mine.activity.WxBindPhoneActivity;
import cn.xmzt.www.mine.bean.WxRegisterBean;
import cn.xmzt.www.mine.event.LoginEvent;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.APKVersionCodeUtils;
import cn.xmzt.www.utils.ClipboardUtils;
import cn.xmzt.www.utils.MatcherUtils;
import cn.xmzt.www.utils.NetWorkUtils;
import cn.xmzt.www.utils.SPUtils;
import cn.xmzt.www.utils.ToastUtils;
import cn.xmzt.www.utils.VersionUtils;
import cn.xmzt.www.utils.WxLoginHelper;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author tanlei
 * @date 2019/8/5
 * @describe
 */

public class LoginViewModel extends BaseViewModel {
    private LoginActivity activity;
    /**
     * 默认是手机登录（判断是手机登录还是密码登录的标识）
     */
    public ObservableBoolean isPhoneLogin = new ObservableBoolean(true);
    /**
     * 是否展示已发送验证码的提示
     */
    public ObservableBoolean isSendCode = new ObservableBoolean(false);
    /**
     * 标题栏
     */
    public ObservableField<String> titleName = new ObservableField<>("验证码登录");
    /**
     * 左下角的按钮（切换登录方式）
     */
    public ObservableField<String> loginTypeName = new ObservableField<>("账户密码登录");
    /**
     * 是否展示清空手机号按钮(右侧按钮)
     */
    public ObservableBoolean isDelete = new ObservableBoolean(false);
    /**
     * 是否可以发送验证码
     */
    public ObservableBoolean isGetCode = new ObservableBoolean(false);
    /**
     * 验证码是否符合格式
     */
    public ObservableBoolean isCode = new ObservableBoolean(false);
    /**
     * 用户名是否符合格式
     */
    public ObservableBoolean isUserName = new ObservableBoolean(false);
    /**
     * 密码是否符合格式
     */
    public ObservableBoolean isPassWord = new ObservableBoolean(false);
    /**
     * 密码是否可见
     */
    public ObservableBoolean isEye = new ObservableBoolean();

    public void setActivity(LoginActivity activity) {
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
        //判断是否输入字符
        if (s.toString().length() > 0) {
            isDelete.set(true);
        } else {
            isDelete.set(false);
        }
        //判断是否为11位
        if (s.toString().length() == 11) {
            //判断是否符合手机格式
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
     * 用户名输入监听
     *
     * @param s
     * @param start
     * @param before
     * @param count
     */
    public void onUserNameChanged(CharSequence s, int start, int before, int count) {
        //判断是否为11位
        if (s.toString().length() == 11) {
            //判断是否符合手机格式
            if (MatcherUtils.isMobile(s.toString())) {
                isUserName.set(true);
            } else {
                isUserName.set(false);
            }
        } else {
            isUserName.set(false);
        }
    }

    /**
     * 密码输入监听
     *
     * @param s
     * @param start
     * @param before
     * @param count
     */
    public void onPassWordChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().length() > 5) {
            isPassWord.set(true);
        } else {
            isPassWord.set(false);
        }
    }

    /**
     * 微信授权码
     * @param authCode
     */
    public void wxLogin( String authCode){
        ApiService mService = ApiRepertory.getInstance().getApiService();
        String ipAddress= NetWorkUtils.getIPAddress(TourApplication.context);
        //获取剪贴板內容中的邀请码
        String refCode=ClipboardUtils.getClipboardRefCode();
        int versionCode=VersionUtils.getVersionCode(TourApplication.context);
        Observable mObservable = mService.wxLogin(authCode,refCode,"android",""+versionCode,ipAddress);
        mObservable.subscribeOn(Schedulers.io())
                .compose(ComposeUtil.compose(mView))
                .subscribeWith(new CommonDisposableObserver<BaseDataBean<WxRegisterBean>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<WxRegisterBean> body) {
                        if (body.isSuccess()) {
                            WxRegisterBean mWxRegisterBean=body.getRel();
                            //判断是否绑定手机号 1已绑定0未绑定"
                            if (mWxRegisterBean.getIsTel() == 0) {//未绑定手机号
                                EventBus.getDefault().postSticky(mWxRegisterBean);
                                activity.startActivity(new Intent(activity, WxBindPhoneActivity.class));
                            } else {//已绑定手机号 登录成功
                                TourApplication.setToken(mWxRegisterBean.getToken());
                                SPUtils.setToken(mWxRegisterBean.getToken());
                                TourApplication.isLogin = true;
                                EventBus.getDefault().post(new LoginEvent());
                                activity.finish();
                            }
                        } else {
                            ToastUtils.showText(activity, body.getReMsg());
                        }
                    }
                });
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
            case R.id.title_back_iv://返回
                activity.finish();
                break;
            case R.id.ll_wechat://微信登录
//                UMShareAPI.get(activity).getPlatformInfo(activity, SHARE_MEDIA.WEIXIN, authListener);
                WxLoginHelper.wxAuth(WxLoginHelper.STATE_WX_LOGIN);
                break;
            case R.id.iv_phone_delete://清空手机号
                activity.dataBinding.etPhone.setText("");
                isDelete.set(false);
                break;
            case R.id.password_login_tv://密码或手机登录切换
                if (null != isPhoneLogin) {
                    isPhoneLogin.set(!isPhoneLogin.get());
                }
                if (isPhoneLogin.get()) {
                    loginTypeName.set("账户密码登录");
                    titleName.set("验证码登录");
                } else {
                    loginTypeName.set("短信验证码登录");
                    titleName.set("密码登录");
                }
                break;
            case R.id.register_tv://注册
                activity.startToRegisterActivity();
                break;
            case R.id.tv_login://登录
                // 测试
                if (isPhoneLogin.get()) {//手机验证码登录
                    ApiRepertory.getInstance().getApiService().verificationCodeLogin(activity.dataBinding.etPhone.getText().toString(),
                            activity.dataBinding.etCode.getText().toString(), "android", APKVersionCodeUtils.getVersionCode(activity)+"")
                            .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>() {
                        @Override
                        public void onNext(BaseDataBean<Object> objectBaseDataBean) {
                            if (null != objectBaseDataBean) {
                                if (objectBaseDataBean.isSuccess()) {
                                    if (objectBaseDataBean.getRel() instanceof String) {
                                        TourApplication.setToken((String) objectBaseDataBean.getRel());
                                        SPUtils.setToken((String) objectBaseDataBean.getRel());
                                        TourApplication.isLogin = true;
                                        sendJgRid();
                                        activity.finish();
                                        EventBus.getDefault().post(new LoginEvent());
                                        EventBus.getDefault().post(new RefreshSchedulingListBus(1));
//                                        Intent intent = new Intent(activity, MainActivity.class);
//                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        activity.startActivity(intent);
                                    }
                                } else {
                                    ToastUtils.showText(activity, objectBaseDataBean.getReMsg());
                                }
                            }
                        }
                    });
                } else {//用户密码登录
                    ApiRepertory.getInstance().getApiService().passwordLogin(activity.dataBinding.accountEt.getText().toString(),
                            activity.dataBinding.pwdEt.getText().toString(), "android", APKVersionCodeUtils.getVersionCode(activity)+"")
                            .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>() {
                        @Override
                        public void onNext(BaseDataBean<Object> objectBaseDataBean) {
                            if (null != objectBaseDataBean) {
                                if (objectBaseDataBean.isSuccess()) {
                                    if (objectBaseDataBean.getRel() instanceof String) {
                                        TourApplication.setToken((String) objectBaseDataBean.getRel());
                                        SPUtils.setToken((String) objectBaseDataBean.getRel());
                                        TourApplication.isLogin = true;
                                        sendJgRid();
                                        activity.finish();
                                        EventBus.getDefault().post(new LoginEvent());
                                        EventBus.getDefault().post(new RefreshSchedulingListBus(1));
//                                        Intent intent = new Intent(activity, MainActivity.class);
//                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        activity.startActivity(intent);
                                    }
                                } else {
                                    ToastUtils.showText(activity, objectBaseDataBean.getReMsg());
                                }
                            }
                        }
                    });
                }
                break;
            case R.id.tv_forget_pwd://忘记密码
                activity.startToFindPassWordActivity();
                break;
            case R.id.tv_code://获取验证码
                ApiRepertory.getInstance().getApiService().getVerificationCode(activity.dataBinding.etPhone.getText().toString())
                        .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                    @Override
                    public void onNext(BaseDataBean<Object> verificationCodeBean) {
                        if (null != verificationCodeBean) {
                            if (verificationCodeBean.isSuccess()) {
                                TimeCount timeCount = new TimeCount(60000, 1000);
                                timeCount.start();
                            }
                        } else {
                            ToastUtils.showText(activity, verificationCodeBean.getReMsg());
                        }
                    }
                });
                break;
            case R.id.iv_wechat://微信登录

                break;
            case R.id.pwd_iv:
                isEye.set(!isEye.get());
                if (isEye.get()) {
                    activity.dataBinding.pwdEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    activity.dataBinding.pwdEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
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
            isSendCode.set(true);
            activity.dataBinding.tvCode.setTextColor(Color.parseColor("#8824A4FF"));
            activity.dataBinding.tvCode.setClickable(false);
            activity.dataBinding.tvCode.setText(millisUntilFinished / 1000 + " s后重新发送");
        }

        @Override
        public void onFinish() {
            isSendCode.set(false);
            activity.dataBinding.tvCode.setText("重新获取验证码");
            activity.dataBinding.tvCode.setClickable(true);
            activity.dataBinding.tvCode.setTextColor(Color.parseColor("#24A4FF"));

        }
    }

}
