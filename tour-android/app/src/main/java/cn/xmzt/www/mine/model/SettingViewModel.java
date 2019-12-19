package cn.xmzt.www.mine.model;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.dialog.TextTitleDialog;
import cn.xmzt.www.helper.UserHelper;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.manager.IntentManager;
import cn.xmzt.www.mine.activity.AboutUsActivity;
import cn.xmzt.www.mine.activity.AccountSecurityActivity;
import cn.xmzt.www.mine.activity.FeedbackActivity;
import cn.xmzt.www.mine.activity.SettingActivity;
import cn.xmzt.www.mine.event.LogOutEvent;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.CleanMessageUtil;
import cn.xmzt.www.utils.ToastUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

/**
 * @author tanlei
 * @date 2019/8/8
 * @describe
 */

public class SettingViewModel extends BaseViewModel {
    private SettingActivity activity;
    private TextTitleDialog dialog;

    public void setActivity(SettingActivity activity) {
        this.activity = activity;
        try {
            activity.dataBinding.cacheTv.setText(CleanMessageUtil.getTotalCacheSize(activity));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_logout://退出登录
                dialog = new TextTitleDialog(activity, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {//进行网络请求
                        ApiRepertory.getInstance().getApiService().loginOut(TourApplication.getToken())
                                .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>() {
                            @Override
                            public void onNext(BaseDataBean<Object> objectBaseDataBean) {
                                if (null != objectBaseDataBean) {
                                    if (objectBaseDataBean.isSuccess()) {
//                                        SPUtils.clear();
//                                        SPUtils.setToken();
                                        TourApplication.setToken(null);
                                        TourApplication.setUser(null);
                                        UserHelper.logout();
                                        activity.finish();
                                        EventBus.getDefault().post(new LogOutEvent());
                                        //删除授权
                                        UMShareAPI.get(activity).deleteOauth(activity, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
                                            @Override
                                            public void onStart(SHARE_MEDIA share_media) {

                                            }

                                            @Override
                                            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {

                                            }

                                            @Override
                                            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

                                            }

                                            @Override
                                            public void onCancel(SHARE_MEDIA share_media, int i) {

                                            }
                                        });
                                    } else {
                                        ToastUtils.showText(activity, objectBaseDataBean.getReMsg());
                                    }
                                }
                            }
                        });
                        dialog.dismiss();
                    }
                });
                dialog.setTitle("确认要退出登录？");
                dialog.show();
                break;
            case R.id.account_number_rl://账号与安全
                activity.startActivity(new Intent(activity, AccountSecurityActivity.class));
                break;
            case R.id.title_back_iv://返回
                activity.finish();
                break;
            case R.id.feedback_rl://意见反馈
                activity.startActivity(new Intent(activity, FeedbackActivity.class));
                break;
            case R.id.about_us_rl://关于我们
                activity.startActivity(new Intent(activity, AboutUsActivity.class));
                break;
            case R.id.apply_rl://应用好评
                // TODO: 2019/8/28 应用正式上线时还需要改
//                Uri uri = Uri.parse("market://details?id=" + "cn.xmzt.www");
//                Uri uri = Uri.parse("market://details?id=" + "com.tencent.mm");
//                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
//                activity.startActivity(goToMarket);
                ToastUtils.showText(activity,"正在研发中……");
                break;
            case R.id.offline_package_layout://离线包管理
                IntentManager.getInstance().goOfflineFileManagerActivity(activity);
                break;
            case R.id.clear_ll://清理缓存
                dialog = new TextTitleDialog(activity, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CleanMessageUtil.clearAllCache(activity);
                        try {
                            activity.dataBinding.cacheTv.setText(CleanMessageUtil.getTotalCacheSize(activity));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });
                dialog.setTitle("确认要清除缓存吗？");
                dialog.show();
                break;
            default:
                break;
        }
    }
}
