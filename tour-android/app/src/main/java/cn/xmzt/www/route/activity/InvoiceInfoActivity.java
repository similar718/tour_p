package cn.xmzt.www.route.activity;

import android.content.Intent;
import android.view.View;

import cn.xmzt.www.R;
import cn.xmzt.www.base.TourBaseActivity;
import cn.xmzt.www.databinding.ActivityInvoiceInfoBinding;
import cn.xmzt.www.route.bean.OrderInvoiceInfo;
import cn.xmzt.www.route.vm.InvoiceInfoViewModel;

/**
 * @describe 发票信息
 */
public class InvoiceInfoActivity extends TourBaseActivity<InvoiceInfoViewModel, ActivityInvoiceInfoBinding> {
    @Override
    protected int setLayoutId() {
        return R.layout.activity_invoice_info;
    }
    @Override
    protected InvoiceInfoViewModel createViewModel() {
        viewModel = new InvoiceInfoViewModel();
        return viewModel;
    }
    private OrderInvoiceInfo orderInvoiceInfo = null;
    private int openInvoice;//是否开发票(0:不需要发票,1:电子发票)
    @Override
    protected void initData() {
        Intent intent=getIntent();
        orderInvoiceInfo= (OrderInvoiceInfo) intent.getSerializableExtra("A");
        openInvoice= intent.getIntExtra("B",0);
        dataBinding.setActivity(this);
        dataBinding.setVm(viewModel);
        dataBinding.setOrderInvoiceInfo(orderInvoiceInfo);
        viewModel.openInvoice.set(openInvoice);
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            default:
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
