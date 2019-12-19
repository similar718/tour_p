package cn.xmzt.www.pay.activity;


import androidx.lifecycle.Observer;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import android.view.View;

import com.alipay.sdk.app.PayTask;
import com.blankj.utilcode.util.ToastUtils;
import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.bean.BaseDataBean;
import cn.xmzt.www.config.Constants;
import cn.xmzt.www.databinding.ActivityChoosePayBinding;
import cn.xmzt.www.dialog.ConfirmDialog;
import cn.xmzt.www.pay.bean.OrderUnpaid;
import cn.xmzt.www.pay.eventbus.PayResultBus;
import cn.xmzt.www.pay.vm.ChoosePayViewModel;
import cn.xmzt.www.route.activity.RouteOrderDetailActivity;
import cn.xmzt.www.route.eventbus.RefreshOrderBus;
import cn.xmzt.www.rxjava2.AsyncUtil;
import cn.xmzt.www.utils.TimeUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * 选择支付方式
 */
public class ChoosePayActivity extends TourBaseActivity<ChoosePayViewModel, ActivityChoosePayBinding> {
    public static final String WX_PAY_RESULT_ACTION = "wx_pay_result_action";
    //订单类型
    protected final int TYPE_ROUTE_ORDER = 1;//线路订单支付
    @Override
    protected int setLayoutId() {
        return R.layout.activity_choose_pay;
    }
    @Override
    protected ChoosePayViewModel createViewModel() {
        viewModel = new ChoosePayViewModel();
        viewModel.orderUnpaid.observe(this, new Observer<OrderUnpaid>() {
            @Override
            public void onChanged(@Nullable OrderUnpaid result) {
                dataBinding.setOrderUnpaid(result);
                remainTime=(result.getExpireTimestamp()-result.getCurrentTimestamp())/1000;
                handler.sendEmptyMessageDelayed(1,1000);
            }
        });
        viewModel.placeOrder.observe(this, new Observer<BaseDataBean<Object>>() {
            @Override
            public void onChanged(@Nullable BaseDataBean<Object> result) {
                if(result.isSuccess()){
                    if(viewModel.typeSelected.get()==1){
                        //微信支付
                        if(result.getRel() instanceof Map){
                            payByWx((Map)result.getRel());
                        }

                    }else if(viewModel.typeSelected.get()==2){
                        //支付宝支付
                        if(result.getRel() instanceof String){
                            payByZfb((String) result.getRel());
                        }
                    }
                }else {
                    if(BaseDataBean.CODE_ORDER_PAY_TIMEOUT.equals(result.getReCode())){
                        showPayTimeoutDialog();
                    }else {
                        showLoadFail(result.getReMsg());
                    }
                }
            }
        });
        return viewModel;
    }
    private String orderCode;
    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        Intent intent=getIntent();
        orderCode=intent.getStringExtra("A");
        dataBinding.setVm(viewModel);
        dataBinding.setActivity(this);
        viewModel.getOrderUnpaid(orderCode);
    }

    private ConfirmDialog confirmDialog;

    private void showConfirmDialog(){
        if (confirmDialog == null){
            confirmDialog = new ConfirmDialog(ChoosePayActivity.this, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.confirm_tv){ // 点击确定
                        onBackPressed();
                    }
                    confirmDialog.dismiss();
                }
            });
        }
        confirmDialog.setViewData("您确定要放弃支付吗？");
        confirmDialog.show();
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                // 需要弹出提示框
                showConfirmDialog();
                break;
            case R.id.confirmLayout:
                if(remainTime>0){
                    viewModel.postPlaceOrder(orderCode,viewModel.typeSelected.get());
                }else {
                    showPayTimeoutDialog();
                }
                break;
            default:
                break;
        }
    }
    private ConfirmDialog payTimeoutDialog=null;
    private void showPayTimeoutDialog(){
        if(payTimeoutDialog==null){
            payTimeoutDialog=new ConfirmDialog(this, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //订单详情
                    Intent intent=new Intent(ChoosePayActivity.this, RouteOrderDetailActivity.class);
                    intent.putExtra("A",orderCode);
                    startActivity(intent);
                    finish();
                }
            });
            payTimeoutDialog.setCancelGONE();
            payTimeoutDialog.setViewData("您的订单由于超时未支付已自动取消，无法继续支付！","查看详情");
        }
        payTimeoutDialog.show();
    }
    private void payByZfb(final String orderInfo) {
        AsyncUtil.async(new Function<String,  Map<String, String>>() {
            @Override
            public Map<String, String> apply(String o) throws Exception {
                PayTask alipay = new PayTask(ChoosePayActivity.this);
                Map<String, String> map = alipay.payV2(orderInfo, true);
                return map;
            }
        }, new Consumer<Map<String, String>>() {
            @Override
            public void accept(Map<String, String> map) throws Exception {
                String resultStatus = map.get("resultStatus");//
                String result = map.get("result");//
                String memo = map.get("memo");//
                if ("9000".equals(resultStatus)) {//支付成功
                    try {
                        JSONObject object = new JSONObject(result);
                        String out_trade_no = object.optJSONObject("alipay_trade_app_pay_response").optString("out_trade_no");
                        String trade_no = object.optJSONObject("alipay_trade_app_pay_response").optString("trade_no");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startToActivity(PaySuccessActivity.class);
                    setResult(RESULT_OK);
                    finish();
                } else {
                    ToastUtils.showShort(memo);
                    startToActivity(PayFailedActivity.class);
                }
            }
        });
    }

    private void payByWx(Map<String,String> jsonMap) {
        try {
            PayReq req = new PayReq();
            req.appId = jsonMap.get("appid");
            req.partnerId = jsonMap.get("partnerid");
            req.prepayId = jsonMap.get("prepayid");
            req.packageValue = jsonMap.get("package");
            req.nonceStr = jsonMap.get("noncestr");
            req.timeStamp = jsonMap.get("timestamp");
            req.sign = jsonMap.get("sign");
//                        req.extData = "app data"; // optional
//                        MToast.show("正常调起支付");
            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
            boolean isHaveWx = Constants.getWx_api().sendReq(req);
            if(!isHaveWx){
                ToastUtils.showShort("请确定是否已安装微信!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showShort("支付失败");
        }
    }
    private long remainTime=0;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                remainTime=remainTime-1;
                dataBinding.tvRemainTime.setText(TimeUtil.change(remainTime));
                if(remainTime>0){
                    handler.sendEmptyMessageDelayed(1,1000);
                }
            }
        }
    };
    @Subscribe
    public void onPayResult(PayResultBus payResultBus){
        if(payResultBus!=null){
            if(payResultBus.isSuccess()){
                EventBus.getDefault().post(new RefreshOrderBus(1));
                Intent intent = new Intent(this, PaySuccessActivity.class);
                intent.putExtra("A",orderCode);
                startActivity(intent);
                setResult(RESULT_OK);
                finish();
            }else {
                Intent intent = new Intent(this, PayFailedActivity.class);
                intent.putExtra("A",orderCode);
                startActivity(intent);
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        handler.removeMessages(1);
    }
}