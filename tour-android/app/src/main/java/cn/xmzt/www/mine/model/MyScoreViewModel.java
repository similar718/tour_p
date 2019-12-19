package cn.xmzt.www.mine.model;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;

import cn.xmzt.www.BR;
import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseAdapter;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.dialog.ScoreRuleExplainDialog;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.mine.activity.MyScoreActivity;
import cn.xmzt.www.mine.bean.UserScoreBean;
import cn.xmzt.www.mine.bean.UserScoreDetailsBean;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.ToastUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.ObservableInt;

import io.reactivex.annotations.NonNull;

/**
 * @author tanlei
 * @date 2019/8/9
 * @describe
 */

public class MyScoreViewModel extends BaseViewModel {
    private MyScoreActivity activity;
    /**
     * 区分积分明细的获取类型
     */
    public ObservableInt type = new ObservableInt(0);
    /**
     * 积分数
     */
    public ObservableInt scoreNumber = new ObservableInt();
    /**
     * 页码
     */
    public int page = 1;
    /**
     * 一页数据大小
     */
    public int pageSize = 20;
    /**
     * 数据源
     */
    private List<UserScoreDetailsBean> list = new ArrayList<>();
    private BaseAdapter adapter;

    public void setActivity(MyScoreActivity activity) {
        this.activity = activity;
        adapter = new BaseAdapter<>(activity, list, R.layout.item_score, BR.userScoreDetailsBean);
        activity.dataBinding.setAdapter(adapter);
        getUserScoreDetailed();
//        activity.dataBinding.refreshLayout.autoRefresh();
        activity.dataBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getUserScoreDetailed();
            }
        });
        activity.dataBinding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                getUserScoreDetailed();
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_iv:
                activity.finish();
                break;
            case R.id.all_rl://全部
                if (type.get() != 0) {
                    type.set(0);
                    page = 1;
                    getUserScoreDetailed();
//                    activity.dataBinding.refreshLayout.autoRefresh();
                }
                break;
            case R.id.get_score_rl://积分获取
                if (type.get() != 1) {
                    type.set(1);
                    page = 1;
                    getUserScoreDetailed();
//                    activity.dataBinding.refreshLayout.autoRefresh();
                }
                break;
            case R.id.use_score_rl://积分消耗
                if (type.get() != 2) {
                    type.set(2);
                    page = 1;
                    getUserScoreDetailed();
//                    activity.dataBinding.refreshLayout.autoRefresh();
                }
                break;
            case R.id.score_rule_tv://积分规则
                ScoreRuleExplainDialog dialog = new ScoreRuleExplainDialog(activity);
                dialog.show();
                break;
            default:
                break;
        }
    }

    /**
     * 获取用户积分信息
     */
    public void getUserScore() {
        ApiRepertory.getInstance().getApiService().getMyScore(TourApplication.getToken())
                .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<UserScoreBean>>() {
            @Override
            public void onNext(BaseDataBean<UserScoreBean> userInfoBean) {
                if (null != userInfoBean && null != userInfoBean.getRel()) {
                    if (userInfoBean.isSuccess()) {
                        scoreNumber.set(userInfoBean.getRel().getEnableIntegral());
                    } else {
                        ToastUtils.showText(activity, userInfoBean.getReMsg());
                    }
                }
            }
        });
    }

    /**
     * 获取用户积分明细 type.get() 0 全部 1 积分获取 2 积分消耗
     */
    public void getUserScoreDetailed() {
        ApiRepertory.getInstance().getApiService().getScoreDetail(TourApplication.getToken(), page, pageSize, type.get())
                .compose(ComposeUtil.compose()).subscribeWith(new CommonDisposableObserver<BaseDataBean<List<UserScoreDetailsBean>>>() {
            @Override
            public void onNext(BaseDataBean<List<UserScoreDetailsBean>> userInfoBean) {
                if (null != userInfoBean && null != userInfoBean.getRel()) {
                    if (userInfoBean.isSuccess()) {
                        activity.dataBinding.refreshLayout.finishRefresh(true);
                        activity.dataBinding.refreshLayout.finishLoadMore(true);
                        if (userInfoBean.getRel().size() > 0) {
                            if (page == 1) {
                                list.clear();
                            }
                            activity.dataBinding.tvNoData.setVisibility(View.GONE);
                            activity.dataBinding.lv.setVisibility(View.VISIBLE);
                            list.addAll(userInfoBean.getRel());
                            adapter.notifyDataSetChanged();
                        } else {
                            if (page == 1) {
                                activity.dataBinding.tvNoData.setVisibility(View.VISIBLE);
                                activity.dataBinding.lv.setVisibility(View.GONE);
                            }
                        }
                        if (type.get() == 0) {
                            activity.dataBinding.allTv.setTextColor(Color.parseColor("#333333"));
                            activity.dataBinding.allTv.setTypeface(Typeface.DEFAULT_BOLD);
                            activity.dataBinding.getScoreTv.setTextColor(Color.parseColor("#666666"));
                            activity.dataBinding.getScoreTv.setTypeface(Typeface.DEFAULT);
                            activity.dataBinding.useScoreTv.setTextColor(Color.parseColor("#666666"));
                            activity.dataBinding.useScoreTv.setTypeface(Typeface.DEFAULT);
                        } else if (type.get() == 1) {
                            activity.dataBinding.allTv.setTextColor(Color.parseColor("#666666"));
                            activity.dataBinding.allTv.setTypeface(Typeface.DEFAULT);
                            activity.dataBinding.getScoreTv.setTextColor(Color.parseColor("#333333"));
                            activity.dataBinding.getScoreTv.setTypeface(Typeface.DEFAULT_BOLD);
                            activity.dataBinding.useScoreTv.setTextColor(Color.parseColor("#666666"));
                            activity.dataBinding.useScoreTv.setTypeface(Typeface.DEFAULT);
                        } else {
                            activity.dataBinding.allTv.setTextColor(Color.parseColor("#666666"));
                            activity.dataBinding.allTv.setTypeface(Typeface.DEFAULT);
                            activity.dataBinding.getScoreTv.setTextColor(Color.parseColor("#666666"));
                            activity.dataBinding.getScoreTv.setTypeface(Typeface.DEFAULT);
                            activity.dataBinding.useScoreTv.setTextColor(Color.parseColor("#333333"));
                            activity.dataBinding.useScoreTv.setTypeface(Typeface.DEFAULT_BOLD);
                        }
                    } else {
                        ToastUtils.showText(activity, userInfoBean.getReMsg());
                    }
                }
            }
        });
    }
}
