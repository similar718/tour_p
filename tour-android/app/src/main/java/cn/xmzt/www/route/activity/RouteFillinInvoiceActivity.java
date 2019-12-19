package cn.xmzt.www.route.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.bean.InvoiceTitleBean;
import cn.xmzt.www.databinding.ActivityRouteFillInInvoiceBinding;
import cn.xmzt.www.dialog.MakeInvoiceServiceDialog;
import cn.xmzt.www.dialog.TextTitleDialog;
import cn.xmzt.www.route.bean.OrderInvoiceForm;
import cn.xmzt.www.route.vm.RouteFillinInvoiceViewModel;
import cn.xmzt.www.utils.MatcherUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 线路填写发票
 */
public class RouteFillinInvoiceActivity extends TourBaseActivity<RouteFillinInvoiceViewModel, ActivityRouteFillInInvoiceBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_route_fill_in_invoice;
    }
    @Override
    protected RouteFillinInvoiceViewModel createViewModel() {
        viewModel = new RouteFillinInvoiceViewModel();
        return viewModel;
    }
    private int type=0;//1表示线路订单填写发票
    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        type=getIntent().getIntExtra("A",0);
        dataBinding.setActivity(this);
        dataBinding.setVm(viewModel);
    }
    private InvoiceTitleBean mInvoiceTitleBean=null;
    private OrderInvoiceForm mOrderInvoiceForm=new OrderInvoiceForm();
    @Subscribe
    public void selectInvoiceTitleBean(InvoiceTitleBean mInvoiceTitleBean){
       this.mInvoiceTitleBean=mInvoiceTitleBean;
       dataBinding.tvInvoiceTitle.setText(mInvoiceTitleBean.getTitleName());
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.layout1:
                showPickServiceDialog();
                break;
            case R.id.layout3:
                Intent intent=new Intent(this, SelectInvoiceTitleActivity.class);
                intent.putExtra("A",type);//线路订单填写发票
                startActivity(intent);
                break;
            case R.id.tv_submit:
                if(viewModel.selected.get()==2){
                    if(mInvoiceTitleBean!=null){
                        if(MatcherUtils.isEmial(viewModel.email.get())){
                            showHintDialog();
                        }else {
                            ToastUtils.showShort("请输入有效的邮箱");
                        }
                    }else {
                        ToastUtils.showShort("请选择发票抬头");
                    }
                }else {
                    EventBus.getDefault().post(mOrderInvoiceForm);
                    onBackPressed();
                }
                break;
            default:
                break;
        }
    }
    private String makeInvoiceItemTitle="*旅游服务*门票服务费";//开票项
    private MakeInvoiceServiceDialog pickServiceDialog;
    private void showPickServiceDialog(){
        if(pickServiceDialog==null){
            pickServiceDialog=new MakeInvoiceServiceDialog(RouteFillinInvoiceActivity.this, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickServiceDialog.cancel();
                    if(v instanceof TextView){
                        TextView mTextView= (TextView) v;
                        makeInvoiceItemTitle=mTextView.getText().toString();
                    }
                }
            });
        }

        pickServiceDialog.show();
    }
    private TextTitleDialog hintDialog;
    private void showHintDialog(){
        if(hintDialog==null){
            hintDialog = new TextTitleDialog(RouteFillinInvoiceActivity.this, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hintDialog.dismiss();
                    mOrderInvoiceForm.setInvoiceTitleId(mInvoiceTitleBean.getId());
                    mOrderInvoiceForm.setItemTitle(makeInvoiceItemTitle);
                    mOrderInvoiceForm.setReceiveEmail(viewModel.email.get());
                    EventBus.getDefault().post(mOrderInvoiceForm);
                    onBackPressed();
                }
            });
        }
        hintDialog.setTitle("发票信息一旦提交，即不可更改");
        hintDialog.show();;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
