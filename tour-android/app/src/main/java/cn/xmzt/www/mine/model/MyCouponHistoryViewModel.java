package cn.xmzt.www.mine.model;

import android.graphics.Typeface;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.mine.activity.MyCouponHistoryActivity;
import cn.xmzt.www.mine.adapter.MyCouponAdapter;
import cn.xmzt.www.mine.bean.MyCouponBean;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.ToastUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * @author tanlei
 * @date 2019/8/13
 * @describe
 */

public class MyCouponHistoryViewModel extends BaseViewModel {
    private MyCouponHistoryActivity activity;
    /**
     * 优惠券的页码
     */
    private int pageNum = 1;
    /**
     * 一页的数据个数
     */
    private int pageSize = 20;
    /**
     * 排序方式(1: 领取时间倒序，2：到期时间顺序)
     */
    private int sortType = 1;
    /**
     * 优惠券状态(0:未使用,1:已使用,2:已过期)
     */
    private int state = 1;
    /**
     * 类型(0:通用,1:线路,2:酒店,3:门票)
     */
    private String type = "";
    private List<MyCouponBean> list = new ArrayList<>();
    private MyCouponAdapter adapter;

    public void setActivity(MyCouponHistoryActivity activity) {
        this.activity = activity;
        adapter = new MyCouponAdapter(activity, list);
        activity.dataBinding.lvHistory.setAdapter(adapter);
        getCoupon();
//        activity.dataBinding.refreshLayout.autoRefresh();
        activity.dataBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum = 1;
                getCoupon();
            }
        });
        activity.dataBinding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                getCoupon();
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_iv:
                activity.finish();
                break;
            case R.id.used_rl://使用
                state = 1;
                pageNum = 1;
                activity.dataBinding.usedTv.setTypeface(Typeface.DEFAULT_BOLD);
                activity.dataBinding.expiredTv.setTypeface(Typeface.MONOSPACE);
                activity.dataBinding.usedV.setVisibility(View.VISIBLE);
                activity.dataBinding.expiredV.setVisibility(View.GONE);
                getCoupon();
//                activity.dataBinding.refreshLayout.autoRefresh();
                break;
            case R.id.expired_rl://过期
                state = 2;
                pageNum = 1;
                activity.dataBinding.usedTv.setTypeface(Typeface.MONOSPACE);
                activity.dataBinding.expiredTv.setTypeface(Typeface.DEFAULT_BOLD);
                activity.dataBinding.usedV.setVisibility(View.GONE);
                activity.dataBinding.expiredV.setVisibility(View.VISIBLE);
                getCoupon();
//                activity.dataBinding.refreshLayout.autoRefresh();
                break;
        }
    }

    /**
     * 获取优惠券
     */
    public void getCoupon() {
        ApiRepertory.getInstance().getApiService().getMyCouponList(sortType, state, TourApplication.getToken(), type)
                .compose(ComposeUtil.compose()).subscribeWith(new CommonDisposableObserver<BaseDataBean<List<MyCouponBean>>>() {
            @Override
            public void onNext(BaseDataBean<List<MyCouponBean>> userInfoBean) {
                if (null != userInfoBean && null != userInfoBean.getRel()) {
                    if (userInfoBean.isSuccess()) {
                        activity.dataBinding.refreshLayout.finishRefresh(true);
                        activity.dataBinding.refreshLayout.finishLoadMore(true);
                        if (userInfoBean.getRel().size() > 0) {
                            if (pageNum == 1) {
                                list.clear();
                            }
                            activity.dataBinding.tvNoData.setVisibility(View.GONE);
                            activity.dataBinding.lvHistory.setVisibility(View.VISIBLE);
                            list.addAll(userInfoBean.getRel());
                            adapter.setState(state);
                            adapter.onNotifyDataSetChanged(list);
                        } else {
                            if (pageNum == 1) {
                                activity.dataBinding.tvNoData.setVisibility(View.VISIBLE);
                                activity.dataBinding.lvHistory.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        ToastUtils.showText(activity, userInfoBean.getReMsg());
                    }
                }
            }
        });
    }
}
