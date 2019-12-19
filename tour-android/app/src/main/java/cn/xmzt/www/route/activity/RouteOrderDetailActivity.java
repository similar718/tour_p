package cn.xmzt.www.route.activity;

import androidx.lifecycle.Observer;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.ToastUtils;
import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityRouteOrderDetailBinding;
import cn.xmzt.www.dialog.SendInvoiceEmailDialog;
import cn.xmzt.www.dialog.TextTitleDialog;
import cn.xmzt.www.pay.activity.ChoosePayActivity;
import cn.xmzt.www.popup.RouteCostDetailsPopupWindow;
import cn.xmzt.www.route.adapter.RouteOrderAdapter;
import cn.xmzt.www.route.bean.OrderInvoiceForm;
import cn.xmzt.www.route.bean.OrderInvoiceInfo;
import cn.xmzt.www.route.bean.RouteOrderDetailBean;
import cn.xmzt.www.route.eventbus.RefreshEvent;
import cn.xmzt.www.route.eventbus.RefreshOrderBus;
import cn.xmzt.www.route.vm.RouteOrderDetailViewModel;
import cn.xmzt.www.utils.MatcherUtils;
import cn.xmzt.www.utils.StatusBarUtil;
import cn.xmzt.www.utils.TimeUtil;
import cn.xmzt.www.view.listener.ItemListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 线路订单详情
 */
public class RouteOrderDetailActivity extends TourBaseActivity<RouteOrderDetailViewModel, ActivityRouteOrderDetailBinding> implements ItemListener {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_route_order_detail;
    }
    @Override
    protected RouteOrderDetailViewModel createViewModel() {
        viewModel = new RouteOrderDetailViewModel();
        viewModel.mRouteOrderDetail.observe(this, new Observer<RouteOrderDetailBean>() {
            @Override
            public void onChanged(@Nullable RouteOrderDetailBean mRouteOrderDetail) {
                adapter.setData(mRouteOrderDetail);
                setOrderStateAction(adapter.orderState,adapter.refundState);
            }
        });
        return viewModel;
    }

    public void setOrderStateAction(int orderState,int refundState ) {
        dataBinding.vLine.setVisibility(View.VISIBLE);
        dataBinding.tvRefund.setVisibility(View.GONE);
        dataBinding.tvCancel.setVisibility(View.GONE);
        dataBinding.tvDelete.setVisibility(View.GONE);
        dataBinding.tvReserve.setVisibility(View.GONE);
        dataBinding.payLayout.setVisibility(View.GONE);
        dataBinding.tvRemainTime.setText("");
        if(orderState==10){
            //待支付 操作 继续支付，取消
            dataBinding.tvCancel.setVisibility(View.VISIBLE);
            dataBinding.payLayout.setVisibility(View.VISIBLE);
            dataBinding.tvRemainTime.setText("剩余支付时间"+ TimeUtil.change(adapter.remainTime));
            handler.sendEmptyMessageDelayed(1,1000);

        }else if(orderState==20||orderState==30||orderState==100||orderState==110){
            //订单超时 已完成 已关闭 操作 删除
            dataBinding.tvDelete.setVisibility(View.VISIBLE);
            dataBinding.tvReserve.setVisibility(View.VISIBLE);
        }else if(orderState==50){
            //已取消 预定失败 操作 再次预定
            dataBinding.tvReserve.setVisibility(View.VISIBLE);
        }else if(orderState==40||orderState==60){
            //待确认  待出行 操作 退款
            dataBinding.tvRefund.setVisibility(View.VISIBLE);
            dataBinding.tvRefund.setEnabled(true);
            if(refundState==1){ //退款状态(1.退款中、2.退款失败、3.退款成功)(只有待出行才有退款状态)
                dataBinding.tvRefund.setVisibility(View.GONE);
                dataBinding.vLine.setVisibility(View.GONE);
            }else if(refundState==2){
                dataBinding.tvRefund.setVisibility(View.GONE);
                dataBinding.vLine.setVisibility(View.GONE);
            }else if(refundState==3){
                dataBinding.tvRefund.setVisibility(View.GONE);
                dataBinding.tvDelete.setVisibility(View.VISIBLE);
                dataBinding.tvReserve.setVisibility(View.VISIBLE);
            }
        }else {
            dataBinding.vLine.setVisibility(View.GONE);
        }
    }

    RouteOrderAdapter adapter =null;
    GridLayoutManager manager=null;
    private String orderCode;//订单编码
    private int statusBarHeight;
    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        StatusBarUtil.setStatusBarLightMode(this,false);
        statusBarHeight= StatusBarUtil.getStatusBarHeight(getApplicationContext());
        FrameLayout.LayoutParams listParams = (FrameLayout.LayoutParams) dataBinding.titleLayout.getLayoutParams();
        listParams.topMargin=statusBarHeight;
        dataBinding.setActivity(this);
        Intent intent=getIntent();
        orderCode=intent.getStringExtra("A");
        adapter = new RouteOrderAdapter();
        adapter.setItemListener(this);
        dataBinding.recyclerView.setAdapter(adapter);
        manager= (GridLayoutManager) dataBinding.recyclerView.getLayoutManager();
        manager.setSpanCount(1);
        /**
         * 自定义RecyclerView实现对AppBarLayout的滚动效果
         */
        dataBinding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrollH =scrollH+dy;
                int titleBarHeight = dataBinding.titleLayout.getHeight()+statusBarHeight;
                float percent = (float) scrollH / titleBarHeight;
                if (percent >= 1) {
                    percent = 1;
                }
                float alpha = percent * 255;
                dataBinding.titleBarLayout.setBackgroundColor(Color.argb((int) alpha, 64, 186, 255));
            }
        });
        viewModel.getOrderDetail(orderCode);
    }
    int scrollH = 0;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                adapter.remainTime=adapter.remainTime-1;
                dataBinding.tvRemainTime.setText("剩余支付时间"+TimeUtil.change(adapter.remainTime));
                if(adapter.remainTime>0){
                    handler.sendEmptyMessageDelayed(1,1000);
                }
            }
        }
    };
    @Subscribe
    public void refreshOrderBus(RefreshOrderBus refreshOrder){
        if(refreshOrder!=null){
            if(refreshOrder.getActionType()==3){//删除后
                finish();
            }else {
                if(!TextUtils.isEmpty(orderCode)){
                    viewModel.getOrderDetail(orderCode);
                }
            }
        }
    }
    @Subscribe
    public void selectOrderInvoiceForm(OrderInvoiceForm mOrderInvoiceForm){
        if(mOrderInvoiceForm.getInvoiceTitleId()>0){
            //发票
            viewModel.addInvoice(orderCode,mOrderInvoiceForm);
        }
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                EventBus.getDefault().post(new RefreshEvent());
                onBackPressed();
                break;
            case R.id.tv_refund:
               //退款
                showHintDialog(3);
                break;
            case R.id.tv_cancel:
                //取消订单
                showHintDialog(1);
                break;
            case R.id.tv_delete:
                //取消订单
                showHintDialog(2);
                break;
            case R.id.payLayout:{
                //继续支付
                Intent intent=new Intent(this, ChoosePayActivity.class);
                intent.putExtra("A",orderCode);
                startActivity(intent);
                break;
            }
            case R.id.tv_reserve:{
                //再次预定
                if(adapter.common!=null){
                    Intent intent=new Intent(this,RouteDetailActivity1.class);
                    intent.putExtra("A",adapter.common.getProductId());
                    intent.putExtra("B",adapter.common.getProductName());
                    startActivity(intent);
                }
                break;
            }
            default:
                break;
        }
    }


    @Override
    public void onItemClick(View view, int position) {
        if(view.getId()==R.id.tv_cost_detail){
            showDetailDialog();
        }else if(view.getId()==R.id.tv_send_email){
            OrderInvoiceInfo invoice=adapter.mOrderBean.getInvoice();
            String email="";
            if(invoice!=null){
                email=invoice.getReceiveEmail();
            }
            String title = invoice.getSendTimes() > 0 ? "重发电子发票" : "发送电子发票";
            showSendInvoiceEmailDialog(email,title);
        }else if(view.getId()==R.id.tv_refund_status){
            //退款详情
        } else if (view.getId() == R.id.itemLayout){
            Intent intent=new Intent(this,RouteDetailActivity1.class);
            intent.putExtra("A",adapter.common.getProductId());
            intent.putExtra("B",adapter.common.getProductName());
            startActivity(intent);
        }
    }

    private TextTitleDialog hintDialog;
    private int dialogType=0;//1 表示取消 2表示删除 表示申请退款
    private void showHintDialog(int dialogType){
        this.dialogType=dialogType;
        if(hintDialog==null){
            hintDialog = new TextTitleDialog(RouteOrderDetailActivity.this, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hintDialog.dismiss();
                    if(RouteOrderDetailActivity.this.dialogType==1){
                        if(adapter.mOrderBean!=null){
                            viewModel.cancelOrder(adapter.mOrderBean.getId());
                        }
                    }else if(RouteOrderDetailActivity.this.dialogType==2){
                        if(adapter.mOrderBean!=null){
                            viewModel.deleteOrder(adapter.mOrderBean.getId());
                        }
                    }else if(RouteOrderDetailActivity.this.dialogType==3){
                        viewModel.applyRefund(orderCode);
                    }
                }
            });
        }
        if(dialogType==1){
            hintDialog.setTitle("确认要取消订单吗？");
        }else if(dialogType==2){
            hintDialog.setTitle("确认要删除订单吗？");
        }else if(dialogType==3){
            hintDialog.setTitle("确认要申请退款吗？");
        }
        hintDialog.show();;
    }
    private RouteCostDetailsPopupWindow costDetailsPopupWindow;
    private void showDetailDialog(){
        if(costDetailsPopupWindow==null){
            costDetailsPopupWindow=new RouteCostDetailsPopupWindow(RouteOrderDetailActivity.this);
        }
        costDetailsPopupWindow.setViewData(adapter.mProduct,adapter.mOrderBean);
        costDetailsPopupWindow.showAsDropDown(dataBinding.popupLine);
    }
    private SendInvoiceEmailDialog sendInvoiceEmailDialog;
    private void showSendInvoiceEmailDialog(String email,String title){
        if(sendInvoiceEmailDialog==null){
            sendInvoiceEmailDialog=new SendInvoiceEmailDialog(RouteOrderDetailActivity.this, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v.getId()==R.id.iv_del){
                        sendInvoiceEmailDialog.clear();
                    }else if(v.getId()==R.id.iv_cancel){
                        sendInvoiceEmailDialog.cancel();
                    }else if(v.getId()==R.id.tv_send){
                        String email=sendInvoiceEmailDialog.getEmail();
                        if(MatcherUtils.isEmial(email)){
                            sendInvoiceEmailDialog.cancel();
                            viewModel.sendInvoice(orderCode,email);
                        }else {
                            ToastUtils.showShort("请输入有效邮箱");
                        }
                    }
                }
            });
        }
        sendInvoiceEmailDialog.setViewData(email,title);
        sendInvoiceEmailDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        handler.removeMessages(1);
    }


}
