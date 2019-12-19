package cn.xmzt.www.mine.model;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.view.View;
import android.widget.PopupWindow;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.bean.ToRouteListBus;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.main.activity.MainActivity;
import cn.xmzt.www.mine.activity.MyCouponActivity;
import cn.xmzt.www.mine.activity.MyCouponHistoryActivity;
import cn.xmzt.www.mine.adapter.MyCouponAdapter;
import cn.xmzt.www.mine.bean.MyCouponBean;
import cn.xmzt.www.pay.activity.PaySuccessActivity;
import cn.xmzt.www.popup.CouponRangePopupWindow;
import cn.xmzt.www.popup.CouponReceivePopupWindow;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.ToastUtils;
import cn.xmzt.www.view.listener.ItemListener;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import org.greenrobot.eventbus.EventBus;

/**
 * @author tanlei
 * @date 2019/8/12
 * @describe
 */

public class MyCouponViewModel extends BaseViewModel {
    private MyCouponActivity activity;
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
    private int state = 0;
    /**
     * 类型(0:通用,1:线路,2:酒店,3:门票)
     */
    private String type = "";
    private List<MyCouponBean> list = new ArrayList<>();
    private MyCouponAdapter adapter;
    /**
     * 领取时间的弹出框
     */
    private CouponReceivePopupWindow receivePopupWindow;
    /**
     * 适用范围的弹出框
     */
    private CouponRangePopupWindow rangePopupWindow;
    public ObservableField<String> sort = new ObservableField<>("最近领取");
    public ObservableField<String> rangeType = new ObservableField<>("全部");

    public void setActivity(MyCouponActivity activity) {
        this.activity = activity;
        adapter = new MyCouponAdapter(activity, list);
        activity.dataBinding.lvCoupon.setAdapter(adapter);
        getCoupon();
//        activity.dataBinding.refreshLayout.autoRefresh();
        activity.dataBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum = 1;
                getCoupon();
            }
        });
        activity.dataBinding.refreshLayout.setEnableLoadMore(false);
        adapter.setItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                //跳首页
                EventBus.getDefault().post(new ToRouteListBus(1));
                Intent intent = new Intent();
                intent.setClass(activity, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
                activity.finish();
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_iv:
                activity.finish();
                break;
            case R.id.coupon_history_record_tv://优惠券历史
                activity.startActivity(new Intent(activity, MyCouponHistoryActivity.class));
                break;
            case R.id.sort_type_rl://领取时间
                if (receivePopupWindow == null) {
                    receivePopupWindow = new CouponReceivePopupWindow(activity);
                    receivePopupWindow.setOnTypeClickListener(new CouponReceivePopupWindow.OnTypeClickListener() {
                        @Override
                        public void onTypeClick(int position) {
                            if (position == 0) {
                                sortType = 1;
                                sort.set("最近领取");
                            } else {
                                sortType = 2;
                                sort.set("最近到期");
                            }
                            pageNum = 1;
                            getCoupon();
//                            activity.dataBinding.refreshLayout.autoRefresh();
                        }
                    });
                }
                if (sortType == 1) {
                    receivePopupWindow.setType(0);
                } else {
                    receivePopupWindow.setType(1);
                }
                if (receivePopupWindow.isShowing()) {
                    receivePopupWindow.dismiss();
                } else {
                    receivePopupWindow.showAsDropDown(activity.dataBinding.sortTypeRl);
//                    activity.dataBinding.sortTypeIv.setImageResource(R.drawable.icon_pop_up);
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(activity.dataBinding.sortTypeIv, "rotation", 0f,-180f);
                    objectAnimator.start();
                }
                receivePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
//                        activity.dataBinding.sortTypeIv.setImageResource(R.drawable.icon_pop_down);
                        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(activity.dataBinding.sortTypeIv, "rotation", -180f,0f);
                        objectAnimator.start();
                    }
                });
                break;

            case R.id.type_rl://使用范围
                if (rangePopupWindow == null) {
                    rangePopupWindow = new CouponRangePopupWindow(activity);
                    rangePopupWindow.setOnTypeClickListener(new CouponReceivePopupWindow.OnTypeClickListener() {
                        @Override
                        public void onTypeClick(int position) {
                            switch (position) {
                                case 0:
                                    type = "";
                                    rangeType.set("全部");
                                    break;
                                case 1:
                                    type = "1";
                                    rangeType.set("线路");
                                    break;
                                case 2:
                                    type = "2";
                                    rangeType.set("酒店");
                                    break;
                                case 3:
                                    type = "3";
                                    rangeType.set("门票");
                                    break;
                            }
                            pageNum = 1;
                            getCoupon();
//                            activity.dataBinding.refreshLayout.autoRefresh();
                        }
                    });
                }
                rangePopupWindow.setType(type);
                if (rangePopupWindow.isShowing()) {
                    rangePopupWindow.dismiss();
                } else {
                    rangePopupWindow.showAsDropDown(activity.dataBinding.sortTypeRl);
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(activity.dataBinding.typeIv, "rotation", 0f,-180f);
                    objectAnimator.start();
//                    activity.dataBinding.typeIv.setImageResource(R.drawable.icon_pop_up);
                }
                rangePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(activity.dataBinding.typeIv, "rotation", -180f,0f);
                        objectAnimator.start();
//                        activity.dataBinding.typeIv.setImageResource(R.drawable.icon_pop_down);
                    }
                });
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
                            activity.dataBinding.lvCoupon.setVisibility(View.VISIBLE);
                            list.addAll(userInfoBean.getRel());
                            adapter.onNotifyDataSetChanged(list);
                        } else {
                            if (pageNum == 1) {
                                activity.dataBinding.tvNoData.setVisibility(View.VISIBLE);
                                activity.dataBinding.lvCoupon.setVisibility(View.GONE);
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
