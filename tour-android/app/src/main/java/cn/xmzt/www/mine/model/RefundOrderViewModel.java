package cn.xmzt.www.mine.model;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.BaseViewModel;
import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.dialog.TextTitleDialog;
import cn.xmzt.www.http.ApiRepertory;
import cn.xmzt.www.mine.activity.RefundOrderActivity;
import cn.xmzt.www.mine.adapter.MyOrderAdapter;
import cn.xmzt.www.mine.bean.MyOrderBean;
import cn.xmzt.www.mine.event.RefreshOrderEvent;
import cn.xmzt.www.pay.activity.ChoosePayActivity;
import cn.xmzt.www.route.activity.RouteDetailActivity1;
import cn.xmzt.www.route.activity.RouteOrderDetailActivity;
import cn.xmzt.www.rxjava2.CommonDisposableObserver;
import cn.xmzt.www.rxjava2.ComposeUtil;
import cn.xmzt.www.utils.ToastUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tanlei
 * @date 2019/8/19
 * @describe
 */

public class RefundOrderViewModel extends BaseViewModel implements MyOrderAdapter.OnLeftOrRightClickListener {
    private RefundOrderActivity activity;
    /**
     * 订单状态:1待支付,2待确认,3待出行,4已成交,5已退款（不传默认全部）
     */
    private int ordState = 5;
    /**
     * 优惠券的页码
     */
    private int pageNum = 1;
    /**
     * 一页的数据个数
     */
    private int pageSize = 20;
    private List<MyOrderBean> list = new ArrayList<>();
    private MyOrderAdapter adapter;
    /**
     * 取消订单的弹窗
     */
    private TextTitleDialog dialog;

    public void setActivity(RefundOrderActivity activity) {
        this.activity = activity;
        adapter = new MyOrderAdapter(list, activity);
        adapter.setOnLeftOrRightClickListener(this);
        getOrderList();
        activity.dataBinding.rvCollection.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        activity.dataBinding.rvCollection.setAdapter(adapter);
        activity.dataBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum = 1;
                getOrderList();
            }
        });
        activity.dataBinding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                getOrderList();
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back_iv:
                EventBus.getDefault().post(new RefreshOrderEvent());
                activity.finish();
                break;
        }
    }

    /**
     * 修改订单状态为已读
     */
    private void updateReadState() {
        ApiRepertory.getInstance().getApiService().updateReadState(TourApplication.getToken(), 4)
                .compose(ComposeUtil.compose()).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>() {
            @Override
            public void onNext(BaseDataBean<Object> userInfoBean) {
                if (null != userInfoBean) {
                    if (userInfoBean.isSuccess()) {

                    } else {
                        ToastUtils.showText(activity, userInfoBean.getReMsg());
                    }
                }
            }
        });
    }

    /**
     * 获取订单列表
     */
    private void getOrderList() {
        ApiRepertory.getInstance().getApiService().getMyOrder(TourApplication.getToken(), ordState, pageNum, pageSize)
                .compose(ComposeUtil.compose()).subscribeWith(new CommonDisposableObserver<BaseDataBean<List<MyOrderBean>>>() {
            @Override
            public void onNext(BaseDataBean<List<MyOrderBean>> userInfoBean) {
                if (null != userInfoBean && null != userInfoBean.getRel()) {
                    if (userInfoBean.isSuccess()) {
                        activity.dataBinding.refreshLayout.finishRefresh(true);
                        activity.dataBinding.refreshLayout.finishLoadMore(true);
                        if (userInfoBean.getRel().size() > 0) {
                            if (pageNum == 1) {
                                list.clear();
                            }
                            activity.dataBinding.tvNoData.setVisibility(View.GONE);
                            activity.dataBinding.rvCollection.setVisibility(View.VISIBLE);
                            list.addAll(userInfoBean.getRel());
                            adapter.notifyDataSetChanged();
                        } else {
                            if (pageNum == 1) {
                                activity.dataBinding.tvNoData.setVisibility(View.VISIBLE);
                                activity.dataBinding.rvCollection.setVisibility(View.GONE);
                            }
                        }
                        updateReadState();
                    } else {
                        ToastUtils.showText(activity, userInfoBean.getReMsg());
                    }
                }
            }
        });
    }

    /**
     * 删除订单
     */
    private void deleteOrder(int orderId, int position) {
        ApiRepertory.getInstance().getApiService().deleteOrder(TourApplication.getToken(), orderId)
                .compose(ComposeUtil.compose()).subscribeWith(new CommonDisposableObserver<BaseDataBean<Object>>() {
            @Override
            public void onNext(BaseDataBean<Object> userInfoBean) {
                if (null != userInfoBean) {
                    if (userInfoBean.isSuccess()) {
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.showText(activity, userInfoBean.getReMsg());
                    }
                }
            }
        });
    }

    @Override
    public void onLeftOrRightClick(int position, int leftOrRight) {
        if (leftOrRight == MyOrderAdapter.LEFT_CLICK) {//点击的是左边的按钮
            if (list.get(position).getOrdState() == 1) {//取消订单
                dialog = new TextTitleDialog(activity, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
//                        cancelOrder(list.get(position).getId(), position);
                    }
                });
                dialog.setTitle("确认取消订单吗？");
                dialog.show();
            } else if (list.get(position).getOrdState() == 2 || list.get(position).getOrdState() == 9 ||
                    list.get(position).getOrdState() == 10 || list.get(position).getOrdState() == 11) {//删除订单
                dialog = new TextTitleDialog(activity, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        deleteOrder(list.get(position).getId(), position);
                    }
                });
                dialog.setTitle("删除订单记录无法还原和查询，确定 删除该订单吗？");
                dialog.show();
            } else if (list.get(position).getOrdState() == 3 || list.get(position).getOrdState() == 6) {//退款
                dialog = new TextTitleDialog(activity, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.setTitle("确认要退款吗？");
                dialog.show();
            }
        } else if (leftOrRight == MyOrderAdapter.RIGHT_CLICK) {//右侧按钮
            if (list.get(position).getOrdState() == 1) {
                Intent intent = new Intent(activity, ChoosePayActivity.class);
                intent.putExtra("A", list.get(position).getOrderId());
                activity.startActivity(intent);
            } else if (list.get(position).getOrdState() == 2 || list.get(position).getOrdState() == 9 ||
                    list.get(position).getOrdState() == 10 || list.get(position).getOrdState() == 11) {
                Intent mIntent = new Intent(activity, RouteDetailActivity1.class);
                mIntent.putExtra("A", list.get(position).getProductId());
                mIntent.putExtra("B", list.get(position).getOrderName());
                activity.startActivity(mIntent);
            }
        } else {//最外层控件（跳转至订单详情）
            switch (list.get(position).getOrderType()) {
                case 1://线路
                    Intent intent = new Intent(activity, RouteOrderDetailActivity.class);
                    intent.putExtra("A", list.get(position).getOrderId());
                    activity.startActivity(intent);
                    break;
            }
        }
    }
}
