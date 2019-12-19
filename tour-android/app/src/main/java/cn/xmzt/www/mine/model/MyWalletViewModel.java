package cn.xmzt.www.mine.model;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;

import cn.xmzt.www.BR;
import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseAdapter;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.mine.activity.CashWithdrawalActivity;
import cn.xmzt.www.mine.activity.MyWalletActivity;
import cn.xmzt.www.mine.bean.UserWalletBean;
import cn.xmzt.www.mine.bean.UserWalletDetailsBean;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.ToastUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.ObservableDouble;
import androidx.databinding.ObservableInt;

import io.reactivex.annotations.NonNull;

/**
 * @author tanlei
 * @date 2019/8/15
 * @describe
 */

public class MyWalletViewModel extends BaseViewModel {
    private MyWalletActivity activity;
    /**
     * 区分资金明细的获取类型
     */
    public ObservableInt type = new ObservableInt(0);
    /**
     * 余额数
     */
    public ObservableDouble balanceNumber = new ObservableDouble();
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
    private List<UserWalletDetailsBean> list = new ArrayList<>();
    private BaseAdapter adapter;
    private UserWalletBean userWalletBean;

    public void setActivity(MyWalletActivity activity) {
        this.activity = activity;
        adapter = new BaseAdapter<>(activity, list, R.layout.item_bill, BR.detailsBean);
        activity.dataBinding.setAdapter(adapter);
//        activity.dataBinding.refreshLayout.autoRefresh();
        getMoneyDetails();
        activity.dataBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getMoneyDetails();
            }
        });
        activity.dataBinding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                getMoneyDetails();
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
//                    activity.dataBinding.refreshLayout.autoRefresh();
                    getMoneyDetails();
                }
                break;
            case R.id.income_rl://收入
                if (type.get() != 1) {
                    type.set(1);
                    page = 1;
//                    activity.dataBinding.refreshLayout.autoRefresh();
                    getMoneyDetails();
                }
                break;
            case R.id.expend_rl://支出
                if (type.get() != 2) {
                    type.set(2);
                    page = 1;
//                    activity.dataBinding.refreshLayout.autoRefresh();
                    getMoneyDetails();
                }
                break;
            case R.id.cash_tv://提现
                EventBus.getDefault().postSticky(userWalletBean);
                activity.startActivity(new Intent(activity, CashWithdrawalActivity.class));
                break;
            default:
                break;
        }
    }


    /**
     * 获取用户资金信息
     */
    public void getMoneySum() {
        ApiRepertory.getInstance().getApiService().getMoneySum(TourApplication.getToken())
                .compose(ComposeUtil.compose(mView)).subscribeWith(new CommonDisposableObserver<BaseDataBean<UserWalletBean>>() {
            @Override
            public void onNext(BaseDataBean<UserWalletBean> userInfoBean) {
                if (null != userInfoBean && null != userInfoBean.getRel()) {
                    if (userInfoBean.isSuccess()) {
                        userWalletBean = userInfoBean.getRel();
                        balanceNumber.set(userInfoBean.getRel().getEnableNum());
                    } else {
                        ToastUtils.showText(activity, userInfoBean.getReMsg());
                    }
                }
            }
        });
    }

    /**
     * 获取用户资金明细信息
     */
    public void getMoneyDetails() {
        ApiRepertory.getInstance().getApiService().getBill(TourApplication.getToken(), page, pageSize, type.get())
                .compose(ComposeUtil.compose()).subscribeWith(new CommonDisposableObserver<BaseDataBean<List<UserWalletDetailsBean>>>() {
            @Override
            public void onNext(BaseDataBean<List<UserWalletDetailsBean>> userInfoBean) {
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
                            activity.dataBinding.incomeTv.setTextColor(Color.parseColor("#666666"));
                            activity.dataBinding.incomeTv.setTypeface(Typeface.DEFAULT);
                            activity.dataBinding.expendTv.setTextColor(Color.parseColor("#666666"));
                            activity.dataBinding.expendTv.setTypeface(Typeface.DEFAULT);
                        } else if (type.get() == 1) {
                            activity.dataBinding.allTv.setTextColor(Color.parseColor("#666666"));
                            activity.dataBinding.allTv.setTypeface(Typeface.DEFAULT);
                            activity.dataBinding.incomeTv.setTextColor(Color.parseColor("#333333"));
                            activity.dataBinding.incomeTv.setTypeface(Typeface.DEFAULT_BOLD);
                            activity.dataBinding.expendTv.setTextColor(Color.parseColor("#666666"));
                            activity.dataBinding.expendTv.setTypeface(Typeface.DEFAULT);
                        } else {
                            activity.dataBinding.allTv.setTextColor(Color.parseColor("#666666"));
                            activity.dataBinding.allTv.setTypeface(Typeface.DEFAULT);
                            activity.dataBinding.incomeTv.setTextColor(Color.parseColor("#666666"));
                            activity.dataBinding.incomeTv.setTypeface(Typeface.DEFAULT);
                            activity.dataBinding.expendTv.setTextColor(Color.parseColor("#333333"));
                            activity.dataBinding.expendTv.setTypeface(Typeface.DEFAULT_BOLD);
                        }
                    } else {
                        ToastUtils.showText(activity, userInfoBean.getReMsg());
                    }
                }
            }
        });
    }
}
