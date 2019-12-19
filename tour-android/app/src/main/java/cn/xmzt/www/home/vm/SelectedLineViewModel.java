package cn.xmzt.www.home.vm;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.home.activity.SelectedLineActivity;
import cn.xmzt.www.home.bean.ArticleBean;
import cn.xmzt.www.home.bean.ArticleDetailsBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.mine.activity.LoginActivity;
import cn.xmzt.www.route.activity.RouteDetailActivity1;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.share.ShareFunction;
import cn.xmzt.www.utils.ToastUtils;

/**
 * @author tanlei
 * @date 2019/8/23
 * @describe
 */

public class SelectedLineViewModel extends BaseViewModel {
    private SelectedLineActivity activity;
    private ArticleDetailsBean articleDetailsBean;

    public void setBean(ArticleBean bean) {
        queryById(bean.getId());
    }

    public void setActivity(SelectedLineActivity activity) {
        this.activity = activity;
    }

    private void queryById(int id) {
        ApiRepertory.getInstance().getApiService().queryById(TourApplication.getToken(), id)
                .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<ArticleDetailsBean>>(mView) {
            @Override
            public void onNext(BaseDataBean<ArticleDetailsBean> verificationCodeBean) {
                if (null != verificationCodeBean) {
                    if (verificationCodeBean.isSuccess()) {
                        articleDetailsBean = verificationCodeBean.getRel();
                        activity.dataBinding.webView.getSettings().setJavaScriptEnabled(true);
                        activity.dataBinding.webView.getSettings().setLoadWithOverviewMode(true);
                        activity.dataBinding.webView.getSettings().setUseWideViewPort(true);
                        activity.dataBinding.webView.getSettings().setDisplayZoomControls(false);
                        activity.dataBinding.webView.getSettings().setDomStorageEnabled(true);
                        activity.dataBinding.webView.getSettings().setDatabaseEnabled(true);
                        activity.dataBinding.webView.loadDataWithBaseURL(null, verificationCodeBean.getRel().getContent(),
                                "text/html", "utf-8", null);
                        activity.dataBinding.titleNameTv.setText(verificationCodeBean.getRel().getTitle());
                        if (verificationCodeBean.getRel().getGiveCountByUser() > 0) {//已经点赞
                            activity.dataBinding.ivLike.setImageResource(R.drawable.icon_unlike);
                        } else {//未点赞
                            activity.dataBinding.ivLike.setImageResource(R.drawable.icon_like);
                        }
                    } else {
                        ToastUtils.showText(activity, verificationCodeBean.getReMsg());
                    }
                }
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_iv:
                activity.finish();
                break;
            case R.id.ll_like://喜欢或者不喜欢
                if (TextUtils.isEmpty(TourApplication.getToken())) {
                    activity.startActivity(new Intent(activity, LoginActivity.class));
                } else {
                    ApiRepertory.getInstance().getApiService().addOrDeleteById(TourApplication.getToken(), articleDetailsBean.getId())
                            .compose(ComposeUtil.compose()).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>(mView) {
                        @Override
                        public void onNext(BaseDataBean<Object> verificationCodeBean) {
                            if (null != verificationCodeBean) {
                                if (verificationCodeBean.isSuccess()) {
                                    if (articleDetailsBean.getGiveCountByUser() > 0) {
                                        articleDetailsBean.setGiveCountByUser(0);
                                    } else {
                                        articleDetailsBean.setGiveCountByUser(1);
                                    }
                                    if (articleDetailsBean.getGiveCountByUser() > 0) {//已经点赞
                                        activity.dataBinding.ivLike.setImageResource(R.drawable.icon_unlike);
                                    } else {//未点赞
                                        activity.dataBinding.ivLike.setImageResource(R.drawable.icon_like);
                                    }
                                } else {
                                    ToastUtils.showText(activity, verificationCodeBean.getReMsg());
                                }
                            }

                        }
                    });
                }
                break;
            case R.id.tv_share_friend://分享好友
                String s;
                if (!TextUtils.isEmpty(TourApplication.getToken())) {
                    s = "https://g.xmzt.cn/g/g?t=1&i=" + articleDetailsBean.getId() + "&p=" + TourApplication.getUser().getRefCode();

                } else {
                    s = "https://g.xmzt.cn/g/g?t=1&i=" + articleDetailsBean.getId() + "&p=" + "";
                }
                ShareFunction.getInstance().showWebShareAction(activity, articleDetailsBean.getTitle(), articleDetailsBean.getMinImage(), articleDetailsBean.getDes(),
                        s, ShareFunction.SHARE_WEIXIN);
                break;
            case R.id.tv_line_details://线路详情
                Intent mIntent = new Intent(activity, RouteDetailActivity1.class);
                mIntent.putExtra("A", Integer.parseInt(articleDetailsBean.getUrl()));
                mIntent.putExtra("B", articleDetailsBean.getTitle());
                activity.startActivity(mIntent);
                break;
        }
    }
}
