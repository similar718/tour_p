package cn.xmzt.www.mine.model;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.dialog.SignInRuleExplainDialog;
import cn.xmzt.www.glide.GlideUtil;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.mine.activity.SignInActivity;
import cn.xmzt.www.mine.adapter.SignInAdapter;
import cn.xmzt.www.mine.bean.SignDayBean;
import cn.xmzt.www.mine.bean.SignInBean;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tanlei
 * @date 2019/8/23
 * @describe
 */

public class SignInViewModel extends BaseViewModel {
    private SignInActivity activity;
    private List<SignDayBean> list = new ArrayList<>();
    private SignInBean rel;
    private SignInAdapter adapter;

    public void setActivity(SignInActivity activity) {
        this.activity = activity;
        for (int i = 1; i < 31; i++) {
            list.add(new SignDayBean(i + ""));
        }
        GridLayoutManager layoutManager = new GridLayoutManager(activity, 6, LinearLayoutManager.VERTICAL, false);
        adapter = new SignInAdapter(list, activity);
        activity.dataBinding.rv.setLayoutManager(layoutManager);
        activity.dataBinding.rv.setAdapter(adapter);
        getSignInInfo();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_iv:
                activity.finish();
                break;
            case R.id.tv_sign://签到
                goSignIn();
                break;
            case R.id.tv_rule://奖励规则
                SignInRuleExplainDialog dialog = new SignInRuleExplainDialog(activity);
                dialog.show();
                break;
        }
    }

    /**
     * 去签到
     */
    private void goSignIn() {
        ApiRepertory.getInstance().getApiService().signIn(TourApplication.getToken())
                .compose(ComposeUtil.compose()).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>() {
            @Override
            public void onNext(BaseDataBean<Object> objectBaseDataBean) {
                if (objectBaseDataBean.isSuccess()) {
                    getSignInInfo();
                } else {
                    ToastUtils.showText(activity, objectBaseDataBean.getReMsg());
                }
            }
        });
    }

    /**
     * 获取用户签到信息
     */
    private void getSignInInfo() {
        ApiRepertory.getInstance().getApiService().getSignInInfo(TourApplication.getToken())
                .compose(ComposeUtil.compose()).subscribeWith(new CommonDisposableObserver<BaseDataBean<SignInBean>>() {
            @Override
            public void onNext(BaseDataBean<SignInBean> objectBaseDataBean) {
                if (null != objectBaseDataBean) {
                    if (objectBaseDataBean.isSuccess()) {
                        rel = objectBaseDataBean.getRel();
                        if (rel != null) {
                            if (rel.isSign()) {
                                activity.dataBinding.tvSign.setText("已签到");
                                activity.dataBinding.tvSign.setEnabled(false);
                                activity.dataBinding.tvSign.setBackgroundResource(R.drawable.shape_sign_un);
                                activity.dataBinding.tvIntegral.setText("今日已签到，获得" + rel.getLastGainIntegral() + "积分");
                            } else {
                                activity.dataBinding.tvSign.setText("签到");
                                activity.dataBinding.tvSign.setEnabled(true);
                                activity.dataBinding.tvSign.setBackgroundResource(R.drawable.shape_sign);
                                activity.dataBinding.tvIntegral.setText("今日还没签到");
                            }
                            if (rel.getContinuouNum() < 10) {
                                activity.dataBinding.tvLeft.setText(0 + "");
                                activity.dataBinding.tvRight.setText(rel.getContinuouNum() + "");
                            } else {
                                String a = rel.getContinuouNum() + "";
                                activity.dataBinding.tvLeft.setText(a.substring(0, 1));
                                activity.dataBinding.tvRight.setText(a.substring(1, 2));
                            }
                            for (int i = 0; i < rel.getContinuouNum(); i++) {
                                list.get(i).setSign(true);
                            }
                            adapter.notifyDataSetChanged();
                            activity.dataBinding.tv1.setText(rel.getAwards().get(0).getAwardName());
                            activity.dataBinding.tv2.setText(rel.getAwards().get(1).getAwardName());
                            activity.dataBinding.tv3.setText(rel.getAwards().get(2).getAwardName());
                            GlideUtil.loadImage(activity.dataBinding.iv1, rel.getAwards().get(0).getAwardPicture());
                            GlideUtil.loadImage(activity.dataBinding.iv2, rel.getAwards().get(1).getAwardPicture());
                            GlideUtil.loadImage(activity.dataBinding.iv3, rel.getAwards().get(2).getAwardPicture());
                            activity.dataBinding.tvProbability1.setText(rel.getAwards().get(0).getAwardProb() + "%");
                            activity.dataBinding.tvProbability2.setText(rel.getAwards().get(1).getAwardProb() + "%");
                            activity.dataBinding.tvProbability3.setText(rel.getAwards().get(2).getAwardProb() + "%");
                        }
                    } else {
                        ToastUtils.showText(activity, objectBaseDataBean.getReMsg());
                    }
                }
            }
        });
    }
}
